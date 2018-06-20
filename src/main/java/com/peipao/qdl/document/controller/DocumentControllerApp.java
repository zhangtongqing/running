package com.peipao.qdl.document.controller;


import com.peipao.framework.annotation.Register;
import com.peipao.framework.annotation.SystemControllerLog;
import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.model.Response;
import com.peipao.framework.util.DateUtil;
import com.peipao.framework.util.StringUtil;
import com.peipao.framework.util.ValidateUtil;
import com.peipao.qdl.document.model.DocComment;
import com.peipao.qdl.document.service.DocService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 方法名称：DocumentControllerApp
 * 功能描述：发现-精品阅读实体类
 * 作者：Na Jun
 * 版本：1.0
 * 创建日期：2017/10/17 13:31
 * 修订记录：
 */
@Api(
        value = "/document/app",
        description = "发现-精品阅读(app接口)"
)
@RestController
@RequestMapping({"/document/app"})
public class DocumentControllerApp {

    @Autowired
    private DocService docService;

    @RequestMapping(value = {"/getDocList"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "查询精品文章列表")
    @ApiOperation(value = "1.1 查询精品文章列表")
    public Response getDocList(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "页码:每页条数(默认10)") @RequestBody JSONObject json
    ) throws Exception {

        String[] params = new String[]{"pageindex"};    //必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);   //必填项请求参数不完整，请检查
        }

        Integer pageSize = 10;
        if(json.containsKey("pagesize") && null != json.get("pagesize") && StringUtil.isNotEmpty(json.getString("pagesize"))) {
            pageSize = json.getInt("pagesize");
        }

        Integer pageIndex = json.getInt("pageindex");

        Long total = docService.getDocListTotal(null);

        List<Map<String, Object>> mapList = docService.getDocList(userId,(pageIndex - 1) * pageSize,pageSize);
        //System.out.println("mapList.size()=" + mapList.size());

        Integer hasNext = 0;
        if( total > pageIndex * pageSize )
            hasNext = 1;

        //封装返回信息
        JSONObject resJson = new JSONObject();
        JSONArray resJsonData = new JSONArray();

        resJson.put("pageindex", pageIndex);
        resJson.put("pagesize", pageSize);
        resJson.put("hasNext", hasNext);

        if( !CollectionUtils.isEmpty(mapList) && mapList.size() > 0 ) {

            List<Long> docIdList = new ArrayList<Long>();

            for(int i = 0; i < mapList.size(); i++) {

                Integer idocId = (Integer)mapList.get(i).get("docId");
                docIdList.add( new Long( idocId ) );
            }

            //文章的 img_url
            List<Map<String, Object>> imgList = docService.getImgImgUrlListByDocId(docIdList);
            //System.out.println("imgList.size()=" + imgList.size());

            //文章的 tag_name
            List<Map<String, Object>> tagList = docService.getTagTagNameList(docIdList);
            //System.out.println("tagList.size()=" + tagList.size());

            for(int i = 0; i < mapList.size(); i++) {

                JSONObject resJsonT = new JSONObject();
                JSONArray resDocTags = new JSONArray();
                JSONArray resDocImgs = new JSONArray();

                resJsonT.put("docId", mapList.get(i).get("docId"));
                resJsonT.put("viewType", mapList.get(i).get("viewType"));
                resJsonT.put("docTitle", StringUtil.isNotEmpty((String) mapList.get(i).get("docTitle")) ? (String) mapList.get(i).get("docTitle") : "");
                resJsonT.put("readAmount", mapList.get(i).get("readAmount"));
                resJsonT.put("commentAmount", mapList.get(i).get("commentAmount"));
                resJsonT.put("ifUpvote", mapList.get(i).get("ifUpvote"));
                resJsonT.put("docUrl", StringUtil.isNotEmpty((String) mapList.get(i).get("docUrl")) ? (String) mapList.get(i).get("docUrl") : "");

                if( !CollectionUtils.isEmpty(imgList) ) {
                    for (int j = 0; j < imgList.size(); j++) {
                        if (imgList.get(j).get("docId") == mapList.get(i).get("docId"))
                            resDocImgs.add(imgList.get(j).get("imgUrl"));
                    }
                }

                if( !CollectionUtils.isEmpty(tagList) ) {
                    for (int j = 0; j < tagList.size(); j++) {
                        if (tagList.get(j).get("docId") == mapList.get(i).get("docId"))
                            resDocTags.add(tagList.get(j).get("tagName"));
                    }
                }

                resJsonT.put("imgUrls", resDocImgs);        //文章封面图片链接
                resJsonT.put("docTags", resDocTags);        //文章标签集合

                resJsonData.add(resJsonT);
            }
        }
        /*else{
            JSONObject resJsonT = new JSONObject();

            resJsonT.put("docId", 0);
            resJsonT.put("viewType", 0);
            resJsonT.put("docTitle", "");
            resJsonT.put("readAmount", 0);
            resJsonT.put("commentAmount", 0);
            resJsonT.put("ifUpvote", 0);
            resJsonT.put("docUrl", "");
            resJsonT.put("imgUrls", new JSONArray());
            resJsonT.put("imgUrls", new JSONArray());
            resJsonData.add(resJsonT);
        }*/

