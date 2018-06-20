package com.peipao.qdl.school.service;


import com.peipao.framework.constant.WebConstants;
import com.peipao.framework.util.StringUtil;
import com.peipao.qdl.school.dao.LevelDao;
import com.peipao.qdl.school.model.Level;
import com.peipao.qdl.statistics.dao.UserStatisticDao;
import com.peipao.qdl.statistics.model.UserStatistic;
import com.peipao.qdl.user.dao.UserDao;
import com.peipao.qdl.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 方法名称： UserLevelService
 * 功能描述： UserLevelService
 * 作者：Liu fan
 * 版本：1.0
 * 创建日期：2017/8/16 14:34
 * 修订记录：
 */
@Service
public class UserLevelService {
    @Autowired
    private UserStatisticDao userStatisticDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private LevelDao levelDao;

    public Level findZeroLevelBySchoolId(Long schoolId) throws Exception {
        return levelDao.findZeroLevelBySchoolId(schoolId);
    }

    public String getMyLevelTitle(Long levelId) throws Exception {
        String levelStr = levelDao.getMyLevelTitle(levelId);
        if(StringUtil.isEmpty(levelStr)) {
            levelStr = "";
        }
        return levelStr;
    }

    @Transactional
    @Async
    public void updateUserLevelAsync(Long userId, Long semesterId){
        updateLevel(userId, semesterId);
    }


    @Transactional
    public void updateUserLevel(Long userId, Long semesterId){
        updateLevel(userId, semesterId);
    }

    private void updateLevel(Long userId, Long semesterId) {
        UserStatistic userStatistic = null;
        User user = null;
        try {
            userStatistic = userStatisticDao.getByUserIdAndSemesterId(userId, semesterId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            user = userDao.getUserById(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(null != user) {
            Float totalLength = 0f;
            if(null != userStatistic) {
                if(null != userStatistic.getMorningRunningRealLength()) {
                    totalLength = totalLength + userStatistic.getMorningRunningRealLength();
                }
                if(null != userStatistic.getFreeRunningRealLength()) {
                    totalLength = totalLength + userStatistic.getFreeRunningRealLength();
                }
                if(null != userStatistic.getActivityRunningRealLength()) {
                    totalLength = totalLength + userStatistic.getActivityRunningRealLength();
                }
            }
            try {
                Level level = levelDao.findUserNewLevel(WebConstants.qdl_level_school_id, totalLength);
                if(null != level) {
                    if(null == user.getLevelId() || (!user.getLevelId().equals(level.getLevelId())) ) {
                        userDao.updateUserLevel(level.getLevelId(), user.getUserId());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
