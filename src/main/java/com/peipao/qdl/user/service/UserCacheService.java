package com.peipao.qdl.user.service;

import com.peipao.qdl.user.dao.UserDao;
import com.peipao.qdl.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserCacheService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisTemplate redisTemplate;

    @Transactional
    @CachePut(value = "peipao", key = "'userId'+#user.getUserId()")
    public User updateUser(User user) throws Exception {
        userDao.updateUser(user);
        return userDao.getUserById(user.getUserId());
    }

    @Transactional
    @CachePut(value = "peipao", key = "'userId'+#user.getUserId()")
    public User insertUser(User user) throws Exception {
        userDao.insertUser(user);
        return user;
    }

    @Transactional
    @CachePut(value = "peipao", key = "'userId'+#userId")
    public User updateUserPassWord(Long userId, String enPassword) throws Exception {
        userDao.updatePassword(userId, enPassword);
        return userDao.getUserById(userId);
    }

    @Cacheable(value = "peipao", key = "'userId'+#userId")
    public User getUserById(Long userId) throws Exception {
        return userDao.getUserById(userId);
    }



    @CacheEvict(value = "peipao", key = "'userId'+#id")
    @Transactional
    public void deleteUserById(Long id) throws Exception {
        userDao.deleteUserById(id);
    }


    /**
     * 批量删除对应的value
     * @param keys
     */
    public void removeByKeys(final String... keys) {
        for (String key : keys) {
            if(exists(key)) {
                redisTemplate.delete(key);
            }
        }
    }

    /**
     * 判断缓存中是否有对应的value
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }


}