        resJson.put("data",resJsonData);

        return Response.success(resJson);
    }

    @RequestMapping(value = {"/getDocExtraInfo"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "根据文章id查询阅读量等数据")
    @ApiOperation(value = "1.2 查询-根据文章id获得阅读量等数据")
    public Response getDocExtraInfo(
            @ApiParam(required = false, value = "token") @RequestParam(required = false) String token,
            @ApiParam(required = false, value = "学生Id") @RequestParam(required = false) Long userId,
            @ApiParam(required = false, value = "签名") @RequestParam(required = false) String sign,
            @ApiParam(required = true, value = "文章ID") @RequestBody JSONObject json
    ) throws Exception {

        String[] params = new String[]{"docId"};    //必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);   //必填项请求参数不完整，请检查
        }

        //根据 docId 取得 doc 中的信息
        Map<String, Object> mapList = docService.getDocExtraInfoByDocId(userId,json.getLong("docId"));

        //封装返回信息
        JSONObject resJson = new JSONObject();

        if( CollectionUtils.isEmpty(mapList) || mapList.size() < 1) {
            resJson.put("readAmount", 0);
            resJson.put("commentAmount", 0);
            resJson.put("upvoteAmount", 0);
            resJson.put("ifUpvote", 0);
        }
        else {
            resJson.put("readAmount", mapList.get("readAmount"));
            resJson.put("commentAmount", mapList.get("commentAmount"));
            resJson.put("upvoteAmount", mapList.get("upvoteAmount"));
            resJson.put("ifUpvote", mapList.get("ifUpvote"));
        }

        return Response.success(resJson);
    }

    @Register
    @RequestMapping(value = {"/upvote"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "对某文章进行点赞操作")
    @ApiOperation(value = "1.3 文章点赞")
    public Response upvote(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "文章ID") @RequestBody JSONObject json
    ) throws Exception {

        String[] params = new String[]{"docId"};    //必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);   //必填项请求参数不完整，请检查
        }

        //根据 docId 取得 doc 中的信息
        Date currTime = new Date();
        Map<String, Object> mapList = docService.upvote(json.getLong("docId"),userId, currTime);

        //封装返回信息
        JSONObject resJson = new JSONObject();

        if(CollectionUtils.isEmpty(mapList) || mapList.size() < 1) {
            resJson.put("docId", json.getLong("docId"));
            resJson.put("upvoteAmount", 0);
        }
        else {
            resJson.put("docId", json.getLong("docId"));
            resJson.put("upvoteAmount", mapList.get("upvoteAmount"));
        }

        return Response.success(resJson);
    }

    @Register
    @RequestMapping(value = {"/upvoteCancel"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "对某文章进行取消点赞操作")
    @ApiOperation(value = "1.4 文章取消点赞")
    public Response upvoteCancel(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "文章ID") @RequestBody JSONObject json
    ) throws Exception {

        String[] params = new String[]{"docId"};    //必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);   //必填项请求参数不完整，请检查
        }

        //根据 docId 取得 doc 中的信息
        Map<String, Object> mapList = docService.cancelUpvote(json.getLong("docId"),userId);

        //封装返回信息
        JSONObject resJson = new JSONObject();

        if(CollectionUtils.isEmpty(mapList) || mapList.size() < 1) {
            resJson.put("docId", json.getLong("docId"));
            resJson.put("upvoteAmount", 0);
        }
        else {
            resJson.put("docId", json.getLong("docId"));
            resJson.put("upvoteAmount", mapList.get("upvoteAmount"));
        }

        return Response.success(resJson);
    }

    @Register
    @RequestMapping(value = {"/commentSave"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "对某文章的评论进行保存")
    @ApiOperation(value = "1.5 文章评论保存")
    public Response commentSave(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "文章ID:评论内容") @RequestBody JSONObject json
    ) throws Exception {

        String[] params = new String[]{"docId","commentContent"};    //必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);   //必填项请求参数不完整，请检查
        }

        //根据 docId 取得 doc 中的信息
        Date currTime = new Date();
        DocComment docComment = new DocComment();

        docComment.setDocId(json.getLong("docId"));
        docComment.setUserId(userId);
        docComment.setCommentContent(json.getString("commentContent"));
        docComment.setCreateTime(currTime);

        docService.saveComment(docComment);

        //封装返回信息
        JSONObject resJson = new JSONObject();

        resJson.put("commentId", docComment.getCommentId());
        resJson.put("username", StringUtil.isNotEmpty(docComment.getUsername()) ?  docComment.getUsername() : "");
        resJson.put("schoolName", StringUtil.isNotEmpty(docComment.getSchoolName()) ?  docComment.getSchoolName() : "");
        resJson.put("commentContent", StringUtil.isNotEmpty(docComment.getCommentContent()) ?  docComment.getCommentContent() : "");
        resJson.put("userImg", StringUtil.isNotEmpty(docComment.getUserImg()) ?  docComment.getUserImg() : "");
        resJson.put("commentTime", DateUtil.getTimeFormatText(currTime.getTime()));

        return Response.success(resJson);
    }

    @Register
    @RequestMapping(value = {"/commentDel"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "对某文章的评论进行删除")
    @ApiOperation(value = "1.6 删除-文章评论")
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
        docService.delComment(json.getLong("commentId"),userId);

        //封装返回信息
        //JSONObject resJson = new JSONObject();

        //resJson.put("commentId", json.getLong("commentId"));

        return Response.success(json.getLong("commentId"));
    }

    @RequestMapping(value = {"/getDocTop"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "查询文章最新发布top1")
    @ApiOperation(value = "1.7 查询-文章最新发布top1")
    public Response getDocTop(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign
    ) throws Exception {

        //文章最新发布top1
        List<Map<String, Object>> mapList = docService.getDocTop();
        //System.out.println("mapList.size()=" + mapList.get(0).get("docId"));


        //封装返回信息
        JSONObject resJson = new JSONObject();
        JSONArray resDocTags = new JSONArray();
        JSONArray resDocImgs = new JSONArray();

        if(!CollectionUtils.isEmpty(mapList) && mapList.size() > 0) {
            resJson.put("docId", mapList.get(0).get("docId"));
            resJson.put("viewType", mapList.get(0).get("viewType"));
            resJson.put("docTitle", StringUtil.isNotEmpty((String) mapList.get(0).get("docTitle")) ? (String) mapList.get(0).get("docTitle") : "");
            resJson.put("readAmount", mapList.get(0).get("readAmount"));
            resJson.put("commentAmount", mapList.get(0).get("commentAmount"));
            resJson.put("docUrl", StringUtil.isNotEmpty((String) mapList.get(0).get("docUrl")) ? (String) mapList.get(0).get("docUrl") : "");

            List<Long> docIdList = new ArrayList<Long>();
            Integer idocId = (Integer)mapList.get(0).get("docId");
            docIdList.add( new Long( idocId ) );

            //文章最新发布 top1 的 img_url
            List<Map<String, Object>> imgList = docService.getImgImgUrlListByDocId(docIdList);
            //System.out.println("imgList.size()=" + imgList.size());

            if( !CollectionUtils.isEmpty(imgList) ) {
                for (int i = 0; i < imgList.size(); i++) {
                    resDocImgs.add(imgList.get(i).get("imgUrl"));
                }
            }

            //文章最新发布 top1 的 img_url
            List<Map<String, Object>> tagList = docService.getTagTagNameList(docIdList);
            //System.out.println("tagList.size()=" + tagList.size());

            if( !CollectionUtils.isEmpty(tagList) ) {
                for (int i = 0; i < tagList.size(); i++) {
                    resDocTags.add(tagList.get(i).get("tagName"));
                }
            }

            resJson.put("imgUrls", resDocImgs);        //文章封面图片链接为空
            resJson.put("docTags", resDocTags);        //文章标签集合为空
        }
        else{
            resJson.put("docId", 0);
            resJson.put("viewType", 0);
            resJson.put("docTitle", "");
            resJson.put("readAmount", 0);
            resJson.put("commentAmount", 0);
            resJson.put("docUrl", "");
            resJson.put("imgUrls", new JSONArray());
            resJson.put("imgUrls", new JSONArray());
        }

        return Response.success(resJson);
    }

    @Register
    @RequestMapping(value = {"/getCommentList"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "查询文章评论列表")
    @ApiOperation(value = "1.8 查询-文章评论列表")
    public Response getCommentList(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "每页条数:页码:文章ID") @RequestBody JSONObject json
    ) throws Exception {

        String[] params = new String[]{"pageindex","docId"};    //必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);   //必填项请求参数不完整，请检查
        }

        Integer pageSize = 10;
        if(json.containsKey("pagesize") && null != json.get("pagesize") && StringUtil.isNotEmpty(json.getString("pagesize"))) {
            pageSize = json.getInt("pagesize");
        }

        Integer pageIndex = json.getInt("pageindex");
        Long docId = json.getLong("docId");

        Long total = docService.getDocListTotal(docId);

        List<Map<String, Object>> mapList = docService.getCommentList(docId,(pageIndex - 1) * pageSize,pageSize);
        //System.out.println("mapList.size()=" + mapList.size());

        Integer hasNext = 0;
        if( total > pageIndex * pageSize )
            hasNext = 1;

        //封装返回信息
        JSONObject resJson = new JSONObject();
        JSONArray resDataArr = new JSONArray();

        resJson.put("pageindex", pageIndex);
        resJson.put("pagesize", pageSize);
        resJson.put("hasNext", hasNext);

        if( !CollectionUtils.isEmpty(mapList) ) {
            for (int i = 0; i < mapList.size(); i++) {

                JSONObject resDataJson = new JSONObject();

                resDataJson.put("commentId", mapList.get(i).get("commentId"));
                resDataJson.put("username", StringUtil.isNotEmpty((String) mapList.get(i).get("userName")) ? (String) mapList.get(i).get("userName") : "");
                resDataJson.put("schoolName", StringUtil.isNotEmpty((String) mapList.get(i).get("schoolName")) ? (String) mapList.get(i).get("schoolName") : "");
                resDataJson.put("commentContent", StringUtil.isNotEmpty((String) mapList.get(i).get("commentContent")) ? (String) mapList.get(i).get("commentContent") : "");
                resDataJson.put("userImg", mapList.get(i).get("imageUrl"));
                resDataJson.put("commentTime", mapList.get(i).get("createTime"));

                resDataArr.add(resDataJson);
            }
        }

        resJson.put("data", resDataArr);

        return Response.success(resJson);
    }
}
