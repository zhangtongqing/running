package com.peipao.framework.log.controller;


import com.peipao.framework.annotation.Register;
import com.peipao.framework.log.service.SystemLogService;
import com.peipao.framework.model.Response;
import com.peipao.framework.page.MyPage;
import io.swagger.annotations.*;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * 方法名称：
 * 功能描述：操作日志
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/8/7 14:16
 * 修订记录：
 */
@Api(value = "/systemLog",description = "操作日志")
@RestController
@RequestMapping({"/systemLog"})
public class SystemLogController {

    @Autowired
    private SystemLogService systemLogService;

    @Register
    @RequestMapping(value = {"/pc/getSystemLog"},method = {RequestMethod.POST})
    @ApiOperation(value = "青动力官方管理系统--系统管理--操作日志--pc--新增接口", notes = "操作日志")
    @ApiResponses({@ApiResponse(code = 200,message = "操作成功完成"),
            @ApiResponse(code = 10004,message = "参数错误"),
            @ApiResponse(code = 9001,message = "其他异常")})
    public Response getSystemLog (
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "翻页参数[必填]:pageindex, pagesize; 查询所需属性：startTime[开始时间，非必填]; endTime[结束时间，非必填]; ") @RequestBody JSONObject json
    ) throws Exception {
        String startTime = null;
        String endTime = null;
        int[] page = MyPage.isValidParams(json);
        if(null != json.get("startTime")) {
            startTime = json.getString("startTime");
        }
        if(null != json.get("endTime")) {
            endTime = json.getString("endTime");
        }
        Map<String, Object> map = systemLogService.getSystemLogs(page[0], page[1], startTime, endTime);
        return Response.success(map);
    }

}
