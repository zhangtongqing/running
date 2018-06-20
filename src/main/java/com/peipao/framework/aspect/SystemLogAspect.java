package com.peipao.framework.aspect;

import com.peipao.framework.log.model.SystemLog;
import com.peipao.framework.log.service.SystemLogService;
import com.peipao.framework.util.IpUtil;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 方法名称：SystemLogAspect
 * 功能描述：controller切点
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/8/11 17:34
 * 修订记录：
 */
@Aspect
@Component
public class SystemLogAspect {
    @Resource
    private SystemLogService systemLogService;

    //本地异常日志记录对象
    private  static  final Logger logger = Logger.getLogger(SystemLogAspect.class);

    //controller层切入点
    @Pointcut("@annotation(com.peipao.framework.annotation.SystemControllerLog)")
    public void controllerAspect() {
        System.out.println("========controllerAspect===========");
    }


    /**
     * 前置通知 用于拦截Controller层操作
     * @param joinPoint 切点
     */
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
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
            // *========控制台输出=========*//
            System.out.println("=====前置通知开始=====");
            System.out.println("请求IP:" + ip);
            System.out.println("请求方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
            System.out.println("方法描述:" + AspectUtils.getControllerMethodDescription(joinPoint));
            System.out.println("用户ID:" + userId);
            System.out.println("用户username:" + username);
            // *========数据库日志=========*//
            SystemLog log = new SystemLog();
            log.setDescription(AspectUtils.getControllerMethodDescription(joinPoint));
            log.setMethod((joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
            log.setIp(ip);
            log.setUserId(userId);
            log.setUsername(username);
            log.setMobile(mobile);
            log.setCreateDate(new Date());
            log.setClientType(clientType);
            log.setVersion(version);
            // 保存数据库
            try {
                systemLogService.insertSystemLog(log);//异步
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("==前置通知log保存出错,请检查==");
                logger.error("异常信息:", e);
            }
            System.out.println("=====前置通知结束=====");
        } catch (Exception ex) {
            ex.printStackTrace();
            // 记录本地异常日志
            logger.error("==前置通知异常==");
            logger.error("异常信息:{}", ex);
        }
    }

}
