package com.peipao.qdl.document.dao;


import com.peipao.qdl.document.model.DocComment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

/**
 * 方法名称：DocCommentDao
 * 功能描述：
 * 作者：Na Jun
 * 版本：1.0
 * 创建日期：2017/10/17 13:28
 * 修订记录：
 */

@Repository
public interface DocCommentDao {

    void insertDocComment(@Param("docComment") DocComment docComment) throws Exception;
    Map<String, Object> getUserExtraInfoByUserId(@Param("userId") Long userId) throws Exception;

    void updateDocCommentLogicDeleteById(@Param("LogicDelete") Integer LogicDelete, @Param("commentId") Long commentId, @Param("userId") Long userId) throws Exception;
    Long getDocIdByCommentId(@Param("commentId") Long commentId) throws Exception;

    List<Map<String, Object>> getCommentList(@Param("docId") Long docId, @Param("from") Integer from, @Param("num") Integer num) throws Exception;

    Long getDocCommentListTotal(@Param("docId") Long docId) throws Exception;
    List<Map<String, Object>> selectCommentList(@Param("docId") Long docId, @Param("from") Integer from, @Param("num") Integer num) throws Exception;

    void updateDocCommentLogicDelete(@Param("docId") Long docId, @Param("LogicDelete") Integer LogicDelete) throws Exception;
}
