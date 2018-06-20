package com.peipao.qdl.document.controller;


import com.peipao.framework.annotation.Register;
import com.peipao.framework.annotation.SystemControllerLog;
import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.model.Response;
import com.peipao.framework.util.DateUtil;
import com.peipao.framework.util.StringUtil;
import com.peipao.framework.util.ValidateUtil;
import com.peipao.qdl.document.model.Doc;
import com.peipao.qdl.document.model.DocImg;
import com.peipao.qdl.document.model.DocTag;
import com.peipao.qdl.document.service.DocService;
import com.peipao.qdl.user.service.UserSecretService;
import com.peipao.qdl.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * 方法名称：DocumentControllerPC
 * 功能描述：发现-精品阅读实体类
 * 作者：Na Jun
 * 版本：1.0
 * 创建日期：2017/10/17 13:31
 * 修订记录：
 */
@Api(
        value = "/document/pc",
        description = "发现-精品阅读(pc接口)"
)
@RestController
@RequestMapping({"/document/pc"})
public class DocumentControllerPC {

    @Autowired
    private DocService docService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserSecretService userSecretService;

    @RequestMapping(value = {"/documentList"},method = {RequestMethod.POST})
    //@SystemControllerLog(description = "进行查询文章列表操作")
    @ApiOperation(value = "2.1 查询-文章列表")
    public Response documentList(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "开始日期:结束日期:发布状态:文章标题搜索关键字:阅读量排序:点赞量排序:评论量排序:发布/保存时间-排序:页码:每页条数(默认15)") @RequestBody JSONObject json
    ) throws Exception {

        String[] params = new String[]{"pageindex"};    //必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);   //必填项请求参数不完整，请检查
        }

        Long pageSize = new Long(15);
        if(json.containsKey("pagesize") && null != json.get("pagesize") && StringUtil.isNotEmpty(json.getString("pagesize"))) {
            pageSize = json.getLong("pagesize");
        }

        Integer pageIndex = json.getInt("pageindex");

        String startTime = null;
        if( ValidateUtil.jsonValidateWithKey(json, "startTime") ) {
            startTime = json.getString("startTime");
        }
        String endTime = null;
        if( ValidateUtil.jsonValidateWithKey(json, "endTime") ) {
            endTime = json.getString("endTime");
        }
        Integer publishState = null;
        if( ValidateUtil.jsonValidateWithKey(json, "publishState") ) {
            publishState = json.getInt("publishState");
        }
        System.out.println(publishState);
        String queryString = null;
        if( ValidateUtil.jsonValidateWithKey(json, "queryString") ) {
            queryString = json.getString("queryString");
        }
        Integer readAmountSort = null;
        if( ValidateUtil.jsonValidateWithKey(json, "readAmountSort") ) {
            readAmountSort = json.getInt("readAmountSort");
        }
        Integer upvoteAmountSort = null;
        if( ValidateUtil.jsonValidateWithKey(json, "upvoteAmountSort") ) {
            upvoteAmountSort = json.getInt("upvoteAmountSort");
        }
        Integer commentAmountSort = null;
        if( ValidateUtil.jsonValidateWithKey(json, "commentAmountSort") ) {
            commentAmountSort = json.getInt("commentAmountSort");
        }
        Integer updateTimeSort = null;
        if( ValidateUtil.jsonValidateWithKey(json, "updateTimeSort") ) {
            updateTimeSort = json.getInt("updateTimeSort");
        }

        Long total = new Long(0);
        total = docService.selectDocListTotal(startTime, endTime, publishState, queryString);

        Long maxpage = new Long(1);
        if( total > pageSize ) {
            if( total%pageSize == 0) {
                maxpage = total/pageSize;
            }
            else {
                maxpage = total / pageSize + 1;
            }
        }
        else {
            maxpage = new Long(1);
        }

        List<Map<String, Object>> mapList = docService.documentList(
                startTime, endTime,
                publishState, queryString,
                readAmountSort, upvoteAmountSort,
                commentAmountSort, updateTimeSort,
                (pageIndex - 1) * pageSize.intValue(),
                pageSize.intValue());
        //System.out.println("mapList.size()=" + mapList.size());

        //封装返回信息
        JSONObject resJson = new JSONObject();
        JSONArray resJsonData = new JSONArray();

        resJson.put("pageindex", pageIndex);
        resJson.put("pagesize", pageSize);
        resJson.put("maxpage", maxpage);
        resJson.put("total", total);

        if( !CollectionUtils.isEmpty(mapList) && mapList.size() > 0 ) {

            for(int i = 0; i < mapList.size(); i++) {

                JSONObject resJsonT = new JSONObject();

                resJsonT.put("key", mapList.get(i).get("key"));
                resJsonT.put("docId", mapList.get(i).get("docId"));
                resJsonT.put("docTitle", StringUtil.isNotEmpty((String) mapList.get(i).get("docTitle")) ? (String) mapList.get(i).get("docTitle") : "");
                resJsonT.put("createUserName", StringUtil.isNotEmpty((String) mapList.get(i).get("createUserName")) ? (String) mapList.get(i).get("createUserName") : "");
                resJsonT.put("publishState", mapList.get(i).get("publishState"));
                resJsonT.put("updateTime", DateUtil.getTimeFormatText(((Date) mapList.get(i).get("updateTime")).getTime()));
                resJsonT.put("readAmount", mapList.get(i).get("readAmount"));
                resJsonT.put("upvoteAmount", mapList.get(i).get("upvoteAmount"));
                resJsonT.put("commentAmount", mapList.get(i).get("commentAmount"));
                resJsonT.put("forwardAmount", mapList.get(i).get("forwardAmount"));
                resJsonT.put("docUrl", StringUtil.isNotEmpty((String) mapList.get(i).get("docUrl")) ? (String) mapList.get(i).get("docUrl") : "");

                resJsonData.add(resJsonT);
            }
        }
        /*else{
            JSONObject resJsonT = new JSONObject();

            resJsonT.put("key", 0);
            resJsonT.put("docId", 0);
            resJsonT.put("docTitle", "");
            resJsonT.put("createUserName", "");
            resJsonT.put("publishState", 0);
            resJsonT.put("updateTime", "");
            resJsonT.put("readAmount", 0);
            resJsonT.put("upvoteAmount", 0);
            resJsonT.put("commentAmount", 0);
            resJsonT.put("docUrl", "");
            resJsonData.add(resJsonT);
        }*/

        resJson.put("data",resJsonData);

        return Response.success(resJson);
    }

    @Register
    @RequestMapping(value = {"/documentSaveOnly"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "对文章草稿进行保存操作")
    @ApiOperation(value = "2.2 新增-保存草稿")
    public Response documentSaveOnly(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "文章ID:文章标题:列表展现方式:文章内容:排序权重,默认1:文章封面图片链接(单张或多张):文章标签string数组") @RequestBody JSONObject json
    ) throws Exception {

        String[] params = new String[]{"docTitle","viewType","content","sortWeight","imgUrls","docTags"};    //必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);   //必填项请求参数不完整，请检查
        }

        Doc doc = new Doc();
        Date currTime = new Date();

        doc.setCreateTime(currTime);
        doc.setUpdateTime(currTime);
        doc.setCreateUserId(userId);
        //文章发布状态，默认0=草稿
        doc.setPublishState(0);
        //逻辑删除，默认0=未删除
        doc.setLogicDelete(0);
        doc.setReadAmount(new Long((long)0));
        doc.setUpvoteAmount(new Long((long)0));
        doc.setCommentAmount(new Long((long)0));
        doc.setDocUrl("/");
        doc.setIsHot(0);

        if(json.containsKey("docId") && null != json.get("docId") && StringUtil.isNotEmpty(json.getString("docId"))) {
            doc.setDocId(json.getLong("docId"));
        }
        else {
            doc.setDocId(null);
        }

        if(ValidateUtil.jsonValidateWithKey(json, "docTitle")) {
            doc.setDocTitle(json.getString("docTitle"));
        }

        if(ValidateUtil.jsonValidateWithKey(json, "viewType")) {
            doc.setViewType(json.getInt("viewType"));
        }

        if(ValidateUtil.jsonValidateWithKey(json, "sortWeight")) {
            doc.setSortWeight(json.getInt("sortWeight"));
        }

        List<DocImg> docImgList = new LinkedList<DocImg>();
        if(ValidateUtil.jsonValidateWithKey(json, "imgUrls")) {
            JSONArray imgUrlArray = (JSONArray) json.get("imgUrls");
            int i = 1;
            for (Object imgUrl : imgUrlArray) {
                DocImg docImg = new DocImg();
                docImg.setImgUrl(imgUrl.toString());
                docImg.setSortNum(i);
                docImgList.add(docImg);
                i++;
            }
        }

        List<DocTag> docTagList = new LinkedList<DocTag>();
        if(ValidateUtil.jsonValidateWithKey(json, "docTags")) {
            JSONArray tagArray = (JSONArray) json.get("docTags");
            for (Object tag : tagArray) {
                DocTag docTag = new DocTag();
                docTag.setTagName(tag.toString());
                docTag.setCreateTime(currTime);
                docTagList.add(docTag);

                if( tag.toString().compareTo("HOT") == 0 ) {
                    doc.setIsHot(1);
                }
            }
        }

        //相关信息写入数据库表
        docService.saveDocumentOnly(doc, docImgList, docTagList);

        //保存文章草稿到 html 并设置 docUrl
        docService.saveDocumentToFs(doc, json.getString("content"));

        //文章保存完成后，开始封装返回信息
        JSONObject resJson = new JSONObject();
        resJson.put("key", doc.getDocId());
        resJson.put("docId", doc.getDocId());
        resJson.put("docTitle", StringUtil.isNotEmpty(doc.getDocTitle()) ?  doc.getDocTitle() : "");
        resJson.put("createUserName", StringUtil.isNotEmpty(doc.getCreateUserName()) ?  doc.getCreateUserName() : "");
        resJson.put("publishState", doc.getPublishState());
        resJson.put("updateTime", DateUtil.getTimeFormatText(doc.getUpdateTime().getTime()));
        resJson.put("readAmount", doc.getReadAmount());
        resJson.put("upvoteAmount", doc.getUpvoteAmount());
        resJson.put("commentAmount", doc.getCommentAmount());
        resJson.put("docUrl", StringUtil.isNotEmpty(doc.getDocUrl()) ?  doc.getDocUrl() : "");

        //resJson.put("delPower", WebConstants.Boolean.TRUE.ordinal());//自己发布的可以删除
        //resJson.put("imgUrls", json.get("imgUrls"));//发布的动力圈图片集合
        //resJson.put("userImgUrls", new JSONArray());//点赞头像集合为空

        /* 返回信息格式
        key	            int	        文章ID(和docId同值，防止前端框架报错)
        docId	        int	        文章ID
        docTitle	    String	    文章标题
        createUserName	String	    创建人
        publishState	int	        发布状态
        updateTime	    String	    发布/保存时间
        readAmount	    int	        浏览量
        upvoteAmount	int	        点赞量
        commentAmount	int	        评论数
        docUrl	        String	    文章链接
         */

        /*Map<String, Object> userInfoMap = userService.getUserInfoForDiscover(userId);
        UserSecret userSecret = userSecretService.getUserSecretByUserId(userId);    //用户是否设置了隐私保护

        if(null != userSecret && WebConstants.Boolean.TRUE.ordinal() == userSecret.getSecretControl()) {
            resJson.put("createUserName", userInfoMap.get("nickName"));
        } else {
            resJson.put("createUserName", userInfoMap.get("username"));
        }*/

        return Response.success(resJson);
    }

    @Register
    @RequestMapping(value = {"/documentSavePublish"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "对文章进行保存和发布操作")
    @ApiOperation(value = "2.3 新增-保存并发布")
    public Response documentSavePublish(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "文章标题:列表展现方式:文章内容:排序权重,默认1:文章封面图片链接(单张或多张):文章标签string数组") @RequestBody JSONObject json
    ) throws Exception {

        String[] params = new String[]{"docTitle","viewType","content","sortWeight","imgUrls","docTags"};    //必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);   //必填项请求参数不完整，请检查
        }

        Doc doc = new Doc();
        Date currTime = new Date();

        doc.setCreateTime(currTime);
        doc.setUpdateTime(currTime);
        doc.setCreateUserId(userId);
        //文章发布状态，默认0=草稿
        doc.setPublishState(1);
        //逻辑删除，默认0=未删除
        doc.setLogicDelete(0);
        doc.setReadAmount(new Long((long)0));
        doc.setUpvoteAmount(new Long((long)0));
        doc.setCommentAmount(new Long((long)0));
        doc.setDocUrl("/");
        doc.setIsHot(0);

        if(json.containsKey("docId") && null != json.get("docId") && StringUtil.isNotEmpty(json.getString("docId"))) {
            doc.setDocId(json.getLong("docId"));
        }
        else {
            doc.setDocId(null);
        }

        if(ValidateUtil.jsonValidateWithKey(json, "docTitle")) {
            doc.setDocTitle(json.getString("docTitle"));
        }

        if(ValidateUtil.jsonValidateWithKey(json, "viewType")) {
            doc.setViewType(json.getInt("viewType"));
        }

        if(ValidateUtil.jsonValidateWithKey(json, "sortWeight")) {
            doc.setSortWeight(json.getInt("sortWeight"));
        }

        List<DocImg> docImgList = new LinkedList<DocImg>();
        if(ValidateUtil.jsonValidateWithKey(json, "imgUrls")) {
            JSONArray imgUrlArray = (JSONArray) json.get("imgUrls");
            int i = 1;
            for (Object imgUrl : imgUrlArray) {
                DocImg docImg = new DocImg();
                docImg.setImgUrl(imgUrl.toString());
                docImg.setSortNum(i);
                docImgList.add(docImg);
                i++;
            }
        }

        List<DocTag> docTagList = new LinkedList<DocTag>();
        if(ValidateUtil.jsonValidateWithKey(json, "docTags")) {
            JSONArray tagArray = (JSONArray) json.get("docTags");
            for (Object tag : tagArray) {
                DocTag docTag = new DocTag();
                docTag.setTagName(tag.toString());
                docTag.setCreateTime(currTime);
                docTagList.add(docTag);

                if( tag.toString().compareTo("HOT") == 0 ) {
                    doc.setIsHot(1);
                }
            }
        }

        //相关信息写入数据库表
        docService.saveDocumentOnly(doc, docImgList, docTagList);

        //保存文章草稿到 html 并设置 docUrl
        docService.saveDocumentToFs(doc,json.getString("content"));

        //文章保存完成后，开始封装返回信息
        JSONObject resJson = new JSONObject();
        resJson.put("key", doc.getDocId());
        resJson.put("docId", doc.getDocId());
        resJson.put("docTitle", StringUtil.isNotEmpty(doc.getDocTitle()) ?  doc.getDocTitle() : "");
        resJson.put("createUserName", StringUtil.isNotEmpty(doc.getCreateUserName()) ?  doc.getCreateUserName() : "");
        resJson.put("publishState", doc.getPublishState());
        resJson.put("updateTime", DateUtil.getTimeFormatText(doc.getUpdateTime().getTime()));
        resJson.put("readAmount", doc.getReadAmount());
        resJson.put("upvoteAmount", doc.getUpvoteAmount());
        resJson.put("commentAmount", doc.getCommentAmount());
        resJson.put("docUrl", StringUtil.isNotEmpty(doc.getDocUrl()) ?  doc.getDocUrl() : "");

        //resJson.put("delPower", WebConstants.Boolean.TRUE.ordinal());//自己发布的可以删除
        //resJson.put("imgUrls", json.get("imgUrls"));//发布的动力圈图片集合
        //resJson.put("userImgUrls", new JSONArray());//点赞头像集合为空

        /* 返回信息格式
        key	            int	        文章ID(和docId同值，防止前端框架报错)
        docId	        int	        文章ID
        docTitle	    String	    文章标题
        createUserName	String	    创建人
        publishState	int	        发布状态
        updateTime	    String	    发布/保存时间
        readAmount	    int	        浏览量
        upvoteAmount	int	        点赞量
        commentAmount	int	        评论数
        docUrl	        String	    文章链接
         */

        /*Map<String, Object> userInfoMap = userService.getUserInfoForDiscover(userId);
        UserSecret userSecret = userSecretService.getUserSecretByUserId(userId);    //用户是否设置了隐私保护

        if(null != userSecret && WebConstants.Boolean.TRUE.ordinal() == userSecret.getSecretControl()) {
            resJson.put("createUserName", userInfoMap.get("nickName"));
        } else {
            resJson.put("createUserName", userInfoMap.get("username"));
        }*/

        return Response.success(resJson);
    }

    @RequestMapping(value = {"/getDocExtraInfo"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "根据文章id获得阅读量等数据")
    @ApiOperation(value = "2.4 查询-根据文章id获得阅读量等数据")
    public Response getDocExtraInfo(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "文章Id") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"docId"};    //必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);   //必填项请求参数不完整，请检查
        }

        //根据 docId 取得 doc 中的信息
        List<Map<String, Object>> mapList = docService.getDocExtraInfo(json.getLong("docId"));

        //封装返回信息
        JSONObject resJsonT = new JSONObject();
        JSONArray resJsonData = new JSONArray();
        JSONObject resJson = new JSONObject();

        if(!CollectionUtils.isEmpty(mapList) && mapList.size() > 0) {
            resJson.put("docId", json.getLong("docId"));
            resJson.put("createUserName", StringUtil.isNotEmpty((String) mapList.get(0).get("createUserName")) ? (String) mapList.get(0).get("createUserName") : "");
            resJson.put("publishState", mapList.get(0).get("publishState"));
            resJson.put("updateTime", DateUtil.getTimeFormatText(((Date) mapList.get(0).get("updateTime")).getTime()));
            resJson.put("docUrl", StringUtil.isNotEmpty((String) mapList.get(0).get("docUrl")) ? (String) mapList.get(0).get("docUrl") : "");
            resJson.put("sortWeight", mapList.get(0).get("sortWeight"));
            resJson.put("readAmount", mapList.get(0).get("readAmount"));
            resJson.put("commentAmount", mapList.get(0).get("commentAmount"));
            resJson.put("upvoteAmount", mapList.get(0).get("upvoteAmount"));
        }
        /*else{
            resJson.put("docId", json.getLong("docId"));
            resJson.put("createUserName", "");
            resJson.put("publishState", 0);
            resJson.put("updateTime", "");
            resJson.put("docUrl", "");
            resJson.put("sortWeight", 0);
            resJson.put("readAmount", 0);
            resJson.put("commentAmount", 0);
            resJson.put("upvoteAmount", 0);
        }*/

        //System.out.println(mapList.size());
        JSONArray resDocTags = new JSONArray();
        //ArrayList<String> arrayList = new ArrayList<String>();

        if(!CollectionUtils.isEmpty(mapList) && mapList.size() > 1) {
            for( int i = 1 ; i < mapList.size() ; i++) {
                //System.out.println((String)mapList.get(i).get("tagName"));
                //arrayList.add((String)mapList.get(i).get("tagName"));
                resDocTags.add(mapList.get(i).get("tagName"));
            }

            resJson.put("docTags", resDocTags);
        }

        resJsonData.add(resJson);

        resJsonT.put("data",resJsonData);

        return Response.success(resJsonT);
    }

    @RequestMapping(value = {"/getCommentList"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "查询文章评论列表")
    @ApiOperation(value = "2.5 查询-文章评论列表")
    public Response getCommentList(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "文章Id:页码:每页条数") @RequestBody JSONObject json
    ) throws Exception {

        String[] params = new String[]{"pageindex","docId"};    //必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);   //必填项请求参数不完整，请检查
        }

        Long pageSize = new Long(15);
        if(json.containsKey("pagesize") && null != json.get("pagesize") && StringUtil.isNotEmpty(json.getString("pagesize"))) {
            pageSize = json.getLong("pagesize");
        }

        Integer pageIndex = json.getInt("pageindex");
        Long docId = json.getLong("docId");

        Long total = docService.getDocCommentListTotal(docId);

        Long maxpage = new Long(1);
        if( total > pageSize ) {
            if( total%pageSize == 0) {
                maxpage = total/pageSize;
            }
            else {
                maxpage = total / pageSize + 1;
            }
        }
        else {
            maxpage = new Long(1);
        }

        List<Map<String, Object>> mapList = docService.getCommentListPc(docId,(pageIndex - 1) * pageSize.intValue(),pageSize.intValue());
        //System.out.println("mapList.size()=" + mapList.size());

        //封装返回信息
        JSONObject resJson = new JSONObject();
        JSONArray resDataArr = new JSONArray();

        resJson.put("pageindex", pageIndex);
        resJson.put("pagesize", pageSize);
        resJson.put("total", total);
        resJson.put("maxpage", maxpage);

        if( !CollectionUtils.isEmpty(mapList) ) {
            for (int i = 0; i < mapList.size(); i++) {

                JSONObject resDataJson = new JSONObject();

                resDataJson.put("key", mapList.get(i).get("key"));
                resDataJson.put("commentId", mapList.get(i).get("commentId"));
                resDataJson.put("userId", mapList.get(i).get("userId"));
                resDataJson.put("username", StringUtil.isNotEmpty((String) mapList.get(i).get("userName")) ? (String) mapList.get(i).get("userName") : "");
                resDataJson.put("commentContent", StringUtil.isNotEmpty((String) mapList.get(i).get("commentContent")) ? (String) mapList.get(i).get("commentContent") : "");
                resDataJson.put("commentTime", DateUtil.getTimeFormatText(((Date) mapList.get(i).get("commentTime")).getTime()));

                resDataArr.add(resDataJson);
            }
        }

        resJson.put("data", resDataArr);

        return Response.success(resJson);
    }

    @RequestMapping(value = {"/getReadList"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "查询文章浏览记录列表")
    @ApiOperation(value = "2.6 查询-文章浏览记录列表")
    public Response getReadList(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "文章Id:页码:每页条数") @RequestBody JSONObject json
    ) throws Exception {

        String[] params = new String[]{"pageindex","docId"};    //必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);   //必填项请求参数不完整，请检查
        }

        Long pageSize = new Long(15);
        if(json.containsKey("pagesize") && null != json.get("pagesize") && StringUtil.isNotEmpty(json.getString("pagesize"))) {
            pageSize = json.getLong("pagesize");
        }

        Integer pageIndex = json.getInt("pageindex");
        Long docId = json.getLong("docId");

        Long total = docService.getDocBrowseListTotal(docId);

        Long maxpage = new Long(1);
        if( total > pageSize ) {
            if( total%pageSize == 0) {
                maxpage = total/pageSize;
            }
            else {
                maxpage = total / pageSize + 1;
            }
        }
        else {
            maxpage = new Long(1);
        }

        List<Map<String, Object>> mapList = docService.getReadListPc(docId,(pageIndex - 1) * pageSize.intValue(),pageSize.intValue());
        System.out.println("mapList.size()=" + mapList.size());

        //封装返回信息
        JSONObject resJson = new JSONObject();
        JSONArray resDataArr = new JSONArray();

        resJson.put("pageindex", pageIndex);
        resJson.put("pagesize", pageSize);
        resJson.put("total", total);
        resJson.put("maxpage", maxpage);

        if( !CollectionUtils.isEmpty(mapList) ) {
            for (int i = 0; i < mapList.size(); i++) {

                JSONObject resDataJson = new JSONObject();

                resDataJson.put("key", mapList.get(i).get("key"));
                resDataJson.put("browseId", mapList.get(i).get("browseId"));
                resDataJson.put("userId", mapList.get(i).get("userId"));
                resDataJson.put("username", StringUtil.isNotEmpty((String) mapList.get(i).get("userName")) ? (String) mapList.get(i).get("userName") : "");
                resDataJson.put("createTime", DateUtil.getTimeFormatText(((Date) mapList.get(i).get("createTime")).getTime()));

                resDataArr.add(resDataJson);
            }
        }

        resJson.put("data", resDataArr);

        return Response.success(resJson);
    }

    @RequestMapping(value = {"/getUpvoteList"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "查询文章点赞记录列表")
    @ApiOperation(value = "2.7 查询-文章点赞记录列表")
    public Response getUpvoteList(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "文章Id:页码:每页条数") @RequestBody JSONObject json
    ) throws Exception {

        String[] params = new String[]{"pageindex","docId"};    //必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);   //必填项请求参数不完整，请检查
        }

        Long pageSize = new Long(15);
        if(json.containsKey("pagesize") && null != json.get("pagesize") && StringUtil.isNotEmpty(json.getString("pagesize"))) {
            pageSize = json.getLong("pagesize");
        }

        Integer pageIndex = json.getInt("pageindex");
        Long docId = json.getLong("docId");

        Long total = docService.getDocUpvoteListTotal(docId);

        Long maxpage = new Long(1);
        if( total > pageSize ) {
            if( total%pageSize == 0) {
                maxpage = total/pageSize;
            }
            else {
                maxpage = total / pageSize + 1;
            }
        }
        else {
            maxpage = new Long(1);
        }

        List<Map<String, Object>> mapList = docService.getUpvoteListPc(docId,(pageIndex - 1) * pageSize.intValue(),pageSize.intValue());
        //System.out.println("mapList.size()=" + mapList.size());

        //封装返回信息
        JSONObject resJson = new JSONObject();
        JSONArray resDataArr = new JSONArray();

        resJson.put("pageindex", pageIndex);
        resJson.put("pagesize", pageSize);
        resJson.put("total", total);
        resJson.put("maxpage", maxpage);

        if( !CollectionUtils.isEmpty(mapList) ) {
            for (int i = 0; i < mapList.size(); i++) {

                JSONObject resDataJson = new JSONObject();

                resDataJson.put("key", mapList.get(i).get("key"));
                resDataJson.put("upvoteId", mapList.get(i).get("upvoteId"));
                resDataJson.put("userId", mapList.get(i).get("userId"));
                resDataJson.put("username", StringUtil.isNotEmpty((String) mapList.get(i).get("userName")) ? (String) mapList.get(i).get("userName") : "");
                resDataJson.put("createTime", DateUtil.getTimeFormatText(((Date) mapList.get(i).get("createTime")).getTime()));

                resDataArr.add(resDataJson);
            }
        }

        resJson.put("data", resDataArr);

        return Response.success(resJson);
    }

    @Register
    @RequestMapping(value = {"/commentDel"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "对某文章的评论进行删除")
    @ApiOperation(value = "2.8 删除-文章评论")
    public Response commentDel(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "文章评论ID") @RequestBody JSONObject json
    ) throws Exception {

        String[] params = new String[]{"commentId"};    //必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);   //必填项请求参数不完整，请检查
        }

        //根据 commentId 删除 comment 中的信息
        docService.delComment(json.getLong("commentId"),null);

        //封装返回信息
        //JSONObject resJson = new JSONObject();

        //resJson.put("commentId", json.getLong("commentId"));

        return Response.success(json.getLong("commentId"));
    }

    @Register
    @RequestMapping(value = {"/documentPublish"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "对文章进行发布操作")
    @ApiOperation(value = "2.9 更新-文章发布")
    public Response documentPublish(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "文章ID") @RequestBody JSONObject json
    ) throws Exception {

        String[] params = new String[]{"docId"};    //必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);   //必填项请求参数不完整，请检查
        }

        //从磁盘删除文章，更新状态为已发布
        //doc.setDocUrl("http://127.0.0.1/doc/" + doc.getDocTitle());
        docService.publishDocument(json.getLong("docId"));

        //根据 docId 取得 doc 中的信息
        List<Map<String, Object>> mapList = docService.documentPublish(json.getLong("docId"));

        //封装返回信息
        //JSONObject resJsonT = new JSONObject();
        //JSONArray resJsonData = new JSONArray();
        JSONObject resJson = new JSONObject();

        if(!CollectionUtils.isEmpty(mapList) && mapList.size() > 0) {
            //resJson.put("key", json.getLong("docId"));
            resJson.put("docId", json.getLong("docId"));
            resJson.put("docTitle", StringUtil.isNotEmpty((String) mapList.get(0).get("docTitle")) ? (String) mapList.get(0).get("docTitle") : "");
            resJson.put("createUserName", StringUtil.isNotEmpty((String) mapList.get(0).get("createUserName")) ? (String) mapList.get(0).get("createUserName") : "");
            resJson.put("publishState", mapList.get(0).get("publishState"));
            resJson.put("updateTime", DateUtil.getTimeFormatText(((Date) mapList.get(0).get("updateTime")).getTime()));
            resJson.put("docUrl", StringUtil.isNotEmpty((String) mapList.get(0).get("docUrl")) ? (String) mapList.get(0).get("docUrl") : "");
            resJson.put("sortWeight", mapList.get(0).get("sortWeight"));
            resJson.put("readAmount", mapList.get(0).get("readAmount"));
            resJson.put("commentAmount", mapList.get(0).get("commentAmount"));
            resJson.put("upvoteAmount", mapList.get(0).get("upvoteAmount"));
        }
        else{
            //resJson.put("key", json.getLong("docId"));
            resJson.put("docId", json.getLong("docId"));
            resJson.put("docTitle", "");
            resJson.put("createUserName", "");
            resJson.put("publishState", 0);
            resJson.put("updateTime", "");
            resJson.put("docUrl", "");
            resJson.put("sortWeight", 0);
            resJson.put("readAmount", 0);
            resJson.put("commentAmount", 0);
            resJson.put("upvoteAmount", 0);
        }

        //System.out.println(mapList.size());
        /*JSONArray resDocTags = new JSONArray();

        if(!CollectionUtils.isEmpty(mapList) && mapList.size() > 0) {
            for( int i = 1 ; i < mapList.size() ; i++) {
                //System.out.println((String)mapList.get(i).get("tagName"));
                //arrayList.add((String)mapList.get(i).get("tagName"));
                resDocTags.add(mapList.get(i).get("tagName"));
            }
        }

        resJson.put("docTags", resDocTags);*/

        //resJsonData.add(resJson);

        //resJsonT.put("data",resJson);

        return Response.success(resJson);
    }

    @Register
    @RequestMapping(value = {"/documentDel"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "删除文章")
    @ApiOperation(value = "2.10 删除-文章")
    public Response documentDel(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "文章ID") @RequestBody JSONObject json
    ) throws Exception {

        String[] params = new String[]{"docId"};    //必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);   //必填项请求参数不完整，请检查
        }

        //根据 commentId 删除 comment 中的信息
        docService.deleteDocument(json.getLong("docId"));

        //封装返回信息
        //JSONObject resJson = new JSONObject();

        //resJson.put("docId", json.getLong("docId"));

        return Response.success(json.getLong("docId"));
    }

    @RequestMapping(value = {"/getDocExtraInfoForEdit"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "根据文章id获得编辑页面数据")
    @ApiOperation(value = "2.11 查询-根据文章id获得编辑页面数据")
    public Response getDocExtraInfoForEdit(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "文章Id") @RequestBody JSONObject json
    ) throws Exception {

        String[] params = new String[]{"docId"};        //必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);   //必填项请求参数不完整，请检查
        }

        //根据 docId 取得 doc 中的信息
        Map<String, Object> map = docService.getDocExtraInfoForEdit(json.getLong("docId"));

        //封装返回信息
        JSONObject resJsonT = new JSONObject();
        JSONArray resJsonData = new JSONArray();
        JSONObject resJson = new JSONObject();

        //System.out.println(mapList.size());
        JSONArray resDocImgs = new JSONArray();
        JSONArray resDocTags = new JSONArray();
        //ArrayList<String> arrayList = new ArrayList<String>();
        List<Long> docIdList = new ArrayList<Long>();

        if( !CollectionUtils.isEmpty(map) && StringUtil.isNotEmpty((String) map.get("docUrl")) ) {

            String content = docService.getDocContent((String) map.get("docUrl"));

            resJson.put("docId", json.getLong("docId"));
            resJson.put("docTitle", (String) map.get("docTitle"));
            resJson.put("content", content != null && StringUtil.isNotEmpty(content) ? content : "");
            resJson.put("viewType", map.get("viewType"));
            resJson.put("sortWeight", map.get("sortWeight"));
        /*}
        else{
            resJson.put("docId", json.getLong("docId"));
            resJson.put("docTitle", "");
            resJson.put("content", "");
            resJson.put("viewType", null);
            resJson.put("sortWeight", null);
        }*/


            docIdList.add(json.getLong("docId"));

            //文章的 img_url
            List<Map<String, Object>> imgList = docService.getImgImgUrlListByDocId(docIdList);
            //System.out.println("imgList.size()=" + imgList.size());

            if (!CollectionUtils.isEmpty(imgList)) {
                for (int i = 0; i < imgList.size(); i++) {
                    resDocImgs.add(imgList.get(i).get("imgUrl"));
                }
            }

            //文章的 tag_name
            List<Map<String, Object>> tagList = docService.getTagTagNameList(docIdList);
            //System.out.println("tagList.size()=" + tagList.size());

            if (!CollectionUtils.isEmpty(tagList)) {
                for (int i = 0; i < tagList.size(); i++) {
                    resDocTags.add(tagList.get(i).get("tagName"));
                }
            }

            resJson.put("docImgs", resDocImgs);
            resJson.put("docTags", resDocTags);

            resJsonData.add(resJson);
        }

        resJsonT.put("data",resJsonData);

        return Response.success(resJsonT);
    }
}

