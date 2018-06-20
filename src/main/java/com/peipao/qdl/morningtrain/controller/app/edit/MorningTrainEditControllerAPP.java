package com.peipao.qdl.morningtrain.controller.app.edit;

import com.peipao.framework.annotation.Register;
import com.peipao.framework.annotation.SystemControllerLog;
import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.constant.WebConstants;
import com.peipao.framework.model.Response;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.util.DateUtil;
import com.peipao.framework.util.StringUtil;
import com.peipao.qdl.morningtrain.model.MorningTrainRecord;
import com.peipao.qdl.morningtrain.model.MorningTrainRecordVo;
import com.peipao.qdl.morningtrain.service.MorningTrainRecordService;
import com.peipao.qdl.morningtrain.service.MorningTrainRecordUtilService;
import com.peipao.qdl.running.model.RunningEffectiveEnum;
import com.peipao.qdl.running.model.RunningEnum;
import com.peipao.qdl.runningrule.model.vo.RunningRuleVo;
import com.peipao.qdl.runningrule.service.utils.RuleUtilService;
import com.peipao.qdl.school.model.UserSchool;
import com.peipao.qdl.school.service.SchoolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


/**
 * 方法名称：MorningTrainEditControllerAPP
 * 功能描述：晨练记录管理
 * 作者：Liu Fan
 * 版本：2.1.0
 * 创建日期：2018/2/28 17:57
 * 修订记录：MorningTrainRecord
 */

@Api(value = "/morningtrain/app",description = "晨练记录管理")
@RestController
@RequestMapping({"/morningtrain/app"})
public class MorningTrainEditControllerAPP {
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private RuleUtilService ruleUtilService;
    @Autowired
    private MorningTrainRecordService morningTrainRecordService;
    @Autowired
    private MorningTrainRecordUtilService morningTrainRecordUtilService;


    @Register
    @RequestMapping(value = {"/sign"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "晨练签到")
    @ApiOperation(value = "sign", notes = "晨练签到")
    public Response<?> sign(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "json: 晨练对象属性") @RequestBody MorningTrainRecord morningTrainRecord
    ) throws Exception {
        UserSchool userSchool = schoolService.getParaByUserId(userId,  new Date());
        if(null == userSchool || null == userSchool.getSchoolId() || null == userSchool.getSemesterId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }
        Date now = new Date();
        RunningRuleVo runningRule = ruleUtilService.getRunningRuleBySemesterIdAndType(userSchool.getSemesterId(), RunningEnum.MORNINGTRAINING.getValue());
        if(null != runningRule && runningRule.getIsUse() == WebConstants.Boolean.TRUE.ordinal()) {
            String datStr = DateUtil.dateToStr(now, "YYYY-MM-dd");
            Date startDate = DateUtil.parseDate(datStr + " " + runningRule.getStartTime() + ":00", "yyyy-MM-dd HH:mm:ss");
            Date endDate = DateUtil.parseDate(datStr + " " + runningRule.getEndTime() + ":00", "yyyy-MM-dd HH:mm:ss");
            if(now.getTime() < startDate.getTime() || now.getTime() > endDate.getTime()) {//必须在晨练规定的时间范围内开始晨练
                return Response.fail(ResultMsg.MORNING_TRAIN_TIME_ERROR);//晨练不在规定时间范围内
            }
            MorningTrainRecord mtRecord = morningTrainRecordUtilService.getMorningTrainRecordForToday(runningRule, userSchool.getSemesterId(), userId);
            if(null != mtRecord) {
                return Response.fail(ResultMsg.MORNING_TRAIN_RECORD_EXIST);//今日已存在晨练记录，不能多次晨练
            }

            // user_id, semester_id, start_date, phone_str, sign_latitude, sign_longitude
            if(null == morningTrainRecord.getSignLatitude() || null == morningTrainRecord.getSignLongitude()) {
                return Response.fail(ResultMsg.MORNING_TRAIN_SIGN_LOCATION_EMTYP);//晨练签到时必须提供签到坐标
            }
            morningTrainRecord.setUserId(userId);
            morningTrainRecord.setSemesterId(userSchool.getSemesterId());
            morningTrainRecord.setStartDate(new Date());
            morningTrainRecordService.insertMorningTrainRecord(morningTrainRecord);
        } else {
            return Response.fail(ResultMsg.MORNING_TRAIN_RULE_EMTYP);//晨练功能没有开启
        }
        MorningTrainRecordVo mvo = new MorningTrainRecordVo();
        mvo.setSignLatitude(morningTrainRecord.getSignLatitude());
        mvo.setSignLongitude(morningTrainRecord.getSignLongitude());
        mvo.setStartDate(morningTrainRecord.getStartDate());
        mvo.setCurrTimeLong(now.getTime());
        return Response.success(mvo);
    }

