package com.peipao.qdl.activity.controller;


import com.peipao.framework.annotation.Register;
import com.peipao.framework.annotation.SystemControllerLog;
import com.peipao.framework.constant.ReturnConstant;
import com.peipao.framework.exception.BusinessException;
import com.peipao.framework.model.Response;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.page.MyPage;
import com.peipao.framework.page.MyPageInfo;
import com.peipao.framework.util.ExcelExport;
import com.peipao.framework.util.JsonUtils;
import com.peipao.framework.util.StringUtil;
import com.peipao.framework.util.ValidateUtil;
import com.peipao.qdl.activity.model.Activity;
import com.peipao.qdl.activity.model.ActivityMemberTypeEnum;
import com.peipao.qdl.activity.model.ActivityStatusEnum;
import com.peipao.qdl.activity.model.ActivityTypeEnum;
import com.peipao.qdl.activity.service.ActivityService;
import io.swagger.annotations.*;
import net.sf.json.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author meteor.wu
 * @since 2017/6/26
 **/
@Api(value = "/activity/pc", description = "课程")
@RestController
@RequestMapping("/activity/pc")
public class ActivityControllerPC {
    @Autowired
    private ActivityService activityService;

    @Register
    @RequestMapping(value = {"/getActivityMemberList"},method = {RequestMethod.POST})
//    @SystemControllerLog(description = "查询活动成员列表pc")
    @ApiOperation("查询活动成员列表(活动管理-活动详情-成员)--11--ok")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
                   @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
                   @ApiResponse(code = ReturnConstant.SCHOOL_NOT_EXIST.value, message = ReturnConstant.SCHOOL_NOT_EXIST.desc),
                   @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<Map<String, Object>> getActivityMemberListForWeb(
            @ApiParam(required = true,value = "token") @RequestParam String token,
            @ApiParam(required = true,value = "userId") @RequestParam Long userId,
            @ApiParam(required = true,value = "json:activityId pageindex pagesize") @RequestBody JSONObject json,
            @ApiParam(required = true,value = "签名") @RequestParam String sign) throws Exception {

        int[] page = MyPage.isValidParams(json);
        Map<String, Object> ret = getActivityMemberList(userId, json, page);
        return Response.success(ret);
    }


    /**
     * 修改 活动成员列表 分页
     * 2018-03-30
     */
    @Register
    @RequestMapping(value = {"/officialActivityMemberList"},method = {RequestMethod.POST})
    @ApiOperation("查询官方活动成员列表new")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.SCHOOL_NOT_EXIST.value, message = ReturnConstant.SCHOOL_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<?> officialActivityMemberList(
            @ApiParam(required = true,value = "token") @RequestParam String token,
            @ApiParam(required = true,value = "userId") @RequestParam Long userId,
            @ApiParam(required = true,value = "json:activityId pageindex pagesize") @RequestBody JSONObject json,
            @ApiParam(required = true,value = "签名") @RequestParam String sign) throws Exception {


        int[] page = MyPage.isValidParams(json);
        json.remove("pageindex");
        json.remove("pagesize");
        Map<String, Object> paramsMap = JsonUtils.getMap4Json(json.toString());
        if(!ValidateUtil.jsonValidateWithKey(json, "status")) {
            paramsMap.remove("status");
        }
        if(!ValidateUtil.jsonValidateWithKey(json, "searchkey")) {
            paramsMap.remove("searchkey");
        }
        paramsMap.put("userId",userId);
        MyPageInfo pageInfo = activityService.getActivityMemberListForWebNew(paramsMap, page[0], page[1]);
        return Response.success(pageInfo);
    }

