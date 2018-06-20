package com.peipao.framework.log.dao;


import com.peipao.framework.log.model.SystemLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 方法名称：SystemLogAdminDao
 * 功能描述：SystemLogAdminDao
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/8/3 16:41
 * 修订记录：
 */
@Repository
public interface SystemLogAdminDao {
    void insertSystemLogAdmin(@Param("systemLog") SystemLog systemLog) throws Exception;
}
