package com.peipao.qdl.course.controller;

import com.peipao.framework.annotation.Register;
import com.peipao.framework.annotation.SystemControllerLog;
import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.constant.ReturnConstant;
import com.peipao.framework.exception.BusinessException;
import com.peipao.framework.model.Response;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.page.MyPage;
import com.peipao.framework.page.MyPageInfo;
import com.peipao.framework.util.HttpRequestUtil;
import com.peipao.framework.util.ValidateUtil;
import com.peipao.qdl.course.model.Course;
import com.peipao.qdl.course.model.CourseSchedule;
import com.peipao.qdl.course.service.CourseCacheService;
import com.peipao.qdl.course.service.CourseService;
import com.peipao.qdl.school.model.UserSchool;
import com.peipao.qdl.school.service.SchoolService;
import com.peipao.qdl.user.model.User;
import com.peipao.qdl.user.service.UserCacheService;
import com.peipao.qdl.user.service.UserService;
import io.swagger.annotations.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程pc端接口
 *
 * @author Meteor.wu
 * @since 2018/2/27 16:36
 */
@Api(value = "/coursePC", description = "课程pc")
@RestController
@RequestMapping({"/course/pc"})
public class CourseControllerPC {
    @Autowired
    private CourseService courseService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private UserCacheService userCacheService;
    @Autowired
    private CourseCacheService courseCacheService;
    @Autowired
    private UserService userService;


