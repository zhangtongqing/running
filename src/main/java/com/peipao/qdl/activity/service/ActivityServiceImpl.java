package com.peipao.qdl.activity.service;


import ch.qos.logback.classic.util.LoggerNameUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.constant.WebConstants;
import com.peipao.framework.exception.BusinessException;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.page.MyPage;
import com.peipao.framework.page.MyPageInfo;
import com.peipao.framework.util.DateUtil;
import com.peipao.framework.util.LatLonUtil;
import com.peipao.framework.util.StringUtil;
import com.peipao.qdl.activity.dao.ActivityDao;
import com.peipao.qdl.activity.dao.ActivityMemberDao;
import com.peipao.qdl.activity.dao.ActivityPartInRecordDao;
import com.peipao.qdl.activity.model.*;
import com.peipao.qdl.course.service.CourseService;
import com.peipao.qdl.luckdraw.service.ActivityLuckRecordService;
import com.peipao.qdl.running.model.Running;
import com.peipao.qdl.running.model.RunningLine;
import com.peipao.qdl.running.service.RunningCacheService;
import com.peipao.qdl.running.service.RunningLineCacheService;
import com.peipao.qdl.running.service.RunningService;
import com.peipao.qdl.school.model.UserSchool;
import com.peipao.qdl.school.service.SchoolService;
import com.peipao.qdl.schooluser.service.SchoolUserService;
import com.peipao.qdl.statistics.service.UserStatisticService;
import com.peipao.qdl.user.model.User;
import com.peipao.qdl.user.model.UserTypeEnum;
import com.peipao.qdl.user.service.UserCacheService;
import com.peipao.qdl.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author meteor.wu
 * @since 2017/6/29
 **/
