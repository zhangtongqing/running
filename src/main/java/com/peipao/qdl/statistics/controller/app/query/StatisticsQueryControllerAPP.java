package com.peipao.qdl.statistics.controller.app.query;


import com.peipao.framework.annotation.Register;
import com.peipao.framework.annotation.Visitor;
import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.constant.WebConstants;
import com.peipao.framework.model.Response;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.page.MyPage;
import com.peipao.framework.page.MyPageInfo;
import com.peipao.framework.util.DateUtil;
import com.peipao.framework.util.StringUtil;
import com.peipao.framework.util.ValidateUtil;
import com.peipao.qdl.discover.model.SortEnum;
import com.peipao.qdl.running.model.RunningEffectiveEnum;
import com.peipao.qdl.running.model.RunningEnum;
import com.peipao.qdl.runningrule.model.vo.RunningRuleVo;
import com.peipao.qdl.runningrule.service.utils.RuleUtilService;
import com.peipao.qdl.school.model.School;
import com.peipao.qdl.school.model.UserSchool;
import com.peipao.qdl.school.service.SchoolCacheService;
import com.peipao.qdl.school.service.SchoolService;
import com.peipao.qdl.statistics.model.SortTypeEnum;
import com.peipao.qdl.statistics.service.RunningStatisticService;
import com.peipao.qdl.statistics.service.UserStatisticCacheService;
import com.peipao.qdl.statistics.service.UserStatisticUtilService;
import com.peipao.qdl.user.model.User;
import com.peipao.qdl.user.model.UserTypeEnum;
import com.peipao.qdl.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.peipao.qdl.running.model.RunningEnum.*;

/**
 * 方法名称：StatisticsQueryControllerAPP
 * 功能描述：综合信息统计查询
 * 作者：Liu Fan
 * 版本：2.0.10
 * 创建日期：2018/01/16 11:58
 * 修订记录：
 */

@Api(value = "/statistics/app",description = "综合信息统计查询")
@RestController
@RequestMapping({"/statistics/app"})
public class StatisticsQueryControllerAPP {
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private RunningStatisticService runningStatisticService;
    @Autowired
    private RuleUtilService ruleUtilService;
    @Autowired
    private UserStatisticUtilService userStatisticUtilService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserStatisticCacheService userStatisticCacheService;
    @Autowired
    private SchoolCacheService schoolCacheService;


    @Register
    @RequestMapping(value = {"/getMyStatistics"},method = {RequestMethod.GET})
//    @SystemControllerLog(description = "数据中心")
    @ApiOperation(value = "getMyStatistics", notes = "数据中心")
    public Response<?> getMyStatistics(
        @ApiParam(required = true, value = "token") @RequestParam String token,
        @ApiParam(required = true, value = "userId") @RequestParam Long userId,
        @ApiParam(required = true, value = "签名") @RequestParam String sign
    ) throws Exception {
        UserSchool userSchool = schoolService.getParaByUserId(userId,  new Date());
        if(null == userSchool || null == userSchool.getSemesterId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("userId", userId);
        paramsMap.put("schoolId", userSchool.getSchoolId());
        return Response.success(getTheStatistics(paramsMap, userSchool.getSemesterId()));
    }


    /**
     *  自由跑、晨跑、晨练 获取目标数据/完成数据
     * @param token
     * @param userId
     * @param sign
     * @return
     * @throws Exception
     */
    @Register
    @RequestMapping(value = {"/getActivityTargetCompleteByType"},method = {RequestMethod.POST})
    @ApiOperation(value = "getActivityTargetCompleteByType", notes = "自由跑、晨跑、晨练 获取目标数据/完成数据")
    public Response<?> getActivityTargetCompleteByType(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "RequestBody type:运动类型,自由跑=4; 晨跑=1;晨练=10") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"type"};//必填项 有效标识
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params) && !ValidateUtil.isDigits(json.get("type").toString())) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        Long type = Long.parseLong(json.get("type").toString());
        UserSchool userSchool = schoolService.getParaByUserId(userId,  new Date());
        if(null == userSchool || null == userSchool.getSemesterId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("userId", userId);
        paramsMap.put("schoolId", userSchool.getSchoolId());
        paramsMap.put("semesterId", userSchool.getSemesterId());
        paramsMap.put("type",type);
        return Response.success(getAcTargetComInfo(paramsMap, type));
    }

    @Register
    @RequestMapping(value = {"/getSemesterTargetStatistics"},method = {RequestMethod.GET})
