package com.peipao.qdl.discover.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 方法名称：DiscoverReportDetailDao
 * 功能描述：DiscoverReportDetailDao
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/10/17 13:31
 * 修订记录：
 */
@Repository
public interface DiscoverReportDetailDao {
    int getIfUserReportTheDiscover(@Param("discoverId") Long discoverId, @Param("userId") Long userId) throws Exception;
}