@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private UserService userService;

    @Autowired
    private ActivityDao activityDao;

    @Autowired
    private ActivityMemberDao activityMemberDao;

    @Autowired
    private ActivityPartInRecordDao activityPartInRecordDao;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private SchoolUserService schoolUserService;

    @Autowired
    private RunningService runningService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserStatisticService userStatisticService;

    @Autowired
    private UserCacheService userCacheService;

    @Autowired
    private ActivityUtilService activityUtilService;

    @Autowired
    private ActivityLuckRecordService activityLuckRecordService;

    @Autowired
    private ActivityCacheService activityCacheService;

    @Autowired
    private RunningCacheService runningCacheService;

    @Autowired
    private RunningLineCacheService runningLineCacheService;

    @Override
    public List<Map<String, Object>> getMyActivityList(Long userId, Integer from, Integer num) throws Exception {
        List<Map<String, Object>> list = activityDao.queryActivityListByUserId(userId, from, num);
//        增加系数
//        Map<String, Object> quotietyParamsMap =  getQuotiety();
//        int baomingQuotiety = Integer.parseInt(quotietyParamsMap.get("baomingQuotiety").toString());
//        list.forEach(i -> baomingCount(i, baomingQuotiety));
        list.forEach(i -> activityUtilService.processActivityStatus(i));
        list.forEach(i -> removeUserlessParams(i));
        return list;
    }

    private void removeUserlessParams(Map<String, Object> map) {
        map.remove("startTime");
        map.remove("enrollStartTime");
        map.remove("enrollEndTime");
        map.remove("endTime");
    }

    private void baomingCount(Map<String, Object> map, int baomingQuotiety) {
        if(null == map.get("schoolId")) {
            int enrollcount = Integer.parseInt(map.get("enrollcount").toString());
            enrollcount = enrollcount * baomingQuotiety;
            map.put("enrollcount", enrollcount);
        }
    }

    private Map<String, Object> getQuotiety() throws Exception {
        return activityDao.getQuotiety();
    }

    @Override
    public List<Map<String, Object>> getActivityListBySchoolId(Long userId, Integer from, Integer num) throws Exception {
        UserSchool userSchool = schoolService.getParaByUserId(userId, Calendar.getInstance().getTime());
        if (userSchool == null || userSchool.getSchoolId() == null) {
            return null;
        }
        List<Map<String, Object>> list = activityDao.queryActivityListBySchoolId(userId, userSchool.getSchoolId(), from, num);
        list.forEach(i -> activityUtilService.processActivityStatus(i));
        list.forEach(i-> activityUtilService.processActivityEnrollStatus(i));
        list.forEach(i -> removeUserlessParams(i));
        return list;
    }

    @Override
    public List<Map<String, Object>> getOfficialActivityList(Long userId, Integer from, Integer num) throws Exception {
        List<Map<String, Object>> list = activityDao.queryActivityListByPublishType(userId, ActivityPublishTypeEnum.OFFICIAL.getValue(), from, num);
//        Map<String, Object> quotietyParamsMap =  getQuotiety();
//        int baomingQuotiety = Integer.parseInt(quotietyParamsMap.get("baomingQuotiety").toString());
//        list.forEach(i -> baomingCount(i, baomingQuotiety));
        list.forEach(i -> activityUtilService.processActivityStatus(i));//status
        list.forEach(i -> activityUtilService.processActivityEnrollStatus(i));//enrollStatus
        list.forEach(i -> removeUserlessParams(i));
        return list;
    }

    @Override
    public Activity getActivityDetailForApp(Long userId, Long activityId) throws Exception {
//        Map<String, Object> map = validateActivityAndStudent(userId, activityId);
        Activity activity = validateActivity(activityId);
        if (activity.getType() != null && activity.getType() == ActivityTypeEnum.RUNNING.ordinal()) {
            Running running = runningCacheService.getByActivityId(activityId);
            activity.setRunning(running);
        }

        activityUtilService.processActivityStatus(activity);
        activityUtilService.processActivityEnrollStatus(activity);

        /***************************************** 开始 配置人数等信息 **********************************************/
        if(null == activity.getSchoolId()) {//官方活动
            //calcQuotiety(activity);
            activity.setLuckCount(activityLuckRecordService.countLuckRecordForPC(activityId).intValue());
        }

        ActivityMember activityMember = activityMemberDao.queryActivityMemberById(activityId, userId);
        activity.setPartinStatus(activityMember == null ? ActivityPartinStatusEnum.UNENROLL.getValue() : ActivityPartinStatusEnum.ENROLLED.getValue());
        activity.setSucCount(activityMember == null || activityMember.getSucCount() == null ? 0 : activityMember.getSucCount());
        return activity;
    }

    @Transactional
    @Override
    public void enroll(Long userId, Long activityId) throws Exception {

        Map<String, Object> map = validateActivityAndStudent(userId, activityId);
        Activity activity = (Activity) map.get("activity");
        User user = (User) map.get("student");

        if (activity.getEnrollCount() >= activity.getMaxCapacity()) {
            throw new BusinessException(ReturnStatus.ACTIVITY_ENROLL_LIMIT);
        }

        if (activity.getEnrollStartTime().getTime() > Calendar.getInstance().getTimeInMillis()
                || activity.getEnrollEndTime().getTime() < Calendar.getInstance().getTimeInMillis()) {
            throw new BusinessException(ReturnStatus.ACTIVITY_ENROLL_AT_TIME);
        }

        ActivityMember activityMember = activityMemberDao.queryActivityMemberById(activityId, userId);
        if (activityMember != null) {//活动已经有人报名，不能删除
            throw new BusinessException(ReturnStatus.ACTIVITY_CAN_NOT_DELETE);
        }

        activityMemberDao.insertActivityMember(userId, activityId, Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
        synchronized (activityId) {
            activityCacheService.updateActivityEnrollAndSign(activityId, 1, 0, 0);
        }
    }

    @Transactional
    @Override
    public void sign(Long userId, Long activityId, Double longitude, Double latitude, String signAddress) throws Exception {
        Map<String, Object> map = validateActivityAndStudent(userId, activityId);
        Activity activity = (Activity)map.get("activity");

        if (activity.getStartTime().getTime() > Calendar.getInstance().getTimeInMillis()
                || activity.getEndTime().getTime() < Calendar.getInstance().getTimeInMillis()) {
            throw new BusinessException(ReturnStatus.ACTIVITY_SIGN_AT_TIME);
        }

        if (activity.getSign()) {
            double length = LatLonUtil.LantitudeLongitudeDist(longitude, latitude, activity.getLongitude(), activity.getLatitude());
            if (length > WebConstants.Activity.SIGN_DISTANCE_DIFF) {
                throw new BusinessException(ReturnStatus.ACTIVITY_SIGN_AT_ADDRESS);
            }
        }

        ActivityMember activityMember = activityMemberDao.queryActivityMemberById(activityId, userId);
        if (activityMember == null) {
            throw new BusinessException(ReturnStatus.ACTIVITY_NOT_ENROLL);
        }

        if (activityMember.getSucCount() + 1 > activity.getEffectiveSignCount()) {
            throw new BusinessException(ReturnStatus.ACTIVITY_SIGN_LIMIT);
        }

        ActivityPartinRecord activityPartinRecord = new ActivityPartinRecord(activityMember.getActivityMemberId(), Calendar.getInstance().getTime(), latitude, longitude, signAddress);
        if (activity.getType() != ActivityTypeEnum.RUNNING.ordinal()) {     // 非跑步活动，打卡就是有效，直接奖励学分或者课时
            activityPartinRecord.setEffective(88);
            activityPartinRecord.setRewardScore(activity.getRewardScore());
        }
        activityPartInRecordDao.insertActvityPartinRecord(activityPartinRecord);

        if (activity.getType() != ActivityTypeEnum.RUNNING.ordinal()) {// 非跑步活动，打卡就是有效
            ActivityMember activityMemberUpdate = new ActivityMember();
            activityMemberUpdate.setActivityMemberId(activityMember.getActivityMemberId());
            activityMemberUpdate.setSucCount(activityMember.getSucCount()+1);
            activityMemberUpdate.setSigncount(1);
            activityMemberDao.updateActivityMember(activityMemberUpdate);
        }

        if (activity.getType() != ActivityTypeEnum.RUNNING.ordinal() ) {// 计算活动参与人数
            int signcount = activityMember.getSigncount() != null && activityMember.getSigncount() == 0 ? 1 : 0;
            synchronized (activityId) {
                activityCacheService.updateActivityEnrollAndSign(activityId, 0, signcount, 1);
            }
        }

        // 专门和产品及测试确认过，确实是每参加一次活动，就给一次分数或置换课时，若活动设置可以无限参加，则无限次给分。。
        userStatisticService.setUserStatisticByActivityWithOutRunning(userId, activity);
    }

    @Transactional
    @Override
    public Map<String, Object> addActivity(Long userId, Activity activity) throws Exception {
        User teacher = schoolUserService.validateTeacher(userId);
        activity.setCreateTime(Calendar.getInstance().getTime());
        activity.setSchoolId(teacher.getSchoolId());
        setCourseId(activity);// 将courseId 转化为对应的course name
        if (activity.getEffectiveSignCount() == null || activity.getEffectiveSignCount() <= 0 ) {
            activity.setEffectiveSignCount(9999);//不限次数就是9999
        }
        activityCacheService.insertActivity(activity);

        if (activity.getType() != null && activity.getType() == ActivityTypeEnum.RUNNING.ordinal()) {
            activity.getRunning().setActivityId(activity.getActivityId());
            runningService.createRunning(activity.getRunning());
        }

        activity.setEnrollCount(0);
        return obj2Map(activity);
    }

    private void setCourseId(Activity activity) throws Exception {
        if (activity.getMemberType() == null) {
            return;
        }
        if (activity.getMemberType() == 3) {//指定课程
            List<String> names = courseService.getCourseNameByIds(activity.getCourseId());
            if (names.size() == 0) {
                throw new BusinessException(ReturnStatus.PARAMETER_INCORRECT);
            }
            StringJoiner joiner = new StringJoiner(",");
            names.forEach(i -> joiner.add(i));
            activity.setMemberStr(joiner.toString());
        } else {
            activity.setCourseId(null);
            activity.setMemberStr("本校");
        }
    }

    @Transactional
    @Override
    public Map<String, Object>  updateActivity(Long userId, Activity activity) throws Exception {
        Map<String, Object> map1 = validateTeacherAndActivity(userId, activity.getActivityId());
        User teacher = (User) map1.get("teacher");
        if (teacher.getUserType() != UserTypeEnum.SCHOOLMANAGER.getValue() && teacher.getUserId().longValue() != activity.getUserId()) {
            throw new BusinessException(ReturnStatus.NO_PERMISSION);
        }
        Activity activityTemp = (Activity)map1.get("activity");

        if (activity.getMemberType() != null && activity.getMemberType() != activityTemp.getMemberType().intValue()) {// 参与对象发生变化
            if (activity.getMemberType() == ActivityMemberTypeEnum.LOCALSCHOOL.ordinal()) {
                activity.setMemberStr("本校");
                activity.setCourseId(null);
            }else if (StringUtil.isNotEmpty(activity.getCourseId()) && !activity.getCourseId().equals(activityTemp.getCourseId())) {// 参与对象发生变化
                setCourseId(activity);
            }
        }

        if (activity.getEffectiveSignCount() == null || activity.getEffectiveSignCount() <= 0) {
            activity.setEffectiveSignCount(9999);//9999
        }
        activityCacheService.updateActivity(activity);

        if (activity.getType() != null && activity.getType() == ActivityTypeEnum.NON_RUNNING.ordinal()) {// 普通活动没有跑步信息,无论有没有，都删除
            runningService.deleteRunningByActivityId(activity.getActivityId());
        }

        activity.setEnrollCount(activityTemp.getEnrollCount());
        updateRunning(activity);
        return obj2Map(activity);
    }

    private void updateRunning(Activity activity) throws Exception {
        if (activity.getStatus() == ActivityStatusEnum.DRAFT.getValue() && activity.getRunning() != null
                && (activity.getType() != null && activity.getType() == ActivityTypeEnum.RUNNING.ordinal() ) ) {
            activity.getRunning().setActivityId(activity.getActivityId());
            runningService.updateRunning(activity.getRunning());
        } else {
            if (activity.getType() != null && activity.getType() == ActivityTypeEnum.RUNNING.ordinal()
                    && (activity.getEnrollEndTime().getTime() > Calendar.getInstance().getTimeInMillis() &&  activity.getEnrollCount() == 0)  ) {// 报名结束前，报名人数为0 ，才能修改跑步规则
                if (activity.getRunning() != null) {
                    activity.getRunning().setActivityId(activity.getActivityId());
                    runningService.updateRunning(activity.getRunning());
                }
            }
        }
    }

    private Map<String, Object> obj2Map(Activity activity) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("key", activity.getActivityId());
        map.put("activityId", activity.getActivityId());
        map.put("name", activity.getName());
        map.put("type", activity.getType());
        map.put("username", userCacheService.getUserById(activity.getUserId()).getUsername());
        map.put("enrollcount", activity.getEnrollCount());
        map.put("memberStr", activity.getMemberStr());
        if (activity.getStartTime() != null) {
            map.put("startTime", DateUtil.dateToStr(activity.getStartTime(), DateUtil.PATTERN_DATE_AND_TIME));
        } else {
            map.put("startTime", "");
        }
        if (activity.getEndTime() != null) {
            map.put("endTime", DateUtil.dateToStr(activity.getEndTime(), DateUtil.PATTERN_DATE_AND_TIME));
        } else {
            map.put("endTime", "");
        }
        if (activity.getEnrollStartTime() != null) {
            map.put("enrollStartTime", DateUtil.dateToStr(activity.getEnrollStartTime(), DateUtil.PATTERN_DATE_AND_TIME));
        } else {
            map.put("enrollStartTime", "");
        }
        if (activity.getEnrollEndTime() != null) {
            map.put("enrollEndTime", DateUtil.dateToStr(activity.getEnrollEndTime(), DateUtil.PATTERN_DATE_AND_TIME));
        } else {
            map.put("enrollEndTime", "");
        }
        map.put("status", activity.getStatus());
        activityUtilService.processActivityStatus(map);
        return map;
    }

    @Transactional
    @Override
    public void deleteActivity(Long userId, Long activityId) throws Exception {
        Map<String, Object> map1 = validateTeacherAndActivity(userId, activityId);
        User teacher = (User) map1.get("teacher");
        Activity activity = (Activity) map1.get("activity");
        if (teacher.getUserType() != UserTypeEnum.SCHOOLMANAGER.getValue() && teacher.getUserId().longValue() != activity.getUserId()) {
            throw new BusinessException(ReturnStatus.NO_PERMISSION);
        }
        Long count = activityMemberDao.queryActivityMemberListForWebCount(activityId, null);
        if (count > 0) {
            throw new BusinessException(ReturnStatus.ACTIVITY_CAN_NOT_DELETE);
        }
        activityCacheService.deleteActivity(activityId);
        runningService.deleteRunningByActivityId(activityId);
    }

    @Transactional
    @Override
    public void officiaDeletelActivity(Long userId, Long activityId) throws Exception {
        schoolUserService.validateOfficialManager(userId);

        Long count = activityMemberDao.queryActivityMemberListForWebCount(activityId, null);
        if (count > 0) {
            throw new BusinessException(ReturnStatus.ACTIVITY_CAN_NOT_DELETE);
        }

        activityCacheService.deleteActivity(activityId);
        runningService.deleteRunningByActivityId(activityId);
    }


    @Override
    public Map<String, Object> getStudentFinishActivityList(Long userId, Long studentId, int pageindex, int pagesize) throws Exception {
        validateTeacherAndStudent(userId, studentId);
        List<Map<String, Object>> list = activityDao.queryEffectiveActivityListByUserId(studentId, (pageindex-1)*pagesize, pagesize);

        list.forEach(i->i.put("reward","" + Integer.parseInt(i.get("rewardScore").toString()) + "学分"));
        list.forEach(i->i.remove("rewardDuration"));
        list.forEach(i -> i.remove("rewardScore"));

        Long allcount = activityDao.queryEffectiveActivityListByUserIdCount(studentId);
        return MyPage.processPage(allcount, pagesize, pageindex, list);
    }

    @Override
    public Map<String, Object> getStudentNonFinishActivityList(Long userId, Long studentId, int pageindex, int pagesize) throws Exception {
        validateTeacherAndStudent(userId, studentId);
        List<Map<String, Object>> list = activityDao.queryNonEffectiveActivityListByUserId(studentId, (pageindex-1)*pagesize, pagesize);
        Long allcount = activityDao.queryNonEffectiveActivityListByUserIdCount(studentId);
        return MyPage.processPage(allcount, pagesize, pageindex, list);
    }

    @Override
    public Map<String, Object> getActivityMemberList(Long activityId, Long userId) throws Exception {
        Activity activity = validateActivity(activityId);
        List<Map<String, Object>> list = activityMemberDao.getActivityMemberList(activityId);
        list.forEach(i->i.putIfAbsent("runningLength", 0));

        int index = isContain(userId, list);
        if (index == -1) {
            Map<String, Object> map = activityMemberDao.getMyRankInActivityMemberList(activityId, userId);
            if (map != null) {
                list.add(0, map);
            }
        }else{
            list.add(0, list.get(index));
        }

       // calcQuotiety(activity);

        Map<String, Object> ret = new HashMap<>();
        ret.put("enrollCount", activity.getEnrollCount());
        ret.put("signCount", activity.getSignCount());
        ret.put("allSucCount", activity.getAllSucCount());
        ret.put("list", list);
        return ret;
    }

    private int isContain(Long userId, List<Map<String, Object>> list){
        for (int i = 0; i < list.size(); i++) {
            Map map = list.get(i);
            if (Long.parseLong(map.get("userId").toString()) == userId){
                return i;
            }
        }

        return -1;
    }

    @Override
    public Map<String, Object> getActivityList(Long userId, Integer type, Integer status, String name, int pageindex, int pagesize) throws Exception {
        UserSchool userSchool = schoolService.getParaByUserId(userId, Calendar.getInstance().getTime());
        if (userSchool == null || userSchool.getSchoolId() == null) {
            throw new BusinessException(ReturnStatus.USERSCHOOL_PARAMETER_MISS);
        }
        int pageIndex = (pageindex-1)*pagesize;
        List<Map<String, Object>> ret = activityDao.getActivityList(userSchool.getSchoolId(), type, status, name, (pageIndex < 0 ? 0 : pageIndex), pagesize);
        ret.forEach(i->activityUtilService.processActivityStatus(i));  //活动状态
        ret.forEach(i-> i.put("key", i.get("activityId")));
        Long allcount = activityDao.getActivityListCount(userSchool.getSchoolId(), type, status, name);
        return MyPage.processPage(allcount, pagesize, pageindex, ret);
    }

    @Override
    public Map<String, Object> getActivityListBySchoolId(Long userId, Long schoolId, int pageindex, int pagesize) throws Exception {
        schoolUserService.validateOfficialManager(userId);
        List<Map<String, Object>> ret = activityDao.getActivityList(schoolId, 0, 0, null, (pageindex-1)*pagesize, pagesize);
        ret.forEach(i->i.put("reward","" + Integer.parseInt(i.get("rewardScore").toString()) + "学分"));
        ret.forEach(i->i.remove("rewardScore"));
        ret.forEach(i->i.remove("rewardDuration"));
        ret.forEach(i->activityUtilService.processActivityStatus(i));  //活动状态

        Long allcount = activityDao.getActivityListCount(schoolId, 0, 0, null);
        return MyPage.processPage(allcount, pagesize, pageindex, ret);
    }

    @Override
    public Activity getActivityDetailForWeb(Long userId, Long activityId) throws Exception {
        Map<String, Object> map = validateTeacherAndActivity(userId, activityId);
        Activity activity = (Activity)map.get("activity");
        return getActvityDetail(activity);
    }

    @Override
    public Map<String, Object> getActivityMemberListForWeb(Long userId, Long activityId, int pageindex, int pagesize,int status) throws Exception {
        schoolUserService.validateTeacher(userId);
        List<Map<String, Object>> list =activityMemberDao.queryActivityMemberListForWeb(activityId, (pageindex-1)*pagesize, pagesize, status);
        list.forEach(i -> i.put("runningDuration", DateUtil.second2Time(Integer.parseInt(i.get("duration").toString()))));

        if (pageindex > 0) {
            Long allcount = activityMemberDao.queryActivityMemberListForWebCount(activityId, status);
            return MyPage.processPage(allcount, pagesize, pageindex, list);
        }else {
            Map<String, Object> map = new HashMap<>();
            map.put("data", list);
            Activity activity = activityCacheService.getActivityById(activityId);
            map.put("name", activity.getName());
            return map;
        }
    }

    @Override
    public Map<String, Object> getActivityBasicForWeb(Long userId, Long activityId) throws Exception {
        Map<String, Object> map = validateTeacherAndActivity(userId, activityId);
        Activity activity = (Activity)map.get("activity");
        activityUtilService.processActivityStatus(activity);

        Map<String, Object> ret = new HashMap<>();
        ret.put("name", activity.getName());
        ret.put("signCount", activity.getSignCount());
        ret.put("enrollCount", activity.getEnrollCount());
        ret.put("frontCoverURL", activity.getFrontCoverURL());
        ret.put("courseName", activity.getMemberStr());
        ret.put("status", activity.getStatus());
        ret.put("username", userCacheService.getUserById(activity.getUserId()).getUsername());
        return ret;
    }

    private Map<String, Object> validateTeacherAndActivity(Long userId, Long activityId) throws Exception{
        User teacher = schoolUserService.validateTeacher(userId);

        Activity activity = validateActivity(activityId);
        if (teacher.getUserType() != UserTypeEnum.OFFICIALMANAGER.getValue() && activity.getSchoolId() != null && teacher.getSchoolId().longValue() != activity.getSchoolId()) {
            throw new BusinessException(ReturnStatus.NO_PERMISSION);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("teacher", teacher);
        map.put("activity", activity);
        return map;
    }
    private Map<String, Object> validateTeacherAndStudent(Long teacherId, Long studentId) throws Exception{
        User teacher = schoolUserService.validateTeacher(teacherId);
        User student = userService.validateUser(studentId);
        if (teacher.getSchoolId().longValue() != student.getSchoolId()) {
            throw new BusinessException(ReturnStatus.NO_PERMISSION);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("teacher", teacher);
        map.put("student", student);
        return map;
    }
    private Map<String, Object> validateActivityAndStudent(Long userId, Long activityId) throws Exception{
        User student = userService.validateUser(userId);

        Activity activity = validateActivity(activityId);
        if (activity.getSchoolId() != null && student.getSchoolId().longValue() != activity.getSchoolId()) {
            throw new BusinessException(ResultMsg.ACTIVITY_PERMISSION_ERROR);//活动太火爆了，请稍后再来查看
        }
        if (activity.getMemberType() != null && activity.getMemberType() == ActivityPublishTypeEnum.COURSE.getValue()) {
            if (student.getCourseId() == null) {
                throw new BusinessException(ReturnStatus.ACTIVITY_ABDEFDFDF);//活动太火爆了，请稍后再来查看
            }
            if (!activity.getCourseId().contains(student.getCourseId().toString())) {
                throw new BusinessException(ReturnStatus.ACTIVITY_ABDEFDFDF);//活动太火爆了，请稍后再来查看
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("activity", activity);
        map.put("student", student);
        return map;
    }

    private Activity validateActivity(Long activityId) throws Exception {
        Activity activity = activityCacheService.getActivityById(activityId);
        if (activity == null) {
            throw new BusinessException(ResultMsg.ACTIVITY_PERMISSION_ERROR);//活动太火爆了，请稍后再来查看
        }
        return activity;
    }

    @Override
    public Integer countActivityByUserId(Long userId) throws Exception {
        return activityDao.countActivityByUserId(userId);
    }

    @Transactional
    @Override
    public Map<String, Object> addOfficalActivity(Long userId, Activity activity) throws Exception {
        schoolUserService.validateOfficialManager(userId);
        activity.setCreateTime(Calendar.getInstance().getTime());
        activity.setSchoolId(null);
        //activity.setEffectiveSignCount(9999);
        activityCacheService.insertActivity(activity);

        if (activity.getType() != null && activity.getType() == ActivityTypeEnum.RUNNING.ordinal()) {
            activity.getRunning().setActivityId(activity.getActivityId());
            runningService.createRunning(activity.getRunning());
        }

        activity.setEnrollCount(0);
        return obj2Map(activity);
    }

    @Override
    public Map<String, Object> getOfficialActivityListForWeb(Long userId, Integer type, Integer status, String name, int pageindex, int pagesize) throws Exception {
        schoolUserService.validateOfficialManager(userId);
        List<Map<String, Object>> list = activityDao.getOfficialActivityListForWeb(type, status, name,(pageindex - 1) * pagesize, pagesize);
        list.forEach(i -> activityUtilService.processActivityStatus(i));  //活动状态
        list.forEach(i -> i.put("key", i.get("activityId")));
        Long allcount = activityDao.getOfficialActivityListForWebCount(type, status, name);
        return MyPage.processPage(allcount, pagesize, pageindex, list);
    }

    @Transactional
    @Override
    public Map<String, Object> updateOfficialActivity(Long userId, Activity activity) throws Exception {
        schoolUserService.validateOfficialManager(userId);
        Activity activityTemp = validateActivity(activity.getActivityId());
        activityCacheService.updateActivity(activity);

        if (activity.getType() != null && activity.getType() == ActivityTypeEnum.NON_RUNNING.ordinal()) {// 普通活动没有跑步信息,无论有没有，都删除
            runningService.deleteRunningByActivityId(activity.getActivityId());
        }
        activity.setEnrollCount(activityTemp.getEnrollCount());
        updateRunning(activity);
        activity.setEnrollCount(activityTemp.getEnrollCount());
        return obj2Map(activity);
    }

    @Override
    public Activity getOfficialActivityDetailForWeb(Long userId, Long activityId) throws Exception {
        schoolUserService.validateOfficialManager(userId);
        Activity activity = validateActivity(activityId);
        return getActvityDetail(activity);
    }

    private Activity getActvityDetail(Activity activity) throws Exception {
        if (activity.getType() != null && activity.getType() == ActivityTypeEnum.RUNNING.ordinal()) {
            Running running = runningCacheService.getByActivityId(activity.getActivityId());
            if (running != null ) {
                if (running.getPassNode() != null && running.getPassNode()) {
                    List<RunningLine> runningLineList = runningLineCacheService.getRunningLineByRunningId(running.getRunningId());
                    running.setRunningLineList(runningLineList);
                } else {
                    running.setRunningLineList(new ArrayList<>());//返回空数组
                }
                activity.setRunning(running);
            }
        }

        activityUtilService.processActivityStatus(activity);
        return activity;
    }

    @Override
    public Map<String, Object> getOfficialActivityMemberListForWeb(Long userId, Long activityId, int pageindex, int pagesize, int status) throws Exception {
        schoolUserService.validateOfficialManager(userId);
        List<Map<String, Object>> list =activityMemberDao.queryActivityMemberListForWeb(activityId, (pageindex-1)*pagesize, pagesize, status);
        Long allcount = activityMemberDao.queryActivityMemberListForWebCount(activityId, status);

        list.forEach(i -> i.put("activityDuration", (DateUtil.getSecDiff((Date)i.get("end_time") , (Date)i.get("start_time")))));   // 活动时间长度，秒
        list.forEach(i -> i.remove("start_time"));
        list.forEach(i -> i.remove("end_time"));
        return MyPage.processPage(allcount, pagesize, pageindex, list);
    }

    @Override
    public Map<String, Object> getAllSchoolActivityList(Long userId, int pageindex, int pagesize) throws Exception {
        schoolUserService.validateOfficialManager(userId);
        List<Map<String, Object>> list =activityDao.queryAllSchoolActivityList((pageindex - 1) * pagesize, pagesize);
        list.forEach(i->activityUtilService.processActivityStatus(i));  //活动状态

        Long allcount = activityDao.queryAllSchoolActivityListCount();

        return MyPage.processPage(allcount, pagesize, pageindex, list);
    }

    @Transactional
    @Override
    public void updateActivityMemberAfterRunning(Long activityId, Long userId, int duration, int calorie, float length, int isEffective, String runningRecordId) throws Exception {
        Activity activity = validateActivity(activityId);
        ActivityMember activityMember = activityMemberDao.queryActivityMemberById(activityId, userId);
        if(null != activityMember && null != activityMember.getSucCount()) {
            int sucCount = activity.getEffectiveSignCount();//活动可参与次数上限
            int myCount = activityMember.getSucCount();
            if(myCount < sucCount) {
                int signCount = activityMember.getSigncount() != null && activityMember.getSigncount() == 0 ? 1 : 0;
                synchronized (activityId) {
                    activityCacheService.updateActivityEnrollAndSign(activityId, 0, signCount, 1);
                }

                activityMember.setSigncount(1);
                if (isEffective == 88) {
                    activityMember.setSucCount(activityMember.getSucCount() + 1);
                    activityMember.setRunningLenght(length + activityMember.getRunningLenght());
                    activityMember.setDuration(duration + activityMember.getDuration());
                    activityMember.setCalorieCount(calorie + activityMember.getCalorieCount());
                }
                activityMemberDao.updateActivityMember(activityMember);

                float score = 0;
                int durationTemp = 0;
                if (isEffective == 88) {
                    score = activity.getRewardScore();
                }
                ActivityPartinRecord record = new ActivityPartinRecord(activityMember.getActivityMemberId(), score, (byte)durationTemp, isEffective, runningRecordId);
                activityPartInRecordDao.updateActivityPartinRecord(record);
            }
        }
    }

    @Override
    public Activity getActivityDetailForShare(long activityId) throws Exception {
        Activity activity = validateActivity(activityId);
        activityUtilService.processActivityStatus(activity);
        activityUtilService.processActivityEnrollStatus(activity);

        /***************************************** 开始 配置人数等信息 **********************************************/
        if(null == activity.getSchoolId()) {//官方活动
            calcQuotiety(activity);
            activity.setLuckCount(activityLuckRecordService.countLuckRecordForPC(activityId).intValue());
        }
        return activity;
    }

    @Override
    public List<Map<String, Object>> getActivityAccess(long userId) throws Exception {
        return activityDao.getActivityAccess(userId);
    }

    @Override
    public MyPageInfo getStudentActivityList(Map<String, Object> paramsMap, int pageindex, int pagesize) throws Exception {
        PageHelper.startPage(pageindex, pagesize);
        List<Map<String, Object>> list = activityPartInRecordDao.getStudentActivityList(paramsMap);
        PageInfo<Map<String, Object>> p = new PageInfo<Map<String, Object>>(list);
        MyPageInfo pageInfo = new MyPageInfo(p);
        return pageInfo;
    }

    @Override
    public MyPageInfo getActivityMemberListForWebNew(Map<String, Object> paramsMap, int pageindex, int pagesize) throws Exception {
        PageHelper.startPage(pageindex, pagesize);
        schoolUserService.validateTeacher(Long.parseLong(paramsMap.get("userId").toString()));
        List<Map<String, Object>> list =activityMemberDao.queryActivityMemberListForWebNew(paramsMap);
        list.forEach(i -> i.put("runningDuration", DateUtil.second2Time(Integer.parseInt(i.get("duration").toString()))));
        PageInfo<Map<String, Object>> p = new PageInfo<Map<String, Object>>(list);
        return new MyPageInfo(p);
    }

    @Override
    public MyPageInfo getActivityList(Map<String, Object> paramsMap, int pageindex, int pagesize) throws Exception {
        UserSchool userSchool = schoolService.getParaByUserId(Long.parseLong(paramsMap.get("userId").toString()), Calendar.getInstance().getTime());
        if (userSchool == null || userSchool.getSchoolId() == null) {
            throw new BusinessException(ReturnStatus.USERSCHOOL_PARAMETER_MISS);
        }

        User user =  userCacheService.getUserById(Long.parseLong(paramsMap.get("userId").toString()));
        //如果是校方管理员
        if(user.getUserType().equals(UserTypeEnum.SCHOOLMANAGER.getValue()) || user.getUserType().equals(UserTypeEnum.TEACHER.getValue())){
            paramsMap.put("schoolId",userSchool.getSchoolId());
        }
        PageHelper.startPage(pageindex, pagesize);
        List<Map<String, Object>> list = activityDao.getActivityLists(paramsMap);
        list.forEach(i->activityUtilService.processActivityStatus(i));  //活动状态
        list.forEach(i -> i.put("key", i.get("activityId")));
        PageInfo<Map<String, Object>> p = new PageInfo<Map<String, Object>>(list);
        return new MyPageInfo(p);

//        int pageIndex = (pageindex-1)*pagesize;
//        List<Map<String, Object>> ret = activityDao.getActivityList(userSchool.getSchoolId(), type, status, name, (pageIndex < 0 ? 0 : pageIndex), pagesize);
//        ret.forEach(i->activityUtilService.processActivityStatus(i));  //活动状态
//        ret.forEach(i-> i.put("key", i.get("activityId")));
//        Long allcount = activityDao.getActivityListCount(userSchool.getSchoolId(), type, status, name);
//        return MyPage.processPage(allcount, pagesize, pageindex, ret);
    }

    @Override
    public List<Map<String, Object>> getActivityRunningRecordByUserId(Map<String, Object> paramsMap) {
        return activityMemberDao.getActivityRunningRecordByUserId(paramsMap);
    }

    @Override
    public List<Map<String, Object>> getActivityByMemberList(Long activityId, int size) {
        return  activityMemberDao.getActivityByMemberList(activityId,size);
    }


    private void calcQuotiety(Activity activity) throws Exception {
        if (activity.getSchoolId() == null) {
            int[] ret = new int[3];
            Map<String, Object> quotietyParamsMap =  getQuotiety();
            int baomingQuotiety = Integer.parseInt(quotietyParamsMap.get("baomingQuotiety").toString());//报名人数系数
            int canyuQuotiety = Integer.parseInt(quotietyParamsMap.get("canyuQuotiety").toString());//参与人数系数
            int renciQuotiety = Integer.parseInt(quotietyParamsMap.get("renciQuotiety").toString());//参与人次系数
            activity.setEnrollCount(activity.getEnrollCount()* baomingQuotiety);//乘以设置的系数，构成虚拟数值
            activity.setSignCount(activity.getSignCount() * canyuQuotiety);
            activity.setAllSucCount(activity.getAllSucCount() * renciQuotiety);
//
//            Map<String,Object>  statisticsData  = activityMemberDao.getStatisticsData(activity.getActivityId());
//            activity.setEnrollCount(statisticsData.get("enrollCount") != null ? Integer.parseInt(statisticsData.get("enrollCount").toString()) : 0);
//            activity.setSignCount(statisticsData.get("signCount") != null ? Integer.parseInt(statisticsData.get("signCount").toString()) : 0);
//            activity.setAllSucCount(statisticsData.get("sucCount") != null ? Integer.parseInt(statisticsData.get("sucCount").toString()) : 0);
        }
    }


}
