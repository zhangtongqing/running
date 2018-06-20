package com.peipao.qdl.guide.controller;

import com.peipao.framework.annotation.Register;
import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.model.Response;
import com.peipao.qdl.guide.model.Guide;
import com.peipao.qdl.guide.service.GuideItemService;
import com.peipao.qdl.guide.service.GuideService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 指南app
 *
 * @author Meteor.wu
 * @since 2018/1/12 11:49
 */
@RestController
@RequestMapping("/guide/app")
public class GuideControllerAPP {

    @Autowired
    private GuideService guideService;

    @Autowired
    private GuideItemService guideItemService;

    @Register
    @RequestMapping(value = "/getGuideItemListByKeyword", method = RequestMethod.POST)
    @ApiOperation(value = "根据keyword查询指南项列表")
    public Response<?> getGuideItemListByKeyword(
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "keyword") @RequestBody Guide guide) {
        if(guide.getKeyword() == null) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);
        }
        guide = guideService.getGuideByGuide(guide);
        if (guide == null) {
            return Response.success(new ArrayList<>());
        }
        Map map = guideItemService.getGuideItemList(guide.getGuideId());
        List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("data");
        if (list.size() == 0) {
            map = getDefaultBrandMobile();
        }
        list.forEach(i->i.remove("key"));
        map.clear();
        map.put("keyword", guide.getKeyword());
        map.put("data", list);
        return Response.success(map);
    }

    private Map getDefaultBrandMobile() {
        Guide guide = new Guide();
        guide.setKeyword("其他品牌");
        Map map = guideItemService.getGuideItemList(guide.getGuideId());
        List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("data");
        if (list.size() == 0) {
            map = guideItemService.getGuideItemList(guide.getGuideId());
        }
        return map;
    }
}
