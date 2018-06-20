package com.peipao.qdl.user.service;


import com.peipao.qdl.user.model.UserSecret;
import java.util.Map;

/**
 * 方法名称：UserSecretService
 * 功能描述：UserSecretService
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/10/19 14:36
 * 修订记录：
 */
public interface UserSecretService {

    UserSecret getUserSecretByUserId(Long userId) throws Exception;

    void updateUserSecretByUserId(Map<String, String> map) throws Exception;
    
    void insertUserSecret(UserSecret userSecret) throws Exception;
    
}
