package com.peipao.framework.interceptor;



import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.constant.ReturnConstant;
import com.peipao.framework.exception.BusinessException;
import com.peipao.framework.log.model.SystemLog;
import com.peipao.framework.log.service.SystemLogService;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.util.IpUtil;
import com.peipao.framework.util.StringUtil;
import com.peipao.qdl.exception.model.ExceptionInfo;
import org.apache.http.HttpException;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import com.peipao.framework.model.Response;
import java.util.Date;

/**
 * 方法名称：GlobalExceptionHandler
 * 功能描述：全局异常处理
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/8/4 10:37
 * 修订记录：
 */
@RestController
@ControllerAdvice
public class GlobalExceptionHandler {
    @Autowired
    private SystemLogService systemLogService;
    private final Logger logger = Logger.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler
    public Response<?> defaultExceptionHandler(Exception ex, HttpServletRequest request) {
        System.out.println(ex);
        ExceptionInfo exceptionInfo = new ExceptionInfo();
        ReturnStatus rs = null;
        ResultMsg resultMsg = null;
        if (ex instanceof BusinessException) {
            BusinessException tex = (BusinessException) ex;
            exceptionInfo.setCode(tex.getCode());
            exceptionInfo.setUrl(request.getRequestURL().toString());
            exceptionInfo.setMessage(tex.getDescription());
            exceptionInfo.setDescription(ex.getMessage());
            if(null != tex.getReturnStatus()) {
                rs = tex.getReturnStatus();
            } else if(null != tex.getResultMsg()){
                resultMsg = tex.getResultMsg();
            }
        } else if (ex instanceof FileUploadBase.SizeLimitExceededException) {
            exceptionInfo.setCode(ReturnStatus.UPLOAD_ERROR.getValue());
            exceptionInfo.setUrl(request.getRequestURL().toString());
            exceptionInfo.setMessage(ReturnStatus.UPLOAD_ERROR.getDescription());
            exceptionInfo.setDescription(ex.getMessage());
            rs = ReturnStatus.UPLOAD_ERROR;
        } else if (ex instanceof NullPointerException) {
            exceptionInfo.setCode(ReturnStatus.NULL_POINT_ERROR.getValue());
            exceptionInfo.setUrl(request.getRequestURL().toString());
            exceptionInfo.setMessage(ReturnStatus.NULL_POINT_ERROR.getDescription());
            exceptionInfo.setDescription(ex.getMessage());
            rs = ReturnStatus.NULL_POINT_ERROR;
        }else if (ex instanceof HttpException) {
            exceptionInfo.setCode(ReturnStatus.NOT_FOUND.getValue());
            exceptionInfo.setUrl(request.getRequestURL().toString());
            exceptionInfo.setMessage(ReturnStatus.NOT_FOUND.getDescription());
            exceptionInfo.setDescription(ex.getMessage());
            rs = ReturnStatus.NOT_FOUND;
        }  else {
            exceptionInfo.setCode(ReturnStatus.OTHER_ERROR.getValue());
            exceptionInfo.setUrl(request.getRequestURL().toString());
            exceptionInfo.setMessage(ReturnStatus.OTHER_ERROR.getDescription());
            exceptionInfo.setDescription(ex.getMessage());
            rs = ReturnStatus.OTHER_ERROR;
        }

        // 获取登陆用户信息
        String userId = null;
        String username = null;
        String mobile = null;
        String clientType = null;//客户端类型
        String version = null;//版本号
        if(null != request.getParameter("userId")) {
            userId = request.getParameter("userId");
        }
        if(null != request.getParameter("username")) {
            username = request.getParameter("username");
        }
        if(null != request.getParameter("mobile")) {
            mobile = request.getParameter("mobile");
        }
        if(null != request.getParameter("t")) {
            clientType = request.getParameter("t");
        }
        if(null != request.getParameter("v")) {
            version = request.getParameter("v");
        }

        // 获取请求ip
        String ip = IpUtil.getIpAddr(request);
        try {
            StackTraceElement[] stackTraceElements = ex.getStackTrace();
            /* ========控制台输出========= */
            System.out.println("=====异常通知开始=====");
            String errStr = exceptionInfo.getMessage();
            errStr = "[" + errStr + "]--" + exceptionInfo.getDescription();
            System.out.println("异常方法:" + exceptionInfo.getUrl());
            System.out.println("异常代码:" + exceptionInfo.getCode());
            System.out.println("异常信息:" + exceptionInfo.getMessage());
            System.out.println("方法描述:" + exceptionInfo.getDescription());
            System.out.println("请求人:" + userId);
            System.out.println("请求IP:" + ip);
            System.out.println("用户username:" + username);
            System.out.println("用户mobile:" + mobile);
            /* ==========数据库日志========= */
            SystemLog log = new SystemLog();
            log.setDescription(exceptionInfo.getMessage());
            log.setExceptionCode(exceptionInfo.getCode() + "");
            if(null != errStr && errStr.length() > 500) {
                errStr = errStr.substring(0, 500);
            }
            String params = null;
            if(null != exceptionInfo.getParams() && exceptionInfo.getParams().length() > 1000) {
                params = exceptionInfo.getParams().substring(0, 1000);
            }

            log.setExceptionDetail(errStr);
            log.setMethod(exceptionInfo.getUrl());
            log.setUserId(userId);
            log.setUsername(username);
            log.setMobile(mobile);
            log.setCreateDate(new Date());
            log.setIp(ip);
            log.setClientType(clientType);
            log.setVersion(version);
            if(!StringUtil.isEmpty(params)) {
                log.setParams(params);
            }
            StringBuilder sb = new StringBuilder();
            int count = 0;
            for (int i = 0; i < stackTraceElements.length; i++) {
                StackTraceElement element = stackTraceElements[i];
                if (element.getClassName().contains("com.peipao") && !element.getClassName().contains("$")) {
                    sb.append("-").append(element.getClassName()).append("-").append(element.getLineNumber());
                    System.out.println(element.getClassName());
                    System.out.println(element.getLineNumber());
                    count++;
                }
                if (count == 2) {
                    break;
                }
            }
            String path = sb.toString();
            log.setPath(path.length() > 200 ? path.substring(0, 200) : path);
            // 保存数据库


//            systemLogService.insertSystemLogAdmin(log);//测试时所有抛出的异常都保存

            //生产只保存 -- 9001 其他异常; 2001 空指针异常
            int eCode = Integer.parseInt(log.getExceptionCode());
            if(eCode == ReturnConstant.OTHER_ERROR.value || eCode == ReturnConstant.NULL_POINT_ERROR.value) {
                systemLogService.insertSystemLogAdmin(log);
            }
            System.out.println("=====异常通知结束=====");


        } catch (Exception exp) {
            // 记录本地异常日志
            exp.printStackTrace();
            logger.error("==异常通知异常==");
            logger.error("异常信息:{}", exp);
        }
        if(null != rs) {
            return Response.fail(rs, ex);
        } else if(null != resultMsg) {
            return Response.fail(resultMsg);
        }
        return Response.fail();
    }
}
