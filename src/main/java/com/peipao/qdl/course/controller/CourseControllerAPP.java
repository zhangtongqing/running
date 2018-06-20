package com.peipao.qdl.course.controller;

import com.peipao.framework.annotation.Register;
import com.peipao.framework.annotation.SystemControllerLog;
import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.constant.ReturnConstant;
import com.peipao.framework.constant.WebConstants;
import com.peipao.framework.model.Response;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.page.MyPage;
import com.peipao.framework.page.MyPageInfo;
import com.peipao.framework.util.JsonUtils;
import com.peipao.framework.util.ValidateUtil;
import com.peipao.qdl.course.model.Course;
import com.peipao.qdl.course.model.CourseSchedule;
import com.peipao.qdl.course.service.CourseCacheService;
import com.peipao.qdl.course.service.CourseService;
import com.peipao.qdl.school.model.UserSchool;
import com.peipao.qdl.school.service.SchoolService;
import com.peipao.qdl.statistics.service.UserStatisticUtilService;
import com.peipao.qdl.user.model.User;
import com.peipao.qdl.user.model.UserTypeEnum;
import com.peipao.qdl.user.service.UserService;
import io.swagger.annotations.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author meteor.wu
 * @since 2017/6/26
 **/
@Api(value = "/course", description = "课程")
@RestController
@RequestMapping({"/course/app"})
public class CourseControllerAPP {
    @Autowired
    private CourseService courseService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private CourseCacheService courseCacheService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserStatisticUtilService userStatisticUtilService;

