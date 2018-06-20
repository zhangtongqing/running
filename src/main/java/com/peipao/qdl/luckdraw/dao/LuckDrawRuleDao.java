package com.peipao.qdl.luckdraw.dao;


import com.peipao.qdl.luckdraw.model.LuckDrawRule;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * 方法名称：
 * 功能描述：
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/12/19 11:22
 * 修订记录：
 */
@Repository
public interface LuckDrawRuleDao {

    void addLuckDrawRule(LuckDrawRule luckDrawRule) throws Exception;

    LuckDrawRule getLuckDrawById(@Param("ruleId") Long ruleId) throws Exception;

    void updateLuckDrawRule(LuckDrawRule luckDrawRule) throws Exception;

    LuckDrawRule getLuckDrawRuleByActivityId(Map<String, Object> paramsMap) throws Exception;
    void logicDeleteLuckDrawRuleByActivityId(Map<String, Object> paramsMap) throws Exception;

}
