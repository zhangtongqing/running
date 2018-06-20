package com.peipao.framework.util;

import com.peipao.qdl.user.dao.UserDao;
import com.peipao.qdl.user.service.UserCacheService;
import com.peipao.qdl.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

/**
 * 方法名称：
 * 功能描述：
 * 作者：Liu Fan
 * 版本：
 * 创建日期：2017/9/11 16:36
 * 修订记录：
 */
@Component
public class ServiceUtils {
    @Autowired
    public UserDao userDao;

    @Autowired
    public UserCacheService userCacheService;

    public static ServiceUtils serviceUtils;

    @PostConstruct
    public void init() {
        serviceUtils = this;
    }
}