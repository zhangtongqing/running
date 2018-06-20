package com.peipao.qdl.running.service;

import com.peipao.framework.page.MyPageInfo;


import java.util.Map;

/**
 * 方法名称：RecordService
 * 功能描述：RecordService
 * 作者：Liu Fan
 * 版本：2.0.10
 * 创建日期：2018/01/15 16:56
 * 修订记录：
 */

public interface RecordService {
    MyPageInfo getMyRunningRecord(Map<String, Object> paramsMap, int[] page) throws Exception;
    MyPageInfo getStudentRunningList(Map<String, Object> paramsMap, int pageindex, int pagesize) throws Exception;
    MyPageInfo getStudentActivityRunningList(Map<String, Object> paramsMap, int pageindex, int pagesize) throws Exception;
    MyPageInfo getMyMorningExercisesRecord(Map<String, Object> paramsMap, int[] page);
}
