package com.peipao.qdl.user.controller;


import com.peipao.framework.annotation.Register;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

/**
 * 方法名称：UtilController
 * 功能描述：UtilController
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/11/2 16:47
 * 修订记录：
 */
@Api(value = "/util", description = "工具接口类")
@RestController
@RequestMapping({"/util"})
public class UtilController {

    @Register
    @RequestMapping(value={"/checkToken"},method = {RequestMethod.POST})
    public String checkToken() throws Exception {
        return "SUCCESS";
    }
}
