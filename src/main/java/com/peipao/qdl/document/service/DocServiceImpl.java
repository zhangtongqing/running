package com.peipao.qdl.document.service;


import com.peipao.framework.util.FileUtil;
import com.peipao.framework.util.StringUtil;
import com.peipao.qdl.document.dao.*;
import com.peipao.qdl.document.model.Doc;
import com.peipao.qdl.document.model.DocComment;
import com.peipao.qdl.document.model.DocImg;
import com.peipao.qdl.document.model.DocTag;
import net.sf.json.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 方法名称：DiscoverServiceImpl
 * 功能描述：DiscoverServiceImpl
 * 作者：Na Jun
 * 版本：1.0
 * 创建日期：2017/10/17 13:31
 * 修订记录：
 */
@Service
public class DocServiceImpl implements DocService {

    @Autowired
    private DocDao docDao;
    @Autowired
    private DocImgDao docImgDao;
    @Autowired
    private DocTagDao docTagDao;
    @Autowired
    private DocUpvoteDao docUpvoteDao;
    @Autowired
    private DocCommentDao docCommentDao;
    @Autowired
    private DocBrowseDao docBrowseDao;

    String ENCODE = "utf-8";

    @Override
    @Transactional
    public void saveDocumentOnly(Doc doc, List<DocImg> docImgList, List<DocTag> docTagList) throws Exception {


        if( doc.getDocId() == null ) {      //新增文章

            docDao.insertDoc(doc);

            if (doc.getDocId() != null) {

                String documentPath = getClass().getResource("/").getFile().toString() + "../../data/";
                //System.out.println( documentPath );

                String destFile = doc.getDocId() + ".html";
                String fullDestFile = FileUtil.createFilePath(documentPath, destFile);

                doc.setDocUrl(fullDestFile);

                docDao.updateDocDocUrl(doc.getDocId(), fullDestFile);

                if (!CollectionUtils.isEmpty(docImgList)) {
                    docImgDao.insertDocImgByBatch(doc.getDocId(), docImgList);
                }

                if (!CollectionUtils.isEmpty(docTagList)) {
                    docTagDao.insertDocTagByBatch(doc.getDocId(), docTagList);
                }

                doc.setCreateUserName(docDao.getUserNameByUserId(doc.getCreateUserId()));
            }
        }
        else {      //编辑文章

            String documentPath = getClass().getResource("/").getFile().toString() + "../../data/";
            //System.out.println( documentPath );

            String destFile = doc.getDocId() + ".html";
            String fullDestFile = FileUtil.createFilePath(documentPath, destFile);

            doc.setDocUrl(fullDestFile);

            docDao.updateDoc(doc);

            docImgDao.deleteDocImg(doc.getDocId());
            docTagDao.deleteDocTag(doc.getDocId());

            if (!CollectionUtils.isEmpty(docImgList)) {
                docImgDao.insertDocImgByBatch(doc.getDocId(), docImgList);
            }

            if (!CollectionUtils.isEmpty(docTagList)) {
                docTagDao.insertDocTagByBatch(doc.getDocId(), docTagList);
            }
        }
    }

    @Override
    public void saveDocumentToFs(Doc doc,String content) throws Exception {

        //FSystem.out.println(classPath);ileUtil.createFilePath(String tempPath, String fileName);
        //String str = new String();
        //String classPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        //File file = new File("");
        //System.out.println(file.getAbsolutePath());

        String templateFile = getClass().getResource("/").getFile().toString() + "../../static/template/index.html";
        //System.out.println( templateFile );

        JSONObject jsonObj = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        jsonObj.put("docTitle", StringUtil.isNotEmpty(doc.getDocTitle()) ?  doc.getDocTitle() : "");
        jsonObj.put("updateTime", sdf.format(doc.getUpdateTime()));
        jsonObj.put("content", StringUtil.isNotEmpty(content) ?  content : "");

        FileUtil.createHtmlFile(templateFile, doc.getDocUrl(), jsonObj);
    }

    @Override
    @Transactional
    public List<Map<String, Object>> getDocExtraInfo(Long docId) throws Exception {
        //System.out.println("***************************");
        //System.out.println(docId);
        List<Map<String, Object>> docList = docDao.selectDocList(
                null,null,
                null,null,
                null,null,
                null,null,
                0,1,docId);
        //System.out.println(docList.get(0).get("docTitle"));

        if( !CollectionUtils.isEmpty(docList) && docList.size() > 0 ) {
            List<Long> docIdList = new ArrayList<Long>();
            docIdList.add(docId);
            //System.out.println(docIdList.size());
            List<Map<String, Object>> tagList = docTagDao.getTagTagNameList(docIdList);
            //System.out.println(tagList.size());
            //System.out.println(tagList.get(0).get("tagName"));

            docList.addAll(tagList);
        }

        return docList;
    }

