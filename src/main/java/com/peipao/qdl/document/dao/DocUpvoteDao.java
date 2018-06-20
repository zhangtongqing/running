package com.peipao.qdl.document.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 方法名称：DocUpvoteDao
 * 功能描述：
 * 作者：Na Jun
 * 版本：1.0
 * 创建日期：2017/10/17 13:28
 * 修订记录：
 */

@Repository
public interface DocUpvoteDao {

    //根据 user_id 得到此 userId 在 t_doc_upvote 中的记录的 doc_id
    List<Map<String, Object>> getUpvoteDocIdListByUserId(@Param("userId") Long userId, @Param("docIdList") List<Long> docIdList) throws Exception;

    void insertDocUpvote(@Param("docId") Long docId, @Param("userId") Long userId, @Param("createTime") Date createTime) throws Exception;

    void deleteDocUpvote(@Param("docId") Long docId, @Param("userId") Long userId) throws Exception;

    Long getDocUpvoteListTotal(@Param("docId") Long docId) throws Exception;
    List<Map<String, Object>> selectUpvoteList(@Param("docId") Long docId, @Param("from") Integer from, @Param("num") Integer num) throws Exception;
}
