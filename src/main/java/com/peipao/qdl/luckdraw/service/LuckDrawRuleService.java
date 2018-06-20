package com.peipao.qdl.luckdraw.service;


import com.peipao.qdl.luckdraw.model.LuckDrawRule;

import java.util.Map;

/**
 * 方法名称：LuckDrawRuleService
 * 功能描述：LuckDrawRuleService
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/12/19 11:33
 * 修订记录：
 */
public interface LuckDrawRuleService {
    LuckDrawRule getLuckDrawRuleByActivityId(Map<String, Object> paramsMap) throws Exception;

    void addLuckDrawRule(Long userId, LuckDrawRule luckDrawRule) throws Exception;

    LuckDrawRule getLuckDrawById(Long ruleId) throws Exception;

    void updateLuckDrawRule(Long userId, LuckDrawRule luckDrawRule) throws Exception;
}
