package com.peipao.qdl.appeal.controller;


import com.peipao.framework.annotation.Register;
import com.peipao.framework.annotation.SystemControllerLog;
import com.peipao.framework.model.Response;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.util.StringUtil;
import com.peipao.framework.util.ValidateUtil;
import com.peipao.qdl.appeal.model.QA;
import com.peipao.qdl.appeal.service.AppealService;
import io.swagger.annotations.*;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author meteor.wu
 * @since 2017/6/27
 **/
@Api(value = "/appeal/pc",description = "申诉")
@RestController
@RequestMapping({"/appeal/pc"})
public class AppealControllerPC {
    @Autowired
    private AppealService appealService;

//    @Register
//    @RequestMapping(value = {"/getAppealListByStatus"},method = {RequestMethod.GET})
////    @ApiOperation(value = "根据申诉状态查询申诉列表", notes = "老师端")
//    @ApiResponses({@ApiResponse(code = ReturnConstant.SUCCESS.value, message = ReturnConstant.SUCCESS.desc),
//            @ApiResponse(code = ReturnConstant.PARAMETER_INCORRECT.value,message = ReturnConstant.PARAMETER_INCORRECT.desc),
//            @ApiResponse(code = ReturnConstant.NO_PERMISSION.value, message = ReturnConstant.NO_PERMISSION.desc),
//            @ApiResponse(code = ReturnConstant.USER_NOT_EXIST.value, message = ReturnConstant.USER_NOT_EXIST.desc),
//            @ApiResponse(code = ReturnConstant.SCHOOL_NOT_EXIST.value, message = ReturnConstant.SCHOOL_NOT_EXIST.desc),
//            @ApiResponse(code = ReturnConstant.OTHER_ERROR.value, message = ReturnConstant.OTHER_ERROR.desc)})
//    public Response<Appeal> getAppealListByStatus(@ApiParam(required = true, value = "token") @RequestParam String token,
//                                                   @ApiParam(required = true, value = "学校ID") @RequestParam String schoolCode,
//                                                   @ApiParam(required = true, value = "老师userId") @RequestParam Long userId,
//                                                   @ApiParam(required = true, value = "申诉状态") @RequestParam Byte status,
//                                                   @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
//        if (userId == null || StringUtil.isEmpty(schoolCode) || status == null) {
//            Response.fail(ReturnStatus.PARAMETER_INCORRECT);
//        }
//        return Response.success();
//    }


    @Register
    @RequestMapping(value = {"/addQA"}, method = {RequestMethod.POST})
    @SystemControllerLog(description = "增加常见问题")
    @ApiOperation(value = "增加常见问题")
    public Response<?> addQA(@ApiParam(required = true, value = "token") @RequestParam String token,
                             @ApiParam(required = true, value = "老师userId") @RequestParam Long userId,
                             @ApiParam(required = true, value = "json:titile,content") @RequestBody QA qa,
                             @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (StringUtil.isEmpty(qa.getContent()) || StringUtil.isEmpty(qa.getTitle())) {
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }

        appealService.addQA(userId, qa);
        return Response.success(qa);
    }

    @Register
    @RequestMapping(value = {"/updateQA"}, method = {RequestMethod.POST})
    @SystemControllerLog(description = "增加常见问题")
    @ApiOperation(value = "增加常见问题")
    public Response<?> updateQA(@ApiParam(required = true, value = "token") @RequestParam String token,
                             @ApiParam(required = true, value = "老师userId") @RequestParam Long userId,
                             @ApiParam(required = true, value = "json:qaId") @RequestBody QA qa,
                             @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (qa.getQaId() == null) {
            return Response.fail(ReturnStatus.PARAMETER_EMPTY);
        }
        appealService.updateQA(userId, qa);
        return Response.success(qa);
    }



    @Register
    @RequestMapping(value = {"/getQAItem"}, method = {RequestMethod.POST})
//    @SystemControllerLog(description = "根据id查询常见问")
    @ApiOperation(value = "根据id查询常见问题")
    public Response<?> getQAItem(
            @ApiParam(required = true, value = "token") @RequestParam String token,
            @ApiParam(required = true, value = "老师userId") @RequestParam Long userId,
            @ApiParam(required = true, value = "老师userId") @RequestBody JSONObject json,
            @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (!json.containsKey("qaId") || !ValidateUtil.isDigits(json.getString("qaId"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        Long qaId = json.getLong("qaId");
        return Response.success(appealService.getQaById(qaId));
    }

    @Register
    @RequestMapping(value = {"/deleteQA"}, method = {RequestMethod.POST})
    @SystemControllerLog(description = "删除常见问题")
    @ApiOperation(value = "删除常见问题")
    public Response<?> deleteQA(@ApiParam(required = true, value = "token") @RequestParam String token,
                                @ApiParam(required = true, value = "老师userId") @RequestParam Long userId,
                                @ApiParam(required = true, value = "json:qaId") @RequestBody JSONObject json,
                                @ApiParam(required = true, value = "签名") @RequestParam String sign) throws Exception {
        if (!json.containsKey("qaId") || !ValidateUtil.isDigits(json.getString("qaId"))) {
            return Response.fail(ReturnStatus.PARAMETER_INCORRECT);
        }
        Long qaId = json.getLong("qaId");
        appealService.deleteQA(userId, qaId);
        return Response.success(qaId);
    }
}