    /**
     * 导出官方某活动对应的成员列表
     * */
    @Register
    @RequestMapping(value = {"/officialActivityMemberListExport"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "导出官方某活动成员列表")
    @ApiOperation("导出官方某活动成员列表(活动管理-活动详情-活动成员-导出)")
    public Response<?> officialActivityMemberListExport(
            HttpServletResponse response, HttpServletRequest request,
            @ApiParam(required = true,value = "token") @RequestParam String token,
            @ApiParam(required = true,value = "userId") @RequestParam Long userId,
            @ApiParam(required = true,value = "json:activityId pageindex pagesize") @RequestBody JSONObject json,
            @ApiParam(required = true,value = "签名") @RequestParam String sign) throws Exception {

        int[] page = MyPage.isValidParams(json);
        json.remove("pageindex");
        json.remove("pagesize");
        Activity activity = null;

        if(ValidateUtil.jsonValidateWithKey(json, "activityId")) {
            activity = activityService.getActivityDetailForShare(Long.parseLong(json.get("activityId").toString()));
        }else {
            throw new BusinessException(ReturnStatus.PARAMETER_INCORRECT);
        }
        Map<String, Object> paramsMap = JsonUtils.getMap4Json(json.toString());
        if(!ValidateUtil.jsonValidateWithKey(json, "status")) {
            paramsMap.remove("status");
        }
        if(!ValidateUtil.jsonValidateWithKey(json, "mobile")) {
            paramsMap.remove("mobile");
        }
        paramsMap.put("userId", userId);
        MyPageInfo pageInfo = activityService.getActivityMemberListForWebNew(paramsMap, page[0], page[1]);
        List<Map<String, Object>> list = (List<Map<String, Object>>)pageInfo.getData();
        String downloadPath = officialCreateExcel(response, request, list,activity.getName());
        Map<String,Object> resjson = new HashMap<String,Object>();
        resjson.put("path",downloadPath);
        resjson.put("size",list.size());
        return Response.success(resjson);
    }


    /**
     * 参与活动跑步详细记录
     * @param response
     * @param request
     * @param token
     * @param userId
     * @param json
     * @param sign
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/activityRunningRecord"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "活动参与详情")
    @ApiOperation("活动参与详情)")
    public Response<?> activityRunningRecord(
            HttpServletResponse response, HttpServletRequest request,
            @ApiParam(required = true,value = "token") @RequestParam String token,
            @ApiParam(required = true,value = "userId") @RequestParam Long userId,
            @ApiParam(required = true,value = "json:activityId pageindex pagesize") @RequestBody JSONObject json,
            @ApiParam(required = true,value = "签名") @RequestParam String sign) throws Exception {

        if(!ValidateUtil.jsonValidateWithKey(json, "activityId") && !ValidateUtil.jsonValidateWithKey(json, "memberId")) {
            throw new BusinessException(ReturnStatus.PARAMETER_INCORRECT);
        }
        Map<String, Object> paramsMap = new HashMap<String,Object>();
        paramsMap.put("activityId",json.get("activityId"));
        paramsMap.put("userId",json.get("memberId"));
        List<Map<String, Object>> runningRecord = activityService.getActivityRunningRecordByUserId(paramsMap);
        return Response.success(runningRecord);
    }



    /**
     * 官方活动参与人员列表导出excel
     */
    private String officialCreateExcel(HttpServletResponse response, HttpServletRequest request, List<Map<String, Object>> list, String activityName) throws Exception {
        HSSFWorkbook wbarray = new HSSFWorkbook();
        HSSFSheet sheet = wbarray.createSheet( "第1页" );
        HSSFRow row = null;
        HSSFCell cell = null;

        int totalnum = list.size();// 总共多少条数据list
        for ( int r = 0; r < totalnum + 1; r++ ) {
            row = sheet.createRow( r );
            row.setHeight( ( short ) 350 );
            for ( int col = 0; col < 9; col++ ) {
                cell = row.createCell( col );
            }
        }

        for ( int j = 0; j <= totalnum; j++ ) {
            row = sheet.getRow(j);
            cell = row.getCell(0);
            if ( j == 0 )
                cell.setCellValue( "参与成员" );
            else{
                cell.setCellValue( list.get(j-1).get("username").toString() );
            }

            cell = row.getCell( 1 );
            if ( j == 0 )
                cell.setCellValue( "手机号码" );
            else{
                cell.setCellValue( list.get(j-1).get("mobile").toString() );
            }

            cell = row.getCell( 2 );
            if ( j == 0 )
                cell.setCellValue( "学号" );
            else{
                cell.setCellValue( list.get(j-1).get("studentNO").toString() );
            }

            cell = row.getCell( 3 );
            if ( j == 0 )
                cell.setCellValue( "所属院校" );
            else{
                cell.setCellValue( list.get(j-1).get("schoolName").toString() );
            }

            cell = row.getCell( 4 );
            if ( j == 0 )
                cell.setCellValue( "参与次数" );
            else{
                cell.setCellValue( list.get(j-1).get("activityCount").toString() );
            }

            cell = row.getCell( 5 );
            if ( j == 0 )
                cell.setCellValue( "成功次数" );
            else{
                cell.setCellValue( list.get(j-1).get("activitySucCount").toString() );
            }

            cell = row.getCell( 6 );
            if ( j == 0 )
                cell.setCellValue( "运动里程(公里)" );
            else{
                cell.setCellValue( list.get(j-1).get("runningLength").toString() );
            }

            cell = row.getCell( 7 );
            if ( j == 0 )
                cell.setCellValue( "运动时长" );
            else{
                cell.setCellValue( list.get(j-1).get("runningDuration").toString() );
            }

        }
        return ExcelExport.createExcel(response, request, activityName +"_活动参与信息导出", wbarray);
    }




    @Register
    @RequestMapping(value = {"/activityMemberListExport"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "导出活动成员列表")
    @ApiOperation("导出活动成员列表(活动管理-活动详情-成员)--ok")
    public Response<?> activityMemberListExport(
            HttpServletResponse response, HttpServletRequest request,
            @ApiParam(required = true,value = "token") @RequestParam String token,
            @ApiParam(required = true,value = "userId") @RequestParam Long userId,
            @ApiParam(required = true,value = "json:activityId pageindex pagesize") @RequestBody JSONObject json,
            @ApiParam(required = true,value = "签名") @RequestParam String sign) throws Exception {
        Map<String, Object> ret = getActivityMemberList(userId, json, null);
        List<Map<String, Object>> list = (List<Map<String, Object>>) ret.get("data");
        if (list != null && list.size() > 0) {
            String downloadPath = createExcel(response, request, list, ret.get("name").toString(), json.getInt("status"));
            json.put("path", downloadPath);
            json.put("size", list.size());
        } else {
            json.put("size", 0);
        }
        json.remove("status");
        json.remove("activityId");
        return Response.success(json);
    }

    private String createExcel(HttpServletResponse response, HttpServletRequest request, List<Map<String, Object>> list, String activityName, int status) throws Exception {
        HSSFWorkbook wbarray = new HSSFWorkbook();
        HSSFSheet sheet = wbarray.createSheet( "第1页" );
        HSSFRow row = null;
        HSSFCell cell = null;

        int totalnum = list.size();// 总共多少条数据list
        for ( int r = 0; r < totalnum + 1; r++ ) {
            row = sheet.createRow( r );
            row.setHeight( ( short ) 350 );
            for ( int col = 0; col < 9; col++ ) {
                cell = row.createCell( col );
            }
        }

        for ( int j = 0; j <= totalnum; j++ ) {
            row = sheet.getRow( j );
            cell = row.getCell( 0 );
            if ( j == 0 )
                cell.setCellValue( "序号" );
            else{
                cell.setCellValue(j);
            }

            cell = row.getCell( 1 );
            if ( j == 0 )
                cell.setCellValue( "学号" );
            else{
                if(null != list.get(j-1).get("studentNO")) {
                    cell.setCellValue( list.get(j-1).get("studentNO").toString() );
                } else {
                    cell.setCellValue( "" );
                }
            }
            cell = row.getCell( 2 );
            if ( j == 0 )
                cell.setCellValue( "姓名" );
            else
                cell.setCellValue( list.get(j-1).get("username").toString() );

            cell = row.getCell( 3 );
            if ( j == 0 )
                cell.setCellValue( "手机" );
            else
                cell.setCellValue( list.get(j-1).get("mobile").toString() );

            cell = row.getCell( 4 );
            if ( j == 0 )
                cell.setCellValue( "参与活动次数" );
            else
                cell.setCellValue( list.get(j-1).get("activityCount").toString() );

            cell = row.getCell( 5 );
            if ( j == 0 )
                cell.setCellValue( "运动里程" );
            else {
                cell.setCellValue( list.get(j-1).get("runningLength").toString() );
            }

            cell = row.getCell( 6 );
            if ( j == 0 )
                cell.setCellValue( "运动时长" );
            else {
                cell.setCellValue( list.get(j-1).get("runningDuration").toString() );
            }

            cell = row.getCell( 7 );
            if ( j == 0 )
                cell.setCellValue( "卡路里" );
            else {
                cell.setCellValue( list.get(j-1).get("calorieCount").toString() );
            }

            cell = row.getCell( 8 );
            if ( j == 0 )
                cell.setCellValue( "成功次数" );
            else {
                cell.setCellValue( list.get(j-1).get("activitySucCount").toString() );
            }
        }
        return ExcelExport.createExcel(response, request, activityName +"_活动成员" + (status == 1? "_已完成" : "_未完成"), wbarray);
    }

    private Map<String, Object> getActivityMemberList(Long userId, JSONObject json, int[] page) throws Exception {
        if (!json.containsKey("activityId") || !json.containsKey("status")) {
            throw new BusinessException(ReturnStatus.PARAMETER_EMPTY);
        }
        if (!ValidateUtil.isDigits(json.getString("activityId")) || !ValidateUtil.isDigits(json.getString("status"))) {
            throw new BusinessException(ReturnStatus.PARAMETER_INCORRECT);
        }
        Long activityId = json.getLong("activityId");

        int status = json.getInt("status");
        if (null == page) {
            return activityService.getActivityMemberListForWeb(userId, activityId, -1,-1, status);
        } else {
            return activityService.getActivityMemberListForWeb(userId, activityId, page[0], page[1], status);
        }
    }

    @Register
    @RequestMapping(value = {"/getStudentFinishActivityList"},method = {RequestMethod.POST})
//    @SystemControllerLog(description = "查询学生已经完成活动成绩")
    @ApiOperation(value = "查询学生已经完成活动成绩(学生主页活动成绩)--26--ok")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.NO_PERMISSION.value, message = ReturnConstant.NO_PERMISSION.desc),
            @ApiResponse(code = ReturnConstant.COURSE_SCHEDULE_NOT_EXIST.value, message = ReturnConstant.COURSE_SCHEDULE_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<Map<String, Object>> getStudentFinishActivityList(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "json包含studentId pageindex pagesize") @RequestBody JSONObject json,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {

        if (!json.containsKey("studentId")){
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        if (!ValidateUtil.isDigits(json.getString("studentId"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        Long studentId = json.getLong("studentId");
        int[] page = MyPage.isValidParams(json);
        Map<String, Object> ret = activityService.getStudentFinishActivityList(userId, studentId, page[0], page[1]);
        return Response.success(ret);
    }

    @Register
    @RequestMapping(value = {"/getStudentNonFinishActivityList"},method = {RequestMethod.POST})
//    @SystemControllerLog(description = "查询学生无效活动成绩")
    @ApiOperation(value = "查询学生无效活动成绩(学生主页活动成绩)--27--ok")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.NO_PERMISSION.value, message = ReturnConstant.NO_PERMISSION.desc),
            @ApiResponse(code = ReturnConstant.COURSE_SCHEDULE_NOT_EXIST.value, message = ReturnConstant.COURSE_SCHEDULE_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<Map<String, Object>> getStudentNonEffectiveActivityList(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "json包含studentId") @RequestBody JSONObject json,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {

        if (!json.containsKey("studentId")){
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        if (!ValidateUtil.isDigits(json.getString("studentId"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        Long studentId = json.getLong("studentId");
        int[] page = MyPage.isValidParams(json);
        Map<String, Object> ret = activityService.getStudentNonFinishActivityList(userId, studentId, page[0], page[1]);
        return Response.success(ret);
    }

    @Register
    @RequestMapping(value = {"/addActivity"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "添加活动")
    @ApiOperation(value = "添加活动(活动管理-创建活动)--9--ok")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.NO_PERMISSION.value, message = ReturnConstant.NO_PERMISSION.desc),
            @ApiResponse(code = ReturnConstant.COURSE_SCHEDULE_NOT_EXIST.value, message = ReturnConstant.COURSE_SCHEDULE_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<Map> addActivity(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "activity") @RequestBody Activity activity,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (activity.getStatus() == ActivityStatusEnum.UNENROLL.getValue()) {
            ReturnStatus rs = validateActivity(activity, false, false);
            if (!rs.equals(ReturnStatus.SUCCESS)) { //校验数据合法性
                return Response.fail(rs);
            }
        }
        activity.setUserId(userId);
        Map map = activityService.addActivity(activity.getUserId(), activity);
        return Response.success(map);
    }

    @Register
    @RequestMapping(value = {"/updateActivity"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "活动编辑")
    @ApiOperation("活动编辑(活动管理-活动编辑--ok)")
    public Response<?> updateActivity(
            @ApiParam(required = true,value = "token") @RequestParam String token,
            @ApiParam(required = true,value = "userId") @RequestParam Long userId,
            @ApiParam(required = true,value = "json:activity") @RequestBody Activity activity,
            @ApiParam(required = true,value = "签名") @RequestParam String sign) throws Exception {
        if (activity.getStatus() == ActivityStatusEnum.UNENROLL.getValue()) {
            ReturnStatus rs = validateActivity(activity, true, false);
            if (!rs.equals(ReturnStatus.SUCCESS)) { //校验数据合法性
                return Response.fail(rs);
            }
        }

        if (activity.getMemberType() != null && activity.getMemberType() == ActivityMemberTypeEnum.OHTHER.ordinal() && StringUtil.isEmpty(activity.getCourseId())) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }

//        activity.setUserId(userId);
        Map map = activityService.updateActivity(userId, activity);
        return Response.success(map);
    }

    private ReturnStatus validateActivity(Activity activity, boolean isUpdate, boolean isOfficial){
        if (isUpdate && activity.getActivityId() == null) {
            return ReturnStatus.PARAMETER_EMPTY;
        }

        if (!isOfficial) {//非官方活动校验
            if (activity.getMemberType() == null || activity.getEffectiveSignCount() == null ||  activity.getRewardScore() == null ) {
                return ReturnStatus.PARAMETER_EMPTY;
            }
            if (activity.getMemberType() == ActivityMemberTypeEnum.OHTHER.ordinal() && StringUtil.isEmpty(activity.getCourseId())) {// 选择指定用户，courseId不能为空
                return ReturnStatus.PARAMETER_INCORRECT;
            }
        }else{
            //官方检查 活动可参与次数
            if(null == activity.getEffectiveSignCount()){
                return ReturnStatus.EFFECTIVE_SIGN_COUNT_EMPTY;
            }
            if(null != activity.getEffectiveSignCount() && activity.getEffectiveSignCount() <= 0){
                return ReturnStatus.EFFECTIVE_SIGN_COUNT_EMPTY;
            }
            if(activity.getEffectiveSignCount() > 9999){
                return ReturnStatus.EFFECTIVE_SIGN_COUNT_EXCEED;
            }
        }
        if (activity.getStatus() == ActivityStatusEnum.UNENROLL.getValue()) {
            if (StringUtil.isEmpty(activity.getName()) || activity.getStartTime() == null || activity.getEndTime() == null || activity.getType() == null
                    || StringUtil.isEmpty(activity.getFrontCoverURL()) || activity.getSign() == null
                    || activity.getEnrollStartTime() == null || activity.getEnrollEndTime() == null
                    || activity.getStatus() == null ||activity.getMaxCapacity() == null) {
                return ReturnStatus.PARAMETER_EMPTY;
            }
            if (activity.getEndTime().getTime() < activity.getStartTime().getTime() || activity.getEnrollStartTime().getTime() > activity.getEnrollEndTime().getTime()
                    || activity.getEndTime().getTime() < activity.getEnrollEndTime().getTime() || activity.getMaxCapacity() <= 0) {
                return ReturnStatus.PARAMETER_INCORRECT;
            }

            if (activity.getSign() && (activity.getLongitude() == null || activity.getLatitude() == null || activity.getSignAddress() == null) ) {//需要打卡时候，必须有经纬度
                return ReturnStatus.PARAMETER_EMPTY;
            }

            if (activity.getType() == ActivityTypeEnum.RUNNING.ordinal()) {// 跑步活动必须有跑步规则
                if (activity.getRunning() == null || activity.getRunning().getPassNode() == null || activity.getRunning().getByOrder() == null
                        || activity.getRunning().getLength() == null || activity.getRunning().getSpeedMax() ==null || activity.getRunning().getSpeedMin() == null
                        || (activity.getRunning().getPassNode() && (activity.getRunning().getNodeCount() == null
                        || activity.getRunning().getRunningLineList() == null || activity.getRunning().getRunningLineList().size() == 0))) {
                    return ReturnStatus.PARAMETER_EMPTY;
                }
                if (activity.getRunning() != null && activity.getRunning().getRunningLineList() != null && activity.getRunning().getNodeCount() != null
                        && activity.getRunning().getRunningLineList().size() < activity.getRunning().getNodeCount()) {
                    return ReturnStatus.PARAMETER_EMPTY;
                }
            }
        }
        if(StringUtil.isNotEmpty(activity.getDesc()) && activity.getDesc().length() > 20000) {
            return ReturnStatus.DATA_TOO_LONG;//字符长度超过系统限制
        }

        return ReturnStatus.SUCCESS;
    }

    @Register
    @RequestMapping(value = {"/deleteActivity"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "删除活动")
    @ApiOperation(value = "删除活动(活动管理-删除活动)--64--ok")
    public Response<?> deleteActivity(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "token") @RequestParam Long userId,
            @ApiParam(required = true, value = "activityId") @RequestBody JSONObject json,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (!json.containsKey("activityId")){
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        if (!ValidateUtil.isDigits(json.getString("activityId"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        Long activityId = json.getLong("activityId");
        activityService.deleteActivity(userId, activityId);

        return Response.success(activityId);
    }

    @Register
    @RequestMapping(value = {"/getActivityList"},method = {RequestMethod.POST})
    @ApiOperation(value = "查询活动(活动管理-主页)--8--ok")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.NO_PERMISSION.value, message = ReturnConstant.NO_PERMISSION.desc),
            @ApiResponse(code = ReturnConstant.COURSE_SCHEDULE_NOT_EXIST.value, message = ReturnConstant.COURSE_SCHEDULE_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<?> getActivityList(@ApiParam(required = true, value = "token") @RequestParam String token,
                                      @ApiParam(required = true, value = "userId") @RequestParam Long userId,
                                      @ApiParam(required = true, value = "json:type status") @RequestBody JSONObject json,
                                      @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        int[] page = MyPage.isValidParams(json);
        json.remove("pageindex");
        json.remove("pagesize");
        Map<String, Object> paramsMap = JsonUtils.getMap4Json(json.toString());
        if(!ValidateUtil.jsonValidateWithKey(json, "type")) {
            paramsMap.remove("type");
        }
        if(!ValidateUtil.jsonValidateWithKey(json, "status")) {
            paramsMap.remove("status");
        }
        if(!ValidateUtil.jsonValidateWithKey(json, "name")) {
            paramsMap.remove("name");
        }
        paramsMap.put("userId",userId);
        //--------------------
//        Integer type = null;
//        Integer status = null;
//        String name = null;
//        if (json.containsKey("type") ){
//            type = json.getInt("type");
//        }
//        if (json.containsKey("status")) {
//            status = json.getInt("status");
//        }
//        if (json.containsKey("name")) {
//            name = json.getString("name");
//        }
//        int[] page = MyPage.isValidParams(json);
        MyPageInfo pageInfo  = activityService.getActivityList(paramsMap,page[0], page[1]);
        //Map<String, Object> ret = activityService.getActivityList(userId, type, status, name, page[0], page[1]);
        return Response.success(pageInfo);
    }

    @Register
    @RequestMapping(value = {"/getActivityDetail"},method = {RequestMethod.POST})
    @ApiOperation("查询活动详情(活动管理-活动详情10--ok)")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.SCHOOL_NOT_EXIST.value, message = ReturnConstant.SCHOOL_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<Activity> getActivityDetailForWeb(
            @ApiParam(required = true,value = "token") @RequestParam String token,
            @ApiParam(required = true,value = "userId") @RequestParam Long userId,
            @ApiParam(required = true,value = "活动ID") @RequestBody JSONObject json,
            @ApiParam(required = true,value = "签名") @RequestParam String sign) throws Exception {
        if (!json.containsKey("activityId")){
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        if (!ValidateUtil.isDigits(json.getString("activityId"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        Long activityId = json.getLong("activityId");
        Activity activity = activityService.getActivityDetailForWeb(userId, activityId);
        return Response.success(activity);
    }

    @Register
    @RequestMapping(value = {"/getActivityBasicForWeb"},method = {RequestMethod.POST})
    @ApiOperation("查询活动基本信息(活动管理-活动详情10--top--ok)")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.SCHOOL_NOT_EXIST.value, message = ReturnConstant.SCHOOL_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<Map<String, Object>> getActivityBasicForWeb(@ApiParam(required = true,value = "token") @RequestParam String token,
                                                                 @ApiParam(required = true,value = "userId") @RequestParam Long userId,
                                                                 @ApiParam(required = true,value = "json:activity") @RequestBody JSONObject json,
                                                                 @ApiParam(required = true,value = "签名") @RequestParam String sign) throws Exception {
        if (!json.containsKey("activityId")){
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        if (!ValidateUtil.isDigits(json.getString("activityId"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        Long activityId = json.getLong("activityId");
        Map<String, Object> activity = activityService.getActivityBasicForWeb(userId, activityId);
        return Response.success(activity);
    }

    /************************************************************************************************************/
    @Register
    @RequestMapping(value = {"/getActivityListBySchoolId"},method = {RequestMethod.POST})
    @ApiOperation(value = "查询校园活动(官方后台--校园管理--学校信息--活动)--8--ok")
    public Response<Map<String, Object>> getActivityListBySchoolId(@ApiParam(required = true, value = "token") @RequestParam String token,
                                                         @ApiParam(required = true, value = "userId") @RequestParam Long userId,
                                                         @ApiParam(required = true, value = "json:schoolId") @RequestBody JSONObject json,
                                                         @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {

        Long schoolId = null;
        if (json.containsKey("schoolId")) {
            schoolId = json.getLong("schoolId");
        }

        int[] page = MyPage.isValidParams(json);
        Map<String, Object> ret = activityService.getActivityListBySchoolId(userId, schoolId, page[0], page[1]);
        return Response.success(ret);
    }


    /***********************************************************************************************************************************************/
    /*********************************************************** 官方活动管理 ***********************************************************************/
    @Register
    @RequestMapping(value = {"/addOfficalActivity"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "添加活动-官方后台")
    @ApiOperation(value = "添加活动(官方后台--运营管理--发布活动)--9--ok")
    public Response<Map> addOfficalActivity(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "activity") @RequestBody Activity activity,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (activity.getStatus() == ActivityStatusEnum.UNENROLL.getValue()) {
            ReturnStatus rs = validateActivity(activity, false, true);
            if (!rs.equals(ReturnStatus.SUCCESS)) { //校验数据合法性
                return Response.fail(rs);
            }
        }
        activity.setUserId(userId);
        Map map = activityService.addOfficalActivity(userId, activity);
        return Response.success(map);
    }

    @Register
    @RequestMapping(value = {"/getOfficialActivityListForWeb"},method = {RequestMethod.POST})
    @ApiOperation(value = "查询官方活动(官方后台--运营管理--主页)--10--ok")
    public Response<Map<String, Object>> getOfficialActivityListForWeb(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "json:schoolId") @RequestBody JSONObject json,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        int[] page = MyPage.isValidParams(json);
        Integer type = null;
        Integer status = null;
        String name = null;
        if (json.containsKey("type") ){
            type = json.getInt("type");
        }
        if (json.containsKey("status")) {
            status = json.getInt("status");
        }
        if (json.containsKey("name")) {
            name = json.getString("name");
        }
        return Response.success(activityService.getOfficialActivityListForWeb(userId, type, status, name, page[0], page[1]));
    }

    @Register
    @RequestMapping(value = {"/deleteOfficialActivity"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "官方删除活动")
    @ApiOperation(value = "官方删除活动(官方后台--运营管理)--11--ok")
    public Response<?> officiaDeletelActivity(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "json:activityId") @RequestBody JSONObject json,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (!json.containsKey("activityId")){
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        Long activityId = json.getLong("activityId");
        activityService.officiaDeletelActivity(userId, activityId);
        return Response.success(activityId);
    }


    @Register
    @RequestMapping(value = {"/updateOfficialActivity"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "官方活动编辑")
    @ApiOperation("官方活动编辑(官方活动管理-活动编辑--ok)")
    public Response<?> updateOfficialActivity(
            @ApiParam(required = true,value = "token") @RequestParam String token,
            @ApiParam(required = true,value = "userId") @RequestParam Long userId,
            @ApiParam(required = true,value = "json:activity") @RequestBody Activity activity,
            @ApiParam(required = true,value = "签名") @RequestParam String sign) throws Exception {
        if (activity.getStatus() == ActivityStatusEnum.UNENROLL.getValue()) {
            ReturnStatus rs = validateActivity(activity, true, true);
            if (!rs.equals(ReturnStatus.SUCCESS)) { //校验数据合法性
                return Response.fail(rs);
            }
        }

        Map map = activityService.updateOfficialActivity(userId, activity);
        return Response.success(map);
    }


    @Register
    @RequestMapping(value = {"/getOfficialActivityDetail"},method = {RequestMethod.POST})
    @ApiOperation("查询官方活动详情(官方活动管理-活动详情)")
    public Response<?> getOfficialActivityDetail(
            @ApiParam(required = true,value = "token") @RequestParam String token,
            @ApiParam(required = true,value = "userId") @RequestParam Long userId,
            @ApiParam(required = true,value = "活动ID") @RequestBody JSONObject json,
            @ApiParam(required = true,value = "签名") @RequestParam String sign) throws Exception {
        if (!json.containsKey("activityId")){
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        if (!ValidateUtil.isDigits(json.getString("activityId"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        Long activityId = json.getLong("activityId");
        Activity activity = activityService.getOfficialActivityDetailForWeb(userId, activityId);
        return Response.success(activity);
    }


    @Register
    @RequestMapping(value = {"/getOfficialActivityMemberList"},method = {RequestMethod.POST})
//    @SystemControllerLog(description = "官方查询活动成员列表")
    @ApiOperation("官方查询活动成员列表(官方活动管理-活动详情-成员)--11--ok")
    public Response<Map<String, Object>> getOfficialActivityMemberList(
            @ApiParam(required = true,value = "token") @RequestParam String token,
            @ApiParam(required = true,value = "userId") @RequestParam Long userId,
            @ApiParam(required = true,value = "json:activityId pageindex pagesize") @RequestBody JSONObject json,
            @ApiParam(required = true,value = "签名") @RequestParam String sign) throws Exception {
        if (!json.containsKey("activityId") || !json.containsKey("status")) {
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        if (!ValidateUtil.isDigits(json.getString("activityId")) || !ValidateUtil.isDigits(json.getString("status"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        Long activityId = json.getLong("activityId");
        int[] page = MyPage.isValidParams(json);
        int status = json.getInt("status");
        Map<String, Object> ret = activityService.getOfficialActivityMemberListForWeb(userId, activityId, page[0], page[1], status);
        return Response.success(ret);
    }

    @Register
    @RequestMapping(value = {"/getAllSchoolActivityList"},method = {RequestMethod.POST})
//    @SystemControllerLog(description = "查询所有校园活动official")
    @ApiOperation(value = "查询所有校园活动(官方后台--校园活动管理主页)")
    public Response<?> getAllSchoolActivityList(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "json:schoolId") @RequestBody JSONObject json,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        int[] page = MyPage.isValidParams(json);

        Map map = activityService.getAllSchoolActivityList(userId, page[0], page[1]);
        return Response.success(map);
    }

    @Register
    @RequestMapping(value = {"/getStudentActivityList"},method = {RequestMethod.POST})
//    @SystemControllerLog(description = "个人主页-非跑步活动数据列表-PC")
    @ApiOperation(value = "个人主页-非跑步活动数据列表")
    public Response<?> getStudentActivityList(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "json:studentId：学生ID[必填]; 翻页参数:pageindex,pagesize") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"studentId"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ReturnStatus.PARAMETER_MISS);//必填项请求参数不完整，请检查
        }
        int[] page = MyPage.isValidParams(json);
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("userId", json.getLong("studentId"));
        paramsMap.put("type", ActivityTypeEnum.NON_RUNNING.ordinal());
        MyPageInfo myPageInfo = activityService.getStudentActivityList(paramsMap, page[0], page[1]);
        return Response.success(myPageInfo);
    }
}