//    @SystemControllerLog(description = "本学期已达标人数统计--教务端")
    @ApiOperation(value = "getSemesterTargetStatistics", notes = "本学期已达标人数统计--教务端")
    public Response<?> getSemesterTargetStatistics(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign
    ) throws Exception {
        UserSchool userSchool = schoolService.getParaByUserId(userId,  new Date());
        if(null == userSchool || null == userSchool.getSemesterId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }
        JSONObject resJson = ruleUtilService.getTargetValuesJson(userSchool.getSemesterId());//加载得分规则
        String[] params = new String[]{"morningRunningCountTarget","freeRunningLengthTarget"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(resJson, params)) {
            return Response.fail(ReturnStatus.PARAMETER_MISS);//必填项请求参数不完整，请检查
        }
        User currUser = userService.validateSchoolUser(userId);//当前操作的用户(教师或学校管理员)
        Integer morningRunningCountTarget = Integer.parseInt(resJson.getString("morningRunningCountTarget"));
        Float freeRunningLengthTarget = Float.parseFloat(resJson.getString("freeRunningLengthTarget"));
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("semesterId", userSchool.getSemesterId());
        paramsMap.put("target", morningRunningCountTarget);
        if(currUser.getUserType() == UserTypeEnum.TEACHER.getValue()) {
            userStatisticUtilService.initCourseArrayString(paramsMap, currUser.getUserId(), userSchool.getSemesterId());
        }
        Integer morningRunningCount = runningStatisticService.getMorningRunningTargetStatistics(paramsMap);
        paramsMap.put("target", freeRunningLengthTarget);
        Integer freeRunningCount = runningStatisticService.getFreeRunningTargetStatistics(paramsMap);
        resJson = new JSONObject();
        resJson.accumulate("morningRunningCount", morningRunningCount);
        resJson.accumulate("freeRunningCount", freeRunningCount);
        String courseArray = null;
        if(paramsMap.containsKey("courseArray")) {
            courseArray = paramsMap.get("courseArray").toString();
        }
        paramsMap.clear();
        paramsMap.put("schoolId", userSchool.getSchoolId());
        paramsMap.put("userType", UserTypeEnum.STUDENT.getValue());
        paramsMap.put("status", WebConstants.Boolean.TRUE.ordinal());
        if(currUser.getUserType() == UserTypeEnum.TEACHER.getValue()) {
            paramsMap.put("courseArray", courseArray);
        }
        Integer count = userService.getSchoolUserCount(paramsMap);
        resJson.accumulate("studentNum", count);
        return Response.success(resJson);
    }



    @Register
    @RequestMapping(value = {"/getMyStdentTargetList"},method = {RequestMethod.POST})
//    @SystemControllerLog(description = "查询成绩列表--教务端")
    @ApiOperation(value = "getMyStdentTargetList", notes = "查询成绩列表--教务端")
    public Response<?> getMyStdentTargetList(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "RequestBody 翻页参数+查询条件") @RequestBody JSONObject json
    ) throws Exception {
        int[] pageParams = MyPage.isValidParams(json);
        json.remove("pageindex");
        json.remove("pagesize");
        UserSchool userSchool = schoolService.getParaByUserId(userId, new Date());
        if (null == userSchool || null == userSchool.getSemesterId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }
        Map<String, Object> paramsMap = new HashMap<>();
        String queryString = null;
        if(ValidateUtil.jsonValidateWithKey(json, "queryString")) {
            queryString = json.getString("queryString").trim();
            if(ValidateUtil.hasDigit(queryString)) {
                paramsMap.put("queryType", "student_no");
            } else if(ValidateUtil.isChinese(queryString)) {
                paramsMap.put("queryType", "username");
            } else {
                paramsMap.put("queryType", "username");
            }
            paramsMap.put("queryString", queryString);
        }
        if(ValidateUtil.jsonValidateWithKey(json, "sortName")) {
            String sortName = json.getString("sortName");
            if(sortName.equals(SortTypeEnum.freeRunningLength.name())) {
                paramsMap.put("sortName", SortTypeEnum.freeRunningLength.name());
            } else if(sortName.equals(SortTypeEnum.morningRunningCount.name())) {
                paramsMap.put("sortName", SortTypeEnum.morningRunningCount.name());
            } else {
                paramsMap.put("sortName", SortTypeEnum.freeRunningLength.name());
            }
        } else {
            paramsMap.put("sortName", SortTypeEnum.freeRunningLength.name());
        }
        paramsMap.put("sortType", SortEnum.DESC.name());
        paramsMap.put("schoolId", userSchool.getSchoolId());
        paramsMap.put("semesterId", userSchool.getSemesterId());
        User currUser = userService.validateSchoolUser(userId);//当前操作的用户(教师或学校管理员)
        if(currUser.getUserType() == UserTypeEnum.TEACHER.getValue()) {
            userStatisticUtilService.initCourseArrayString(paramsMap, currUser.getUserId(), userSchool.getSemesterId());
        }
        MyPageInfo pageInfo = runningStatisticService.getMyStdentTargetList(paramsMap, pageParams);
        return Response.success(pageInfo);
    }


    @Visitor
    @RequestMapping(value = {"/getStatistics"},method = {RequestMethod.GET})
