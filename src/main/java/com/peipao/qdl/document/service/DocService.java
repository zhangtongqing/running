package com.peipao.qdl.document.service;


import com.peipao.qdl.document.model.Doc;
import com.peipao.qdl.document.model.DocComment;
import com.peipao.qdl.document.model.DocImg;
import com.peipao.qdl.document.model.DocTag;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 方法名称：DocService
 * 功能描述：DocService
 * 作者：Na Jun
 * 版本：1.0
 * 创建日期：2017/10/17 13:31
 * 修订记录：
 */
public interface DocService {

    //String templateFile = "E:\\najun\\items\\schoolAPI\\trunk\\src\\main\\webapp\\static\\template\\index.html";
    //String documentPath = "E:\\najun\\data\\";

    void saveDocumentOnly(Doc doc, List<DocImg> docImgList, List<DocTag> docTagList) throws Exception;
    void saveDocumentToFs(Doc doc, String content) throws Exception;

    /*List<Map<String, Object>> getDocExtraInfo(
            String startTime,String endTime,
            Integer publishState,String queryString,
            Integer readAmountSort,Integer upvoteAmountSort,
            Integer commentAmountSort,Integer updateTimeSort,
            Long docId
    ) throws Exception;*/

    List<Map<String, Object>> getDocExtraInfo(Long docId) throws Exception;

    Map<String, Object> getDocExtraInfoByDocId(@Param("userId") Long userId, @Param("docId") Long docId) throws Exception;

    Map<String, Object> upvote(@Param("docId") Long docId, @Param("userId") Long userId, @Param("createTime") Date createTime) throws Exception;

    Map<String, Object> cancelUpvote(@Param("docId") Long docId, @Param("userId") Long userId) throws Exception;

    void saveComment(@Param("DocComment") DocComment docComment) throws Exception;

    void delComment(@Param("commentId") Long commentId, @Param("userId") Long userId) throws Exception;

    List<Map<String, Object>> getDocTop() throws Exception;
    List<Map<String, Object>> getTagTagNameList(@Param("docIdList") List<Long> docIdList) throws Exception;
    List<Map<String, Object>> getImgImgUrlListByDocId(@Param("docIdList") List<Long> docIdList) throws Exception;

    List<Map<String, Object>> getCommentList(@Param("docId") Long docId, @Param("from") Integer from, @Param("num") Integer num) throws Exception;

    Long getDocListTotal(@Param("docId") Long docId) throws Exception;

    List<Map<String, Object>> getDocList(@Param("userId") Long userId, @Param("from") Integer from, @Param("num") Integer num) throws Exception;

    Long getDocCommentListTotal(@Param("docId") Long docId) throws Exception;
    List<Map<String, Object>> getCommentListPc(@Param("docId") Long docId, @Param("from") Integer from, @Param("num") Integer num) throws Exception;

    Long getDocBrowseListTotal(@Param("docId") Long docId) throws Exception;
    List<Map<String, Object>> getReadListPc(@Param("docId") Long docId, @Param("from") Integer from, @Param("num") Integer num) throws Exception;

    Long getDocUpvoteListTotal(@Param("docId") Long docId) throws Exception;
    List<Map<String, Object>> getUpvoteListPc(@Param("docId") Long docId, @Param("from") Integer from, @Param("num") Integer num) throws Exception;

    List<Map<String, Object>> documentList(
            @Param("startTime") String startTime, @Param("endTime") String endTime,
            @Param("publishState") Integer publishState, @Param("queryString") String queryString,
            @Param("readAmountSort") Integer readAmountSort, @Param("upvoteAmountSort") Integer upvoteAmountSort,
            @Param("commentAmountSort") Integer commentAmountSort, @Param("updateTimeSort") Integer updateTimeSort,
            @Param("from") Integer from, @Param("num") Integer num
    ) throws Exception;

    Long selectDocListTotal(
            @Param("startTime") String startTime, @Param("endTime") String endTime,
            @Param("publishState") Integer publishState, @Param("queryString") String queryString
    ) throws Exception;

    void publishDocument(@Param("docId") Long docId) throws Exception;

    void deleteDocument(@Param("docId") Long docId) throws Exception;

    Map<String, Object> getDocExtraInfoForEdit(@Param("docId") Long docId) throws Exception;

    String getDocContent(@Param("docUrl") String docUrl) throws Exception;

    List<Map<String, Object>> documentPublish(Long docId) throws Exception;
}
