package com.peipao.qdl.version.controller;


import com.peipao.framework.annotation.Visitor;
import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.model.Response;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.util.StringUtil;
import com.peipao.qdl.version.model.AppTypeEnum;
import com.peipao.qdl.version.model.ClientType;
import com.peipao.qdl.version.model.Version;
import com.peipao.qdl.version.service.VersionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

/**
 * 方法名称：VersionController
 * 功能描述：VersionController
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/10/31 13:59
 * 修订记录：
 */

@Api(value = "/version/app", description = "版本管理")
@RestController
@RequestMapping({"/version/app"})
public class VersionControllerAPP {
    @Autowired
    private VersionService versionService;

    @Visitor
    @RequestMapping(value={"/getMaxVersionByClient"},method = {RequestMethod.GET})
//    @SystemControllerLog(description = "查询安卓或苹果最新版本信息")
    @ApiOperation(value = "查询安卓或苹果最新版本信息")
    public Response getMaxVersionByClient(HttpServletRequest request) throws Exception {
        if(null == request.getParameter("t") || StringUtils.isBlank(request.getParameter("t").toString())) {
            return Response.fail(ResultMsg.VERSION_CLIENT_MISS);//缺少客户端类型参数，请检查
        }
        String t = request.getParameter("t").toUpperCase();
        if(StringUtil.isNotEmpty(request.getParameter("t"))) {
            int clientType = -1;//没有传该参数的话，默认为 -1 (无任何意义)
            if(t.length() > ClientType.ANDROID.name().length()) {
                t = t.substring(0, ClientType.ANDROID.name().length());
            }
            if(t.equals(ClientType.IOS.toString())) {
                clientType = ClientType.IOS.ordinal();
            } else if(t.equals(ClientType.ANDROID.toString())) {
                clientType = ClientType.ANDROID.ordinal();
            } else {
                return Response.fail(ResultMsg.VERSION_CLIENT_ERROR);//客户端类型参数错误，请检查
            }
            Version version = versionService.getMaxVersionByClient(clientType, AppTypeEnum.STUDENT.ordinal());
            if(null != version) {
                JSONObject resJson = new JSONObject();
                resJson.put("versionCode", version.getVersionCode());//版本号
                resJson.put("versionCodeMin", version.getVersionCodeMin());//可以正常支持的最小app版本号
                resJson.put("clientType", version.getClientType());//客户端类型
                resJson.put("downloadUrl", version.getDownloadUrl());//app安装包下载地址(安卓端使用)
                resJson.put("updateType", version.getUpdateType());//更新类型
                resJson.put("updateContent", version.getUpdateContent());//更新说明

                String constraintUpdateCodes = version.getConstraintUpdateCodes();
                String tipUpdateCodes = version.getTipUpdateCodes();

                if(StringUtils.isNotBlank(constraintUpdateCodes)) {
                    if(constraintUpdateCodes.contains(";")) {
                        String[] cList = constraintUpdateCodes.split(";");
                        int[] c = new int[cList.length];
                        int i = 0;
                        for (String cStr : cList) {
                            c[i] = Integer.parseInt(cStr);
                            i++;
                        }
                        resJson.put("constraintUpdateCodes", c);//有条件的强制更新的版本号信息
                    } else {
                        int[] c = new int[1];
                        c[0] = Integer.parseInt(constraintUpdateCodes);
                        resJson.put("constraintUpdateCodes", c);
                    }
                } else {
                    resJson.put("constraintUpdateCodes", new JSONArray());
                }

                if(StringUtils.isNotBlank(tipUpdateCodes)) {
                    if(tipUpdateCodes.contains(";")) {
                        String[] tList = tipUpdateCodes.split(";");
                        int[] tip = new int[tList.length];
                        int i = 0;
                        for (String tStr : tList) {
                            tip[i] = Integer.parseInt(tStr);
                            i++;
                        }
                        resJson.put("tipUpdateCodes", tip);//有条件的提示更新的版本号信息
                    } else {
                        int[] tip = new int[1];
                        tip[0] = Integer.parseInt(tipUpdateCodes);
                        resJson.put("tipUpdateCodes", tip);
                    }
                } else {
                    resJson.put("tipUpdateCodes", new JSONArray());
                }
                return Response.success(resJson);
            }
            return Response.success();
        }
        return Response.fail(ReturnStatus.PARAMETER_INCORRECT);//缺少参数
    }

}
