package com.peipao.qdl.discover.dao;


import com.peipao.qdl.discover.model.DiscoverUpvote;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

/**
 * 方法名称：DiscoverUpvoteDao
 * 功能描述：DiscoverUpvoteDao
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/10/17 13:31
 * 修订记录：
 */
@Repository
public interface DiscoverUpvoteDao {

    int getIfUserUpvoteTheDiscover(@Param("discoverId") Long discoverId, @Param("userId") Long userId) throws Exception;

    List<Map<String, String>> getTop5UserImgs(@Param("discoverId") Long discoverId) throws Exception;

    List<Map<String, Object>> getUpvoteByDiscoverId(@Param("discoverId") Long discoverId) throws Exception;

    void insertDiscoverUpvote(@Param("discoverUpvote") DiscoverUpvote discoverUpvote) throws Exception;

    void deleteDiscoverUpvote(@Param("discoverId") Long discoverId, @Param("userId") Long userId) throws Exception;
}