//    @SystemControllerLog(description = "教务端首页统计")
    @ApiOperation(value = "getStatistics", notes = "教务端首页统计")
    public Response<?> getStatistics(
            @ApiParam(required = true, value = "userId") @RequestParam Long userId
    ) throws Exception {
        UserSchool userSchool = schoolService.getParaByUserId(userId,  new Date());
        if(null == userSchool || null == userSchool.getSchoolId() || null == userSchool.getSemesterId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }
        JSONObject resJson = ruleUtilService.getTargetValuesJson(userSchool.getSemesterId());//加载得分规则
        String[] params = new String[]{"morningRunningCountTarget","freeRunningLengthTarget"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(resJson, params)) {
            return Response.fail(ReturnStatus.PARAMETER_MISS);//必填项请求参数不完整，请检查
        }

        Integer morningRunningCountTarget = Integer.parseInt(resJson.getString("morningRunningCountTarget"));//晨跑次数--学期目标
        Float freeRunningLengthTarget = Float.parseFloat(resJson.getString("freeRunningLengthTarget"));//自由跑里程--学期目标

        User currUser = userService.validateSchoolUser(userId);//当前操作的用户(教师或学校管理员)
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("semesterId", userSchool.getSemesterId());
        if(currUser.getUserType() == UserTypeEnum.TEACHER.getValue()) {
            userStatisticUtilService.initCourseArrayString(paramsMap, currUser.getUserId(), userSchool.getSemesterId());
        }
        //统计晨跑次数和里程
        Map<String, Object> morningRunningMap = runningStatisticService.getMorningRunningStatistics(paramsMap);
        //统计自由跑次数和里程
        Map<String, Object>  freeRunningMap = runningStatisticService.getFreeRunningStatistics(paramsMap);

        int morningRunningCount = 0, freeRunningCount = 0;
        float morningRunningLength = 0f, freeRunningLength = 0f;
        if(!CollectionUtils.isEmpty(morningRunningMap)) {
            if(null != morningRunningMap.get("morningRunningCount")) {
                morningRunningCount = Integer.parseInt(morningRunningMap.get("morningRunningCount").toString());
            }

            if(null != morningRunningMap.get("morningRunningLength")) {
                morningRunningLength = Float.parseFloat(morningRunningMap.get("morningRunningLength").toString());
            }
        }
        if(!CollectionUtils.isEmpty(freeRunningMap)) {
            if(null != freeRunningMap.get("freeRunningCount")) {
                freeRunningCount = Integer.parseInt(freeRunningMap.get("freeRunningCount").toString());
            }
            if(null != freeRunningMap.get("freeRunningLength")) {
                freeRunningLength = Float.parseFloat(freeRunningMap.get("freeRunningLength").toString());
            }
        }

        String courseArray = null;
        if(paramsMap.containsKey("courseArray")) {
            courseArray = paramsMap.get("courseArray").toString();
        }
        paramsMap.clear();
        paramsMap.put("schoolId", userSchool.getSchoolId());
        paramsMap.put("userType", UserTypeEnum.STUDENT.getValue());
        paramsMap.put("status", WebConstants.Boolean.TRUE.ordinal());
        if(currUser.getUserType() == UserTypeEnum.TEACHER.getValue()) {
            paramsMap.put("courseArray", courseArray);
        }
        Integer count = userService.getSchoolUserCount(paramsMap);//统计总人数
        String morningRunningCountPerCapitaStr = "0", freeRunningLengthPerCapitaStr = "0";
        if(null!= count && count > 0 && morningRunningCount > 0) {
            float morningRunningCountPerCapita = morningRunningCount/Float.parseFloat(count.toString());//晨跑人均完成次数
            int m = (int)morningRunningCountPerCapita;
            morningRunningCountPerCapitaStr = morningRunningCountPerCapita % 1 + "";
            if(morningRunningCountPerCapitaStr.length() >= "#.##".length()) {
                morningRunningCountPerCapitaStr = morningRunningCountPerCapitaStr.substring(1, "#.##".length());
            } else if(morningRunningCountPerCapitaStr.length() >= "#.#".length()) {
                morningRunningCountPerCapitaStr = morningRunningCountPerCapitaStr.substring(1, "#.#".length());
            }
            morningRunningCountPerCapitaStr = m + morningRunningCountPerCapitaStr;
        }
        if(null!= count && count > 0 && freeRunningLength > 0) {
            float freeRunningLengthPerCapita = freeRunningLength/count;//自由跑人均完成里程数
            int f = (int)freeRunningLengthPerCapita;
            freeRunningLengthPerCapitaStr = freeRunningLengthPerCapita % 1 + "";
            if(freeRunningLengthPerCapitaStr.length() > "#.##".length()) {
                freeRunningLengthPerCapitaStr = freeRunningLengthPerCapitaStr.substring(1, "#.##".length());
            } else if(freeRunningLengthPerCapitaStr.length() > "#.#".length()) {
                freeRunningLengthPerCapitaStr = freeRunningLengthPerCapitaStr.substring(1, "#.#".length());
            }
            freeRunningLengthPerCapitaStr = f + freeRunningLengthPerCapitaStr;
        }

        paramsMap.clear();
        paramsMap.put("statisticId", userSchool.getSemesterId());
        paramsMap.put("type", MORNINGRUNNING.getValue());//查询晨跑
        paramsMap.put("isEffective", RunningEffectiveEnum.Success.getCode());//仅查询有效记录
        if(StringUtil.isNotEmpty(courseArray)){
            paramsMap.put("courseArray", courseArray);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 7);
        String beginTime = DateUtil.dateToStr(calendar.getTime(), "YYYY-MM-dd") + " 00:00:00";
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 6);
        String endTime = DateUtil.dateToStr(calendar.getTime(), "YYYY-MM-dd") + " 23:59:59";
        //晨跑7天内数据补点
        List<Map<String, Object>> morningList = userStatisticCacheService.getMorningRunWeekCount(userId, paramsMap);
        //生成过去7天的日期字符串
        List<Date> sevenDates = DateUtil.getBetweenDates(DateUtil.parseDate(beginTime, "yyyy-MM-dd HH:mm:ss"), DateUtil.parseDate(endTime, "yyyy-MM-dd HH:mm:ss"));
        //晨跑日期补点
        List<Map<String, Object>> morningRunWeekList = new ArrayList<Map<String, Object>>();
        init7DaysInfo(sevenDates, morningList, morningRunWeekList);

        //自由跑数据补点
        paramsMap.put("type", FREERUNNING.getValue());//查询自由跑
        List<Map<String, Object>> freeList = userStatisticCacheService.getFreeRunWeekCount(userId, paramsMap);

        List<Map<String, Object>> freeRunWeekList = new ArrayList<Map<String, Object>>();
        init7DaysInfo(sevenDates, freeList, freeRunWeekList);
        //-------------------------------------------- 查询数据完毕 ---------------------------------------------------/
         /*
        学期目标    管理学生人数    人均完成目标数
        本学期累计:
        有效晨跑2561 次，有效晨跑里程 236523.15公里；
        有效自由跑2555 次，计分自由跑里程 236523.15公里。
         */
        School school = schoolCacheService.getSchoolById(userSchool.getSchoolId());
        String schoolName = school.getName();
        /********************************************* 开始构造返回数据 ***********************************************/
        resJson = new JSONObject();
        resJson.put("studentNum", count);//管理学生人数
        resJson.put("morningRunningCountPerCapita", morningRunningCountPerCapitaStr);//晨跑人均完成次数
        resJson.put("morningRunningCountTarget", morningRunningCountTarget);//晨跑次数--学期目标
        resJson.put("freeRunningLengthPerCapita", freeRunningLengthPerCapitaStr);////自由跑人均完成里程数
        resJson.put("freeRunningLengthTarget", freeRunningLengthTarget);//自由跑里程--学期目标
        resJson.put("morningRunningCount", morningRunningCount);//本学期累计晨跑总次数
        resJson.put("morningRunningLength", morningRunningLength);//本学期累计晨跑总里程
        resJson.put("freeRunningCount", freeRunningCount);//本学期累计自由跑总次数
        resJson.put("freeRunningLength", freeRunningLength);//本学期累计自由跑总里程
        resJson.put("morningRunWeekList", morningRunWeekList);
        resJson.put("freeRunWeekList", freeRunWeekList);
        resJson.put("userName", currUser.getUsername());
        resJson.put("schoolName", schoolName);
        resJson.put("today", DateUtil.dateToStr(Calendar.getInstance().getTime(), "YYYY-MM-dd"));
        return Response.success(resJson);
    }


    @Register
    @RequestMapping(value = {"/getStudentStatistics"},method = {RequestMethod.POST})
