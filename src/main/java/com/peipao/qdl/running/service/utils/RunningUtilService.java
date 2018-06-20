package com.peipao.qdl.running.service.utils;

import com.peipao.framework.exception.BusinessException;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.util.DateUtil;
import com.peipao.framework.util.StringUtil;
import com.peipao.qdl.course.model.Course;
import com.peipao.qdl.course.service.CourseCacheService;
import com.peipao.qdl.running.model.RankTypeEnum;
import com.peipao.qdl.running.model.RunningEffectiveEnum;
import com.peipao.qdl.running.model.RunningEnum;
import com.peipao.qdl.running.model.RunningRecord;
import com.peipao.qdl.runningrule.model.vo.RunningRuleVo;
import com.peipao.qdl.school.model.School;
import com.peipao.qdl.school.service.SchoolCacheService;
import com.peipao.qdl.school.service.UserLevelService;
import com.peipao.qdl.statistics.service.UserStatisticService;
import com.peipao.qdl.user.model.User;
import com.peipao.qdl.user.service.UserCacheService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 方法名称：RunningUtilService
 * 功能描述：RunningUtilService
 * 作者：Liu Fan
 * 版本：2.0.10
 * 创建日期：2018/01/15 16:56
 * 修订记录：
 */
@Service
public class RunningUtilService {
    @Autowired
    private CourseCacheService courseCacheService;
    @Autowired
    private UserCacheService userCacheService;
    @Autowired
    private UserLevelService userLevelService;
    @Autowired
    private UserStatisticService userStatisticService;
    @Autowired
    private SchoolCacheService schoolCacheService;

    public void setRunningRecordByRule(RunningRuleVo runningRule, RunningRecord runningRecord, boolean isMaxCount) throws Exception {
        Float myKiometer = 0f;
        Float validKiometerMin = 0f;
        Float validKiometerMax = 0f;

        //kilometeorCount=学生跑步实际里程
        myKiometer = runningRecord.getKilometeorCount();//获得学生实际跑步里程
        validKiometerMin = runningRule.getValidKiometerMin();//单次里程要求:最小必须达到
        validKiometerMax = runningRule.getValidKiometerMax();//最大不能超过

        if(runningRecord.getIsEffective() == RunningEffectiveEnum.Success.getCode()) {
            if(isMaxCount) {
                runningRecord.setValidKilometeorCount(0f);//次数超限后，有效里程不记录，只记录真实里程
            } else {
                if (myKiometer > validKiometerMax) {
                    runningRecord.setValidKilometeorCount(validKiometerMax);//学生跑步数值大于规则最大参数，封顶
                } else {
                    runningRecord.setValidKilometeorCount(myKiometer);
                }
            }
        } else {
            runningRecord.setValidKilometeorCount(0f);
        }
    }


    public JSONObject createDataForList(RunningRecord runningRecord) throws Exception {
        User user = userCacheService.getUserById(runningRecord.getUserId());
        if(null == user) {
            throw new BusinessException(ReturnStatus.USER_NOT_EXIST);//用户不存在
        }
        Course course = null;
        String courseName = "";
        if(null != user.getCourseId() && user.getCourseId() > 0) {
            course = courseCacheService.getCourseById(user.getCourseId());
        }
        if(null != course) {
            courseName = course.getName();
        }
        JSONObject json = new JSONObject();
        json.put("key", runningRecord.getRunningRecordId());
        json.put("runningRecordId", runningRecord.getRunningRecordId());
        json.put("invalidReason", runningRecord.getInvalidReason());
        json.put("status", runningRecord.getStatus());
        json.put("type", runningRecord.getType());
        json.put("appealTime", DateUtil.dateToStr(runningRecord.getAppealTime(), "yyyy-MM-dd HH:mm:ss"));
        json.put("username", user.getUsername());
        json.put("studentNo", user.getStudentNO());
        json.put("mobile", user.getMobile());
        json.put("courseName", courseName);
        return json;
    }

    public JSONObject getMyRankingIfNull(long userId, int queryType, int runningType) throws Exception {
        JSONObject myRankingJson = new JSONObject();
        User user = userCacheService.getUserById(userId);
        if(queryType == RankTypeEnum.RankingInCountry.getCode()) {
            String schoolName = "<学校未绑定>";
            if(null != user.getSchoolId() && user.getSchoolId() > 0L) {
                School school = schoolCacheService.getSchoolById(user.getSchoolId());
                schoolName = school.getName();
            }
            myRankingJson.accumulate("schoolName", schoolName);
        } else {
            String level = "";
            if(null != user.getLevelId() && user.getLevelId() > 0L) {
                level = userLevelService.getMyLevelTitle(user.getLevelId());
            }
            myRankingJson.accumulate("level", level);
        }
        Float myLength = 0f;
        myRankingJson.accumulate("userId", userId);
        myRankingJson.accumulate("username", StringUtil.isEmpty(user.getUsername()) == true ? "" : user.getUsername());
        myRankingJson.accumulate("sex", user.getSex());
        myRankingJson.accumulate("imageURL", null == user.getImageURL() ? "" : user.getImageURL() );

        if(runningType == RunningEnum.FREERUNNING.getValue()){
            myLength = userStatisticService.getMyRankingLength(userId);
            myRankingJson.accumulate("runningLength", null != myLength ? myLength : 0.00f);
        }else if(runningType == RunningEnum.MORNINGRUNNING.getValue()){
            myLength = userStatisticService.getMyRankingLengthByMorningRun(userId);
            myRankingJson.accumulate("morningRunningLength", null != myLength ? myLength : 0.00f);
        }
        int rownum = 0;
        myRankingJson.accumulate("rownum", rownum);

        return myRankingJson;
    }
}
