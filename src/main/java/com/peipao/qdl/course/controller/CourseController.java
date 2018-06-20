//package com.peipao.qdl.course.controller;
//
//
//import com.peipao.framework.annotation.Register;
//import com.peipao.framework.annotation.SystemControllerLog;
//import com.peipao.framework.constant.ReturnConstant;
//import com.peipao.framework.constant.WebConstants;
//import com.peipao.framework.model.Response;
//import com.peipao.framework.model.ReturnStatus;
//import com.peipao.framework.util.ValidateUtil;
//import com.peipao.qdl.course.model.Course;
//import com.peipao.qdl.course.service.CourseCacheService;
//import com.peipao.qdl.course.service.CourseService;
//import com.peipao.qdl.school.model.UserSchool;
//import com.peipao.qdl.school.service.SchoolService;
//import com.peipao.qdl.statistics.service.UserStatisticUtilService;
//import com.peipao.qdl.user.model.User;
//import com.peipao.qdl.user.model.UserTypeEnum;
//import com.peipao.qdl.user.service.UserService;
//import io.swagger.annotations.*;
//import net.sf.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author meteor.wu
// * @since 2017/6/26
// **/
//@Api(value = "/course", description = "课程")
//@RestController
//@RequestMapping({"/course"})
//public class CourseController {
//    @Autowired
//    private CourseService courseService;
//    @Autowired
//    private SchoolService schoolService;
//    @Autowired
//    private CourseCacheService courseCacheService;
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private UserStatisticUtilService userStatisticUtilService;
//
//    @Register
//    @RequestMapping(value = {"/app/chooseCourse"},method = {RequestMethod.POST})
//    @SystemControllerLog(description = "选课")
//    @ApiOperation(value = "选课(课程中心)--14--ok")
//    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value,message = ReturnConstant.SUCCESS.desc),
//                   @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value,message = ReturnConstant.PARAMETER_INCORRECT.desc),
//                   @ApiResponse(code = ReturnConstant.COURSE_CHOOSE_COUNT_LIMIT.value,message = ReturnConstant.COURSE_CHOOSE_COUNT_LIMIT.desc),
//                   @ApiResponse(code = ReturnConstant.COURSE_NOT_EXIST.value,message = ReturnConstant.COURSE_NOT_EXIST.desc),
//                   @ApiResponse(code = ReturnConstant.COURSE_HAVE_CHOOSE.value,message = ReturnConstant.COURSE_HAVE_CHOOSE.desc),
//                   @ApiResponse(code = ReturnConstant.OTHER_ERROR.value,message = ReturnConstant.OTHER_ERROR.desc)})
//    public Response<?> chooseCourse(
//            @ApiParam(required = true, value = "token") @RequestParam String token,
//            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
//            @ApiParam(required = true, value = "json:courseId") @RequestBody JSONObject json,
//            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
//        if (!json.containsKey("courseId") || !ValidateUtil.isDigits(json.getString("courseId"))) {
//            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
//        }
//        Long courseId = Long.valueOf(json.getString("courseId"));
//        courseService.chooseCourse(userId, courseId);
//        return Response.success();
//    }
//
//    @Register
//    @RequestMapping(value = {"/pub/chooseCourseByTeacher"},method = {RequestMethod.POST})
//    @ApiOperation(value = "老师给学生选课(老师app)")
//    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value,message = ReturnConstant.SUCCESS.desc),
//                   @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value,message = ReturnConstant.PARAMETER_INCORRECT.desc),
//                   @ApiResponse(code = ReturnConstant.COURSE_CHOOSE_COUNT_LIMIT.value,message = ReturnConstant.COURSE_CHOOSE_COUNT_LIMIT.desc),
//                   @ApiResponse(code = ReturnConstant.COURSE_NOT_EXIST.value,message = ReturnConstant.COURSE_NOT_EXIST.desc),
//                   @ApiResponse(code = ReturnConstant.COURSE_HAVE_CHOOSE.value,message = ReturnConstant.COURSE_HAVE_CHOOSE.desc),
//                   @ApiResponse(code = ReturnConstant.OTHER_ERROR.value,message = ReturnConstant.OTHER_ERROR.desc)})
//    public Response<?> chooseCourseByTeacher(
//            @ApiParam(required = true, value = "token") @RequestParam String token,
//            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
//            @ApiParam(required = true, value = "json:courseId,studentId") @RequestBody JSONObject json,
//            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
//        if (!json.containsKey("courseId") || !json.containsKey("studentId")) {
//            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
//        }
//        if (!ValidateUtil.isDigits(json.getString("courseId")) || !ValidateUtil.isDigits(json.getString("studentId"))) {
//            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
//        }
//
//        Long courseId = Long.valueOf(json.getString("courseId"));
//        Long studentId = Long.valueOf(json.getString("studentId"));
//        courseService.chooseCourse(userId, studentId, courseId);
//        return Response.success();
//    }
//
//    @Register
//    @RequestMapping(value = {"/app/getSelectCourseList"},method = {RequestMethod.POST})
//    @ApiOperation(value = "查询课程名和老师List(课程中心)--13--ok")
//    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value,message = ReturnConstant.SUCCESS.desc),
//                   @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value,message = ReturnConstant.PARAMETER_INCORRECT.desc),
//                   @ApiResponse(code = ReturnConstant.OTHER_ERROR.value,message = ReturnConstant.OTHER_ERROR.desc)})
//    public Response<Map<String, Object>> getSelectCourseList(
//            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
//            @ApiParam(required = true, value = "token") @RequestParam String token,
//            @ApiParam(required = true, value = "签名") @RequestParam String sign,
//            @ApiParam(required = true, value = "value") @RequestBody JSONObject json) throws Exception {
//
//        String value= json.containsKey("value") ? json.getString("value") : null;
//        UserSchool userSchool = schoolService.getParaByUserId(userId, Calendar.getInstance().getTime());
//        if (userSchool == null) {
//            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);
//        }
//        Map<String, Object> paramsMap = new HashMap<>();
//        paramsMap.put("semesterId", userSchool.getSemesterId());
//        paramsMap.put("value", value);
//        List<Map<String, Object>> ret = courseService.getSelectCourseList(paramsMap);
//        Map<String, Object> map = new HashMap<>();
//        map.put("data", ret);
////        map.put("courseId", userSchool.getCourseId());
////        if (userSchool.getCourseId() != null) {
////            Course course = courseCacheService.getCourseById(userSchool.getCourseId());
////            if(null != course) {
////                //return Response.fail(ReturnStatus.COURSE_NOT_EXIST);//课程不存在
////                map.put("name", course.getName());
////            } else {
////                userSchool.setCourseId(null);
////            }
////        }
////        if (userSchool.getCourseId() == null){
////            map.put("chooseCount", 0);
////        }else{
////            map.put("chooseCount", courseService.getRecordCount(userId, userSchool.getSemesterId()));
////        }
////        map.put("maxCount", WebConstants.Course.COURSECHOOSELIMIT);
//        return Response.success(map);
//    }
//
//    @Register
//    @RequestMapping(value = {"/app/getCourseScheduleDescDetail"},method = {RequestMethod.POST})
//    @ApiOperation(value = "查询课程详情")
//    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value,message = ReturnConstant.SUCCESS.desc),
//                   @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value,message = ReturnConstant.PARAMETER_INCORRECT.desc),
//                   @ApiResponse(code = ReturnConstant.OTHER_ERROR.value,message = ReturnConstant.OTHER_ERROR.desc)})
//    public Response<Map<String, Object>> getCourseDetail(
//            @ApiParam(required = true, value = "学校ID") @RequestParam String token,
//            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
//            @ApiParam(required = true, value = "json:courseId") @RequestBody JSONObject json,
//            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
//        if (!json.containsKey("courseId")) {
//            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
//        }
//        long courseId = json.getLong("courseId");
//        Map<String, Object> paramsMap = new HashMap<>();
//        paramsMap.put("courseId", courseId);
//        List<Map<String, Object>> ret = courseService.getSelectCourseList(paramsMap);
//        if (ret.size() > 0) {
//            int studentCount = userService.countByCourseId(courseId);//课程当前报名人数
//
//            Map<String, Object> map = ret.get(0);
//            map.put("desc", courseCacheService.getCourseById(Long.valueOf(map.get("courseId").toString())).getDesc());
//            map.put("studentCount", studentCount);
//            UserSchool userSchool = schoolService.getParaByUserId(userId, Calendar.getInstance().getTime());
//            if (userSchool == null) {
//                return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);
//            }
//            map.put("oldCourseId", userSchool.getCourseId());
//            if (userSchool.getCourseId() == null){
//                map.put("chooseCount", 0);
//            }else{
//                map.put("chooseCount", courseService.getRecordCount(userId, userSchool.getSemesterId()));
//            }
//            map.put("maxCount", WebConstants.Course.COURSECHOOSELIMIT);
//
//            return Response.success(map);
//        }
//        return Response.success();
//    }
//
//    @Register
//    @RequestMapping(value = {"/app/getCourseScheduleList"},method = {RequestMethod.POST})
//    @ApiOperation("查询课程表列表(课程)--12--ok")
//    public Response<List<Map<String, Object>>> getCourseScheduleListForApp(
//        @ApiParam(required = true, value = "token") @RequestParam String token,
//        @ApiParam(required = true, value = "userId") @RequestParam Long userId,
//        @ApiParam(required = true, value = "签名") @RequestParam String sign,
//        @ApiParam(required = true, value = "json:weekIndex周次") @RequestBody JSONObject json
//    ) throws Exception {
//        if (!json.containsKey("weekIndex") || !ValidateUtil.isDigits(json.getString("weekIndex"))) {
//            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
//        }
//        int weekIndex = Integer.parseInt(json.getString("weekIndex"));
//        if (weekIndex <= 0 || weekIndex > 30 ) {
//            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
//        }
//        List<Map<String, Object>> ret = courseService.getCourseScheduleList(userId, weekIndex);
//        return Response.success(ret);
//    }
//
//    @Register
//    @RequestMapping(value = {"/app/sign"},method = {RequestMethod.POST})
//    @SystemControllerLog(description = "课程签到app")
//    @ApiOperation(value = "课程签到(课程详情)--42--ok")
//    public Response<?> sign(@ApiParam(required = true, value = "token") @RequestParam String token,
//                            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
//                            @ApiParam(required = true, value = "json:courseScheduleId") @RequestBody JSONObject json,
//                            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
//        if (!json.containsKey("courseScheduleId") ){
//            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
//        }
//        if (!ValidateUtil.isDigits(json.getString("courseScheduleId"))) {
//            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
//        }
//        Long courseScheduleId = Long.valueOf(json.getString("courseScheduleId"));
//        Double longitude = json.containsKey("longitude") ? Double.valueOf(json.getString("longitude")) : null;
//        Double latitude = json.containsKey("latitude") ? Double.valueOf(json.getString("latitude")) : null;
//
//        courseService.sign(userId, courseScheduleId, longitude, latitude);
//        return Response.success();
//    }
//
//    @Register
//    @RequestMapping(value = {"/pub/getEnrollCountAndSignCount"},method = {RequestMethod.POST})
//    @ApiOperation(value = "查询课程表报名和签到人数(课程详情)---62--ok")
//    public Response<Map<String, Object>> getEnrollCountAndSignCount(
//            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
//            @ApiParam(required = true, value = "json:courseScheduleId") @RequestBody JSONObject json) throws Exception {
//
//        if (!json.containsKey("courseScheduleId") || !ValidateUtil.isDigits(json.getString("courseScheduleId"))) {
//            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
//        }
//        Long courseScheduleId = json.getLong("courseScheduleId");
//
//        Map<String, Object> map = courseService.getEnrollCountAndSignCount(userId, courseScheduleId);
//        return Response.success(map);
//    }
//
//    @Register
//    @RequestMapping(value = {"/pub/getCourseScheduleWithTeacherName"},method = {RequestMethod.POST})
//    @ApiOperation(value = "查询课程表信息和老师名字(课程详情top)---62--ok")
//    public Response<Map<String, Object>> getCourseScheduleWithTeacherName(
//            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
//            @ApiParam(required = true, value = "json:courseScheduleId") @RequestBody JSONObject json) throws Exception {
//        if (!json.containsKey("courseScheduleId") || !ValidateUtil.isDigits(json.getString("courseScheduleId"))) {
//            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
//        }
//        Long courseScheduleId = json.getLong("courseScheduleId");
//
//        Map<String, Object> map = courseService.getCourseScheduleWithTeacherName(userId, courseScheduleId);
//        return Response.success(map);
//    }
//
//    @Register
//    @RequestMapping(value = "/app/getCourseScheduleDetail", method = RequestMethod.POST)
//    public Response getCourseScheduleDetail(
//            @ApiParam(required = true, value = "token") @RequestParam String token,
//            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
//            @ApiParam(required = true, value = "签名") @RequestParam String sign,
//            @ApiParam(required = true, value = "RequestBody查询条件") @RequestBody JSONObject json
//    ) throws Exception {
//        if (!json.containsKey("courseScheduleId") || !ValidateUtil.isDigits(json.getString("courseScheduleId"))) {
//            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
//        }
//        return Response.success(courseService.getCourseScheduleDetail(userId, json.getLong("courseScheduleId")));
//    }
//
//    /**************************************** 以下教务端APP接口 **************************************************************************/
//
//    @Register
//    @RequestMapping(value = {"/app/getChooseCourseList"},method = {RequestMethod.POST})
//    @ApiOperation(value = "查询课程名和老师List(老师给学生换课选课列表)")
//    public Response<List<Map<String, Object>>> getChooseCourseList(
//            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
//            @ApiParam(required = true, value = "token") @RequestParam String token,
//            @ApiParam(required = true, value = "签名") @RequestParam String sign,
//            @ApiParam(required = false, value = "queryString") @RequestBody(required = false) JSONObject json
//    ) throws Exception {
//        UserSchool userSchool = schoolService.getParaByUserId(userId, Calendar.getInstance().getTime());
//        if (userSchool == null) {
//            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);
//        }
//        Map<String, Object> paramsMap = new HashMap<>();
//        paramsMap.put("semesterId", userSchool.getSemesterId());
//        //查询条件：按照课程名称，或者老师名字进行查询
//        if(ValidateUtil.jsonValidateWithKey(json, "queryString")) {
//            paramsMap.put("value", json.getString("queryString"));
//        }
//        User currUser = userService.validateSchoolUser(userId);//当前操作的用户(教师或学校管理员)
//        if(currUser.getUserType() == UserTypeEnum.TEACHER.getValue()) {
//            userStatisticUtilService.initCourseArrayString(paramsMap, currUser.getUserId(), userSchool.getSemesterId());
//        }
//        List<Map<String, Object>> ret = courseService.getSelectCourseList(paramsMap);
//        return Response.success(ret);
//    }
//
//
//}