    @Register
    @RequestMapping(value = {"/signout"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "晨练签退")
    @ApiOperation(value = "signout", notes = "晨练签退")
    public Response<?> signout(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "json: 晨练对象属性") @RequestBody MorningTrainRecord morningTrainRecord
    ) throws Exception {
        UserSchool userSchool = schoolService.getParaByUserId(userId,  new Date());
        if(null == userSchool || null == userSchool.getSchoolId() || null == userSchool.getSemesterId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }
        Date now = new Date();
        RunningRuleVo runningRule = ruleUtilService.getRunningRuleBySemesterIdAndType(userSchool.getSemesterId(), RunningEnum.MORNINGTRAINING.getValue());
        if(null != runningRule && runningRule.getIsUse() == WebConstants.Boolean.TRUE.ordinal()) {
            String datStr = DateUtil.dateToStr(now, "YYYY-MM-dd");
            float durationMin = runningRule.getValidKiometerMin()*60;//最小晨练时长(分钟换算成秒)
            if(morningTrainRecord.getDuration() < durationMin) {
                return Response.fail(ResultMsg.MORNING_TRAIN_TIME_SHORT);//晨练时长不足
            }
            MorningTrainRecord mtRecord = morningTrainRecordUtilService.getMorningTrainRecordForToday(runningRule, userSchool.getSemesterId(), userId);
            if(null == mtRecord) {
                return Response.fail(ResultMsg.MORNING_TRAIN_RECORD_NOT_EXIST);//今日晨练记录不存在，不能签退
            }
            //durationMax = 晨练记录的开始时间 加上 规则规定的最大时长 取毫秒
            long durationMax = mtRecord.getStartDate().getTime() + runningRule.getValidKiometerMax().intValue() * 60 * 1000;
            if(now.getTime() > durationMax) {//必须在晨练规定的时间范围内开始晨练
                return Response.fail(ResultMsg.MORNING_TRAIN_TIME_ERROR_OUT);//晨练不在规定时间范围内
            }
            if(null == morningTrainRecord.getLatitudeOut() || null == morningTrainRecord.getLongitudeOut()) {
                return Response.fail(ResultMsg.MORNING_TRAIN_SIGNOUT_LOCATION_EMTYP);//晨练签退时必须提供签到坐标
            }
            if(null == morningTrainRecord.getDuration() || StringUtil.isEmpty(morningTrainRecord.getDurationStr())) {
                return Response.fail(ResultMsg.MORNING_TRAIN_DURATION_EMTYP);//缺少晨练时长
            }
            morningTrainRecord.setEndDate(new Date());
            morningTrainRecord.setIsEffective(RunningEffectiveEnum.Success.getCode());
            morningTrainRecord.setMorningTrainRecordId(mtRecord.getMorningTrainRecordId());
            morningTrainRecord.setUserId(mtRecord.getUserId());
            morningTrainRecord.setSemesterId(mtRecord.getSemesterId());

            morningTrainRecordService.updateMorningTrainRecordAndStatistic(userSchool.getSchoolId(), morningTrainRecord);
        } else {
            return Response.fail(ResultMsg.MORNING_TRAIN_RULE_EMTYP);//晨练功能没有开启
        }

        MorningTrainRecordVo mvo = new MorningTrainRecordVo();
        mvo.setSignLatitude(morningTrainRecord.getSignLatitude());
        mvo.setSignLongitude(morningTrainRecord.getSignLongitude());
        mvo.setStartDate(morningTrainRecord.getStartDate());
        mvo.setLatitudeOut(morningTrainRecord.getLatitudeOut());
        mvo.setLongitudeOut(morningTrainRecord.getLongitudeOut());
        mvo.setDuration(morningTrainRecord.getDuration());
        mvo.setDurationStr(morningTrainRecord.getDurationStr());
        mvo.setEndDate(new Date());
        mvo.setIsEffective(RunningEffectiveEnum.Success.getCode());
        mvo.setCurrTimeLong(now.getTime());
        return Response.success(mvo);
    }

}
