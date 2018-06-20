package com.peipao.qdl.discover.controller;


import com.peipao.framework.annotation.Register;
import com.peipao.framework.annotation.SystemControllerLog;
import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.constant.WebConstants;
import com.peipao.framework.model.Response;
import com.peipao.framework.page.MyPage;
import com.peipao.framework.page.MyPageInfo;
import com.peipao.framework.util.JsonUtils;
import com.peipao.framework.util.ValidateUtil;
import com.peipao.qdl.discover.model.Discover;
import com.peipao.qdl.discover.model.DiscoverComment;
import com.peipao.qdl.discover.model.HotParams;
import com.peipao.qdl.discover.model.SortEnum;
import com.peipao.qdl.discover.service.DiscoverCommentService;
import com.peipao.qdl.discover.service.DiscoverService;
import com.peipao.qdl.discover.service.DiscoverUpvoteService;
import com.peipao.qdl.discover.service.HotParamsService;
import com.peipao.qdl.version.model.ClientType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 方法名称：Discover
 * 功能描述：发现-动力圈实体类
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/10/17 13:31
 * 修订记录：
 */
@Api(value = "/discover/pc", description = "发现-动力圈(pc接口)")
@RestController
@RequestMapping({"/discover/pc"})
public class DiscoverControllerPC {

    @Autowired
    private HotParamsService hotParamsService;

    @Autowired
    private DiscoverService discoverService;
    @Autowired
    private DiscoverCommentService discoverCommentService;
    @Autowired
    private DiscoverUpvoteService discoverUpvoteService;

