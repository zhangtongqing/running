package com.peipao.qdl.course.controller;


import com.peipao.framework.annotation.Register;
import com.peipao.framework.annotation.SystemControllerLog;
import com.peipao.framework.constant.ReturnConstant;
import com.peipao.framework.constant.WebConstants;
import com.peipao.framework.model.Response;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.page.MyPage;
import com.peipao.framework.util.ValidateUtil;
import com.peipao.qdl.course.model.Course;
import com.peipao.qdl.course.service.CourseCacheService;
import com.peipao.qdl.course.service.CourseService;
import com.peipao.qdl.school.model.UserSchool;
import com.peipao.qdl.school.service.SchoolService;
import com.peipao.qdl.statistics.service.UserStatisticUtilService;
import com.peipao.qdl.user.model.User;
import com.peipao.qdl.user.model.UserTypeEnum;
import com.peipao.qdl.user.service.UserService;
import io.swagger.annotations.*;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author meteor.wu
 * @since 2017/6/26
 **/
@Api(value = "/course", description = "课程")
@RestController
@RequestMapping({"/course/pub"})
public class CourseControllerPUB {
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
    @RequestMapping(value = {"/chooseCourseByTeacher"},method = {RequestMethod.POST})
    @ApiOperation(value = "老师给学生选课(老师app)")
    public Response<?> chooseCourseByTeacher(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "json:courseId,studentId") @RequestBody JSONObject json,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (!json.containsKey("courseId") || !json.containsKey("studentId")) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        if (!ValidateUtil.isDigits(json.getString("courseId")) || !ValidateUtil.isDigits(json.getString("studentId"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }

        Long courseId = Long.valueOf(json.getString("courseId"));
        Long studentId = Long.valueOf(json.getString("studentId"));
        courseService.chooseCourse(userId, studentId, courseId);
        return Response.success();
    }

    @Register
    @RequestMapping(value = {"/getEnrollCountAndSignCount"},method = {RequestMethod.POST})
    @ApiOperation(value = "查询课程表报名和签到人数(课程详情)---62--ok")
    public Response<Map<String, Object>> getEnrollCountAndSignCount(
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "json:courseScheduleId") @RequestBody JSONObject json) throws Exception {

        if (!json.containsKey("courseScheduleId") || !ValidateUtil.isDigits(json.getString("courseScheduleId"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        Long courseScheduleId = json.getLong("courseScheduleId");

        Map<String, Object> map = courseService.getEnrollCountAndSignCount(userId, courseScheduleId);
        return Response.success(map);
    }

    @Register
    @RequestMapping(value = {"/getCourseScheduleWithTeacherName"},method = {RequestMethod.POST})
    @ApiOperation(value = "查询课程表信息和老师名字(课程详情top)---62--ok")
    public Response<Map<String, Object>> getCourseScheduleWithTeacherName(
            @ApiParam(required = true, value = "userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "json:courseScheduleId") @RequestBody JSONObject json) throws Exception {
        if (!json.containsKey("courseScheduleId") || !ValidateUtil.isDigits(json.getString("courseScheduleId"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        Long courseScheduleId = json.getLong("courseScheduleId");

        Map<String, Object> map = courseService.getCourseScheduleWithTeacherName(userId, courseScheduleId);
        return Response.success(map);
    }

}
