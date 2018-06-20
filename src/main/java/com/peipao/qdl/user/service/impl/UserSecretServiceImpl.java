package com.peipao.qdl.user.service.impl;


import com.peipao.qdl.user.dao.UserSecretDao;
import com.peipao.qdl.user.model.UserSecret;
import com.peipao.qdl.user.service.UserSecretService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 方法名称：UserSecretServiceImpl
 * 功能描述：UserSecretServiceImpl
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/10/19 14:36
 * 修订记录：
 */
@Service
public class UserSecretServiceImpl implements UserSecretService {

    @Autowired
    private UserSecretDao userSecretDao;

    @Override
    public UserSecret getUserSecretByUserId(Long userId) throws Exception {
        return userSecretDao.getUserSecretByUserId(userId);
    }

	@Override
	public void updateUserSecretByUserId(Map<String,String> map) throws Exception {
		userSecretDao.updateUserSecretByUserId(map);
	}

	@Override
	public void insertUserSecret(UserSecret userSecret) throws Exception {
		userSecretDao.insertUserSecret(userSecret);
	}

}