    @Register
    @RequestMapping(value = {"/loadStudentCourseExcel"},method = {RequestMethod.POST})
    @ApiOperation(value = "老师批量给学生选课(老师app)")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value,message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value,message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.COURSE_CHOOSE_COUNT_LIMIT.value,message = ReturnConstant.COURSE_CHOOSE_COUNT_LIMIT.desc),
            @ApiResponse(code = ReturnConstant.COURSE_NOT_EXIST.value,message = ReturnConstant.COURSE_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.COURSE_HAVE_CHOOSE.value,message = ReturnConstant.COURSE_HAVE_CHOOSE.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value,message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<?> loadStudentCourseExcel(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "json:courseId,studentId") @RequestBody JSONObject json,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        String url = json.getString("url");
        String fileType = url.contains(".xlsx") ? "xlsx" : "xls";
        InputStream in = HttpRequestUtil.httpRequestIO(url);

        Workbook wb;
        if (fileType.toLowerCase().equals("xls")) {
            wb = new HSSFWorkbook(in);
        }else if(fileType.toLowerCase().equals("xlsx")) {
            wb = new XSSFWorkbook(in);
        }else {
            return Response.fail(ReturnStatus.FILE_TYPE_ERROR);
        }
        int count = courseService.loadStudentCourseExcel(userId, wb);
        String msg = String.format("已经成功修改%d条学生课程", count);
        return Response.success(msg);
    }

    @Register
    @RequestMapping(value = {"/getCourseList"},method = {RequestMethod.GET})
    @ApiOperation(value = "查询课程列表(设置-课程设置)--50--ok", notes = "导航主页,管理员显示全部课程，个人显示自己所教课程")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.NO_PERMISSION.value, message = ReturnConstant.NO_PERMISSION.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<?> getCourseList(@ApiParam(required = true, value = "token") @RequestParam String token,
                                     @ApiParam(required = true, value = "老师或者管理员ID") @RequestParam Long userId,
                                     @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {

        Map<String, Object> list = courseService.getCourseListForWeb(userId);
        return Response.success(list);
    }

    @Register
    @RequestMapping(value = {"/getCourseById"},method = {RequestMethod.POST})
    @ApiOperation(value = "查询课程列表(设置-课程设置)--50--ok")
    public Response<?> getCourseById(
        @ApiParam(required = true, value = "token") @RequestParam String token,
        @ApiParam(required = true, value = "老师或者管理员ID") @RequestParam Long userId,
        @ApiParam(required = true, value = "json:courseId") @RequestBody JSONObject json,
        @ApiParam(required = true, value = "签名") @RequestParam String sign
    ) throws Exception {
        if (!json.containsKey("courseId") || !ValidateUtil.isDigits(json.getString("courseId"))){
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        Long courseId = json.getLong("courseId");
        Course course = courseCacheService.getCourseById(courseId);
        if (course == null) {
            throw new BusinessException(ReturnStatus.PARAMETER_INCORRECT);
        }
        json.put("userId", course.getUserId());
        json.put("name", course.getName());
        json.put("desc", course.getDesc() == null ? "" : course.getDesc());
        json.put("maxCapacity", course.getMaxCapacity());
        json.put("latitude", course.getLatitude());
        json.put("longitude", course.getLongitude());
        json.put("needSignLocation", course.getNeedSignLocation());

        User user = userCacheService.getUserById(course.getUserId());
        json.put("username", user.getUsername());

        return Response.success(json);
    }

    @Register
    @RequestMapping(value = {"/getAllCourseList"},method = {RequestMethod.POST})
    @ApiOperation(value = "查询课程列表(导航主页)--2--ok", notes = "导航主页,管理员显示全部课程，个人显示自己所教课程")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.NO_PERMISSION.value, message = ReturnConstant.NO_PERMISSION.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<List<Map<String, Object>>> getAllCourseList(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "老师或者管理员ID") @RequestParam Long userId,
            @ApiParam(required = true, value = "json:courseId") @RequestBody JSONObject json,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        UserSchool userSchool = schoolService.getParaByUserId(userId, Calendar.getInstance().getTime());
        if (userSchool == null || userSchool.getSchoolId() == null || userSchool.getSemesterId() == null) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);
        }
        Long schoolId = userSchool.getSchoolId();
        Long semesterId = userSchool.getSemesterId();
        String value = json.containsKey("value") ? json.getString("value") :null;
        List<Map<String, Object>> list = courseService.getCourseListWithMemberCount(null, schoolId, semesterId, value);
        list.add(0, courseService.getUnselectCourseStudent(schoolId));
        return Response.success(list);
    }

    @Register
    @RequestMapping(value = {"/getMyCourseList"},method = {RequestMethod.POST})
    @ApiOperation(value = "查询课程列表(导航主页)--3--ok", notes = "导航主页,管理员显示全部课程，个人显示自己所教课程")
    public Response<List<Map<String, Object>>> getMyCourseList(
            @ApiParam(required = true, value = "老师或者管理员ID") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = false, value = "json:value") @RequestBody(required = false) JSONObject json) throws Exception {
        UserSchool userSchool = schoolService.getParaByUserId(userId, Calendar.getInstance().getTime());
        if (userSchool == null || userSchool.getSchoolId() == null || userSchool.getSemesterId() == null) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);
        }
        Long schoolId = userSchool.getSchoolId();
        Long semesterId = userSchool.getSemesterId();
        String value = json.containsKey("value") ? json.getString("value") :null;
        List<Map<String, Object>> list = courseService.getCourseListWithMemberCount(userId, schoolId, semesterId, value);

        return Response.success(list);
    }

    @Register
    @RequestMapping(value = {"/getCourseMemberList"},method = {RequestMethod.POST})
    @ApiOperation(value = "查询课程成员列表(课程管理详情)--19--ok")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.NO_PERMISSION.value, message = ReturnConstant.NO_PERMISSION.desc),
            @ApiResponse(code = ReturnConstant.COURSE_SCHEDULE_NOT_EXIST.value, message = ReturnConstant.COURSE_SCHEDULE_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<?> getCourseMemberList(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "json: courseScheduleId, pageindex pagesize, queryString") @RequestBody JSONObject json,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (!json.containsKey("courseScheduleId") || !ValidateUtil.isDigits(json.getString("courseScheduleId"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        Long courseScheduleId = json.getLong("courseScheduleId");
        int[] page = MyPage.isValidParams(json);
        Map<String, Object> map = courseService.getCourseScheduleMemberWeb(userId, courseScheduleId, page[0],page[1]);
        return Response.success(map);
    }

    @Register
    @RequestMapping(value = {"/getStudentCourseList"},method = {RequestMethod.POST})
    @ApiOperation(value = "查询学生课程考勤(学生主页课程考勤)--25--ok")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.NO_PERMISSION.value, message = ReturnConstant.NO_PERMISSION.desc),
            @ApiResponse(code = ReturnConstant.COURSE_SCHEDULE_NOT_EXIST.value, message = ReturnConstant.COURSE_SCHEDULE_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<Map<String, Object>> getStudentCourseList(@ApiParam(required = true, value = "token") @RequestParam String token,
                                                              @ApiParam(required = true, value = "userId") @RequestParam Long userId,
                                                              @ApiParam(required = true, value = "studentId, pageindex, pagesize") @RequestBody JSONObject json,
                                                              @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        int[] page = MyPage.isValidParams(json);
        if (!json.containsKey("studentId") || !ValidateUtil.isDigits(json.getString("studentId"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        Long studentId = json.getLong("studentId");

        Map<String, Object> map = courseService.getStudentCourseList(userId, studentId, page[0], page[1]);
        return Response.success(map);
    }


    @Register
    @RequestMapping(value = {"/signByTeacher"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "批量签到")
    @ApiOperation(value = "批量签到(课程详情-签到)--20--ok")
    public Response<?> signByTeacher(@ApiParam(required = true, value = "token") @RequestParam String token,
                                     @ApiParam(required = true, value = "userId") @RequestParam Long userId,
                                     @ApiParam(required = true, value = "json:courseScheduleId, array[userIds]") @RequestBody JSONObject json,
                                     @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (!json.containsKey("courseScheduleId") || !ValidateUtil.isDigits(json.getString("courseScheduleId"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        Long courseScheduleId = Long.valueOf(json.getString("courseScheduleId"));
        Long studentId = json.getLong("userId");
        int type = json.getInt("type");
        Long courseMemberId = json.containsKey("courseMemberId") ? json.getLong("courseMemberId") : null;

        Map<String, Object> map = courseService.sign(userId, courseScheduleId, studentId, type, courseMemberId);
        return Response.success(map);
    }

    @Register
    @RequestMapping(value = {"/updateCourse"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "编辑课程")
    @ApiOperation(value = "编辑课程(课程设置-更新)--51--ok")
    public Response<CourseSchedule> updateCourse(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "json:courseId必填，其他根据需求") @RequestBody Course course,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (course.getCourseId() == null || course.getName() == null || course.getUserId() == null || course.getMaxCapacity() == null) {
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        courseService.updateCourse(userId, course);
        return Response.success();
    }

    @Register
    @RequestMapping(value = {"/addCourse"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "添加课程")
    @ApiOperation(value = "添加课程(课程设置-添加)--52--OK")
    public Response<?> addCourse(@ApiParam(required = true, value = "token") @RequestParam String token,
                                      @ApiParam(required = true, value = "userId") @RequestParam Long userId,
                                      @ApiParam(required = true, value = "json:name, serial, userId(老师id)") @RequestBody Course course,
                                      @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (course.getName() == null || course.getUserId() == null || course.getMaxCapacity() == null) {
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        courseService.addCourse(userId, course);
        course.setKey(course.getCourseId());
        Map<String, Object> ret= new HashMap<>();
        ret.put("courseId", course.getCourseId());
        ret.put("count", 0);
        ret.put("username", userCacheService.getUserById(course.getUserId()).getUsername());
        ret.put("name", course.getName());
        ret.put("userId", course.getUserId());
        return Response.success(ret);
    }

    @Register
    @RequestMapping(value = {"/deleteCourse"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "删除课程")
    @ApiOperation(value = "删除课程(课程设置-删除)--58--ok")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.NO_PERMISSION.value, message = ReturnConstant.NO_PERMISSION.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<?> deleteCourse(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "json:courseId") @RequestBody JSONObject json,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (!json.containsKey("courseId") || !ValidateUtil.isDigits(json.getString("courseId")) ) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        Long courseId = json.getLong("courseId");
        courseService.deleteCourse(userId, courseId);
        //删除课程后，需要清除学生绑定的课程ID
        List<Map<String, Object>> list = userService.getUserIdsByCourseId(courseId);
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("courseId", courseId);
        userService.updateUserCourseToNull(paramsMap);
        if(!CollectionUtils.isEmpty(list)) {
            int n = list.size();
            String[] userIds = new String[n];
            int i = 0;
            for (Map map : list) {
                userIds[i] = "userId" + map.get("userId").toString();
                i++;
            }
            userCacheService.removeByKeys(userIds);
        }
        return Response.success(courseId);
    }

    @Register
    @RequestMapping(value = {"/getAllCourseNameAndId"},method = {RequestMethod.GET})
    @ApiOperation(value = "查询课程名字和ID(课程管理-全部)--56--ok")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.NO_PERMISSION.value, message = ReturnConstant.NO_PERMISSION.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<List<Map<String, Object>>> getAllCourseNameAndId(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {

        return Response.success(courseService.getAllCourseNameAndId(userId));
    }


    @Register
    @RequestMapping(value = {"/getAllCourseNameAndIdArray"},method = {RequestMethod.POST})
    @ApiOperation(value = "查询课程名字和ID,有查询条件")
    public Response<List<Map<String, Object>>> getAllCourseNameAndIdArray(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = false, value = "json:courseName") @RequestBody(required = false) JSONObject json
    ) throws Exception {
        String courseName = null;
        if(ValidateUtil.jsonValidateWithKey(json, "courseName")) {
            courseName = json.getString("courseName");
        }
        UserSchool userSchool = schoolService.getParaByUserId(userId, Calendar.getInstance().getTime());
        if (userSchool == null || userSchool.getSemesterId() == null) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期等参数
        }
        return Response.success(courseService.getAllCourseNameAndIdArray(userId, userSchool.getSemesterId(), courseName));
    }

    @Register
    @RequestMapping(value = {"/getCourseNameAndId"},method = {RequestMethod.GET})
    @ApiOperation(value = "查询课程名字和ID(课程管理-我负责的)--56--ok")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value, message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
            @ApiResponse(code = ReturnConstant.NO_PERMISSION.value, message = ReturnConstant.NO_PERMISSION.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<List<Map<String, Object>>> getCourseNameAndId(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {

        return Response.success(courseService.getCourseNameAndId(userId));
    }

    @Register
    @RequestMapping(value = {"/signSetting"},method = {RequestMethod.POST})
    @ApiOperation(value = "课程签到设置")
    public Response<Map<String, Object>> signSetting(
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "json:courseId pageindex pagesize") @RequestBody JSONObject json) throws Exception {
        if (!json.containsKey("courseId") || !json.containsKey("needSignLocation") || !json.containsKey("signIntevalTime") ||
                (json.getInt("needSignLocation") == 1 && (!json.containsKey("longitude") || !json.containsKey("latitude")))) {//没有打卡坐标
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        courseService.signSetting(userId, json.getLong("courseId"), json.getInt("needSignLocation"),
                json.containsKey("longitude") ? json.getDouble("longitude") : null,
                json.containsKey("latitude") ? json.getDouble("latitude") : null,
                json.getInt("signIntevalTime"));
        return Response.success();
    }



    @Register
    @RequestMapping(value = {"/getCourseAndTeacher"},method = {RequestMethod.GET})
//    @SystemControllerLog(description = "查询课程名和老师List")
    @ApiOperation(value = "查询课程名和老师List(课程中心)--13--ok")
    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value,message = ReturnConstant.SUCCESS.desc),
            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value,message = ReturnConstant.PARAMETER_INCORRECT.desc),
            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value,message = ReturnConstant.OTHER_ERROR.desc)})
    public Response<Map<String, Object>> getCourseAndTeacher(
            @ApiParam(required = true, value = "学校ID") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {

        UserSchool userSchool = schoolService.getParaByUserId(userId, Calendar.getInstance().getTime());
        if (userSchool == null) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);
        }
        List<Map<String, String>> ret = courseService.getCourseAndTeacher(userSchool.getSchoolId(), userSchool.getSemesterId());
        Map<String, Object> map = new HashMap<>();
        map.put("data", ret);
        return Response.success(map);
    }


    /***************************************  course schedul   *************************************************/


    @Register
    @RequestMapping(value = {"/createCourseSchedule"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "添加课程表")
    @ApiOperation(value = "添加课程表(课程管理-初始)--15--ok")
    public Response<CourseSchedule> createCourseSchedule(
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "详细参数见文档") @RequestBody JSONObject json) throws Exception {
        if (!json.containsKey("startWeekIndex") || !ValidateUtil.isDigits(json.getString("startWeekIndex")) || json.getInt("startWeekIndex") <= 0) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }

        if (!json.containsKey("endWeekIndex") || !ValidateUtil.isDigits(json.getString("endWeekIndex")) || json.getInt("endWeekIndex") <= 0) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        if (!json.containsKey("courseId") || !ValidateUtil.isDigits(json.getString("courseId"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        Integer startWeekIndex = json.getInt("startWeekIndex");
        Integer endWeekIndex = json.getInt("endWeekIndex");
        Long courseId = json.getLong("courseId");
        JSONArray array = json.getJSONArray("data");
        courseService.createCourseSchedule(userId, startWeekIndex, endWeekIndex, courseId, array);
        return Response.success();
    }

    @Register
    @RequestMapping(value = {"/getCourseSchedule"},method = {RequestMethod.POST})
    @ApiOperation(value = "添加课程表(课程管理-初始)--15--ok")
    public Response<?> getCourseSchedule(
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "详细参数见文档") @RequestBody JSONObject json,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (!json.containsKey("courseId")) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        return Response.success(courseService.getCourseSchedule(userId, json.getLong("courseId")));
    }

    @Register
    @RequestMapping(value = {"/updateCourseSchdeule"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "更新课程表")
    @ApiOperation(value = "更新课程表(课程管理-更新)--17--ok")
    public Response<JSONObject> updateCourseSchdeule(
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "json:courseScheduleId必须，其他根据需求") @RequestBody JSONObject json) throws Exception {
        if (!json.containsKey("courseScheduleId") || !ValidateUtil.isDigits(json.getString("courseScheduleId"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        courseService.updateCourseSchedule(userId, json);
        return Response.success(json);
    }

    @Register
    @RequestMapping(value = {"/deleteCourseSchdeule"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "删除课程表")
    @ApiOperation(value = "删除课程表(课程管理-删除)--16--ok")
    public Response<?> deleteCourseSchdeule(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "json:courseScheduleId") @RequestBody JSONObject json,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (!json.containsKey("courseScheduleId") || !ValidateUtil.isDigits(json.getString("courseScheduleId"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        Long courseScheduleId = json.getLong("courseScheduleId");
        courseService.deleteCourseSchedule(userId, courseScheduleId);
        return Response.success(courseScheduleId);
    }

    @Register
    @RequestMapping(value = {"/getCourseScheduleList"},method = {RequestMethod.POST})
    @ApiOperation(value = "查询课程表列表(课程管理主页)---14--ok")
    public Response<Map<String, Object>> getCourseScheduleListForWeb(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "json:courseId pageindex pagesize") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"courseId"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ReturnStatus.PARAMETER_MISS);//必填项请求参数不完整，请检查
        }
        int[] page = MyPage.isValidParams(json);
        Map<String, Object> map = courseService.getCourseScheduleListBycourseIdForWeb(userId, json.getLong("courseId"), page[0], page[1]);
        return Response.success(map);
    }

    @Register
    @RequestMapping(value = {"/getCourseScheduleBasicInfo"},method = {RequestMethod.POST})
    @ApiOperation(value = "查询课次信息---")
    public Response<Map<String, Object>> getCourseScheduleBasicInfo(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "json:courseId, semesterId(非必填默认本学期)") @RequestBody JSONObject json,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (!json.containsKey("courseScheduleId") || !ValidateUtil.isDigits(json.getString("courseScheduleId")) ) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }

        Long semesterId = json.containsKey("semesterId") ?  json.getLong("semesterId") : null;
        Long courseScheduleId = json.getLong("courseScheduleId");

        Map<String, Object> map = courseService.getCourseScheduleBasicInfo(userId, courseScheduleId, semesterId);
        return Response.success(map);
    }

    @Register
    @RequestMapping(value = {"/getCourseScheduleById"},method = {RequestMethod.POST})
    @ApiOperation(value = "查询课次信息---")
    public Response<?> getCourseScheduleById(
                    @ApiParam(required = true, value = "courseScheduleId") @RequestBody JSONObject json) throws Exception {
        if (!json.containsKey("courseScheduleId") || !ValidateUtil.isDigits(json.getString("courseScheduleId")) ) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        Map<String, Object> map = courseService.getCourseScheduleById(json.getLong("courseScheduleId"));
        return Response.success(map);
    }

    /**************************  offical   **************************************************************/
    @Register
    @RequestMapping(value = {"/getCourseListBySchoolId"},method = {RequestMethod.POST})
    @ApiOperation(value = "根据schoolId查询课程列表(官方后台--校园管理--学校信息--课程)--7--ok")
    public Response<List<Map<String, Object>>> getAllCourseListBySchoolId(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "老师或者管理员ID") @RequestParam Long userId,
            @ApiParam(required = true, value = "json:schoolId") @RequestBody JSONObject json,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (!json.containsKey("schoolId") || !ValidateUtil.isDigits(json.getString("schoolId"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        List<Map<String, Object>> list = courseService.getCourseListBySchoolId(userId, json.getLong("schoolId"));
        list.forEach(i->i.put("key", i.get("courseId")));
        return Response.success(list);
    }


    @Register
    @RequestMapping(value = {"/getCourseStatisticByStudent"},method = {RequestMethod.POST})
    @ApiOperation(value = "查询学生主页课程列表信息")
    public Response<MyPageInfo> getCourseStatisticByStudent(
        @ApiParam(required = true, value = "token") @RequestParam String token,
        @ApiParam(required = true, value = "userId") @RequestParam Long userId,
        @ApiParam(required = true, value = "签名") @RequestParam String sign,
        @ApiParam(required = true, value = "studentId, pageindex, pagesize") @RequestBody JSONObject json
        ) throws Exception {
        String[] params = new String[]{"studentId"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ReturnStatus.PARAMETER_MISS);//必填项请求参数不完整，请检查
        }
        int[] page = MyPage.isValidParams(json);
        long studentId = json.getLong("studentId");
        User user = userCacheService.getUserById(studentId);
        if(null == user) {
            return Response.fail(ResultMsg.STUDENT_NOT_FOUND);//学生不存在
        }
        if(null == user.getCourseId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//本学期尚未选课
        }
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("studentId", studentId);
        paramsMap.put("courseId", user.getCourseId());
        MyPageInfo myPageInfo = courseService.getCourseStatisticByStudent(paramsMap, page[0], page[1]);
        return Response.success(myPageInfo);
    }


    @Register
    @RequestMapping(value = {"/getCoursescheduleDescList"},method = {RequestMethod.POST})
    @ApiOperation(value = "查询课程表(总表)列表----新增接口")
    public Response<Map<String, Object>> getCoursescheduleDescList(
        @ApiParam(required = true, value = "userId") @RequestParam Long userId,
        @ApiParam(required = true, value = "token") @RequestParam String token,
        @ApiParam(required = true, value = "签名") @RequestParam String sign,
        @ApiParam(required = false, value = "courseId") @RequestBody(required = false) JSONObject json
    ) throws Exception {
        UserSchool userSchool = schoolService.getParaByUserId(userId, Calendar.getInstance().getTime());
        if (userSchool == null) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);
        }
        Long courseId = null;
        Long semesterId = null;
        if(ValidateUtil.jsonValidateWithKey(json, "semesterId") && json.getLong("semesterId") > 0L) {
            semesterId = json.getLong("semesterId");
            userSchool.setSemesterId(semesterId);
        }
        if(ValidateUtil.jsonValidateWithKey(json, "courseId")) {
            courseId = json.getLong("courseId");//如果有课程ID，就不需要学期ID了
            userSchool.setSemesterId(null);
        }
        List<Map<String, Object>> ret = courseService.getCoursescheduleDescList(courseId, userSchool);
        Map<String, Object> map = new HashMap<>();
        map.put("data", ret);
        map.put("maxpage", 1);
        map.put("pageindex", 1);
        map.put("pagesize", ret.size());
        return Response.success(map);
    }


    @Register
    @RequestMapping(value = {"/deleteCoursescheduleDescById"},method = {RequestMethod.POST})
    @ApiOperation(value = "查询课程表(总表)列表----新增接口")
    public Response<Long> deleteCoursescheduleDescById(
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "descId") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"descId"};//必填项检查
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        long descId = json.getLong("descId");
        courseService.deleteCoursescheduleDescById(descId);
        return Response.success(descId);
    }

}
