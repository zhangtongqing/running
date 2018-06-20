package com.peipao.qdl.runningrule.controller.pc.edit;

import com.peipao.framework.annotation.Register;
import com.peipao.framework.annotation.SystemControllerLog;
import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.model.Response;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.util.JsonUtils;
import com.peipao.framework.util.ValidateUtil;
import com.peipao.qdl.runningrule.model.RailNode;
import com.peipao.qdl.runningrule.model.RunningRule;
import com.peipao.qdl.runningrule.service.utils.RuleCacheService;
import com.peipao.qdl.runningrule.service.utils.RuleUtilService;
import com.peipao.qdl.school.model.UserSchool;
import com.peipao.qdl.school.service.SchoolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 方法名称：RunningRuleEditControllerPC
 * 功能描述：跑步规则接口
 * 作者：Liu Fan
 * 版本：2.0.10
 * 创建日期：2018/01/12 11:58
 * 修订记录：
 */

@Api(value = "runningRule",description = "跑步规则接口")
@RestController
@RequestMapping({"/runningRule/pc"})
public class RunningRuleEditControllerPC {
    protected static final Logger log = LoggerFactory.getLogger(RunningRuleEditControllerPC.class);
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private RuleUtilService ruleUtilService;
    @Autowired
    private RuleCacheService ruleCacheService;

    @Register
    @RequestMapping(value = {"/updateRunningRule"}, method = {RequestMethod.POST})
    @SystemControllerLog(description = "更新跑步规则(设置-校园设置-跑步规则)-pc")
    @ApiOperation(value = "更新跑步规则(设置-校园设置-跑步规则)-pc")
    public Response<?> updateRunningRule(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "老师ID") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "更新跑步规则") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"runningRuleList"};//必填项检查
        if (!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        UserSchool userSchool = schoolService.getParaByUserId(userId, new Date());
        if (null == userSchool || null == userSchool.getSchoolId() || null == userSchool.getSemesterId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }
        JSONArray runningRuleJsonArray = json.getJSONArray("runningRuleList");
        List runningRuleList = JsonUtils.getList4Json(runningRuleJsonArray.toString(), RunningRule.class);
        long schoolId = userSchool.getSchoolId();
        long semesterId = userSchool.getSemesterId();
        List signNodeList = null;//晨练打卡点数据集合
        if(ValidateUtil.jsonValidateWithKey(json, "runningRuleNodeList")) {//如果有晨练打卡点数据
            JSONArray runningRuleNodeListJsonArray = json.getJSONArray("runningRuleNodeList");
            signNodeList = JsonUtils.getList4Json(runningRuleNodeListJsonArray.toString(), RailNode.class);
            if(!CollectionUtils.isEmpty(signNodeList)) {
                ruleUtilService.initSignNodeList(signNodeList, userSchool);
            }
        }

        //加载本学期设置的电子围栏数据
        ruleUtilService.initRunningRuleList(runningRuleList, userSchool);
        //************************************** 参数准备完毕，开始更新数据 ****************************************//
        ruleCacheService.insertOrUpdateRunningRule(userSchool.getSemesterId(), runningRuleList);
        ruleCacheService.updateMorningSignNodeNode(semesterId, signNodeList);

        return Response.success();
    }


    /**
     * 这里用lock实现同步锁，synchronized 在大量访问的时候性能会低于lock
     */
    private Lock lock = new ReentrantLock();

    @Register
    @RequestMapping(value = {"/updateRailNode"}, method = {RequestMethod.POST})
    @SystemControllerLog(description = "更新校园围栏数据-pc")
    @ApiOperation(value = "更新校园围栏数据-pc")
    public Response<?> updateRailNode(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "老师ID") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "更新校园围栏数据") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"runningRuleNodeList","railName"};//必填项检查 railName 围栏名 ，oldRailName要修改的围栏名
        if (!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }

        UserSchool userSchool = schoolService.getParaByUserId(userId, new Date());
        if (null == userSchool || null == userSchool.getSchoolId() || null == userSchool.getSemesterId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }
        JSONArray railNodeJsonArray = json.getJSONArray("runningRuleNodeList");
        String railName = json.get("railName").toString();
        String oldRailName =  json.get("oldRailName") != null ? json.get("oldRailName").toString() : null;
        List runningRuleNodeList = JsonUtils.getList4Json(railNodeJsonArray.toString(), RailNode.class);
        //************************************** 参数准备完毕，开始更新数据 ****************************************//
        //ruleService.updateRailNode(userSchool.getSemesterId(), runningRuleNodeList);
        //设置校区围栏
        //围栏数据修改的时候加锁，防止修改的时候，客户端读取数据造成死锁
        Response<?> resInfo = null;
        lock.lock();
        try{
            log.info("**得到围栏数据锁***");
            resInfo = ruleCacheService.updateRailNode(userSchool.getSemesterId(), railName, runningRuleNodeList, oldRailName);
        }catch (Exception e){
            log.info("围栏数据修改异常={}",e);
        }finally {
            lock.unlock();
            log.info("***释放锁围栏数据锁***");
        }
        return resInfo;
    }


    /**
     * 张同情 date:2018-04-13
     * 根据围栏名称删除围栏 pc端要求异步删除使用
     * @param token
     * @param userId
     * @param sign
     * @param json
     * @return
     * @throws Exception
     */
    @Register
    @RequestMapping(value = {"/deleteRailByName"}, method = {RequestMethod.POST})
    @SystemControllerLog(description = "根据学校围栏名删除围栏-pc")
    @ApiOperation(value = "根据学校围栏名删除围栏-pc")
    public Response<?> deleteRailByName(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "老师ID") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "围栏名等") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"railName"};//必填项检查 railName 围栏名
        if (!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        UserSchool userSchool = schoolService.getParaByUserId(userId, new Date());
        if (null == userSchool || null == userSchool.getSchoolId() || null == userSchool.getSemesterId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }
        String railName = json.get("railName").toString();
        log.info("deleteRailByName params={}",json);
        //************************************** 参数准备完毕，开始更新数据 ****************************************//
        ruleCacheService.deleteRailByName(userSchool.getSemesterId(), railName);
        log.info("delete rail success");
        return Response.success();
    }


    /**
     * 判断某学校围栏名是否存在
     * @param token
     * @param userId
     * @param sign
     * @param json
     * @return
     * @throws Exception
     */
    @Register
    @RequestMapping(value = {"/searchRailByName"}, method = {RequestMethod.POST})
    @SystemControllerLog(description = "根据学校围栏名查询围栏-pc")
    @ApiOperation(value = "根据学校围栏名查询围栏-pc")
    public Response<?> searchRailByName(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "老师ID") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "围栏名等") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"railName"};//必填项检查 railName 围栏名
        if (!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        UserSchool userSchool = schoolService.getParaByUserId(userId, new Date());
        if (null == userSchool || null == userSchool.getSchoolId() || null == userSchool.getSemesterId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }
        String railName = json.get("railName").toString();
        log.info("searchRailByName params={}",json);
        Boolean flag =  ruleCacheService.searchRailByName(userSchool.getSemesterId(), railName);
        log.info("searchRailByName - parames : semesterId = {} ,railName = {}  res ={}", userSchool.getSemesterId(),railName,flag);
        Map res = new HashMap<>();
        res.put("exist",flag); //对应学期下是否存在围栏名
        return Response.success(res);
    }
}
