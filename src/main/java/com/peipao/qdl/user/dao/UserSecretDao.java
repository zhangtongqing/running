package com.peipao.qdl.user.dao;

import com.peipao.qdl.user.model.UserSecret;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.Map;

/**
 * 方法名称：UserSecretDao
 * 功能描述：UserSecretDao
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/10/17 13:31
 * 修订记录：
 */
@Repository
public interface UserSecretDao {


    UserSecret getUserSecretByUserId(@Param("userId") Long userId) throws Exception;
    
    void updateUserSecretByUserId(Map<String, String> map) throws Exception;
    
    void insertUserSecret(@Param("userSecret") UserSecret userSecret) throws Exception;
}
