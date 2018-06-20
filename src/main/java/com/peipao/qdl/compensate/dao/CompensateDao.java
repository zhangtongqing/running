package com.peipao.qdl.compensate.dao;


import com.peipao.qdl.compensate.model.Compensate;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 方法名称：CompensateDao
 * 功能描述：CompensateDao
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/11/30 16:54
 * 修订记录：
 */

@Repository
public interface CompensateDao {
    void insertCompensate(@Param("compensate") Compensate compensate) throws Exception;
    Map<String, Object> getCompensateInfoForStudent(Map<String, Object> paramsMap) throws Exception;
    List<Map<String, Object>> getCompensateListForStudent(Map<String, Object> paramsMap) throws Exception;
}