    @Override
    @Transactional
    public List<Map<String, Object>> documentPublish(Long docId) throws Exception {
        //System.out.println("***************************");
        //System.out.println(docId);
        List<Map<String, Object>> docList = docDao.selectDocList(
                null,null,
                null,null,
                null,null,
                null,null,
                0,1,docId);
        //System.out.println(docList.get(0).get("docTitle"));

        return docList;
    }

    @Override
    @Transactional
    public Map<String, Object> getDocExtraInfoByDocId(@Param("userId") Long userId, @Param("docId") Long docId ) throws Exception {

        if(userId != null) {
            docDao.updateDocAmount(null, 1, null, null, docId);
            docBrowseDao.insertDocBrowse(docId, userId, new Date());
            return docDao.getDocExtraInfoByDocId(userId, docId);
        }
        else {
            return docDao.getDocExtraInfoByDocIdShare(docId);
        }
    }

    @Override
    @Transactional
    public Map<String, Object> upvote(@Param("docId") Long docId, @Param("userId") Long userId, @Param("createTime") Date createTime) throws Exception {

        docUpvoteDao.insertDocUpvote(docId,userId,createTime);

        docDao.updateDocAmount(1,null, null,null, docId);

        return docDao.getDocExtraInfoByDocId(userId,docId);
    }

    @Override
    @Transactional
    public Map<String, Object> cancelUpvote(@Param("docId") Long docId, @Param("userId") Long userId) throws Exception {

        docUpvoteDao.deleteDocUpvote(docId,userId);

        docDao.updateDocAmount(-1,null, null,null, docId);

        return docDao.getDocExtraInfoByDocId(userId,docId);
    }

    @Override
    @Transactional
    public void saveComment(@Param("DocComment") DocComment docComment) throws Exception {

        docCommentDao.insertDocComment(docComment);

        docDao.updateDocAmount(null,null, 1,null, docComment.getDocId());

        Map<String, Object> mapList = docCommentDao.getUserExtraInfoByUserId(docComment.getUserId());

        if( !CollectionUtils.isEmpty(mapList) && mapList.size() > 0 ) {
            docComment.setUsername((String)mapList.get("userName"));
            docComment.setSchoolName((String)mapList.get("schoolName"));
            docComment.setUserImg((String)mapList.get("imageUrl"));
        }
        else {
            docComment.setUsername("");
            docComment.setSchoolName("");
            docComment.setUserImg("");
        }
    }

    @Override
    @Transactional
    public void delComment(@Param("commentId") Long commentId, @Param("userId") Long userId) throws Exception {

        Long docId = docCommentDao.getDocIdByCommentId(commentId);

        docCommentDao.updateDocCommentLogicDeleteById(1,commentId,userId);

        docDao.updateDocAmount(null,null, -1,null, docId);
    }

    @Override
    @Transactional
    public List<Map<String, Object>> getTagTagNameList(@Param("docIdList") List<Long> docIdList) throws Exception {

        return docTagDao.getTagTagNameList(docIdList);
    }

    @Override
    @Transactional
    public List<Map<String, Object>> getImgImgUrlListByDocId(@Param("docIdList") List<Long> docIdList) throws Exception {

        return docImgDao.getImgImgUrlListByDocId(docIdList);
    }

    @Override
    @Transactional
    public List<Map<String, Object>> getDocTop() throws Exception {

        return docDao.getDocTopNum(1);
    }

    @Override
    @Transactional
    public List<Map<String, Object>> getCommentList(@Param("docId") Long docId, @Param("from") Integer from, @Param("num") Integer num) throws Exception {

        return docCommentDao.getCommentList(docId,from,num);
    }

    @Override
    @Transactional
    public Long getDocListTotal(@Param("docId") Long docId) throws Exception {

        return docDao.getDocListTotal(docId);
    }

    @Override
    @Transactional
    public List<Map<String, Object>> getDocList(@Param("userId") Long userId, @Param("from") Integer from, @Param("num") Integer num) throws Exception {

        return docDao.getDocList(userId,from,num);
    }

