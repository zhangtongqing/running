package com.peipao.framework.filter;



//import net.sf.json.JSONObject;
//import org.apache.http.HttpResponse;
//import org.springframework.util.CollectionUtils;
//import org.springframework.web.context.support.WebApplicationContextUtils;
//import org.springframework.web.context.support.XmlWebApplicationContext;
//import java.util.Map;

import com.peipao.framework.constant.ReturnConstant;
import com.peipao.framework.util.SignFormat;
import com.peipao.framework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


/**
 * 安全过滤器
 *
 * @author Meteor.wu
 * @since 2017/11/13 17:36
 */
@WebFilter(filterName = "SecurityFilter", urlPatterns = "/*")
public class SecurityFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(SecurityFilter.class);
//    private UserDao userDao;
//    private CourseDao courseDao;
//    private ActivityDao activityDao;

    private enum flagEnum {
        SIGN_ERROR, SCHOOL_ERROR, OK
    }

    private static final List<String> mothedList = new ArrayList<String>(){{
        add("/version/app/getMaxVersionByClient");
        add("/statistics/app/getStatistics");
        add("/appeal/app/getQAList");
        add("/appeal/app/getQAContent");
        add("/runningRule/app/getRuleTip");
        add("/nodefile/notify");
        add("/util/checkToken");
        add("/activity/pc/addActivity");
        add("/activity/pc/updateActivity");
        add("/activity/pc/addOfficalActivity");
        add("/activity/pc/updateOfficialActivity");
        add("/schoolUser/pc/getStudentList");
        add("/schoolUser/pc/downloadStudentList");
        add("/activity/pub/getActivityDetail");
        add("/luckdraw/app/luckRecord");
        add("/appeal/app/addFeedback");
    }};

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
//        ServletContext sc = filterConfig.getServletContext();
//        XmlWebApplicationContext cxt = (XmlWebApplicationContext) WebApplicationContextUtils.getWebApplicationContext(sc);
//        if(cxt != null && cxt.getBean("userDao") != null && userDao == null){
//            userDao = (UserDao) cxt.getBean("userDao");
//        }
//        if(cxt != null && cxt.getBean("courseDao") != null && courseDao == null){
//            courseDao = (CourseDao) cxt.getBean("courseDao");
//        }
//        if(cxt != null && cxt.getBean("activityDao") != null && activityDao == null){
//            activityDao = (ActivityDao) cxt.getBean("activityDao");
//        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ServletRequest requestWrapper = null;
        PrintWriter out = null;
        String str = null;

        try{
            if(servletRequest instanceof HttpServletRequest) {
                requestWrapper = new BodyReaderHttpServletRequestWrapper((HttpServletRequest) servletRequest);
            }
            if(null == requestWrapper) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                Enum flag = flagEnum.OK;
                flag = checkSign(requestWrapper);
                if (flagEnum.OK.equals(flag)) {
                    filterChain.doFilter(requestWrapper, servletResponse);
                } else {
                    if (flagEnum.SIGN_ERROR.equals(flag)) {
                        str = "{\"code\":"+ ReturnConstant.SIGN_ERROR.value+",\"message\":\"签名错误\",\"body\":null,\"exceptionDescription\":null}";
                    } else if (flagEnum.SCHOOL_ERROR.equals(flag)) {
                        str = "{\"code\":"+ ReturnConstant.DATA_POWER_ERROR.value+",\"message\":\"数据权限错误\",\"body\":null,\"exceptionDescription\":null}";
                    } else {
                        str = "{\"code\":"+ReturnConstant.UNKNOW_ERROR.value+",\"message\":\"数据权限错误\",\"body\":null,\"exceptionDescription\":null}";
                    }
                }
            }
            if(StringUtil.isNotEmpty(str)) {
                out = servletResponse.getWriter();
                out.print(str);
                out.flush();
            }

        }catch (Exception e) {
            e.printStackTrace();
            str = "{\"code\":"+ReturnConstant.SIGN_ERROR.value+",\"message\":\"签名检查出错\",\"body\":null,\"exceptionDescription\":null}";
            out = servletResponse.getWriter();
            out.print(str);
            out.flush();
        } finally {
            if(null != out) {
                out.close();
                out = null;
            }
        }
    }

    @Override
    public void destroy() {

    }

    private Enum checkSign(ServletRequest httpServletRequest) throws Exception {
        if (((HttpServletRequest) httpServletRequest).getMethod().equals("OPTIONS")) {
            return flagEnum.OK;// CORS跨域请求，会看到两次请求记录，一次是option请求,一次是POST请求。。。option不需要签名
        }
        String resUrl = ((HttpServletRequest) httpServletRequest).getServletPath().toString();
        if (mothedList.contains(resUrl)) {
            return flagEnum.OK;
        }
        if (((HttpServletRequest) httpServletRequest).getRequestURL().toString().contains("/download/Excel")) {
            return flagEnum.OK;
        }
        // check sign
        BufferedReader br = httpServletRequest.getReader();
        StringBuilder bodyBuilder = new StringBuilder();
        String str;
        while((str = br.readLine()) != null){
            bodyBuilder.append(str);
        }
        String url = ((HttpServletRequest) httpServletRequest).getRequestURL()
                .append(httpServletRequest.getParameter("token") == null ? "" : "?token=" + httpServletRequest.getParameter("token"))
                .append(httpServletRequest.getParameter("token") == null ? "" : "&userId=" + httpServletRequest.getParameter("userId"))
                .append(httpServletRequest.getParameter("token") == null ? "?sign=" : "&sign=").toString();

//        System.out.println("来自请求的url----" + url);
        String sign = SignFormat.getSignFormat(url, bodyBuilder.toString());//服务端签名算法
        //log.info("签名信息-请求url={}, 签名={},body ={}",url,sign,bodyBuilder.toString());
        log.info("签名={}",sign);
        String parameter = httpServletRequest.getParameter("sign");//传过来的
//        System.out.println("来自请求的sign----" + parameter);
        if (null != parameter) {
            if(!parameter.equals(sign)) {
                return flagEnum.SIGN_ERROR;
            }
        } else {
            return flagEnum.SIGN_ERROR;
        }

//        long userId_body = 0L;
//        long schoolId = 0L;
//        Long schoolId_body = 0L;
//        int userType = 0;
//        String userId = httpServletRequest.getParameter("userId");//公共参数中的userId
//        if(StringUtil.isEmpty(userId)) {
//            return flagEnum.OK;
//        }
//        Map<String, Object> map = userDao.getSchoolIdUserTypeByUserId(Long.parseLong(userId));
//        if(CollectionUtils.isEmpty(map)) {
//            return flagEnum.SCHOOL_ERROR;
//        }
//        userType = Integer.parseInt(map.get("userType").toString());
//        if(userType != UserTypeEnum.OFFICIALMANAGER.getValue()) {
//            schoolId = Long.parseLong(map.get("schoolId").toString());
//            if(userType == UserTypeEnum.OFFICIALMANAGER.getValue()) {
//                return flagEnum.OK;//如果是官方管理员，不校验
//            }
//            JSONObject json = null;
//            try{
//                if(StringUtil.isNotEmpty(bodyBuilder.toString())) {
//                    json = JSONObject.fromObject(bodyBuilder.toString());
//                }
//            }catch (Exception ex) {
//                System.out.println("bodyBuilder：" + bodyBuilder.toString());
//                System.out.println("错误的JSON参数：" + json.toString());
//                ex.printStackTrace();
//            }
//            if(null != json){
//                if(json.containsKey("userId") && StringUtil.isNotEmpty(json.getString("userId"))) {
//                    userId_body = json.getLong("userId");
//                } else if(json.containsKey("studentId") && StringUtil.isNotEmpty(json.getString("studentId"))) {
//                    userId_body = json.getLong("studentId");
//                } else if(json.containsKey("teacherId") && StringUtil.isNotEmpty(json.getString("teacherId"))) {
//                    userId_body = json.getLong("teacherId");
//                }
//                if(userId_body != 0L) {
//                    schoolId_body = userDao.getSchoolIdByUserId(userId_body);
//                    if(null == schoolId_body || schoolId != schoolId_body) {
//                        return flagEnum.SCHOOL_ERROR;
//                    }
//                }
//                if(json.containsKey("courseId") && StringUtil.isNotEmpty(json.getString("courseId"))) {
//                    schoolId_body = courseDao.getSchoolIdByCourseId(json.getLong("courseId"));
//                    if(null == schoolId_body || schoolId != schoolId_body) {
//                        return flagEnum.SCHOOL_ERROR;
//                    }
//                }
//            if(json.containsKey("activityId") && StringUtil.isNotEmpty(json.getString("activityId"))) {
//                schoolId_body = activityDao.getSchoolIdByActivityId(json.getLong("activityId"));
//                if(null == schoolId_body || schoolId != schoolId_body) {
//                    return flagEnum.SCHOOL_ERROR;
//                }
//            }
//            }
//        }

        return flagEnum.OK;
    }

}
