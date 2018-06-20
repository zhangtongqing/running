package com.peipao.qdl.document.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 方法名称：DocBrowseDao
 * 功能描述：
 * 作者：Na Jun
 * 版本：1.0
 * 创建日期：2017/10/17 13:28
 * 修订记录：
 */

@Repository
public interface DocBrowseDao {

    Long getDocBrowseListTotal(@Param("docId") Long docId) throws Exception;
    List<Map<String, Object>> selectBrowseList(@Param("docId") Long docId, @Param("from") Integer from, @Param("num") Integer num) throws Exception;

    void updateDocBrowseLogicDelete(@Param("docId") Long docId, @Param("LogicDelete") Integer LogicDelete) throws Exception;

    void insertDocBrowse(@Param("docId") Long docId, @Param("userId") Long userId, @Param("createTime") Date createTime) throws Exception;
}