//    @SystemControllerLog(description = "教务端查看学生数据中心")
    @ApiOperation(value = "getStudentStatistics", notes = "教务端查看学生数据中心")
    public Response<?> getStudentStatistics(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "studentId") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"studentId"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ReturnStatus.PARAMETER_MISS);//必填项请求参数不完整，请检查
        }
        UserSchool userSchool = schoolService.getParaByUserId(userId,  new Date());
        if(null == userSchool || null == userSchool.getSemesterId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("userId", json.getLong("studentId"));
        paramsMap.put("schoolId", userSchool.getSchoolId());
        return Response.success(getTheStatistics(paramsMap, userSchool.getSemesterId()));
    }

    /**根据运动类型获取 目标/完成数据
     * @param paramsMap
     * @return
     * @throws Exception
     */
    private Map<String, Object> getAcTargetComInfo(Map<String, Object> paramsMap,Long type) throws Exception {
        Map<String, Object> resMap = new HashMap<>();
        Map<String, Object> myTargetMap = runningStatisticService.getMyCurrentStatistic(paramsMap); //当前用户 自由跑，晨跑、晨练 数据
        JSONObject resJson = ruleUtilService.getTargetValuesJson(Long.parseLong(paramsMap.get("semesterId").toString()));

        if (type == RunningEnum.FREERUNNING.getValue()) {//自由跑
            resMap.put("overCount", null == myTargetMap ? 0 : myTargetMap.get("freeRunningLength"));
            resMap.put("targetCount", resJson.get("freeRunningLengthTarget"));
        } else if (type == RunningEnum.MORNINGRUNNING.getValue()) {//晨跑
            resMap.put("overCount", null == myTargetMap  ? 0 : myTargetMap.get("morningRunningCount"));
            resMap.put("targetCount", resJson.get("morningRunningCountTarget"));
        } else if (type == RunningEnum.MORNINGTRAINING.getValue()) {//晨练
            RunningRuleVo runningRule = ruleUtilService.getRunningRuleBySemesterIdAndType(Long.parseLong(paramsMap.get("semesterId").toString()), MORNINGTRAINING.getValue());
            if(null != runningRule && runningRule.getIsUse() == WebConstants.Boolean.TRUE.ordinal()) {
                resMap.put("overCount", null == myTargetMap ? 0 : myTargetMap.get("morningTrainCount"));
                resMap.put("targetCount", resJson.get("morningTrainingTarget"));
            }
        }else{
            resMap.put("overCount", 0);
            resMap.put("targetCount", 0);
        }

        return resMap;

    }


    private List<Map<String, Object>> getTheStatistics(Map<String, Object> paramsMap, long semesterId) throws Exception {
        List<Map<String, Object>> resList = runningStatisticService.getMyStatistics(paramsMap);
        if(!CollectionUtils.isEmpty(resList)) {
            RunningRuleVo runningRule = ruleUtilService.getRunningRuleBySemesterIdAndType(semesterId, MORNINGTRAINING.getValue());
            if(null == runningRule || runningRule.getIsUse() == WebConstants.Boolean.FALSE.ordinal()) {
                resList.forEach(map -> map.remove("morningTrainCount"));
            }
            return resList;
        }else {
            return new ArrayList<>();
        }
    }

    private void init7DaysInfo(List<Date> dates, List<Map<String, Object>> weekListMap, List<Map<String, Object>> weekDayList) {
        for(Date date :dates){
            String tempDate = DateUtil.dateToStr(date,"yyyy-MM-dd");
            boolean hasDay = false;
            Map<String, Object> tempMap = new HashMap<>();
            for(Map<String, Object> map : weekListMap) {
                String dayStr = map.get("day").toString();
                if(dayStr.equals(tempDate)) {
                    hasDay = true;
                    tempMap = map;
                    break;
                }
            }
            if(hasDay){
                weekDayList.add(tempMap);
            }else {
                Map<String, Object> dayMap = new HashMap<>();
                dayMap.put("count",0);
                dayMap.put("day",tempDate);
                weekDayList.add(dayMap);
            }
        }
    }

}
