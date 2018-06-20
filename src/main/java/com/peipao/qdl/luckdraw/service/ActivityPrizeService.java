package com.peipao.qdl.luckdraw.service;

import com.peipao.qdl.luckdraw.model.ActivityPrize;

import java.util.List;
import java.util.Map;

/**
 * 方法名称：ActivityPrizeService
 * 功能描述：ActivityPrizeService
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/12/19 14:04
 * 修订记录：
 */
public interface ActivityPrizeService {
    List<ActivityPrize> getActivityPrizeByActivityId(Map<String, Object> paramsMap) throws Exception;

    void addActivityPrize(ActivityPrize activityPrize) throws Exception;

    ActivityPrize getActivityPrizeById(Long prizeId) throws Exception;

    void updateActivityPrize(ActivityPrize activityPrize) throws Exception;

    void logicDeleteActivityPrizeByPrizeId(Long prizeId, Long userId) throws Exception;
}
