package com.peipao.qdl.compensate.service;


import com.peipao.framework.page.MyPageInfo;
import com.peipao.qdl.compensate.model.Compensate;
import com.peipao.qdl.compensate.model.CompensateMain;
import com.peipao.qdl.school.model.UserSchool;

import java.util.Map;

/**
 * 方法名称：CompensateService
 * 功能描述：CompensateService
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/11/30 17:13
 * 修订记录：
 */
public interface CompensateService {

    Map<String, Object> getCompensateInfoForStudent(Map<String, Object> paramsMap) throws Exception;
    MyPageInfo getCompensateListForStudent(Map<String, Object> paramsMap, int pageindex, int pagesize) throws Exception;
    Map<String, Object> getCompensateMainInfoForStudent(Map<String, Object> paramsMap) throws Exception;

//    void insertCompensateMain(Map<String, Object> paramsMap) throws Exception;

//    void updateCompensateMainForRecord(Map<String, Object> paramsMap) throws Exception;

    void updateStudentScoreOverall(Compensate compensate, UserSchool userSchool) throws Exception;

    CompensateMain getCompensateMainByUser(Map<String, Object> paramsMap) throws Exception;
}
