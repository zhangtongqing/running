package com.peipao.qdl.version.controller;


import com.peipao.framework.annotation.Register;
import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.model.Response;
import com.peipao.framework.page.MyPage;
import com.peipao.framework.page.MyPageInfo;
import com.peipao.framework.util.DateUtil;
import com.peipao.framework.util.ValidateUtil;
import com.peipao.qdl.version.model.AppTypeEnum;
import com.peipao.qdl.version.model.ClientType;
import com.peipao.qdl.version.model.Version;
import com.peipao.qdl.version.service.VersionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 方法名称：VersionController
 * 功能描述：VersionController
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/10/31 16:18
 * 修订记录：
 */

@Api(value = "/version/pc", description = "版本管理")
@RestController
@RequestMapping({"/version/pc"})
public class VersionControllerPC {

    @Autowired
    private VersionService versionService;

    @Register
    @RequestMapping(value={"/getVersionList"},method = {RequestMethod.POST})
//    @SystemControllerLog(description = "查询版本信息列表")
    @ApiOperation(value = "查询版本信息列表")
    public Response getVersionList(
            @ApiParam(required = true, value = "用户Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "pageindex:页码(必填),pagesize(非必填),appType(非必填),clientType(非必填)") @RequestBody JSONObject json
    ) throws Exception {
        int[] pageParams = MyPage.checkPageParams(json);
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        if(ValidateUtil.jsonValidateWithKey(json, "clientType")) {
            int clientType = json.getInt("clientType");
            if(clientType == ClientType.ANDROID.ordinal() || clientType == ClientType.IOS.ordinal()) {
                paramsMap.put("clientType", clientType);
            }
        }
        if(ValidateUtil.jsonValidateWithKey(json, "appType")) {
            int appType = json.getInt("appType");
            if(appType == AppTypeEnum.STUDENT.ordinal() || appType ==  AppTypeEnum.TEACHER.ordinal()) {
                paramsMap.put("appType", appType);
            }
        }
        MyPageInfo pageInfo =  versionService.getVersionList(pageParams, paramsMap);
        return Response.success(pageInfo);
    }


    @Register
    @RequestMapping(value={"/versionSave"},method = {RequestMethod.POST})
//    @SystemControllerLog(description = "保存版本信息")
    @ApiOperation(value = "保存版本信息")
    public Response versionSave(
            @ApiParam(required = true, value = "用户Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "版本信息实体类字段") @RequestBody Version version
    ) throws Exception {
        if(null == version || 0 == version.getVersionCode()) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        String constraintUpdateCodes = version.getConstraintUpdateCodes();
        String tipUpdateCodes = version.getTipUpdateCodes();
        if(StringUtils.isNotBlank(constraintUpdateCodes)) {
            version.setConstraintUpdateCodes(getCodeString(constraintUpdateCodes));
        }
        if(StringUtils.isNotBlank(tipUpdateCodes)) {
            version.setTipUpdateCodes(getCodeString(tipUpdateCodes));
        }
        if(null != version.getVersionId() && 0L != version.getVersionId()) {
            //页面传过来主键ID,修改保存
            Version v = this.versionService.getVersionById(version.getVersionId());
            if(null == v) {
                return Response.fail(ResultMsg.VERSION_NOT_EXIST);//您要修改的版本信息不存在
            }
            version.setCreateTime(v.getCreateTime());
            version.setCreateUserId(v.getCreateUserId());
            version.setUpdateTime(new Date());
            version.setUpdateUserId(userId);
        }
        version.setCreateTime(new Date());
        version.setCreateUserId(userId);
        this.versionService.insertOrUpdateVersion(version);

        JSONObject resjson = new JSONObject();
        resjson.put("key", version.getVersionId());
        resjson.put("versionId", version.getVersionId());
        resjson.put("versionCode", version.getVersionCode());
        resjson.put("clientType", version.getClientType());
        resjson.put("appType", version.getAppType());
        resjson.put("updateType", version.getUpdateType());
        resjson.put("createTime", DateUtil.dateToStr(version.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        return Response.success(resjson);
    }


    @Register
    @RequestMapping(value={"/getVersionById"},method = {RequestMethod.POST})
//    @SystemControllerLog(description = "查看版本信息明细")
    @ApiOperation(value = "查看版本信息明细")
    public Response getVersionById(
            @ApiParam(required = true, value = "用户Id") @RequestParam Long userId,
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "签名") @RequestParam String sign,
            @ApiParam(required = true, value = "versionId 版本主键ID") @RequestBody JSONObject json
    ) throws Exception {
        String[] params = new String[]{"versionId"};//必填项检查
        if(!ValidateUtil.jsonWithNotEmptyKeys(json, params)) {
            return Response.fail(ResultMsg.PARAMETER_NOT_ENOUGH);//必填项请求参数不完整，请检查
        }
        Version version = this.versionService.getVersionById(json.getLong("versionId"));
        return Response.success(version);
    }

    /**
     * 处理版本拼接字符串
     * @param str
     * @return String
     */
    private String getCodeString(String str) {
        if(str.contains("；")) {//处理中文分号
            str = str.replaceAll("\\；", ";");
        }
        if(str.lastIndexOf(";") == str.length()-1) {
            str = str.substring(0, str.length()-1);
        }
        return str;
    }

}
