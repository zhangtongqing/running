package com.peipao.qdl.discover.dao;


import com.peipao.qdl.discover.model.DiscoverComment;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

/**
 * 方法名称：DiscoverCommentDao
 * 功能描述：DiscoverCommentDao
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/10/17 13:31
 * 修订记录：
 */
@Repository
public interface DiscoverCommentDao {

    List<Map<String, Object>> getCommentListByDiscoverId(Map<String, Object> paramsMap) throws Exception;

    void updateForCommentDelete(Map<String, Object> paramsMap) throws Exception;

    DiscoverComment getDiscoverCommentById(Long commentId) throws Exception;

    void insertDiscoverComment(DiscoverComment discoverComment) throws Exception;
}