    @Register
    @RequestMapping(value = {"/chooseCourse"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "选课")
    @ApiOperation(value = "选课(课程中心)--14--ok")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value,message = ReturnConstant.SUCCESS.desc),
                   @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value,message = ReturnConstant.PARAMETER_INCORRECT.desc),
                   @ApiResponse(code = ReturnConstant.COURSE_CHOOSE_COUNT_LIMIT.value,message = ReturnConstant.COURSE_CHOOSE_COUNT_LIMIT.desc),
                   @ApiResponse(code = ReturnConstant.COURSE_NOT_EXIST.value,message = ReturnConstant.COURSE_NOT_EXIST.desc),
                   @ApiResponse(code = ReturnConstant.COURSE_HAVE_CHOOSE.value,message = ReturnConstant.COURSE_HAVE_CHOOSE.desc),
                   @ApiResponse(code = ReturnConstant.OTHER_ERROR.value,message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<?> chooseCourse(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "json:courseId") @RequestBody JSONObject json,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (!json.containsKey("courseId") || !ValidateUtil.isDigits(json.getString("courseId"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        Long courseId = Long.valueOf(json.getString("courseId"));
        courseService.chooseCourse(userId, courseId);
        return Response.success();
    }

    @Register
    @RequestMapping(value = {"/getSelectCourseList"},method = {RequestMethod.POST})
    @ApiOperation(value = "查询课程名和老师List(课程中心)--13--ok")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value,message = ReturnConstant.SUCCESS.desc),
                   @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value,message = ReturnConstant.PARAMETER_INCORRECT.desc),
                   @ApiResponse(code = ReturnConstant.OTHER_ERROR.value,message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<Map<String, Object>> getSelectCourseList(
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "value") @RequestBody JSONObject json) throws Exception {

        String value= json.containsKey("value") ? json.getString("value") : null;
        UserSchool userSchool = schoolService.getParaByUserId(userId, Calendar.getInstance().getTime());
        if (userSchool == null) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);
        }
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("semesterId", userSchool.getSemesterId());
        paramsMap.put("value", value);
        List<Map<String, Object>> ret = courseService.getSelectCourseList(paramsMap,userSchool.getCourseId());
        Collections.sort(ret, new Comparator<Map>() {
            @Override
            public int compare(Map o1, Map o2) {
                int ret = 0;
                //比较两个对象的顺序，如果前者小于、等于或者大于后者，则分别返回-1/0/1
                ret = o2.get("chooseFlag").toString().compareTo(o1.get("chooseFlag").toString());//逆序的话就用o2.compareTo(o1)即可
                return ret;
            }
        });
        Map<String, Object> map = new HashMap<>();
        map.put("data", ret);
        map.put("courseId", userSchool.getCourseId());
        if (userSchool.getCourseId() != null) {
            Course course = courseCacheService.getCourseById(userSchool.getCourseId());
            if(null != course) {
                //return Response.fail(ReturnStatus.COURSE_NOT_EXIST);//课程不存在
                map.put("name", course.getName());
            } else {
                userSchool.setCourseId(null);
            }
        }
        if (userSchool.getCourseId() == null){
            map.put("chooseCount", 0);
        }else{
            map.put("chooseCount", courseService.getRecordCount(userId, userSchool.getSemesterId()));
        }
        map.put("maxCount", WebConstants.Course.COURSECHOOSELIMIT);
        return Response.success(map);
    }

    @Register
    @RequestMapping(value = {"/getCourseScheduleDescDetail"},method = {RequestMethod.POST})
    @ApiOperation(value = "查询课程详情")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value,message = ReturnConstant.SUCCESS.desc),
                   @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value,message = ReturnConstant.PARAMETER_INCORRECT.desc),
                   @ApiResponse(code = ReturnConstant.OTHER_ERROR.value,message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<Map<String, Object>> getCourseDetail(
            @ApiParam(required = true, value = "学校ID") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "json:courseId") @RequestBody JSONObject json,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (!json.containsKey("courseId")) {
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        long courseId = json.getLong("courseId");
        UserSchool userSchool = schoolService.getParaByUserId(userId, Calendar.getInstance().getTime());
        if (userSchool == null) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);
        }
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("courseId", courseId);
        List<Map<String, Object>> ret = courseService.getSelectCourseList(paramsMap, userSchool.getCourseId());
        if (ret.size() > 0) {
            int studentCount = userService.countByCourseId(courseId);//课程当前报名人数

            Map<String, Object> map = ret.get(0);
            map.put("desc", courseCacheService.getCourseById(Long.valueOf(map.get("courseId").toString())).getDesc());
            map.put("studentCount", studentCount);
            map.put("oldCourseId", userSchool.getCourseId());
            if (userSchool.getCourseId() == null){
                map.put("chooseCount", 0);
            }else{
                map.put("chooseCount", courseService.getRecordCount(userId, userSchool.getSemesterId()));
            }
            map.put("maxCount", WebConstants.Course.COURSECHOOSELIMIT);

            return Response.success(map);
        }
        return Response.success();
    }

    @Register
    @RequestMapping(value = {"/getCourseScheduleList"},method = {RequestMethod.POST})
    @ApiOperation("查询课程表列表(课程)--12--ok")
    public Response<List<Map<String, Object>>> getCourseScheduleListForApp(
        @ApiParam(required = true, value = "token") @RequestParam String token,
        @ApiParam(required = true, value = "userId") @RequestParam Long userId,
        @ApiParam(required = true, value = "签名") @RequestParam String sign,
        @ApiParam(required = true, value = "json:weekIndex周次") @RequestBody JSONObject json
    ) throws Exception {
        if (!json.containsKey("weekIndex") || !ValidateUtil.isDigits(json.getString("weekIndex"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        int weekIndex = Integer.parseInt(json.getString("weekIndex"));
        if (weekIndex <= 0 || weekIndex > 30 ) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        List<Map<String, Object>> ret = courseService.getCourseScheduleList(userId, weekIndex);
        return Response.success(ret);
    }

    @Register
    @RequestMapping(value = {"/sign"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "课程签到app")
    @ApiOperation(value = "课程签到(课程详情)--42--ok")
    public Response<?> sign(@ApiParam(required = true, value = "token") @RequestParam String token,
                            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
                            @ApiParam(required = true, value = "json:courseScheduleId") @RequestBody JSONObject json,
                            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (!json.containsKey("courseScheduleId") ){
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        if (!ValidateUtil.isDigits(json.getString("courseScheduleId"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        Long courseScheduleId = Long.valueOf(json.getString("courseScheduleId"));
        Double longitude = json.containsKey("longitude") ? Double.valueOf(json.getString("longitude")) : null;
        Double latitude = json.containsKey("latitude") ? Double.valueOf(json.getString("latitude")) : null;

        courseService.sign(userId, courseScheduleId, longitude, latitude);
        return Response.success();
    }


    @Register
    @RequestMapping(value = "/getCourseScheduleDetail", method = RequestMethod.POST)
    @ApiOperation(value = "查询课次详情")
    public Response getCourseScheduleDetail(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "RequestBody查询条件") @RequestBody JSONObject json
    ) throws Exception {
        if (!json.containsKey("courseScheduleId") || !ValidateUtil.isDigits(json.getString("courseScheduleId"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        return Response.success(courseService.getCourseScheduleDetail(userId, json.getLong("courseScheduleId")));
    }

    /******************************************** 以下教务端APP接口 ***************************************************/
    @Register
    @RequestMapping(value = {"/getChooseCourseList"},method = {RequestMethod.POST})
    @ApiOperation(value = "查询课程名和老师List(老师给学生换课选课列表)--教务端APP")
    public Response<List<Map<String, Object>>> getChooseCourseList(
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = false, value = "queryString") @RequestBody(required = false) JSONObject json
    ) throws Exception {
        UserSchool userSchool = schoolService.getParaByUserId(userId, Calendar.getInstance().getTime());
        if (userSchool == null) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);
        }
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("semesterId", userSchool.getSemesterId());
        //查询条件：按照课程名称，或者老师名字进行查询
        if(ValidateUtil.jsonValidateWithKey(json, "queryString")) {
            paramsMap.put("value", json.getString("queryString"));
        }
        User currUser = userService.validateSchoolUser(userId);//当前操作的用户(教师或学校管理员)
        if(currUser.getUserType() == UserTypeEnum.TEACHER.getValue()) {
            userStatisticUtilService.initCourseArrayString(paramsMap, currUser.getUserId(), userSchool.getSemesterId());
        }
        List<Map<String, Object>> ret = courseService.getSelectCourseList(paramsMap, userSchool.getCourseId());
        return Response.success(ret);
    }

    @Register
    @RequestMapping(value = "/getCourseScheduleInfo", method = RequestMethod.POST)
    @ApiOperation(value = "查询课次详情--教务端APP")
    public Response getCourseScheduleInfo(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "RequestBody查询条件: courseScheduleId") @RequestBody JSONObject json
    ) throws Exception {
        if (!json.containsKey("courseScheduleId") || !ValidateUtil.isDigits(json.getString("courseScheduleId"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        User currUser = userService.validateSchoolUser(userId);//当前操作的用户(教师或学校管理员)
        return Response.success(courseService.getCourseScheduleInfo(json.getLong("courseScheduleId")));
    }


    @Register
    @RequestMapping(value = {"/getCourseMemberList"},method = {RequestMethod.POST})
    @ApiOperation(value = "查询课程成员列表--教务端APP")
    public Response<?> getCourseMemberList(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "json: courseScheduleId, pageindex pagesize, queryString") @RequestBody JSONObject json,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        int[] page = MyPage.isValidParams(json);
        json.remove("pageindex");
        json.remove("pagesize");
        if (!json.containsKey("courseScheduleId") || !ValidateUtil.isDigits(json.getString("courseScheduleId"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        Long courseScheduleId = json.getLong("courseScheduleId");
        int type = WebConstants.Boolean.TRUE.ordinal();//0=查询缺勤(FALSE)，1=查询出勤(TRUE)
        if(ValidateUtil.jsonValidateWithKey(json, "type")) {
            if(json.getInt("type") == WebConstants.Boolean.FALSE.ordinal()) {
                type = WebConstants.Boolean.FALSE.ordinal();
            }
        }
        Map<String, Object> paramsMap = JsonUtils.getMap4Json(json.toString());
        paramsMap.put("type", type);
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

        JSONObject resJson = new JSONObject();
        JSONArray countArray = new JSONArray();
        JSONObject countJson = new JSONObject();

        Map<String, Object> map = courseService.validateTeacherAndCourseSchedule(userId, courseScheduleId);
        CourseSchedule schedule = (CourseSchedule) map.get("schedule");
        paramsMap.put("courseId", schedule.getCourseId());
        MyPageInfo pageInfo = courseService.getCourseScheduleMember(paramsMap, page[0],page[1]);
        countJson.put("type", type);
        countJson.put("count", courseService.getCourseScheduleMemberCount(paramsMap));
        countArray.add(countJson);
        if(type == WebConstants.Boolean.FALSE.ordinal()) {
            type = WebConstants.Boolean.TRUE.ordinal();
        } else {
            type = WebConstants.Boolean.FALSE.ordinal();
        }

        paramsMap.put("type", type);
        countJson = new JSONObject();
        countJson.put("type", type);
        countJson.put("count", courseService.getCourseScheduleMemberCount(paramsMap));
        countArray.add(countJson);
        resJson.put("pageInfo", pageInfo);
        resJson.put("countArray", countArray);
        return Response.success(resJson);
    }


    @Register
    @RequestMapping(value = {"/signByTeacher"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "教务端APP签到或签退")
    @ApiOperation(value = "教务端APP签到或签退")
    public Response<?> signByTeacher(
        @ApiParam(required = true, value = "token") @RequestParam String token,
        @ApiParam(required = true, value = "userId") @RequestParam Long userId,
        @ApiParam(required = true, value = "签名") @RequestParam String sign,
        @ApiParam(required = true, value = "json:courseScheduleId, studentId, type") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"courseScheduleId", "studentId"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ReturnStatus.PARAMETER_MISS);//必填项请求参数不完整，请检查
        }
        if (!ValidateUtil.isDigits(json.getString("studentId")) || !ValidateUtil.isDigits(json.getString("courseScheduleId"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        Long courseScheduleId = json.getLong("courseScheduleId");
        Long studentId = json.getLong("studentId");
        int type = -1;//0=false=签退；1=true=签到
        if(json.getInt("type") == WebConstants.Boolean.FALSE.ordinal()) {
            type = WebConstants.Boolean.FALSE.ordinal();
            params = new String[]{"courseMemberId"};//必填项
            if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
                return Response.fail(ReturnStatus.PARAMETER_MISS);//必填项请求参数不完整，请检查
            }
        } else if(json.getInt("type") == WebConstants.Boolean.TRUE.ordinal()) {
            type = WebConstants.Boolean.TRUE.ordinal();
        } else {
            return Response.fail(ResultMsg.COURSE_SIGN_TYPE_ERROR);//考勤类型参数错误,请检查
        }
        Long courseMemberId = json.containsKey("courseMemberId") ? json.getLong("courseMemberId") : null;
        Map<String, Object> map = courseService.sign(userId, courseScheduleId, studentId, type, courseMemberId);
        return Response.success();
    }

}
