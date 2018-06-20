package com.peipao.qdl.compensate.dao;


import com.peipao.qdl.compensate.model.CompensateMain;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * 方法名称：CompensateMainDao
 * 功能描述：CompensateMainDao
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/11/30 16:54
 * 修订记录：
 */

@Repository
public interface CompensateMainDao {
    void insertCompensateMain(Map<String, Object> paramsMap) throws Exception;
    void updateCompensateMainForRecord(Map<String, Object> paramsMap) throws Exception;
    Map<String, Object> getCompensateMainInfoForStudent(Map<String, Object> paramsMap) throws Exception;
    CompensateMain getCompensateMainByUser(Map<String, Object> paramsMap) throws Exception;

}
