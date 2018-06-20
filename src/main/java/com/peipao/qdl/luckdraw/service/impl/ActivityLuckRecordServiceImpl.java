package com.peipao.qdl.luckdraw.service.impl;


import com.peipao.framework.constant.WebConstants;
import com.peipao.framework.page.MyPage;
import com.peipao.qdl.activity.model.Activity;
import com.peipao.qdl.activity.service.ActivityCacheService;
import com.peipao.qdl.luckdraw.dao.ActivityLuckRecordDao;
import com.peipao.qdl.luckdraw.dao.ActivityPrizeDao;
import com.peipao.qdl.luckdraw.model.ActivityLuckRecord;
import com.peipao.qdl.luckdraw.service.ActivityLuckRecordService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 方法名称：
 * 功能描述：
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/12/19 10:56
 * 修订记录：
 */
@Service
public class ActivityLuckRecordServiceImpl implements ActivityLuckRecordService {
    @Autowired
    private ActivityLuckRecordDao activityLuckRecordDao;
    @Autowired
    private ActivityPrizeDao activityPrizeDao;

    @Autowired
    private ActivityCacheService activityCacheService;

    @Override
    public ActivityLuckRecord getLuckRecordByActivity(Map<String, Object> paramsMap) throws Exception {
        return activityLuckRecordDao.getLuckRecordByActivity(paramsMap);
    }

    @Override
    public int getLuckCountByUser(Map<String, Object> paramsMap) throws Exception {
        return activityLuckRecordDao.getLuckCountByUser(paramsMap);
    }

    @Override
    public int getPrizeCountByUser(Map<String, Object> paramsMap) throws Exception {
        return activityLuckRecordDao.getPrizeCountByUser(paramsMap);
    }

    @Override
    @Transactional
    public void insertActivityLuckRecord(ActivityLuckRecord activityLuckRecord) throws Exception {
        activityLuckRecordDao.insertActivityLuckRecord(activityLuckRecord);
         if(activityLuckRecord.getIsLuck() == WebConstants.Boolean.TRUE.ordinal() && null != activityLuckRecord.getPrizeId()){
             activityPrizeDao.updatePrizeSendTotal(activityLuckRecord.getPrizeId());
         }
    }

    @Override
    public Map<String, Object> getLuckRecordForPC(JSONObject json, int[] pageParams) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("activityId", json.getLong("activityId"));
        if (json.containsKey("mobile")) {
            params.put("mobile", json.getString("mobile"));
        }
        if (json.containsKey("isLuck")) {
            params.put("isLuck", json.getInt("isLuck"));
        }
        if (json.containsKey("key")) {
            params.put("orderkey", json.getString("key"));
            params.put("sort", json.getString("value"));
        }
        if (pageParams == null) {// 导出
            Map<String, Object> ret = new HashMap<>();
            ret.put("data", activityLuckRecordDao.getLuckRecordForPC(params));
            Activity activity = activityCacheService.getActivityById(json.getLong("activityId"));
            ret.put("name", activity.getName());
            return ret;
        }
        Long allcount = activityLuckRecordDao.countLuckRecordForPC(params);
        if (allcount > 0) {
            params.put("from", (pageParams[0]-1)*pageParams[1]);
            params.put("num", pageParams[1]);
            List<Map<String, Object>> list = activityLuckRecordDao.getLuckRecordForPC(params);
            list.stream().forEach(i->i.put("key", i.get("recordId")));
            return MyPage.processPage(allcount, pageParams[1], pageParams[0], list);
        }else {

            return MyPage.processPage(allcount, pageParams[1], pageParams[0], null);
        }
    }

    @Override
    public Long countLuckRecordForPC(Long activityId) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("activityId", activityId);
        return activityLuckRecordDao.countLuckRecordForPC(params);
    }

    @Override
    public List<Map<String, Object>> getActivityIdsByUserId(Map<String, Object> paramsMap) throws Exception {
        return activityLuckRecordDao.getActivityIdsByUserId(paramsMap);
    }

    @Override
    public List<Map<String, Object>> getPrizeIdsByActivityId(Map<String, Object> paramsMap) throws Exception {
        return activityLuckRecordDao.getPrizeIdsByActivityId(paramsMap);
    }

    @Override
    public List<Map<String, Object>> myLuckRecord(Map<String, Object> paramsMap) throws Exception {
        return activityLuckRecordDao.myLuckRecord(paramsMap);
    }

    @Override
    public List<Map<String, Object>> activityLuckRecord(Map<String, Object> paramsMap) throws Exception {
        return activityLuckRecordDao.activityLuckRecord(paramsMap);
    }

    @Override
    public int getPrizeCountByPrizeId(Long prizeId) throws Exception {
        return activityLuckRecordDao.getPrizeCountByPrizeId(prizeId);
    }
}
