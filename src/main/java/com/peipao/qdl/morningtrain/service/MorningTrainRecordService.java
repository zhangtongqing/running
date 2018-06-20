package com.peipao.qdl.morningtrain.service;

import com.peipao.qdl.morningtrain.model.MorningTrainRecord;
import com.peipao.qdl.morningtrain.model.MorningTrainRecordVo;

import java.util.Map;

/**
 * 方法名称：
 * 功能描述：
 * 作者：Liu Fan
 * 版本：2.1.0
 * 创建日期：2018/3/1 11:49
 * 修订记录：
 */

public interface MorningTrainRecordService {

    MorningTrainRecordVo getMorningTrainRecordToday(Map<String, Object> paramsMap) throws Exception;
    MorningTrainRecord findMorningTrainRecordToday(Map<String, Object> paramsMap) throws Exception;

    void insertMorningTrainRecord(MorningTrainRecord morningTrainRecord) throws Exception;

    void updateMorningTrainRecord(MorningTrainRecord morningTrainRecord) throws Exception;

    void updateMorningTrainRecordAndStatistic(long schoolId, MorningTrainRecord morningTrainRecord) throws Exception;



}
