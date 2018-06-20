package com.peipao.qdl.discover.controller;


import com.peipao.framework.annotation.Register;
import com.peipao.framework.annotation.SystemControllerLog;
import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.constant.WebConstants;
import com.peipao.framework.model.Response;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.page.MyPage;
import com.peipao.framework.page.MyPageInfo;
import com.peipao.framework.util.DateUtil;
import com.peipao.framework.util.StringUtil;
import com.peipao.framework.util.ValidateUtil;
import com.peipao.qdl.discover.model.Discover;
import com.peipao.qdl.discover.model.DiscoverComment;
import com.peipao.qdl.discover.model.DiscoverImg;
import com.peipao.qdl.discover.model.DiscoverUpvote;
import com.peipao.qdl.discover.service.DiscoverCommentService;
import com.peipao.qdl.discover.service.DiscoverService;
import com.peipao.qdl.discover.service.DiscoverUpvoteService;
import com.peipao.qdl.school.model.UserSchool;
import com.peipao.qdl.school.service.SchoolService;
import com.peipao.qdl.user.model.UserSecret;
import com.peipao.qdl.user.service.UserSecretService;
import com.peipao.qdl.user.service.UserService;
import com.peipao.qdl.version.model.ClientType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 方法名称：Discover
 * 功能描述：发现-动力圈实体类
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/10/17 13:31
 * 修订记录：
 */
@Api(
    value = "/discover/app",
    description = "发现-动力圈(app接口)"
)
@RestController
@RequestMapping({"/discover/app"})
public class DiscoverControllerAPP {
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private DiscoverService discoverService;
    @Autowired
    private UserSecretService userSecretService;
    @Autowired
    private UserService userService;
    @Autowired
    private DiscoverCommentService discoverCommentService;
    @Autowired
    private DiscoverUpvoteService discoverUpvoteService;


