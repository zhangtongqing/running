package com.peipao.framework.interceptor;


import com.peipao.framework.annotation.Register;
import com.peipao.framework.constant.WebConstants;
import com.peipao.framework.exception.BusinessException;
import com.peipao.framework.model.ReturnStatus;
import com.peipao.framework.util.DateUtil;
import com.peipao.framework.util.ServiceUtils;
import com.peipao.framework.util.StringUtil;
import com.peipao.qdl.user.model.User;
import com.peipao.qdl.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UserService userService;


    private enum User_Auth {
        NO_AUTH, AUTH
    }

    private static String getFullURL(HttpServletRequest request) {
        StringBuffer requestURL = request.getRequestURL();
        String queryString = request.getQueryString();
        if (StringUtil.isEmpty(queryString)) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj, Exception arg3)
            throws Exception {

        String url = request.getRequestURI();
        String userId = request.getParameter("userId");
        if (!(url.startsWith("/api/Void") || url.startsWith("/api/Response")
                || url.startsWith("/api/string") || url.startsWith("/api/docs"))) {

            double longitude = request.getHeader(WebConstants.LONGITUDE) == null ? 0d
                    : Double.valueOf(request.getHeader(WebConstants.LONGITUDE));
            double latitude = request.getHeader(WebConstants.LATITUDE) == null ? 0d
                    : Double.valueOf(request.getHeader(WebConstants.LATITUDE));
            if (longitude == 0) {
                longitude = StringUtil.isEmpty(request.getParameter(WebConstants.LONGITUDE)) ? 0d : Double.valueOf(request.getParameter(WebConstants.LONGITUDE));
            }
            if (latitude == 0) {
                latitude = StringUtil.isEmpty(request.getParameter(WebConstants.LATITUDE)) ? 0d : Double.valueOf(request.getParameter(WebConstants.LATITUDE));
            }

//            String userAgent = request.getHeader("user-agent");
//            if(StringUtil.isNotEmpty(userAgent)) {
//
//            }
        }

    }
    
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj,
                           ModelAndView modelAndView) throws Exception {

    }

    @SuppressWarnings("rawtypes")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long startTime = System.currentTimeMillis();
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "0");
        response.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("XDomainRequestAllowed","1");

        request.setAttribute("accessTime", DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
        request.setAttribute("startTime", startTime);

        //checkSign(request);//签名验证方法有个问题没解决，暂时注释

        if (handler instanceof HandlerMethod) {
            //checkIfOldApp(request, response);//app端没有假如版本控制代码的，请求时直接返回token错误，让用户无法登陆使用
            HandlerMethod detailHandlerMethod = (HandlerMethod) handler;
            //AccessToken检查
//            Object obj = SecurityUtils.getSubject().getPrincipal();
//            if (obj == null) {
//                //如果是用户登录就不检查
////                Class clz = detailHandlerMethod.getBean().getClass();
////                if (clz != null && !clz.equals(ApkCheckAndDownloadController.class)) {
////                    List<Class> clzAnns = new ArrayList<>();
////                    for (Annotation ann : clz.getAnnotations()) {
////                        clzAnns.add(ann.annotationType());
////                    }
////
////                    if (clzAnns != null && clzAnns.size() > 0) {
////                        if (clzAnns.contains(RestController.class)) {//只有Rest服务检查
////                            String appKey = request.getHeader("appKey");
////                            String license = request.getHeader("license");
////                            String api = request.getRequestURI();
////                        }
////                    }
////                }
//            }

            List<Class> annList = new ArrayList<Class>();
            for (Annotation ann : detailHandlerMethod.getMethod().getAnnotations()) {
                // System.out.println("##### ANN = " + ann.annotationType());
                annList.add(ann.annotationType());
            }

            if (annList.contains(Register.class)) {// 注册用户权限
                String token = request.getParameter("token");
                Long userId = Long.valueOf(request.getParameter("userId"));

                if (StringUtil.isEmpty(token)) {
                    throw new BusinessException(ReturnStatus.NO_TOKEN);
                }

//                if (userService == null) {
//                    BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
//                    userService = (UserService) factory.getBean("userService");
//                }

                User userById = ServiceUtils.serviceUtils.userCacheService.getUserById(userId);
                if (userById == null) {
                    throw new BusinessException(ReturnStatus.PARAMETER_INCORRECT);
                }

                if (!userById.getToken().equals(token)) {
                    throw new BusinessException(ReturnStatus.FAULT_TOKEN);
                }
                return true;
            } else {// 游客权限
                // System.out.println("##### 游客权限 #####");
                return true;
            }
        }
        return true;
    }

}