    @Register
    @RequestMapping(value={"/addHotParams"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "添加动力圈热门规则参数")
    @ApiOperation(value = "4.10 添加动力圈热门规则参数")
    public Response addHotParams(
        @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
        @ApiParam(required = true, value = "token") @RequestParam String token,
        @ApiParam(required = true, value = "签名") @RequestParam String sign,
        @ApiParam(required = true, value = "upvoteAmount:点赞数指标;commentAmount:评论数指标") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"upvoteAmount","commentAmount"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.DISCOVER_ROLE_UPVOTEAMOUNT);//必填项请求参数不完整，请检查
        }
        HotParams hotParams = new HotParams(json.getInt("upvoteAmount"), json.getInt("commentAmount"));
        this.hotParamsService.inserOrUpdateHotParams(hotParams);
        json.put("hotParamsId",hotParams.getHotParamsId());
        return Response.success(json);
    }

    @Register
    @RequestMapping(value={"/updateHotParams"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "修改动力圈热门规则参数")
    @ApiOperation(value = "4.11 修改动力圈热门规则参数")
    public Response updateHotParams(
        @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
        @ApiParam(required = true, value = "token") @RequestParam String token,
        @ApiParam(required = true, value = "签名") @RequestParam String sign,
        @ApiParam(required = true, value = "hotParamsId:规则参数ID;upvoteAmount:点赞数指标;commentAmount:评论数指标") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"hotParamsId","upvoteAmount","commentAmount"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.DISCOVER_ROLE_UPVOTEAMOUNT);//必填项请求参数不完整，请检查
        }
        HotParams hotParams = new HotParams(json.getLong("hotParamsId"), json.getInt("upvoteAmount"), json.getInt("commentAmount"));
        this.hotParamsService.inserOrUpdateHotParams(hotParams);
        return Response.success(json);
    }

    @Register
    @RequestMapping(value = {"/getHotParams"},method = {RequestMethod.GET})
    //@SystemControllerLog(description = "查询动力圈自动热门参数")
    @ApiOperation(value = "4.1 查询动力圈自动热门参数")
    public Response getHotParams(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign
    ) throws Exception {
        JSONObject jsonObject = new JSONObject();
        HotParams hotParams = this.hotParamsService.getHotParams();
        if(hotParams == null){
            hotParams = new HotParams();
        } else {
            jsonObject.put("hotParamsId",hotParams.getHotParamsId());
        }
        jsonObject.put("upvoteAmount",hotParams.getUpvoteAmount());
        jsonObject.put("commentAmount",hotParams.getCommentAmount());
        return Response.success(jsonObject);
    }


    @Register
    @RequestMapping(value = {"/getDiscoverList"},method = {RequestMethod.POST})
    //@SystemControllerLog(description = "查询动力圈列表")
    @ApiOperation(value = "4.2 查询动力圈列表")
    public Response getDiscoverList(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "startTime:开始日期,endTime:结束日期," +
                    "isHot:是否热门,queryString:内容搜索关键字," +
                    "sortName:排序字段名称,sortType:排序方式," +
                    "pageindex(必填):页码,pagesize:每页条数") @RequestBody JSONObject json
    ) throws Exception {
        int[] pageParams = MyPage.checkPageParams(json);//检查并封装分页参数
        String[] params = new String[]{"pageindex"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }

        if(!ValidateUtil.jsonValidateWithKey(json, "sortName")) {
            json.put("sortName", "create_time");//默认按发布时间倒序排序
        } else {
            switch (json.getString("sortName")) {
                case "upvoteAmount":
                    json.put("sortName", "upvote_amount");
                    break;
                case "commentAmount":
                    json.put("sortName", "comment_amount");
                    break;
                case "reportAmount":
                    json.put("sortName", "report_amount");
                    break;
                case "createTime":
                    json.put("sortName", "create_time");
                    break;
                default:
                    json.put("sortName", "create_time");
                    break;
            }
        }

        if(ValidateUtil.jsonValidateWithKey(json, "sortType")) {
//            int iAsc = SortEnum.ASC.ordinal();
//            int iDesc = SortEnum.DESC.ordinal();
            if(SortEnum.ASC.ordinal() == json.getInt("sortType")) {
                json.put("sortType", SortEnum.ASC.name());
            } else {
                json.put("sortType", SortEnum.DESC.name());//默认都按倒序
            }
        } else {
            json.put("sortType", SortEnum.DESC.name());//默认1=降序排序(由大到小、从新到旧)
        }
        json = ValidateUtil.checkStartEndTime(json);//检查并封装startTime、endTime(都为空默认查询当天)
        Map paramsMap = JsonUtils.getMap4Json(json.toString());
        Map<String, Object> ret = discoverService.queryDiscoverList(paramsMap, pageParams);
        return Response.success(ret);
    }

    @Register
    @RequestMapping(value = {"/discoverDetails"},method = {RequestMethod.POST})
    //@SystemControllerLog(description = "查询某条动力圈详情")
    @ApiOperation(value = "4.3 查询某条动力圈详情")
    public Response discoverDetails(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "discoverId:动力圈ID") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"discoverId"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        return Response.success(discoverService.getDiscoverInfoById(json.getLong("discoverId")));
    }


    @Register
    @RequestMapping(value = {"/getCommentList"},method = {RequestMethod.POST})
    //@SystemControllerLog(description = "查询某条动力圈评论列表")
    @ApiOperation(value = "4.4 查询某条动力圈评论列表")
    public Response getCommentList(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "discoverId必填:动力圈ID;pageindex必填:页码;pagesize选填:每页条数(默认15)") @RequestBody JSONObject json
    ) throws Exception {
        int[] pageParams = MyPage.checkPageParams(json);
        String[] params = new String[]{"discoverId"};//必填项检查
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("discoverId", json.getLong("discoverId"));
        paramsMap.put("trueVal", WebConstants.Boolean.TRUE.ordinal());
        paramsMap.put("falseVal", WebConstants.Boolean.FALSE.ordinal());
        paramsMap.put("logicDelete", WebConstants.Boolean.FALSE.ordinal());
        MyPageInfo pageInfo = discoverCommentService.getCommentListByDiscoverId(paramsMap, WebConstants.Boolean.FALSE.ordinal(), pageParams);
        return Response.success(pageInfo);
    }

    @Register
    @RequestMapping(value = {"/getUpvoteList"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "查询某条动力圈点赞列表")
    @ApiOperation(value = "4.5 查询某条动力圈点赞列表")
    public Response getUpvoteList(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "discoverId必填:动力圈ID;pageindex必填:页码;pagesize选填:每页条数(默认15)") @RequestBody JSONObject json
    ) throws Exception {
        int[] pageParams = MyPage.checkPageParams(json);
        String[] params = new String[]{"discoverId"};//必填项检查
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        MyPageInfo pageInfo = discoverUpvoteService.getUpvoteByDiscoverId(json.getLong("discoverId"), ClientType.PC, pageParams);
        return Response.success(pageInfo);
    }


    @Register
    @RequestMapping(value = {"/discoverDel"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "屏蔽一条动力圈")
    @ApiOperation(value = "4.6 屏蔽一条动力圈（逻辑删除）")
    public Response discoverDel(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "discoverId必填:动力圈ID") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"discoverId"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        Long discoverId = json.getLong("discoverId");
        this.discoverService.updatediscoverDel(discoverId, WebConstants.Boolean.TRUE.ordinal());
        Discover discoverBean = this.discoverService.getDiscoverById(discoverId);
        if(null != discoverBean){
            return Response.success(this.discoverService.createReturnBodyData(discoverBean));
        }
        return Response.fail(ResultMsg.DISCOVER_NOT_EXIST);
    }


    @Register
    @RequestMapping(value = {"/discoverRecover"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "取消屏蔽一条动力圈")
    @ApiOperation(value = "4.12 取消屏蔽一条动力圈")
    public Response discoverRecover(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "discoverId必填:动力圈ID") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"discoverId"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        Long discoverId = json.getLong("discoverId");
        this.discoverService.updatediscoverDel(discoverId, WebConstants.Boolean.FALSE.ordinal());

        Discover discoverBean = this.discoverService.getDiscoverById(discoverId);
        if(null != discoverBean){
            return Response.success(this.discoverService.createReturnBodyData(discoverBean));
        }
        return Response.fail(ResultMsg.DISCOVER_NOT_EXIST);
    }

    @Register
    @RequestMapping(value = {"/discoverHot"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "动力圈设为热门")
    @ApiOperation(value = "4.7 动力圈设为热门")
    public Response discoverHot(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "discoverId必填:动力圈ID;") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"discoverId"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        Long discoverId = json.getLong("discoverId");
        this.discoverService.updatediscoverHot(discoverId, WebConstants.Boolean.TRUE.ordinal());
        Discover discoverBean = this.discoverService.getDiscoverById(discoverId);
        if(null != discoverBean){
            return Response.success(this.discoverService.createReturnBodyData(discoverBean));
        }
        return Response.fail(ResultMsg.DISCOVER_NOT_EXIST);
    }


    @Register
    @RequestMapping(value = {"/discoverNotHot"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "动力圈取消热门")
    @ApiOperation(value = "4.13 动力圈取消热门")
    public Response discoverNotHot(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "discoverId必填:动力圈ID;") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"discoverId"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        Long discoverId = json.getLong("discoverId");
        this.discoverService.updatediscoverHot(discoverId, WebConstants.Boolean.FALSE.ordinal());
        Discover discoverBean = this.discoverService.getDiscoverById(discoverId);
        if(null != discoverBean){
            return Response.success(this.discoverService.createReturnBodyData(discoverBean));
        }
        return Response.fail(ResultMsg.DISCOVER_NOT_EXIST);
    }

    @Register
    @RequestMapping(value = {"/discoverHotControl"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "动力圈禁上热门")
    @ApiOperation(value = "4.8 动力圈禁上热门")
    public Response updatediscoverHotControl(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "discoverId必填:动力圈ID;") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"discoverId"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        Long discoverId = json.getLong("discoverId");
        this.discoverService.updatediscoverHotControl(discoverId,WebConstants.Boolean.TRUE.ordinal());
        Discover discoverBean = this.discoverService.getDiscoverById(discoverId);
        if(null != discoverBean){
            return Response.success(this.discoverService.createReturnBodyData(discoverBean));
        }
        return Response.fail(ResultMsg.DISCOVER_NOT_EXIST);
    }


    @Register
    @RequestMapping(value = {"/discoverCancelHotControl"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "动力圈取消禁上热门")
    @ApiOperation(value = "4.14 动力圈取消禁上热门")
    public Response discoverCancelHotControl(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "discoverId必填:动力圈ID;") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"discoverId"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        Long discoverId = json.getLong("discoverId");
        this.discoverService.updatediscoverHotControl(discoverId,WebConstants.Boolean.FALSE.ordinal());
        Discover discoverBean = this.discoverService.getDiscoverById(discoverId);
        if(null != discoverBean){
            return Response.success(this.discoverService.createReturnBodyData(discoverBean));
        }
        return Response.fail(ResultMsg.DISCOVER_NOT_EXIST);
    }


    @Register
    @RequestMapping(value = {"/commentDel"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "删除一条动力圈评论")
    @ApiOperation(value = "4.9 删除一条动力圈评论")
    public Response commentDel(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "commentId:动力圈ID") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"commentId"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        Long commentId = json.getLong("commentId");
        DiscoverComment discoverComment = discoverCommentService.getDiscoverCommentById(commentId);
        if(null != discoverComment) {
            Map<String, Object> paramsMap = new HashMap<String, Object>();
            paramsMap.put("commentId", commentId);
            discoverCommentService.updateForCommentDelete(discoverComment.getDiscoverId(), paramsMap);
        } else {
            return Response.fail(ResultMsg.DISCOVER_COMMENTID_NOT_EXIST);//您要删除的评论不存在
        }
        return Response.success(commentId);
    }


}
