package com.peipao.qdl.guide.controller;


import com.peipao.framework.annotation.Register;
import com.peipao.framework.annotation.SystemControllerLog;
import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.model.Response;
import com.peipao.framework.page.MyPage;
import com.peipao.framework.page.MyPageInfo;
import com.peipao.framework.util.ValidateUtil;
import com.peipao.qdl.guide.model.Guide;
import com.peipao.qdl.guide.model.GuideItem;
import com.peipao.qdl.guide.service.GuideItemService;
import com.peipao.qdl.guide.service.GuideService;
import com.peipao.qdl.guide.service.GuideUtilService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Map;

/**
 * 指南controller
 *
 * @author Meteor.wu
 * @since 2018/1/11 11:29
 */
@RestController
@RequestMapping("/guide/pc")
public class GuideControllerPC {
    @Autowired
    private GuideService guideService;

    @Autowired
    private GuideItemService guideItemService;

    @Autowired
    private GuideUtilService guideUtilService;

    @Register
    @RequestMapping(value = "/getGuideList", method = RequestMethod.POST)
    //@SystemControllerLog(description = "查询指南列表")
    @ApiOperation(value = "查询指南列表")
    public Response<?> getGuideList(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "指南查询条件title(非必填),分页数据(pageindex,pagesize)") @RequestBody JSONObject json) {
        String[] params = new String[]{"pageindex","pagesize"};    //必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);   //必填项请求参数不完整，请检查
        }
        int[] page = MyPage.isValidParams(json);
        String title = json.containsKey("title") ? json.getString("title") : null;
        MyPageInfo myPageInfo = guideService.getGuideList(title, page[0], page[1]);
        return Response.success(myPageInfo);
    }

    @Register
    @RequestMapping(value = "/getGuideById", method = RequestMethod.POST)
    @SystemControllerLog(description = "根据id指南主表")
    @ApiOperation(value = "根据id指南主表")
    public Response<?> getGuideById(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "guideId") @RequestBody JSONObject json) {
        String[] params = new String[]{"guideId"};    //必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);   //必填项请求参数不完整，请检查
        }
        Guide guide = guideService.getGuideById(json.getLong("guideId"));
        return Response.success(guideUtilService.object2Map(guide));
    }

    @Register
    @RequestMapping(value = "/updateGuide", method = RequestMethod.POST)
    @SystemControllerLog(description = "更新指南主表")
    @ApiOperation(value = "更新指南主表")
    public Response<?> updateGuide(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "guideId,title,keyword") @RequestBody Guide guide) {
        if (guide.getGuideId() == null || guide.getTitle() == null || guide.getKeyword() == null) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);   //必填项请求参数不完整，请检查
        }
        guide.setUpdateUserId(userId);
        guide.setUpdateTime(Calendar.getInstance().getTime());
        guideService.updateGuide(guide);
        return Response.success(guideUtilService.object2Map(guide));
    }

    @Register
    @RequestMapping(value = "/addGuide", method = RequestMethod.POST)
    @SystemControllerLog(description = "添加指南指引")
    @ApiOperation(value = "添加指南指引")
    public Response<?> addGuide(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "title,keyword") @RequestBody Guide guide) {
        if ( guide.getTitle() == null || guide.getKeyword() == null) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);
        }
        guide.setCreateUserId(userId);
        guide.setCreateTime(Calendar.getInstance().getTime());
        guide.setUpdateUserId(userId);
        guide.setUpdateTime(Calendar.getInstance().getTime());
        guideService.addGuide(guide);
        return Response.success(guideUtilService.object2Map(guide));
    }

    @Register
    @RequestMapping(value = "/deleteGuide", method = RequestMethod.POST)
    @SystemControllerLog(description = "删除指南主表")
    @ApiOperation(value = "删除指南主表")
    public Response<?> deleteGuide(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "guideId,title,keyword") @RequestBody JSONObject json) {
        String[] params = new String[]{"guideId"};    //必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);   //必填项请求参数不完整，请检查
        }
        guideService.deleteGuide(json.getLong("guideId"));
        return Response.success(json.getLong("guideId"));
    }

    /**************************************  子表功能  ***********************************************/
    @Register
    @RequestMapping(value = "/getGuideItemList", method = RequestMethod.POST)
    @SystemControllerLog(description = "删除指南主表")
    @ApiOperation(value = "删除指南主表")
    public Response<?> getGuideSubList(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "guideId,title,keyword") @RequestBody JSONObject json) {
        String[] params = new String[]{"guideId"};    //必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);
        }
        Map map = guideItemService.getGuideItemList(json.getLong("guideId"));
        return Response.success(map);
    }

    @Register
    @RequestMapping(value = "/getGuideItemById", method = RequestMethod.POST)
    @SystemControllerLog(description = "根据id指南项")
    @ApiOperation(value = "根据id指南项")
    public Response<?> getGuideItemById(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "guideId,title,keyword") @RequestBody JSONObject json) {
        String[] params = new String[]{"guideId"};    //必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);
        }
        GuideItem guideItem = guideItemService.getGuideItemById(json.getLong("guideId"));
        return Response.success(guideUtilService.object2Map(guideItem));
    }

    @Register
    @RequestMapping(value = "/updateGuideItem", method = RequestMethod.POST)
    @SystemControllerLog(description = "更新指南项")
    @ApiOperation(value = "更新指南项")
    public Response<?> updateGuideItem(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "guideId,title,keyword") @RequestBody GuideItem guideItem) {
        if (guideItem.getGuideItemId() == null || guideItem.getItemTitle() == null || guideItem.getDesc() == null || guideItem.getType() == null) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);
        }
        guideItemService.updateGuideItem(guideItem);

        return Response.success(guideUtilService.object2Map(guideItem));
    }

    @Register
    @RequestMapping(value = "/addGuideItem", method = RequestMethod.POST)
    @SystemControllerLog(description = "添加指南项")
    @ApiOperation(value = "添加指南项")
    public Response<?> addGuideItem(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "guideId,title,keyword") @RequestBody GuideItem guideItem) {
        if (guideItem.getGuideId() == null || guideItem.getItemTitle() == null || guideItem.getDesc() == null || guideItem.getType() == null) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);
        }
        guideItem.setCreateTime(Calendar.getInstance().getTime());
        guideItem.setCreateUserId(userId);
        guideItem.setUpdateTime(Calendar.getInstance().getTime());
        guideItem.setUpdateUserId(userId);
        guideItemService.addGuideItem(guideItem);
        return Response.success(guideUtilService.object2Map(guideItem));
    }

    @Register
    @RequestMapping(value = "/deleteGuideItem", method = RequestMethod.POST)
    @SystemControllerLog(description = "删除指南项")
    @ApiOperation(value = "删除指南项")
    public Response<?> deleteGuideItem(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "guideId,title,keyword") @RequestBody GuideItem guideItem) {
        if (guideItem.getGuideItemId() == null ) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);
        }
        guideItemService.deleteGuideItem(guideItem.getGuideItemId());
        return Response.success(guideItem.getGuideItemId());
    }
}
