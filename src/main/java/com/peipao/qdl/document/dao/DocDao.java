package com.peipao.qdl.document.dao;


import com.peipao.qdl.document.model.Doc;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 方法名称：DocDao
 * 功能描述：
 * 作者：Na Jun
 * 版本：1.0
 * 创建日期：2017/10/17 13:28
 * 修订记录：
 */

@Repository
public interface DocDao {

    //得到 doc_id 的总数
    Long getDocListTotal(@Param("docId") Long docId) throws Exception;

    //根据 user_id 得到此 userId 在 t_doc_upvote 中的记录的 doc_id
    List<Map<String, Object>> getDocList(@Param("from") Integer from, @Param("num") Integer num) throws Exception;

    //根据 doc_id 得到此 doc_id 在 t_doc 中的记录的 阅读量,评论数,点赞数
    Map<String, Object> getDocExtraInfoByDocId(@Param("docId") Long docId) throws Exception;

    //2.2 新增-保存文章草稿
    //2.3 新增-保存并发布
    void insertDoc(@Param("doc") Doc doc) throws Exception;
    String getUserNameByUserId(@Param("userId") Long userId) throws Exception;

    //2.1 查询-文章列表
    //2.4 查询-根据文章id获得创建者阅读量等数据
    List<Map<String, Object>> selectDocList(
            @Param("startTime") String startTime, @Param("endTime") String endTime,
            @Param("publishState") Integer publishState, @Param("queryString") String queryString,
            @Param("readAmountSort") Integer readAmountSort, @Param("upvoteAmountSort") Integer upvoteAmountSort,
            @Param("commentAmountSort") Integer commentAmountSort, @Param("updateTimeSort") Integer updateTimeSort,
            @Param("from") Integer from, @Param("num") Integer num,
            @Param("docId") Long docId
    ) throws Exception;

    Long selectDocListTotal(
            @Param("startTime") String startTime, @Param("endTime") String endTime,
            @Param("publishState") Integer publishState, @Param("queryString") String queryString
    ) throws Exception;

    Map<String, Object> getDocExtraInfoByDocId(@Param("userId") Long userId, @Param("docId") Long docId) throws Exception;

    void updateDocAmount(
            @Param("upvoteAmount") Integer upvoteAmount, @Param("readAmount") Integer readAmount,
            @Param("commentAmount") Integer commentAmount, @Param("forwardAmount") Integer forwardAmount,
            @Param("docId") Long docId) throws Exception;

     List<Map<String, Object>>getDocTopNum(@Param("num") Integer num) throws Exception;

    List<Map<String, Object>> getDocList(@Param("userId") Long userId, @Param("from") Integer from, @Param("num") Integer num) throws Exception;

    void updateDocPublishState(@Param("docId") Long docId, @Param("publishState") Integer publishState, @Param("updateTime") Date updateTime) throws Exception;

    void updateDocLogicDelete(@Param("docId") Long docId, @Param("LogicDelete") Integer LogicDelete) throws Exception;

    void updateDocDocUrl(@Param("docId") Long docId, @Param("docUrl") String docUrl) throws Exception;

    void updateDoc(@Param("doc") Doc doc) throws Exception;

    Map<String, Object> getDocExtraInfoForEdit(@Param("docId") Long docId) throws Exception;

    Map<String, Object> getDocExtraInfoByDocIdShare(@Param("docId") Long docId) throws Exception;

}