    @Override
    @Transactional
    public List<Map<String, Object>> getCommentListPc(@Param("docId") Long docId, @Param("from") Integer from, @Param("num") Integer num) throws Exception {

        return docCommentDao.selectCommentList(docId,from,num);
    }

    @Override
    @Transactional
    public Long getDocCommentListTotal(@Param("docId") Long docId) throws Exception {

        return docCommentDao.getDocCommentListTotal(docId);
    }

    @Override
    @Transactional
    public Long getDocBrowseListTotal(@Param("docId") Long docId) throws Exception {

        return docBrowseDao.getDocBrowseListTotal(docId);
    }

    @Override
    @Transactional
    public List<Map<String, Object>> getReadListPc(@Param("docId") Long docId, @Param("from") Integer from, @Param("num") Integer num) throws Exception {

        return docBrowseDao.selectBrowseList(docId,from,num);
    }

    @Override
    @Transactional
    public Long getDocUpvoteListTotal(@Param("docId") Long docId) throws Exception {

        return docUpvoteDao.getDocUpvoteListTotal(docId);
    }

    @Override
    @Transactional
    public List<Map<String, Object>> getUpvoteListPc(@Param("docId") Long docId, @Param("from") Integer from, @Param("num") Integer num) throws Exception {

        return docUpvoteDao.selectUpvoteList(docId,from,num);
    }

    @Override
    @Transactional
    public List<Map<String, Object>> documentList(
            @Param("startTime") String startTime, @Param("endTime") String endTime,
            @Param("publishState") Integer publishState, @Param("queryString") String queryString,
            @Param("readAmountSort") Integer readAmountSort, @Param("upvoteAmountSort") Integer upvoteAmountSort,
            @Param("commentAmountSort") Integer commentAmountSort, @Param("updateTimeSort") Integer updateTimeSort,
            @Param("from") Integer from, @Param("num") Integer num
    ) throws Exception {

        return docDao.selectDocList(
                startTime,endTime,
                publishState,queryString,
                readAmountSort,upvoteAmountSort,
                commentAmountSort,updateTimeSort,
                from,num,null);
    }

    @Override
    @Transactional
    public Long selectDocListTotal(
            @Param("startTime") String startTime, @Param("endTime") String endTime,
            @Param("publishState") Integer publishState, @Param("queryString") String queryString
    ) throws Exception {

        return docDao.selectDocListTotal(startTime,endTime,publishState,queryString);
    }

    @Override
    @Transactional
    public void publishDocument(@Param("docId") Long docId) throws Exception {

//        删除磁盘上的文章
//        String documentPath = getClass().getResource("/").getFile().toString() + "../../data/";
//        String destFile = doc.getDocTitle() + "-" + String.valueOf(doc.getUpdateTime().getTime()) + ".html";
//        String fullDestFile = FileUtil.createFilePath(documentPath, destFile);

        docDao.updateDocPublishState(docId,1, new Date());
    }

    @Override
    @Transactional
    public void deleteDocument(@Param("docId") Long docId) throws Exception {

        docDao.updateDocLogicDelete(docId,1);
        docCommentDao.updateDocCommentLogicDelete(docId,1);
        docBrowseDao.updateDocBrowseLogicDelete(docId,1);
        docTagDao.updateDocTagLogicDelete(docId,1);
    }

    @Override
    @Transactional
    public Map<String, Object> getDocExtraInfoForEdit(@Param("docId") Long docId) throws Exception {

        return docDao.getDocExtraInfoForEdit(docId);
    }

    @Override
    @Transactional
    public String getDocContent(@Param("docUrl") String docUrl) throws Exception {
        //System.out.println( docUrl );
        String content = "";

        try {
            BufferedReader bis = new BufferedReader(new InputStreamReader(new FileInputStream( new File(docUrl)), ENCODE) );
            String szTemp;

            while ( (szTemp = bis.readLine()) != null) {
                content += szTemp + "\n";
            }
            bis.close();
        }
        catch( Exception e ) {

            return content;
        }

        try {
            Parser parser = new Parser(content);
            parser.setEncoding(ENCODE);

            NodeFilter filter = new HasAttributeFilter( "id", "text" );
            NodeList nodes = parser.extractAllNodesThatMatch(filter);

            if(nodes != null && nodes.size() > 0)
                content = nodes.elementAt(0).toPlainTextString();


        } catch (ParserException e) {   //捕捉parser的异常
            e.printStackTrace();
        }

        //System.out.println( content );

        return content;
    }
}