    @Register
    @RequestMapping(value = {"/getDiscoverHotList"},method = {RequestMethod.POST})
    //@SystemControllerLog(description = "查询热门动力圈列表")
    @ApiOperation(value = "3.1 查询热门动力圈列表")
    public Response getDiscoverHotList(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "pageindex:页码(必填),pagesize:每页条数(非必填)") @RequestBody JSONObject json
    ) throws Exception {
        int[] page = MyPage.checkPageParams(json);
        MyPageInfo pageInfo =  discoverService.getDiscoverList(
            userId,
            WebConstants.Boolean.TRUE.ordinal(),
            WebConstants.Boolean.FALSE.ordinal(),
            WebConstants.Boolean.TRUE.ordinal(),
            WebConstants.Boolean.FALSE.ordinal(),
            page[0],
            page[1]
        );
        return Response.success(pageInfo);
    }


    @Register
    @RequestMapping(value = {"/getDiscoverSchoolList"},method = {RequestMethod.POST})
    //@SystemControllerLog(description = "查询同校动力圈列表")
    @ApiOperation(value = "3.2 查询同校动力圈列表")
    public Response getDiscoverSchoolList(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "pageindex:页码(必填)") @RequestBody JSONObject json
    ) throws Exception {
        int[] page = MyPage.checkPageParams(json);
        UserSchool userSchool = schoolService.getParaByUserId(userId,  new Date());
        if(null == userSchool || null == userSchool.getSchoolId()) {
            return Response.fail(ReturnStatus.USERSCHOOL_PARAMETER_MISS);//缺少学校、学期、课程等参数
        }
        MyPageInfo pageInfo =  discoverService.getDiscoverSchoolList(
            userSchool.getSchoolId(),
            userId,
            WebConstants.Boolean.TRUE.ordinal(),
            WebConstants.Boolean.FALSE.ordinal(),
            WebConstants.Boolean.FALSE.ordinal(),
            page[0], page[1]
        );
        return Response.success(pageInfo);
    }


    @Register
    @RequestMapping(value = {"/getDiscoverNewList"},method = {RequestMethod.POST})
    //@SystemControllerLog(description = "查询最新动力圈列表")
    @ApiOperation(value = "3.3 查询最新动力圈列表")
    public Response getDiscoverNewList(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "pageindex:页码(必填)") @RequestBody JSONObject json
    ) throws Exception {
        int[] page = MyPage.checkPageParams(json);
        MyPageInfo pageInfo =  discoverService.getDiscoverList(
                userId,
                WebConstants.Boolean.TRUE.ordinal(),
                WebConstants.Boolean.FALSE.ordinal(),
                null,
                WebConstants.Boolean.FALSE.ordinal(),
                page[0],
                page[1]
        );
        return Response.success(pageInfo);
    }


    @Register
    @RequestMapping(value = {"/getCommentList"},method = {RequestMethod.POST})
    //@SystemControllerLog(description = "查询某条动力圈评论列表")
    @ApiOperation(value = "3.4 查询某条动力圈评论列表")
    public Response getCommentList(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "pageindex:页码,discoverId:动力圈ID (都必填)") @RequestBody JSONObject json
    ) throws Exception {
        int[] pageParams = MyPage.checkPageParams(json);
        String[] params = new String[]{"discoverId"};//必填项检查
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("discoverId", json.getLong("discoverId"));
        paramsMap.put("userId", userId);
        paramsMap.put("trueVal", WebConstants.Boolean.TRUE.ordinal());
        paramsMap.put("falseVal", WebConstants.Boolean.FALSE.ordinal());
        paramsMap.put("logicDelete", WebConstants.Boolean.FALSE.ordinal());
        MyPageInfo pageInfo = discoverCommentService.getCommentListByDiscoverId(paramsMap, WebConstants.Boolean.TRUE.ordinal(), pageParams);
        return Response.success(pageInfo);
    }

    @Register
    @RequestMapping(value = {"/getUpvoteList"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "查询某条动力圈点赞列表")
    @ApiOperation(value = "3.5 查询某条动力圈点赞列表")
    public Response getUpvoteList(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "pageindex:页码,discoverId:动力圈ID (都必填)") @RequestBody JSONObject json
    ) throws Exception {
        int[] pageParams = MyPage.checkPageParams(json);
        String[] params = new String[]{"discoverId"};//必填项检查
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        MyPageInfo pageInfo = discoverUpvoteService.getUpvoteByDiscoverId(json.getLong("discoverId"), ClientType.ANDROID, pageParams);
        return Response.success(pageInfo);
    }

    @Register
    @RequestMapping(value = {"/upvote"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "对某条动力圈进行点赞操作")
    @ApiOperation(value = "3.6 对某条动力圈进行点赞操作")
    public Response upvote(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "discoverId:动力圈ID") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"discoverId"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        Long discoverId = json.getLong("discoverId");
        DiscoverUpvote discoverUpvote = new DiscoverUpvote(discoverId, userId);
        discoverUpvoteService.upvote(discoverUpvote);
        Map<String, Object> userInfoMap = userService.getUserInfoForDiscover(userId);
        Discover discover = discoverService.getDiscoverById(discoverId);
        JSONObject resJson = new JSONObject();
        resJson.accumulate("discoverId", discoverId);
        resJson.accumulate("userImgUrl", userInfoMap.get("userImg").toString());
        resJson.accumulate("upvoteAmount", discover.getUpvoteAmount());
        return Response.success(resJson);
    }

    @Register
    @RequestMapping(value = {"/commentSave"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "对某条动力圈进行评论并保存")
    @ApiOperation(value = "3.7 对某条动力圈进行评论并保存")
    public Response commentSave(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "discoverId:动力圈ID，commentContent:评论内容1-140汉字") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"discoverId", "commentContent"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        Long discoverId = json.getLong("discoverId");
        String commentContent = json.getString("commentContent");
        if(commentContent.length() > 140) {
            return Response.fail(ResultMsg.DISCOVER_COMMENTID_TOO_LONG);//评论内容过长
        }
        DiscoverComment discoverComment = new DiscoverComment(discoverId, userId, commentContent);
        discoverCommentService.commentSave(discoverComment);
        /****************************************** 保存完成 开始构造返回参数 *****************************************/
        JSONObject jsonBack = new JSONObject();
        jsonBack.put("commentId", discoverComment.getCommentId());
        jsonBack.put("commentContent", discoverComment.getCommentContent());
        jsonBack.put("commentTime", DateUtil.dateToStr(discoverComment.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> userInfoMap = userService.getUserInfoForDiscover(userId);
        UserSecret userSecret = userSecretService.getUserSecretByUserId(userId);//用户是否设置了隐私保护
        if(null != userSecret && WebConstants.Boolean.TRUE.ordinal() == userSecret.getSecretControl()) {
            jsonBack.put("username", userInfoMap.get("nickName"));
        } else {
            jsonBack.put("username", userInfoMap.get("username"));
        }
        return Response.success(jsonBack);
    }


    @Register
    @RequestMapping(value = {"/upvoteCancel"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "对某条动力圈进行取消点赞操作")
    @ApiOperation(value = "3.8 对某条动力圈进行取消点赞操作")
    public Response upvoteCancel(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "discoverId:动力圈ID") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"discoverId"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        Long discoverId = json.getLong("discoverId");
        discoverUpvoteService.upvoteCancel(discoverId, userId);
        Map<String, Object> userInfoMap = userService.getUserInfoForDiscover(userId);
        Discover discover = discoverService.getDiscoverById(discoverId);
        JSONObject resJson = new JSONObject();
        resJson.accumulate("discoverId", discoverId);
        resJson.accumulate("userImgUrl", userInfoMap.get("userImg").toString());
        resJson.accumulate("upvoteAmount", discover.getUpvoteAmount());
        return Response.success(resJson);
    }

    @Register
    @RequestMapping(value = {"/commentDel"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "删除一条本人发布的动力圈评论")
    @ApiOperation(value = "3.9 删除一条本人发布的动力圈评论")
    public Response commentDel(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "commentId:动力圈评论ID") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"commentId"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        Long commentId = json.getLong("commentId");
        DiscoverComment discoverComment = discoverCommentService.getDiscoverCommentById(commentId);
        if(null != discoverComment) {
            if(userId == discoverComment.getUserId()) {
                Map<String, Object> paramsMap = new HashMap<String, Object>();
                paramsMap.put("commentId", commentId);
                paramsMap.put("userId", userId);
                discoverCommentService.updateForCommentDelete(discoverComment.getDiscoverId(), paramsMap);
            } else {
                return Response.fail(ResultMsg.DISCOVER_COMMENTID_DEL_SELF);//您不能删除他人发布的评论
            }
        } else {
            return Response.fail(ResultMsg.DISCOVER_COMMENTID_NOT_EXIST);//您要删除的评论不存在
        }
        return Response.success(json);
    }

    @Register
    @RequestMapping(value = {"/discoverDel"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "删除一条自己发布的动力圈")
    @ApiOperation(value = "3.10 删除一条自己发布的动力圈")
    public Response discoverDel(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "discoverId:动力圈ID") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"discoverId"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        Long discoverId = json.getLong("discoverId");
        Discover discover = discoverService.getDiscoverById(discoverId);
        if(null != discover && discover.getLogicDelete() == WebConstants.Boolean.FALSE.ordinal()) {
            if(discover.getCreateUserId() == userId){
                this.discoverService.updatediscoverDel(discoverId, WebConstants.Boolean.TRUE.ordinal());
            } else {
                return Response.fail(ResultMsg.DISCOVER_DEL_SELF);//您不能删除他人发布动力圈信息
            }
        } else {
            return Response.fail(ResultMsg.DISCOVER_NOT_EXIST);//您要操作的动力圈信息不存在或已删除
        }

        return Response.success(json);
    }

    @Register
    @RequestMapping(value = {"/discoverReport"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "举报一条动力圈")
    @ApiOperation(value = "3.11 举报一条动力圈")
    public Response discoverReport(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "discoverId:动力圈ID") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"discoverId"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        this.discoverService.updatediscoverDel(json.getLong("discoverId"), WebConstants.Boolean.TRUE.ordinal());
        return Response.success(json);
    }


    @Register
    @RequestMapping(value = {"/discoverSave"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "发布动力圈")
    @ApiOperation(value = "3.12 发布动力圈")
    public Response discoverSave(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "discoverContent:动力圈内容，imgUrls:图片链接(单张或多张)") @RequestBody JSONObject json
    ) throws Exception {
        if(!ValidateUtil.jsonValidateWithKey(json, "discoverContent") && !ValidateUtil.jsonValidateWithKey(json, "imgUrls")) {
            return Response.fail(ResultMsg.DISCOVER_EMTYY);//动力圈发布内容不能为空
        }
        Date currTime = new Date();
        Discover discover = new Discover();
        discover.setCreateTime(currTime);
        discover.setUpdateTime(currTime);
        discover.setCreateUserId(userId);
        if(ValidateUtil.jsonValidateWithKey(json, "discoverContent")) {
            discover.setDiscoverContent(json.getString("discoverContent"));
        }
        if(ValidateUtil.jsonValidateWithKey(json, "location")) {
            discover.setLocation(json.getString("location"));
        }
        List<DiscoverImg> discoverImgList = new LinkedList<DiscoverImg>();
        if(ValidateUtil.jsonValidateWithKey(json, "imgUrls")) {
            JSONArray imgUrlArray = (JSONArray) json.get("imgUrls");
            int i = 1;
            for (Object imgUrl : imgUrlArray) {
                DiscoverImg discoverImg = new DiscoverImg();
                discoverImg.setImgUrl(imgUrl.toString());
                discoverImg.setSortNum(i);
                discoverImgList.add(discoverImg);
                i++;
            }
        }
        discoverService.saveDiscover(discover, discoverImgList);
        //动力圈保存完成后，开始封装返回信息
        JSONObject resJson = new JSONObject();
        resJson.put("discoverId", discover.getDiscoverId());
        resJson.put("discoverContent", StringUtil.isNotEmpty(discover.getDiscoverContent()) ?  discover.getDiscoverContent() : "");
        resJson.put("location", StringUtil.isNotEmpty(discover.getLocation()) ?  discover.getLocation() : "");
        resJson.put("upvoteAmount", discover.getUpvoteAmount());
        resJson.put("commentAmount", discover.getCommentAmount());
        resJson.put("createTime", DateUtil.getTimeFormatText(discover.getCreateTime().getTime()));
        resJson.put("delPower", WebConstants.Boolean.TRUE.ordinal());//自己发布的可以删除
        resJson.put("ifUpvote", WebConstants.Boolean.FALSE.ordinal());//尚未点赞
        resJson.put("ifReport", WebConstants.Boolean.FALSE.ordinal());//尚未举报
        resJson.put("imgUrls", json.get("imgUrls"));//发布的动力圈图片集合
        resJson.put("userImgUrls", new JSONArray());//点赞头像集合为空
        resJson.put("commentList", new JSONArray());//评论集合为空
        /* 返回信息格式
        discoverId	int	动力圈ID
        username	String	发布人姓名或昵称(根据隐私保护开关)
        schoolName	String	发布人学校名称
        userImg	String	发布人头像链接
        discoverContent	String	发布内容
        location	String	地理位置
        upvoteAmount	int	点赞量
        commentAmount	int	评论数
        createTime	String	发布时间
        delPower	int	是否能删除(0=不能，1=能)
        ifUpvote	int	当前用户是否已经点赞
        ifReport	int	当前用户是否已经举报
        imgUrls	String[]	图片链接(单张或多张)
        userImgUrls	String[]	点赞头像链接(n>=1)
        commentList	JsonArray	评论列表，默认第1页15条评论(不需要指定pageindex和pagesize参数)
         */
        Map<String, Object> userInfoMap = userService.getUserInfoForDiscover(userId);
        UserSecret userSecret = userSecretService.getUserSecretByUserId(userId);//用户是否设置了隐私保护
        resJson.put("schoolName", userInfoMap.get("schoolName"));
        resJson.put("userImg", userInfoMap.get("userImg"));
        if(null != userSecret && WebConstants.Boolean.TRUE.ordinal() == userSecret.getSecretControl()) {
            resJson.put("username", userInfoMap.get("nickName"));
        } else {
            resJson.put("username", userInfoMap.get("username"));
        }
        return Response.success(resJson);
    }
}
