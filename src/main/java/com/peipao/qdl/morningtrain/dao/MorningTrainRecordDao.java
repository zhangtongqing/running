package com.peipao.qdl.morningtrain.dao;

import com.peipao.qdl.morningtrain.model.MorningTrainRecord;
import com.peipao.qdl.morningtrain.model.MorningTrainRecordVo;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * 方法名称：
 * 功能描述：
 * 作者：Liu Fan
 * 版本：2.1.0
 * 创建日期：2018/3/1 11:47
 * 修订记录：
 */

@Repository
public interface MorningTrainRecordDao {
    /**
     * app端查询个人运动记录
     * @param paramsMap (long semesterId, long userId, startDate, endDate)
     * @return
     * @throws Exception
     */
    MorningTrainRecordVo getMorningTrainRecordToday(Map<String, Object> paramsMap) throws Exception;
    MorningTrainRecord findMorningTrainRecordToday(Map<String, Object> paramsMap) throws Exception;

    void insertMorningTrainRecord(MorningTrainRecord morningTrainRecord) throws Exception;

    void updateMorningTrainRecord(MorningTrainRecord morningTrainRecord) throws Exception;
}
