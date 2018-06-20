package com.peipao.framework.event;

import com.peipao.framework.constant.ResultMsg;
import com.peipao.framework.exception.EventException;
import com.peipao.framework.interceptor.GlobalExceptionHandler;
import com.peipao.framework.log.model.SystemLog;
import com.peipao.framework.log.service.SystemLogService;
import com.peipao.framework.util.StringUtil;
import com.peipao.qdl.exception.model.ExceptionInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 方法名称：EventExceptionService
 * 功能描述：EventExceptionService
 * 作者：Liu Fan
 * 版本：2.0.11
 * 创建日期：2018/1/30 18:04
 * 修订记录：
 */

@Service
public class EventExceptionService {
    @Autowired
    private SystemLogService systemLogService;
    private final Logger logger = Logger.getLogger(GlobalExceptionHandler.class);

    public void eventExceptionHandler(EventException eventException, Exception e) {
        try {
            ResultMsg resultMsg = eventException.getResultMsg();
            String params = eventException.getParams();
            StackTraceElement[] stackTraceElements = e.getStackTrace();
            /* ========控制台输出========= */
            System.out.println("=====异常通知开始=====");
            String errStr = eventException.getMessage();
            errStr = "[" + errStr + "]--" + e.getMessage();
            System.out.println("异常方法:" + eventException.getEventKey());
            System.out.println("异常编号:" + resultMsg.getCode());
            System.out.println("异常信息:" + resultMsg.getMsg());
            System.out.println("异常描述:" + e.getMessage());

            if(null != errStr && errStr.length() > 500) {
                errStr = errStr.substring(0, 500);
            }
            if(StringUtil.isNotEmpty(params) && params.length() > 1000) {
                params = params.substring(0, 1000);
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

            /* ==========数据库日志========= */
            SystemLog log = new SystemLog();
            log.setMethod(eventException.getEventKey());
            log.setDescription(resultMsg.getMsg());
            log.setExceptionCode(resultMsg.getCode());
            log.setExceptionDetail(errStr);
            log.setCreateDate(new Date());
            log.setParams(params);
            log.setPath(path.length() > 200 ? path.substring(0, 200) : path);
            // 保存数据库
            systemLogService.insertSystemLogAdmin(log);//测试时所有抛出的异常都保存
            System.out.println("=====异常通知结束=====");
        } catch (Exception exp) {
            // 记录本地异常日志
            exp.printStackTrace();
            logger.error("==异常通知异常==");
            logger.error("异常信息:{}", exp);
        }
    }

}
