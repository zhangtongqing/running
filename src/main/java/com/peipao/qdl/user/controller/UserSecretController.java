package com.peipao.qdl.user.controller;


import com.peipao.framework.annotation.Register;
import com.peipao.framework.annotation.SystemControllerLog;
import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.constant.ReturnConstant;
import com.peipao.framework.model.Response;
import com.peipao.framework.util.ValidateUtil;
import com.peipao.qdl.user.model.UserSecret;
import com.peipao.qdl.user.service.UserSecretService;
import io.swagger.annotations.*;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 方法名称：UserSecretController
 * 功能描述：用户隐私保护开关
 * 作者：zhangqg
 * 版本：1.0
 * 创建日期：2017/10/24 16:20
 * 修订记录：
 */
@RestController
@RequestMapping({"/user/secret"})
@Api(value = "/user/secret", description = "用户隐私保护开关")
public class UserSecretController {
	
	@Autowired
    private UserSecretService userSecretService;

	
	@Register
    @RequestMapping(value = {"/app/updateUserSecret"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "用户隐私保护开关-app")
    @ApiOperation(value = "根据用户ID修改用户隐私保护开关")
    @ApiResponses({@ApiResponse(code = 200,message = "操作成功"),
                   @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
                   @ApiResponse(code = 10004,message = "参数错误"),
                   @ApiResponse(code = 9001,message = "其他异常")})
	public Response<?> updateUserSecret(@ApiParam(required = true, value = "token") @RequestParam String token,
	        							@ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
	        							@ApiParam(required = true, value = "签名") @RequestParam String sign,
	        							@ApiParam(required = true, value = "status:隐私保护开关[必填,默认0=关闭;1=开启隐私保护]") @RequestBody JSONObject json
	    ) throws Exception { 
		
		int secretControl = 0;
        String[] params = new String[]{"status"};//必填项
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.USER_SECRET_STATUS);//必填项请求参数不完整，请检查
        }else{
            String status = json.getString("status");
            secretControl = Integer.parseInt(status);
        }

        UserSecret userSecret = userSecretService.getUserSecretByUserId(userId);
		if (userSecret == null) {  // 添加
			userSecret = new UserSecret();
			userSecret.setUserId(userId);
			userSecret.setSecretControl(secretControl);
			userSecret.setCreateTime(new Date());
			userSecretService.insertUserSecret(userSecret);
			
		} else { // 修改
			Map<String,String> map = new HashMap<String,String>();
			map.put("userId", String.valueOf(userId));
			map.put("secretControl", String.valueOf(secretControl));
			userSecretService.updateUserSecretByUserId(map);
		}

		return Response.success(json);
	}


    @Register
    @RequestMapping(value = {"/app/queryUserSecret"},method = {RequestMethod.POST})
    @SystemControllerLog(description = "用户隐私保护开关-app")
    @ApiOperation(value = "根据用户ID获取用户隐私开关状态")
    @ApiResponses({@ApiResponse(code = 200,message = "操作成功"),
            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
            @ApiResponse(code = 10004,message = "参数错误"),
            @ApiResponse(code = 9001,message = "其他异常")})
    public Response<?> queryUserSecret(@ApiParam(required = true, value = "token") @RequestParam String token,
                                       @ApiParam(required = true, value = "学生Id") @RequestParam Long userId,
                                       @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {

	    JSONObject jsonObject = new JSONObject();
        if(StringUtils.isNotBlank(String.valueOf(userId))){
            UserSecret userSecret = userSecretService.getUserSecretByUserId(userId);
            if(userSecret != null){
                jsonObject.put("status",userSecret.getSecretControl());
            }else{
                jsonObject.put("status",0);
            }
        }
        return Response.success(jsonObject);
    }

}
