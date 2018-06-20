package com.peipao.qdl.luckdraw.service.impl;


import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.constant.WebConstants;
import com.peipao.framework.exception.BusinessException;
import com.peipao.qdl.luckdraw.dao.ActivityPrizeDao;
import com.peipao.qdl.luckdraw.model.ActivityPrize;
import com.peipao.qdl.luckdraw.service.ActivityLuckRecordService;
import com.peipao.qdl.luckdraw.service.ActivityPrizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 方法名称：ActivityPrizeServiceImlp
 * 功能描述：ActivityPrizeServiceImlp
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/12/19 14:04
 * 修订记录：
 */
@Service
public class ActivityPrizeServiceImlp implements ActivityPrizeService {
    @Autowired
    private ActivityPrizeDao activityPrizeDao;

    @Autowired
    private ActivityLuckRecordService activityLuckRecordService;

    @Override
    public List<ActivityPrize> getActivityPrizeByActivityId(Map<String, Object> paramsMap) throws Exception {
        return activityPrizeDao.getActivityPrizeByActivityId(paramsMap);
    }

    @Transactional
    @Override
    public void addActivityPrize(ActivityPrize activityPrize) throws Exception {
        activityPrizeDao.addActivityPrize(activityPrize);
    }

    @Override
    public ActivityPrize getActivityPrizeById(Long prizeId) throws Exception {
        return activityPrizeDao.getActivityPrizeById(prizeId);
    }

    @Transactional
    @Override
    public void updateActivityPrize(ActivityPrize activityPrize) throws Exception {
        activityPrizeDao.updateActivityPrize(activityPrize);
    }

    @Transactional
    @Override
    public void logicDeleteActivityPrizeByPrizeId(Long prizeId, Long userId) throws Exception {
        int count = activityLuckRecordService.getPrizeCountByPrizeId(prizeId);
        if (count > 0) {
            throw new BusinessException(ResultMsg.PRIZE_CANT_DELETE);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("prizeId", prizeId);
        map.put("updateUserId", userId);
        map.put("updateTime", Calendar.getInstance().getTime());
        map.put("logicDelete", WebConstants.Boolean.TRUE.ordinal());
        activityPrizeDao.logicDeleteActivityPrizeByPrizeId(map);
    }
}
