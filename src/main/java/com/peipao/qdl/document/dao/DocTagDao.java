package com.peipao.qdl.document.dao;


import com.peipao.qdl.document.model.DocTag;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 方法名称：DocTagDao
 * 功能描述：
 * 作者：Na Jun
 * 版本：1.0
 * 创建日期：2017/10/17 13:28
 * 修订记录：
 */

@Repository
public interface DocTagDao {

    //2.2 新增-保存文章草稿
    void insertDocTagByBatch(@Param("docId") Long docId, @Param("docTagList") List<DocTag> docTagList) throws Exception;

    List<Map<String, Object>> getTagTagNameList(@Param("docIdList") List<Long> docIdList) throws Exception;

    void updateDocTagLogicDelete(@Param("docId") Long docId, @Param("LogicDelete") Integer LogicDelete) throws Exception;

    void deleteDocTag(@Param("docId") Long docId) throws Exception;
}
