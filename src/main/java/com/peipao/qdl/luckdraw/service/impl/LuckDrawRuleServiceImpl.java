package com.peipao.qdl.luckdraw.service.impl;


import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.constant.WebConstants;
import com.peipao.framework.exception.BusinessException;
import com.peipao.qdl.activity.model.Activity;
import com.peipao.qdl.activity.service.ActivityCacheService;
import com.peipao.qdl.activity.service.ActivityService;
import com.peipao.qdl.luckdraw.dao.LuckDrawRuleDao;
import com.peipao.qdl.luckdraw.model.LuckDrawRule;
import com.peipao.qdl.luckdraw.service.ActivityLuckRecordService;
import com.peipao.qdl.luckdraw.service.LuckDrawRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 方法名称：LuckDrawRuleServiceImpl
 * 功能描述：LuckDrawRuleServiceImpl
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/12/19 11:33
 * 修订记录：
 */
@Service
public class LuckDrawRuleServiceImpl implements LuckDrawRuleService {
    @Autowired
    private LuckDrawRuleDao luckDrawRuleDao;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityLuckRecordService activityLuckRecordService;

    @Autowired
    private ActivityCacheService activityCacheService;

    @Override
    public LuckDrawRule getLuckDrawRuleByActivityId(Map<String, Object> paramsMap) throws Exception {
        LuckDrawRule luckDrawRule = luckDrawRuleDao.getLuckDrawRuleByActivityId(paramsMap);
        if (luckDrawRule == null) {
            return null;
        }
        Activity activity = activityCacheService.getActivityById(luckDrawRule.getActivityId());
        luckDrawRule.setIsUse(activity.getHasLuckDraw());
        return luckDrawRule;
    }

    @Transactional
    @Override
    public void addLuckDrawRule(Long userId, LuckDrawRule luckDrawRule) throws Exception {
        luckDrawRuleDao.addLuckDrawRule(luckDrawRule);

        Activity activity = activityCacheService.getActivityById(luckDrawRule.getActivityId());
        activity.setHasLuckDraw(luckDrawRule.getIsUse());
        activityService.updateOfficialActivity(userId, activity);
    }

    @Override
    public LuckDrawRule getLuckDrawById(Long ruleId) throws Exception {
        LuckDrawRule luckDrawRule = luckDrawRuleDao.getLuckDrawById(ruleId);
        Activity activity = activityCacheService.getActivityById(luckDrawRule.getActivityId());
        luckDrawRule.setIsUse(activity.getHasLuckDraw());
        return luckDrawRule;
    }

    @Transactional
    @Override
    public void updateLuckDrawRule(Long userId, LuckDrawRule luckDrawRule) throws Exception {
        Activity activity = activityCacheService.getActivityById(luckDrawRule.getActivityId());
        if (activity.getHasLuckDraw() != WebConstants.Boolean.TRUE.ordinal() && luckDrawRule.getIsUse() == WebConstants.Boolean.FALSE.ordinal()) {
            Long count = activityLuckRecordService.countLuckRecordForPC(activity.getActivityId());
            if (count > 0) {
                throw new BusinessException(ResultMsg.LUCK_DRAW_CANT_DELETE);
            }
        }


        luckDrawRuleDao.updateLuckDrawRule(luckDrawRule);
        activity.setActivityId(luckDrawRule.getActivityId());
        activity.setHasLuckDraw(luckDrawRule.getIsUse());
        activityService.updateOfficialActivity(userId, activity);
    }

}
