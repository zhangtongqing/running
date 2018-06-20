package com.peipao.qdl.document.dao;


import com.peipao.qdl.document.model.DocImg;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

/**
 * 方法名称：DocImgDao
 * 功能描述：
 * 作者：Na Jun
 * 版本：1.0
 * 创建日期：2017/10/17 13:28
 * 修订记录：
 */

@Repository
public interface DocImgDao {

    List<Map<String, Object>> getImgImgUrlListByDocId(@Param("docIdList") List<Long> docIdList) throws Exception;

    //2.2 新增-保存文章草稿
    void insertDocImgByBatch(@Param("docId") Long docId, @Param("docImgList") List<DocImg> docImgList) throws Exception;

    void deleteDocImg(@Param("docId") Long docId) throws Exception;
}
