package com.peipao.qdl.activity.service;

import com.peipao.framework.constant.WebConstants;
import com.peipao.framework.util.DateUtil;
import com.peipao.qdl.activity.model.Activity;
import com.peipao.qdl.activity.model.ActivityStatusEnum;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Map;

/**
 * @author Meteor.wu
 * @since 2018/1/30 13:50
 */
@Service
public class ActivityUtilService {
    /**
     * 设置活动状态
     */
    void processActivityStatus(Map<String, Object> map){
        if (map.get("status") !=null &&Integer.parseInt(map.get("status").toString()) == ActivityStatusEnum.DRAFT.getValue()) {
            map.put("status", ActivityStatusEnum.DRAFT.getValue());
        }else if (map.get("endTime") != null && DateUtil.parseDate(map.get("endTime").toString(), DateUtil.PATTERN_DATE_AND_TIME).getTime() < Calendar.getInstance().getTimeInMillis()){
            map.put("status", ActivityStatusEnum.OVER.getValue());
        }else if(map.get("startTime") != null && DateUtil.parseDate(map.get("startTime").toString(), DateUtil.PATTERN_DATE_AND_TIME).getTime() < Calendar.getInstance().getTimeInMillis()){
            map.put("status", ActivityStatusEnum.INPROCESS.getValue());
        }else if (map.get("enrollEndTime") != null && DateUtil.parseDate(map.get("enrollEndTime").toString(), DateUtil.PATTERN_DATE_AND_TIME).getTime() < Calendar.getInstance().getTimeInMillis()){
            map.put("status", ActivityStatusEnum.ENROLLED.getValue());
        }else if(map.get("enrollStartTime") != null && DateUtil.parseDate(map.get("enrollStartTime").toString(), DateUtil.PATTERN_DATE_AND_TIME).getTime() < Calendar.getInstance().getTimeInMillis()){
            map.put("status", ActivityStatusEnum.ENROLLING.getValue());
        }else {
            map.put("status", ActivityStatusEnum.UNENROLL.getValue());
        }
    }

    /**
     * 设置活动状态
     */
    void processActivityStatus(Activity activity){
        if (activity.getStatus() != null && activity.getStatus() == ActivityStatusEnum.DRAFT.getValue()) {
            activity.setStatus(ActivityStatusEnum.DRAFT.getValue());
        }else if (activity.getEndTime() != null && activity.getEndTime().getTime() < Calendar.getInstance().getTimeInMillis()){
            activity.setStatus(ActivityStatusEnum.OVER.getValue());
        }else if(activity.getStartTime() != null && activity.getStartTime().getTime() < Calendar.getInstance().getTimeInMillis()){
            activity.setStatus(ActivityStatusEnum.INPROCESS.getValue());
        }else if (activity.getEnrollEndTime() != null && activity.getEnrollEndTime().getTime() < Calendar.getInstance().getTimeInMillis()){
            activity.setStatus(ActivityStatusEnum.ENROLLED.getValue());
        }else if(activity.getEnrollStartTime() != null && activity.getEnrollStartTime().getTime() < Calendar.getInstance().getTimeInMillis()){
            activity.setStatus(ActivityStatusEnum.ENROLLING.getValue());
        }else {
            activity.setStatus(ActivityStatusEnum.UNENROLL.getValue());
        }
    }

    /**
     * 设置当前活动报名状态
     */
    void processActivityEnrollStatus(Map<String, Object> map){
        if (DateUtil.parseDate(map.get("enrollStartTime").toString(), DateUtil.PATTERN_DATE_AND_TIME).getTime() < Calendar.getInstance().getTimeInMillis()
                && DateUtil.parseDate(map.get("enrollEndTime").toString(), DateUtil.PATTERN_DATE_AND_TIME).getTime() > Calendar.getInstance().getTimeInMillis()){
            map.put("enrollStatus", WebConstants.Boolean.TRUE.ordinal());
        }else {
            map.put("enrollStatus", WebConstants.Boolean.FALSE.ordinal());
        }
    }

    /**
     * 设置当前活动报名状态
     */
    void processActivityEnrollStatus(Activity activity){
        if (activity.getEnrollStartTime() == null || activity.getEnrollEndTime() == null) {
            return;
        }
        if (activity.getEnrollStartTime().getTime() < Calendar.getInstance().getTimeInMillis()
                && activity.getEnrollEndTime().getTime() > Calendar.getInstance().getTimeInMillis()){
            activity.setEnrollStatus(WebConstants.Boolean.TRUE.ordinal());
        }else {
            activity.setEnrollStatus(WebConstants.Boolean.FALSE.ordinal());
        }
    }
}
