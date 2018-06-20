package com.peipao.qdl.runningrule.dao;


import com.peipao.qdl.runningrule.model.RunningRule;
import com.peipao.qdl.runningrule.model.vo.RunningRuleVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


/**
 * 方法名称：RuleDao
 * 功能描述：RuleDao
 * 作者：Liu Fan
 * 版本：2.0.10
 * 创建日期：2018/01/12 15:18
 * 修订记录：
 */
@Repository
public interface RuleDao {

    List<RunningRuleVo> getRunningRuleBySchool(@Param("semesterId") Long semesterId) throws Exception;

    void insertRunningRule(@Param("runningRule") RunningRule runningRule);

    void updateRunningRule(@Param("runningRule") RunningRule runningRule);

//    List<RunningRuleVo> getRunningRuleBySchool(Map<String, Object> paramsMap) throws Exception;
//    Long getRuleIdBySchoolAndType(Map<String, Object> paramsMap) throws Exception;
//    List<Map<String, Object>> getSchoolTargetBySemesterId(@Param("semesterId") long semesterId);
//    Integer getSportCountMaxByRunningType(Map<String, Object> paramsMap) throws Exception;



}
