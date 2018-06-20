package com.peipao.qdl.course.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.constant.WebConstants;
import com.peipao.framework.exception.BusinessException;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.page.MyPage;
import com.peipao.framework.page.MyPageInfo;
import com.peipao.framework.util.DateUtil;
import com.peipao.framework.util.DateUtils;
import com.peipao.framework.util.LatLonUtil;
import com.peipao.qdl.course.dao.CourseChooseRecordDao;
import com.peipao.qdl.course.dao.CourseDao;
import com.peipao.qdl.course.dao.CourseScheduleDescDao;
import com.peipao.qdl.course.dao.CourseStatisticDao;
import com.peipao.qdl.course.model.*;
import com.peipao.qdl.school.model.Semester;
import com.peipao.qdl.school.model.UserSchool;
import com.peipao.qdl.school.service.SchoolCacheService;
import com.peipao.qdl.school.service.SchoolService;
import com.peipao.qdl.schooluser.service.SchoolUserService;
import com.peipao.qdl.statistics.service.UserStatisticService;
import com.peipao.qdl.statistics.service.UserStatisticUtilService;
import com.peipao.qdl.user.model.User;
import com.peipao.qdl.user.model.UserTypeEnum;
import com.peipao.qdl.user.service.UserCacheService;
import com.peipao.qdl.user.service.UserService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author meteor.wu
 * @since 2017/7/4
 **/
@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseChooseRecordDao courseChooseRecordDao;

    @Autowired
    private SchoolUserService schoolUserService;

    @Autowired
    private UserCacheService userCacheService;

    @Autowired
    private SchoolCacheService schoolCacheService;

    @Autowired
    private CourseCacheService courseCacheService;

    @Autowired
    private CourseScheduleDescDao courseScheduleDescDao;
    @Autowired
    private CourseStatisticDao courseStatisticDao;
    @Autowired
    private UserStatisticService userStatisticService;
    @Autowired
    private UserStatisticUtilService userStatisticUtilService;


    @Override
    public List<Map<String, Object>> getSelectCourseList(Map<String, Object> paramsMap, Long chooseCourseId) throws Exception {
        List<Map<String, Object>> list = courseDao.getSelectCourseList(paramsMap);
        List<Map<String, Object>> ret = new ArrayList<>();

        int courseId = list.size() > 0 ? Integer.parseInt(list.get(0).get("courseId").toString()) : -1;
        for (Map<String, Object> elment : list) {
            if(null != chooseCourseId &&  chooseCourseId == Integer.parseInt(elment.get("courseId").toString())){
                elment.put("chooseFlag",1);
            }else{
                elment.put("chooseFlag",0);
            }
            if (courseId == Integer.parseInt(elment.get("courseId").toString())) {
                if (ret.size() == 0) {// index = 0
                    addResult(elment, ret);
                } else {// 同courseId追加数据
                    Map<String, Object> map = ret.get(ret.size() - 1);
                    List<Map<String, Object>> descs = (List<Map<String, Object>>) map.get("descs");
                    generateDesc(descs, elment);// 添加内层的数组
                }
            } else {//　courseId不相同
                addResult(elment, ret);
                courseId = Integer.parseInt(elment.get("courseId").toString());
            }
        }
        return ret;
    }

    @Override
    public List<Map<String, Object>> getCoursescheduleDescList(Long courseId, UserSchool userSchool) throws Exception {
        User teacher = userService.validateUser(userSchool.getUserId());
        if (teacher.getUserType() == UserTypeEnum.SCHOOLMANAGER.getValue()) {
            userSchool.setUserId(null);
        }
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("courseId", courseId);
        paramsMap.put("semesterId", userSchool.getSemesterId());
        paramsMap.put("userId", userSchool.getUserId());
        List<Map<String, Object>> list = courseDao.getCoursescheduleDescList(paramsMap);
        List<Map<String, Object>> ret = new ArrayList<>();

        int cId = list.size() > 0 ? Integer.parseInt(list.get(0).get("courseId").toString()) : -1;
        for (Map<String, Object> elment : list) {
            if (cId == Integer.parseInt(elment.get("courseId").toString())) {
                if (ret.size() == 0) {// index = 0
                    addResultForWeb(elment, ret);
                } else {// 同courseId追加数据
                    Map<String, Object> map = ret.get(ret.size() - 1);
                    List<Map<String, Object>> descs = (List<Map<String, Object>>) map.get("descs");
                    generateDesc(descs, elment);// 添加内层的数组
                }
            } else {//　courseId不相同
                addResultForWeb(elment, ret);
                cId = Integer.parseInt(elment.get("courseId").toString());
            }
        }
        return ret;
    }

    private void addResult(Map<String, Object> data, List<Map<String, Object>> ret) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", data.get("name"));
        map.put("courseId", data.get("courseId"));
        map.put("username", data.get("username"));
        map.put("maxCapacity", data.get("maxCapacity"));//课程报名人数上限
        map.put("chooseFlag",data.get("chooseFlag"));//是否当前选课标识
        List<Map<String, Object>> descs = new ArrayList<>();
        generateDesc(descs, data);// 添加内层的数组

        map.put("descs", descs);
        ret.add(map);
    }

    private void addResultForWeb(Map<String, Object> data, List<Map<String, Object>> ret) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", data.get("name"));
        map.put("courseId", data.get("courseId"));
        map.put("username", data.get("username"));
        map.put("maxCapacity", data.get("maxCapacity"));//课程报名人数上限
        map.put("descId", data.get("descId"));//desc表主键ID
        List<Map<String, Object>> descs = new ArrayList<>();
        generateDesc(descs, data);// 添加内层的数组

        map.put("descs", descs);
        ret.add(map);
    }

    private void generateDesc(List<Map<String, Object>> descs, Map<String, Object> data) {
        Map<String, Object> desc = new HashMap<>();
        desc.put("weekStart", data.get("weekStart"));
        desc.put("weekEnd", data.get("weekEnd"));
        desc.put("weekday", data.get("weekday"));
        desc.put("time", data.get("time"));
        descs.add(desc);
    }

    @Override
    public List<Map<String, Object>>  getCourseScheduleList(Long userId, Integer weekIndex) throws Exception {
        UserSchool userSchool = schoolService.getParaByUserId(userId, Calendar.getInstance().getTime());
        if (null == userSchool.getSemesterId()) {
            throw new BusinessException(ReturnStatus.USERSCHOOL_PARAMETER_MISS);
        }
        Map<String, Object> paramsMap = new HashMap<>();//构建查询参数
        paramsMap.put("semesterId", userSchool.getSemesterId());
        paramsMap.put("userId", userId);
        paramsMap.put("weekIndex", weekIndex);
        paramsMap.put("semesterId", userSchool.getSemesterId());
        paramsMap.put("schoolId", userSchool.getSchoolId());
        if(userSchool.getUserType() == UserTypeEnum.STUDENT.getValue()) {
            if (null == userSchool.getCourseId()) {
                throw new BusinessException(ReturnStatus.USERSCHOOL_PARAMETER_MISS);
            }
            paramsMap.put("courseId", userSchool.getCourseId());
        } else if(userSchool.getUserType() == UserTypeEnum.TEACHER.getValue() || userSchool.getUserType() == UserTypeEnum.SCHOOLMANAGER.getValue()) {
            userStatisticUtilService.initCourseArrayString(paramsMap, userId, userSchool.getSemesterId());
        } else {
            throw new BusinessException(ResultMsg.USER_TYPE_MISSING);//未能识别用户身份
        }
        List<Map<String, Object>> list = courseDao.queryCourseScheduleListWithName(paramsMap);
        //不要结束时间
        list.forEach(i->calCourseScheduleStatus(i));
        return list;
    }

    @Transactional
    @Override
    public void sign(Long userId, Long courseScheduleId, Double longitude, Double latitude) throws Exception {
        // validate courseScheduleId
        CourseSchedule courseSchedule = courseDao.queryCourseScheduleById(courseScheduleId);
        if (courseSchedule == null) {
            throw new BusinessException(ReturnStatus.COURSE_SCHEDULE_NOT_EXIST);
        }

        Date[] time = getStartEndTime(courseSchedule);
        if ( time[0].getTime() > Calendar.getInstance().getTimeInMillis()
                || time[0].getTime()+15*60*1000 < Calendar.getInstance().getTimeInMillis()){// TODO:METEOR.WU  时间优化
            throw new BusinessException(ReturnStatus.COURSE_SIGN_AT_TIME);
        }

        Integer count = courseDao.checkHaveSign(courseScheduleId, userId);
        if (count > 0) {
            throw new BusinessException(ReturnStatus.COURSE_HAVE_SIGN);
        }

        Course course = courseCacheService.getCourseById(courseSchedule.getCourseId());
        if (course.getNeedSignLocation() == 1) {
            double length = LatLonUtil.LantitudeLongitudeDist(longitude, latitude, course.getLongitude(), course.getLatitude());
            if (length > WebConstants.Course.SIGN_DISTANCE_DIFF) {
                throw new BusinessException(ReturnStatus.ACTIVITY_SIGN_AT_ADDRESS);
            }
        }

        CourseMember courseMember = new CourseMember(courseScheduleId, course.getCourseId(), userId, latitude, longitude, CourseSignTypeEnum.FACE2FACE.ordinal());
        courseDao.insertCourseMember(courseMember);

        synchronized (courseScheduleId){
            courseDao.updateCourseScheduleSomeCount(courseScheduleId, 1);
        }
//        int duration = (int)courseSchedule.getDuration();//课时(2.1.0开始没有课时统计了)
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("userId", userId);
        paramsMap.put("schoolId", course.getSchoolId());
        paramsMap.put("semesterId", course.getSemesterId());
        paramsMap.put("attendanceCount", 1);//签到成功，增加一次
        userStatisticService.insertOrUpdateStatisticByCourseSign(paramsMap);
    }


    @Override
    public Map<String, Object> getCourseScheduleMemberWeb(Long userId, Long courseScheduleId, int pageindex, int pagesize) throws Exception {
        Map<String, Object> map = validateTeacherAndCourseSchedule(userId, courseScheduleId);
        CourseSchedule schedule = (CourseSchedule) map.get("schedule");

        Long allcount = courseDao.queryCourseMemberListCount(schedule.getCourseId());
        if (allcount == 0) {
            return MyPage.processPage(allcount, pagesize, pageindex, new ArrayList());
        }
        List<Map<String, Object>> list = courseDao.queryCourseMemberListWeb(schedule.getCourseId(), courseScheduleId, (pageindex-1)*pagesize, pagesize);
        list.forEach(i->i.put("key", i.get("userId")));
        return MyPage.processPage(allcount, pagesize, pageindex, list);
    }

    @Override
    public MyPageInfo getCourseScheduleMember(Map<String, Object> paramsMap, int pageindex, int pagesize) throws Exception {
        PageHelper.startPage(pageindex, pagesize);
        List<Map<String, Object>> list = courseDao.getCourseScheduleMember(paramsMap);
        PageInfo<Map<String, Object>> p = new PageInfo<Map<String, Object>>(list);
        MyPageInfo pageInfo = new MyPageInfo(p);
        return pageInfo;
    }

    @Override
    public Integer getCourseScheduleMemberCount(Map<String, Object> paramsMap) throws Exception {
        return courseDao.getCourseScheduleMemberCount(paramsMap);
    }


    @Override
    public Map<String, Object> getCourseListForWeb(Long userId) throws Exception {
        User user = schoolUserService.validateTeacher(userId);
        UserSchool userSchool = schoolService.getParaByUserId(userId, Calendar.getInstance().getTime());
        if (userSchool == null || userSchool.getSchoolId() == null || userSchool.getSemesterId() == null) {
            throw new BusinessException(ReturnStatus.USERSCHOOL_PARAMETER_MISS);
        }

        List list = null;
        if (user.getUserType() == UserTypeEnum.TEACHER.getValue()) {
            list= courseDao.queryCourseListForWeb(userId, userSchool.getSchoolId(), userSchool.getSemesterId());
        }else if (user.getUserType() == UserTypeEnum.SCHOOLMANAGER.getValue()){
            list =  courseDao.queryCourseListForWeb(null, userSchool.getSchoolId(), userSchool.getSemesterId());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("data", list);
        map.put("pagesize", list.size());
        map.put("pageindex", 1);
        return map;
    }

    @Override
    public Course validateCourse(Long courseId) throws Exception {
        Course course = courseCacheService.getCourseById(courseId);
        if (course == null) {
            throw new BusinessException(ReturnStatus.COURSE_NOT_EXIST);
        }
        return course;
    }

    @Override
    public void chooseCourse(Long userId, Long courseId) throws Exception {
        UserSchool userSchool = schoolService.getParaByUserId(userId, Calendar.getInstance().getTime());
        if (userSchool == null || userSchool.getSchoolId() == null) {
            throw new BusinessException(ReturnStatus.USERSCHOOL_PARAMETER_MISS);
        }
        if (userSchool.getCourseId() != null){
            Integer count = courseChooseRecordDao.queryRecordCount(userSchool.getUserId(), userSchool.getSemesterId());
            if (count != null && count >= WebConstants.Course.COURSECHOOSELIMIT){
                throw new BusinessException(ReturnStatus.COURSE_CHOOSE_COUNT_LIMIT);
            }
        }
        chooseCourse(userSchool, courseId, true);
    }

    @Override
    public void chooseCourse(Long userId, Long studentId, Long courseId) throws Exception {
        User teacher = schoolUserService.validateTeacher(userId);

        UserSchool userSchool = schoolService.getParaByUserId(studentId, Calendar.getInstance().getTime());
        if (userSchool == null || userSchool.getSchoolId() == null ) {
            throw new BusinessException(ReturnStatus.USERSCHOOL_PARAMETER_MISS);
        }
        if (userSchool.getSchoolId().longValue() != teacher.getSchoolId()) {
            throw new BusinessException(ReturnStatus.NO_PERMISSION);
        }
        chooseCourse(userSchool, courseId, false);
    }


    private synchronized void chooseCourse(UserSchool userSchool, Long courseId, boolean isStudent)throws Exception{
        Course course = validateCourse(courseId);

        if (userSchool.getCourseId() != null){
            if (userSchool.getCourseId().longValue() == courseId) {
                throw new BusinessException(ReturnStatus.COURSE_HAVE_CHOOSE);
            }
        }

        if (course.getSchoolId().longValue() != userSchool.getSchoolId()) {
            throw new BusinessException(ReturnStatus.NO_PERMISSION);
        }

        int count = userService.countByCourseId(courseId);
        if (isStudent && count >= course.getMaxCapacity()) {
            throw new BusinessException(ReturnStatus.COURSE_MAX_CAPACITY);
        }

        if(isStudent) {
            CourseChooseRecord courseChooseRecord = new CourseChooseRecord(userSchool.getUserId(), userSchool.getSemesterId(), userSchool.getCourseId(), courseId);
            if (userSchool.getCourseId() == null) {
                courseChooseRecord.setOldCourseId(WebConstants.Course.OLD_COURSE_ID_BE_NULL);
            }
            courseChooseRecord.setCreateTime(Calendar.getInstance().getTime());
            courseChooseRecordDao.insertCourseChooseRecord(courseChooseRecord);
        }
        User userInsert = new User();
        userInsert.setUserId(userSchool.getUserId());
        userInsert.setCourseId(courseId);
        userCacheService.updateUser(userInsert);
    }

    @Override
    public Integer getRecordCount(Long userId, Long semesterId) throws Exception {
        return courseChooseRecordDao.queryRecordCount(userId, semesterId);
    }

    @Transactional
    @Override
    public void addCourse(Long userId, Course course) throws Exception {
        schoolUserService.validateSchoolManager(userId);
//        schoolUserService.validateTeacher(course.getUserId());

        UserSchool userSchool = schoolService.getParaByUserId(userId, Calendar.getInstance().getTime());
        if (userSchool == null || userSchool.getSchoolId() == null || userSchool.getSemesterId() == null) {
            throw new BusinessException(ReturnStatus.USERSCHOOL_PARAMETER_MISS);
        }

        course.setCreateTime(Calendar.getInstance().getTime());
        course.setSchoolId(userSchool.getSchoolId());
        course.setSemesterId(userSchool.getSemesterId());
        courseCacheService.addCourse(course);
    }

    @Transactional
    @Override
    public void updateCourse(Long userId, Course course) throws Exception {
        //schoolUserService.validateSchoolManager(userId);
        if (course.getUserId() != null) {
            schoolUserService.validateTeacher(course.getUserId());
        }

        Course courseTemp = courseCacheService.getCourseById(course.getCourseId());
        if (courseTemp == null) {
            throw new BusinessException(ReturnStatus.PARAMETER_INCORRECT);
        }

        courseCacheService.updateCourse(course);
    }

    @Transactional
    @Override
    public void deleteCourse(Long userId, Long courseId) throws Exception {
        schoolUserService.validateSchoolManager(userId);
        courseCacheService.deleteCourse(courseId);
    }

    @Override
    public List<Map<String, Object>> getCourseListWithMemberCount(Long userId, Long schoolId, Long semesterId, String value) throws Exception {
        return courseDao.queryCourseListWithMemberCount(schoolId, semesterId, userId, null, value);
    }

    @Override
    public Map getUnselectCourseStudent(Long schoolId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "待定");
        map.put("courseId", 0);
        map.put("username", null);
        map.put("serial", null);
        map.put("logoURL", null);
        map.put("userId", null);
        map.put("count", userService.getUnselectCourseStudentCount(schoolId));
        return map;
    }

    @Override
    public List<Map<String, Object>> getAllCourseNameAndIdArray(Long userId, Long semesterId, String courseName) throws Exception {
        User user = userCacheService.getUserById(userId);
        if(null != user && user.getUserType() > UserTypeEnum.STUDENT.getValue()) {
            if(user.getUserType() == UserTypeEnum.TEACHER.getValue()) {
                //当前老师没有课程的情况
                return courseDao.queryCourseNameAndId(semesterId, userId, courseName);
            } else {
                List<Map<String, Object>> list =  courseDao.queryCourseNameAndId(semesterId, null, courseName);
                Map<String, Object> map = new HashMap<>();//管理员需要显示待定
                map.put("name", "待定");
                map.put("courseId", 0);
                map.put("key", 0);
                list.add(0, map);
                return list;
            }
        } else {
            throw new BusinessException(ReturnStatus.USER_FAULT);//错误的用户信息
        }
    }

    @Override
    public List<Map<String, Object>> getCourseNameAndId(Long userId) throws Exception {
        UserSchool userSchool = schoolService.getParaByUserId(userId, Calendar.getInstance().getTime());
        if (userSchool == null || userSchool.getSemesterId() == null) {
            throw new BusinessException(ReturnStatus.USERSCHOOL_PARAMETER_MISS);
        }
        return courseDao.queryCourseNameAndId(userSchool.getSemesterId(), userId, null);
    }

    @Override
    public List<Map<String, Object>> getAllCourseNameAndId(Long userId) throws Exception {
        UserSchool userSchool = schoolService.getParaByUserId(userId, Calendar.getInstance().getTime());
        if (userSchool == null || userSchool.getSemesterId() == null) {
            throw new BusinessException(ReturnStatus.USERSCHOOL_PARAMETER_MISS);
        }
        return courseDao.queryCourseNameAndId(userSchool.getSemesterId(), null, null);
    }

    @Override
    public Map<String, Object> getCourseScheduleListBycourseIdForWeb(Long userId, Long courseId, int pageindex, int pagesize) throws Exception {
        User teacher = userService.validateUser(userId);
        if (teacher.getUserType() == UserTypeEnum.SCHOOLMANAGER.getValue()) {
            userId = null;
        }
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("courseId", courseId);
        paramsMap.put("userId", userId);
        Long allcount = courseDao.queryCourseScheduleListBycourseIdForWebCount(paramsMap);
        if (allcount == 0) {
            return MyPage.processPage(0L, pagesize, pageindex, new ArrayList());
        } else {
            paramsMap.put("from", (pageindex-1) * pagesize);
            paramsMap.put("num", pagesize);
            List<Map<String, Object>> list = courseDao.queryCourseScheduleListBycourseIdForWeb(paramsMap);
            list.forEach(i -> calCourseScheduleStatusForWeb(i));
            return MyPage.processPage(allcount, pagesize, pageindex, list);
        }
    }

    private void calCourseScheduleStatus(Map<String, Object> map){
        String date = map.get("date").toString();
        String time = map.get("time").toString();
        String[] times = time.split("~");
        map.put("time", times[0]);

        Date startTime = DateUtil.parseDate(date + " " + times[0]+":00", DateUtil.PATTERN_DATE_AND_TIME);
        Date endTime = DateUtil.parseDate(date + " " + times[1]+":00", DateUtil.PATTERN_DATE_AND_TIME);
        if (endTime.getTime() < Calendar.getInstance().getTimeInMillis()) {
            map.put("status",CourseStatus.OVER.getValue() );
        }else if(startTime.getTime() < Calendar.getInstance().getTimeInMillis()){
            map.put("status",CourseStatus.ONGONGING.getValue() );
        }else {
            map.put("status",CourseStatus.BEFORESTART.getValue() );
        }
        String weekday = DateUtil.getDayOfWeek(startTime);
        map.put("weekday", weekday);
    }

    private void calCourseScheduleStatusForWeb(Map<String, Object> map){
        String date = map.get("date").toString();
        String time = map.get("time").toString();
        String[] times = time.split("~");
        Date startTime = DateUtil.parseDate(date + " " + times[0]+":00", DateUtil.PATTERN_DATE_AND_TIME);
        Date endTime = DateUtil.parseDate(date + " " + times[1]+":00", DateUtil.PATTERN_DATE_AND_TIME);
        if (endTime.getTime() < Calendar.getInstance().getTimeInMillis()) {
            map.put("status",CourseStatus.OVER.getValue() );
        }else if(startTime.getTime() < Calendar.getInstance().getTimeInMillis()){
            map.put("status",CourseStatus.ONGONGING.getValue() );
        }else {
            map.put("status",CourseStatus.BEFORESTART.getValue() );
        }
        String weekday = DateUtil.getDayOfWeek(startTime);
        map.put("weekday", weekday);
    }


    private int calCourseScheduleStatus(CourseSchedule courseSchedule){
        Date[] time = getStartEndTime(courseSchedule);
        if (time[1].getTime() < Calendar.getInstance().getTimeInMillis()) {
            return CourseStatus.OVER.getValue();
        }else if(time[0].getTime() < Calendar.getInstance().getTimeInMillis()){
            return CourseStatus.ONGONGING.getValue();
        }else {
            return CourseStatus.BEFORESTART.getValue();
        }
    }

    private Date[] getStartEndTime(CourseSchedule courseSchedule) {
        String date = courseSchedule.getDate();
        String time = courseSchedule.getTime();
        String[] times = time.split("~");
        Date startTime = DateUtil.parseDate(date + " " + times[0]+":00", DateUtil.PATTERN_DATE_AND_TIME);
        Date endTime = DateUtil.parseDate(date + " " + times[1]+":00", DateUtil.PATTERN_DATE_AND_TIME);
        return new Date[]{startTime, endTime};
    }

    @Override
    public Map<String, Object> getCourseScheduleBasicInfo(Long userId, Long courseScheduleId, Long semesterId) throws Exception {
        UserSchool userSchool = schoolService.getParaByUserId(userId, Calendar.getInstance().getTime());
        if (userSchool == null || userSchool.getSchoolId() == null || userSchool.getSemesterId() == null) {
            throw new BusinessException(ReturnStatus.USERSCHOOL_PARAMETER_MISS);
        }
        semesterId = semesterId == null ? userSchool.getSemesterId() : semesterId;
        Semester semester = schoolService.getSemesterById(semesterId);
        if (courseScheduleId > 0) {
            Map<String, Object> map = courseDao.getCourseScheduleBasicInfo(courseScheduleId);
            map.put("SemesterYear", semester.getSemesterYear());
            map.put("semesterType", semester.getSemesterType());
            calCourseScheduleStatus(map);
            return map;
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("SemesterYear", semester.getSemesterYear());
            map.put("semesterType", semester.getSemesterType());
            map.put("name", "待定");
            map.put("courseId", 0);
            map.put("username", "");
            map.put("count", userService.getUnselectCourseStudentCount(userSchool.getSchoolId()));
            return map;
        }
    }

    @Override
    public Map<String, Object> getStudentCourseList(Long userId, Long studentId, int pageindex, int pagesize) throws Exception {
        UserSchool userSchool = schoolService.getParaByUserId(studentId, Calendar.getInstance().getTime());
        if (userSchool == null || userSchool.getSemesterId() == null) {
            throw new BusinessException(ReturnStatus.USERSCHOOL_PARAMETER_MISS);
        }

        List<Map<String, Object>> list = courseDao.queryStudentCourseList(studentId, userSchool.getSemesterId(), (pageindex-1)*pagesize, pagesize);
        Long allcount = courseDao.queryStudentCourseListCount(studentId, userSchool.getSemesterId());

//        ExamItem examItem = examItemService.getEvaluationStardard(userSchool.getSchoolId(), userSchool.getSemesterId());

        list.forEach(i -> i.put("score", 1f * (int)i.get("duration"))); //  getPerAttendanceScore规则，没上一课时给x分。。duration此节课几个课时
        return MyPage.processPage(allcount, pagesize, pageindex, list);
    }

    @Override
    public MyPageInfo getCourseStatisticByStudent(Map<String, Object> paramsMap, int pageindex, int pagesize) throws Exception {
        PageHelper.startPage(pageindex, pagesize);
        List<Map<String, Object>> list = courseStatisticDao.getCourseStatisticByStudent(paramsMap);
        list.forEach(map -> {
            //签到状态
            if(null != map.get("signTime")) {
                map.put("status", CourseSignStatusEnum.ok.description);//已签到
            } else {
                map.put("signTime", "");
                CourseSchedule courseSchedule = null;
                try {
                    courseSchedule = validateCourseSchedule(Long.parseLong(map.get("key").toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new BusinessException(ReturnStatus.COURSE_SCHEDULE_NOT_EXIST);
                }
                //两种状态：上课时间已过(缺勤)；还没有开始
                int courseStatus =  calCourseScheduleStatus(courseSchedule);
                if(courseStatus == CourseStatus.BEFORESTART.getValue()) {
                    map.put("status", CourseSignStatusEnum.not_begin.description);//还没有开始
                } else {
                    map.put("status", CourseSignStatusEnum.missing.description);//缺勤
                }
            }
        });


        PageInfo<Map<String, Object>> p = new PageInfo<Map<String, Object>>(list);
        MyPageInfo pageInfo = new MyPageInfo(p);
        return pageInfo;
    }

    @Transactional
    @Override
    public void deleteCourseSchedule(Long userId, Long courseScheduleId) throws Exception {
        validateTeacherAndCourseSchedule(userId, courseScheduleId);
        Map<String, Object> map = courseDao.queryEnrollCountAndSignCount(courseScheduleId);
        if (map != null && (int) map.get("signCount") > 0) {
            throw new BusinessException(ReturnStatus.COURSE_SCHEDULE_HAVE_MEMBE);
        }
        courseDao.deleteCourseSchedule(courseScheduleId);
    }

    @Transactional
    @Override
    public Map<String, Object> sign(Long userId, Long courseScheduleId, Long studentId, int type, Long courseMemberId) throws Exception {
        Map<String, Object> map = validateTeacherAndCourseSchedule(userId, courseScheduleId);
        CourseSchedule schedule = (CourseSchedule) map.get("schedule");
        Course course = courseCacheService.getCourseById(schedule.getCourseId());
        int attendanceCount = 0;
        if (type == 0) {
            attendanceCount = -1;
            courseDao.deleteCourseMember(courseMemberId);
            synchronized (courseScheduleId){
                courseDao.updateCourseScheduleSomeCount(courseScheduleId, -1);
            }
        } else {
            attendanceCount = 1;
            Integer count = courseDao.checkHaveSign(courseScheduleId, studentId);
            if (count > 0) {
                throw new BusinessException(ReturnStatus.COURSE_HAVE_SIGN);
            }
            CourseMember courseMember = new CourseMember(courseScheduleId, course.getCourseId(), studentId, null, null, CourseSignTypeEnum.CALL.ordinal());
            courseDao.insertCourseMember(courseMember);
            courseMemberId = courseMember.getCourseMemberId();

            synchronized (courseScheduleId){
                courseDao.updateCourseScheduleSomeCount(courseScheduleId, 1);
            }
        }

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("userId", userId);
        paramsMap.put("schoolId", course.getSchoolId());
        paramsMap.put("semesterId", course.getSemesterId());
        paramsMap.put("attendanceCount", attendanceCount);//签到成功，增加一次
        userStatisticService.insertOrUpdateStatisticByCourseSign(paramsMap);

        Map<String, Object> ret = new HashMap<>();
        User student = userCacheService.getUserById(studentId);
        ret.put("key", student.getUserId());
        ret.put("userId", studentId);
        ret.put("studentNO", student.getStudentNO());
        ret.put("username", student.getUsername());
        ret.put("mobile", student.getMobile());
        ret.put("signType", type);
        ret.put("signTime", DateUtils.getDateStringDefault(Calendar.getInstance().getTime()));
        ret.put("courseMemberId", type == 0 ? null : courseMemberId);
        return ret;

    }

    @Transactional
    @Override
    public void createCourseSchedule(Long userId, int startWeekIndex, int endWeekIndex, Long courseId, JSONArray array) throws Exception {
        //validateTeacherAndCourse(userId, courseId);

        UserSchool userSchool = schoolService.getParaByUserId(userId, Calendar.getInstance().getTime());
        Semester semester = schoolService.getSemesterById(userSchool.getSemesterId());

        for (int i = 0; i < array.size(); i++) {
            JSONObject json = array.getJSONObject(i);
            CourseScheduleDesc courseScheduleDesc = new CourseScheduleDesc(courseId, json.getInt("weekday"), json.getInt("courseIndex"),
                    json.getInt("duration"), json.getString("time"), startWeekIndex, endWeekIndex);
            courseScheduleDescDao.insertCourseScheduleDesc(courseScheduleDesc);
            long descId = courseScheduleDesc.getCourseScheduleDescId();//保存后的主键ID
            json.put("descId", descId);
        }

        for (int i = 0; i < endWeekIndex - startWeekIndex +1; i++) {
            for (int j = 0; j < array.size(); j++) {
                Date date = getDateString(array.getJSONObject(j).getInt("weekday"), startWeekIndex + i -1 , semester.getStartTime());
                String time = array.getJSONObject(j).getString("time"); // TODO:METEOR.WU  优化
                if (!time.contains("~")) {   //课程上课时间格式：10:00~12:00
                    throw new BusinessException(ReturnStatus.PARAMETER_INCORRECT);
                }
                long descId = array.getJSONObject(j).getLong("descId");//子表关联主表记录ID
                CourseSchedule courseSchedule = new CourseSchedule(null, courseId, startWeekIndex + i,
                        DateUtil.dateToStr(date, DateUtil.PATTERN_DATE), time, array.getJSONObject(j).getInt("duration"),
                        array.getJSONObject(j).getInt("courseIndex"));

                courseSchedule.setDescId(descId);
                courseDao.insertCourseSchedule(courseSchedule);
            }
        }
    }

    private Date getDateString(int weekday, int weekindex, Date startDate) {
        Calendar date = Calendar.getInstance();
        date.setTime(startDate);
        date.add(Calendar.WEEK_OF_YEAR, weekindex);
        date.set(Calendar.DAY_OF_WEEK, weekday);
        return date.getTime();
    }

    @Transactional
    @Override
    public void updateCourseSchedule(Long userId, JSONObject json) throws Exception {
        Long courseScheduleId = json.getLong("courseScheduleId");
        Map<String, Object> map = validateTeacherAndCourseSchedule(userId, courseScheduleId);
        CourseSchedule schedule = (CourseSchedule) map.get("schedule");
        int status = calCourseScheduleStatus(schedule);
        if (status == CourseStatus.OVER.getValue()) {
            throw new BusinessException(ReturnStatus.COURSE_OVER);
        }

        String date = json.containsKey("weekday") ? getDateString(schedule, Integer.parseInt(json.get("weekday").toString())) : null;
        CourseSchedule courseSchedule = new CourseSchedule(json.getLong("courseScheduleId"), null,  null, date, json.getString("time"),
                json.getInt("duration"), json.getInt("courseIndex"));

        courseDao.updateCourseSchedule(courseSchedule);
    }

    private String getDateString(CourseSchedule schedule, int weekday) {
        String date = schedule.getDate();
        String time =  schedule.getTime();
        String[] times = time.split("~");
        Date startTime = DateUtil.parseDate(date + " " + times[0]+":00", DateUtil.PATTERN_DATE_AND_TIME);
        Calendar old = Calendar.getInstance();
        old.setTime(startTime);
        if (old.get(Calendar.DAY_OF_WEEK) == weekday) {
            return schedule.getDate();
        }else {
            old.set(Calendar.DAY_OF_WEEK, weekday);
            return DateUtil.dateToStr(old.getTime(), DateUtil.PATTERN_DATE);
        }
    }


    @Override
    public Map<String, Object> getEnrollCountAndSignCount(Long userId, Long courseScheduleId) throws Exception {
        validateTeacherAndCourseSchedule(userId, courseScheduleId);
        Map<String, Object> map = courseDao.queryEnrollCountAndSignCount(courseScheduleId);
        if (map == null) {
            map = new HashMap<>();
            map.put("enrollCount", 0);
            map.put("signCount", 0);
        }
        return map;
    }

    @Override
    public Map<String, Object> getCourseScheduleWithTeacherName(Long userId, Long courseScheduleId) throws Exception {
        validateTeacherAndCourseSchedule(userId, courseScheduleId); // TODO:METEOR.WU下面又有一次查询，所以可以优化一下
        Map<String, Object> map = courseDao.queryCourseScheduleWithTeacherName(courseScheduleId);
        if (map == null) {
            throw new BusinessException(ReturnStatus.COURSE_SCHEDULE_NOT_EXIST);
        }
        calCourseScheduleStatus(map);
        return map;
    }

//    private Map<String, Object> validateTeacherAndStudent(Long teacherId, Long studentId) throws Exception{
//        User teacher = schoolUserService.validateTeacher(teacherId);
//        User student = userService.validateUser(studentId);
//        if (student == null) {
//            throw new BusinessException(ReturnStatus.ACTIVITY_NOT_EXIST);
//        }
//        if (teacher.getSchoolId().longValue() != student.getSchoolId()) {
//            throw new BusinessException(ReturnStatus.NO_PERMISSION);
//        }
//        Map<String, Object> map = new HashMap<>();
//        map.put("teacher", teacher);
//        map.put("student", student);
//        return map;
//    }

    private Map<String, Object> validateStudentAndCourse(Long studentId, Long courseId) throws Exception{
        User student = userService.validateUser(studentId);
        Course course = validateCourse(courseId);
        if (course.getSchoolId().longValue() != student.getSchoolId()
                || student.getCourseId() == null
                || student.getCourseId() != course.getCourseId().longValue()) {
            throw new BusinessException(ReturnStatus.COURSE_NOT_EXIST);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("course", course);
        map.put("student", student);
        return map;
    }

    private Map<String, Object> validateStudentAndCourseSchedule(Long studentId, Long courseScheduleId) throws Exception{
        User student = userService.validateUser(studentId);

        CourseSchedule schedule = validateCourseSchedule(courseScheduleId);
        Course course = validateCourse(schedule.getCourseId());
        if (course.getSchoolId().longValue() != student.getSchoolId()
                || student.getCourseId() == null
                || student.getCourseId() != course.getCourseId().longValue()){
            throw new BusinessException(ReturnStatus.NO_PERMISSION);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("schedule", schedule);
        map.put("student", student);
        return map;
    }

    @Override
    public Map<String, Object> validateTeacherAndCourseSchedule(Long teacherId, Long courseScheduleId) throws Exception {
        User teacher = schoolUserService.validateTeacher(teacherId);

        CourseSchedule schedule = validateCourseSchedule(courseScheduleId);
        Course course = validateCourse(schedule.getCourseId());
        if (course.getSchoolId().longValue() != teacher.getSchoolId()){
            throw new BusinessException(ReturnStatus.NO_PERMISSION);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("schedule", schedule);
        map.put("teacher", teacher);
        return map;
    }

    private Map<String, Object> validateTeacherAndCourse(Long teacherId, Long courseId) throws Exception {
        User teacher = schoolUserService.validateTeacher(teacherId);

        Course course = validateCourse(courseId);
        if (course.getSchoolId().longValue() != teacher.getSchoolId()
                || course.getUserId().longValue() != teacherId){
            throw new BusinessException(ReturnStatus.NO_PERMISSION);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("course", course);
        map.put("teacher", teacher);
        return map;
    }

    @Override
    public CourseSchedule validateCourseSchedule(Long courseScheduleId) throws Exception {
        CourseSchedule courseSchedule = courseDao.queryCourseScheduleById(courseScheduleId);
        if (courseSchedule == null) {
            throw new BusinessException(ReturnStatus.COURSE_SCHEDULE_NOT_EXIST);
        }
        return courseSchedule;
    }

    @Override
    public Integer countCourseByUserId(Long userId) throws Exception {
        return courseDao.countCourseByUserId(userId);
    }

    @Override
    public List<String> getCourseNameByIds(String ids) throws Exception {
        return courseDao.queryCourseNameByIds(ids);
    }

    @Override
    public List<Map<String, Object>> getCourseListBySchoolId(Long userId, Long schoolId) throws Exception {
        schoolUserService.validateOfficialManager(userId);
        Semester semester = schoolCacheService.getSemesterBySchoolId(schoolId);
        return getCourseListWithMemberCount(null, schoolId, semester.getSemesterId(), null);
    }


    @Override
    public int loadStudentCourseExcel(Long userid, Workbook wb) throws Exception {
        Map<String, Long> courseMap = new HashMap<>();
        schoolUserService.validateSchoolManager(userid);
        Sheet sheet = wb.getSheetAt(0);

        int count = 0;
        String courseName;
        Long courseId;
        for(int i = 1,len = sheet.getLastRowNum(); i<=len; i++) {
            Row row = sheet.getRow(i);
            courseName = getValue(row.getCell(1));
            if (courseMap.containsKey(courseName)) {
                courseId = courseMap.get(courseName);
            }else {
                UserSchool userSchool = schoolService.getParaByUserId(userid, Calendar.getInstance().getTime());
                List<Map<String, Object>> list =  courseDao.queryCourseNameAndId(userSchool.getSemesterId(), null, courseName);
                if (list.size() == 0) {
                    continue;
                }
                courseId = Long.valueOf(list.get(0).get("courseId").toString());
                courseMap.put(courseName, courseId);
            }
            User user = new User();
            user.setStudentNO(getValue(row.getCell(0)));
            user = userService.getUserByUser(user);
            if (user == null) {
                continue;
            }
            user.setCourseId(courseId);
            userCacheService.updateUser(user);
            count++;
        }

        return count;
    }

    private String getValue(Cell cell) {

        if (cell.getCellType() == cell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
            if (cell.getCellStyle().getDataFormat() == 57 || cell.getCellStyle().getDataFormat() == 31|| cell.getCellStyle().getDataFormat() == 14) {// 处理日期格式、时间格式
                // xxx年xxx月  2016-01-01  xxxx年xx月xx日
                Date date = cell.getDateCellValue();
                return sdf.format(date);
            } else if (cell.getCellStyle().getDataFormat() == 58) {
                // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                double value = cell.getNumericCellValue();
                Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
                return sdf.format(date);
            } else {
                double value = cell.getNumericCellValue();
                CellStyle style = cell.getCellStyle();
                DecimalFormat format = new DecimalFormat();
                String temp = style.getDataFormatString();
                // 单元格设置成常规
                if (temp.equals("General")) {
                    format.applyPattern("#");
                }
                return format.format(value);
            }
        } else {
            return String.valueOf(cell.getStringCellValue());
        }
    }

    @Override
    public Map<String, Object> getCourseScheduleDetail(Long userId, Long courseScheduleId) throws Exception {
        CourseSchedule courseSchedule = validateCourseSchedule(courseScheduleId);
        Map<String, Object> ret = new HashMap<>();
        ret.put("date", courseSchedule.getDate());
        ret.put("time", courseSchedule.getTime());

        Course course = courseCacheService.getCourseById(courseSchedule.getCourseId());
        if (course == null) {
            throw new BusinessException(ReturnStatus.PARAMETER_INCORRECT);
        }
        ret.put("name", course.getName());//课程名称
        ret.put("needSignLocation", course.getNeedSignLocation());
        if (course.getNeedSignLocation() == 1) {
            ret.put("latitude", course.getLatitude());
            ret.put("longitude", course.getLongitude());
        }
        ret.put("signIntevalTime", course.getSignIntevalTime());

        User user = userCacheService.getUserById(course.getUserId());
        if (user == null) {
            throw new BusinessException(ReturnStatus.PARAMETER_INCORRECT);
        }
        ret.put("username", user.getUsername());// 授课老师名称
        calCourseScheduleStatus(ret);
        ret.put("time", courseSchedule.getTime());
        Date now = new Date();
        Integer count = courseDao.checkHaveSign(courseScheduleId, userId);
        //sign=0缺勤  =1已签到 =2未开始
        if(count > 0) {
            ret.put("sign",1);//1=已签到
        } else {
            Date[] time = getStartEndTime(courseSchedule);
            if (time[0].getTime() + course.getSignIntevalTime() * 60 * 1000 > now.getTime()){
                ret.put("sign", 2);
            } else {
                ret.put("sign", 0);
            }
        }
        String[] timeString = courseSchedule.getTime().split("~");
        Date courseTime = DateUtil.parseDate(courseSchedule.getDate() + " " + timeString[0] + ":00", "yyyy-MM-dd HH:mm:ss");
        ret.put("courseTimeLong", courseTime.getTime());//课程时间格式化long
        ret.put("currTimeLong", now.getTime());//服务器当前时间格式化long

        return ret;
    }

    @Override
    public void signSetting(Long userId, Long courseId, int needSignLocation, Double longitude, Double latitude, int signIntevalTime) throws Exception {
        Course course = courseCacheService.getCourseById(courseId);
        if (course == null) {
            throw new BusinessException(ReturnStatus.PARAMETER_INCORRECT);
        }
        course.setNeedSignLocation(needSignLocation);
        course.setSignIntevalTime(signIntevalTime);
        if (needSignLocation == WebConstants.Boolean.TRUE.ordinal()) {
            course.setLatitude(latitude);
            course.setLongitude(longitude);
        }
        courseCacheService.updateCourse(course);
    }

    @Override
    public Map<String, Object> getCourseSchedule(Long userId, Long courseId) throws Exception {
        List<CourseScheduleDesc> courseScheduleDescList = courseScheduleDescDao.getCourseScheduleDescList(courseId);
        return MyPage.processPage((long)courseScheduleDescList.size(), 20, 1, courseScheduleDescList);
    }

    @Override
    public Map<String, Object> getCourseScheduleById(Long courseScheduleId) throws Exception {

        CourseSchedule courseSchedule = courseDao.queryCourseScheduleById(courseScheduleId);
        Map<String, Object> map = new HashMap<>();
        map.put("courseScheduleId", courseSchedule.getCourseScheduleId());
        map.put("weekIndex", courseSchedule.getWeekIndex());
        map.put("date", courseSchedule.getDate());
        map.put("time", courseSchedule.getTime());
        map.put("duration", courseSchedule.getDuration());
        map.put("courseIndex", courseSchedule.getCourseIndex());
        calCourseScheduleStatus(map);
        map.put("time", courseSchedule.getTime());//上个函数调用修改了time，此处time需要结束和开始
        return  map;
    }

    @Override
    @Transactional
    public void deleteCoursescheduleDescById(Long descId) throws Exception {
        courseDao.deleteCourseScheduleByDescId(descId);
        courseScheduleDescDao.deleteCoursescheduleDescById(descId);
    }


    @Override
    public Map<String, Object> getCourseScheduleInfo(Long courseScheduleId) throws Exception {
        CourseSchedule courseSchedule = validateCourseSchedule(courseScheduleId);
        Map<String, Object> ret = new HashMap<>();
        ret.put("date", courseSchedule.getDate());
        ret.put("time", courseSchedule.getTime());

        Course course = courseCacheService.getCourseById(courseSchedule.getCourseId());
        if (course == null) {
            throw new BusinessException(ReturnStatus.PARAMETER_INCORRECT);
        }
        ret.put("name", course.getName());//课程名称
        ret.put("needSignLocation", course.getNeedSignLocation());
        if (course.getNeedSignLocation() == 1) {
            ret.put("latitude", course.getLatitude());
            ret.put("longitude", course.getLongitude());
        }
        ret.put("signIntevalTime", course.getSignIntevalTime());

        User user = userCacheService.getUserById(course.getUserId());
        if (user == null) {
            throw new BusinessException(ReturnStatus.PARAMETER_INCORRECT);
        }
        ret.put("username", user.getUsername());// 授课老师名称
        calCourseScheduleStatus(ret);
        ret.put("time", courseSchedule.getTime());
        int signCount = 0;
        Map<String, Object> map = courseDao.queryEnrollCountAndSignCount(courseScheduleId);
        if (map != null && (int) map.get("signCount") > 0) {
            signCount = Integer.parseInt(map.get("signCount").toString());
        }
        ret.put("signCount", signCount);//签到人数
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("schoolId", user.getSchoolId());
        paramsMap.put("userType", UserTypeEnum.STUDENT.getValue());
        paramsMap.put("status", WebConstants.Boolean.TRUE.ordinal());
        paramsMap.put("courseId", course.getCourseId());

        int courseStudentAll = courseStatisticDao.getCourseStudentCount(paramsMap);
        ret.put("courseStudentAll", courseStudentAll);//应到总人数
        int lackCount = 0;
        if(courseStudentAll >= signCount) {
            lackCount = courseStudentAll - signCount;
        }
        ret.put("lackCount", lackCount);//缺勤人数
        ret.put("courseScheduleId", courseScheduleId);//课程表ID

//        String[] timeString = courseSchedule.getTime().split("~");
//        Date courseTime = DateUtil.parseDate(courseSchedule.getDate() + " " + timeString[0] + ":00", "yyyy-MM-dd HH:mm:ss");
//        ret.put("courseTimeLong", courseTime.getTime());//课程时间格式化long
//        ret.put("currTimeLong", now.getTime());//服务器当前时间格式化long
        return ret;
    }

    @Override
    public List<Map<String, String>> getCourseAndTeacher(Long schoolId, Long semesterId) throws Exception {
        return courseDao.getCourseAndTeacher(schoolId, semesterId);
    }
}
