package com.peipao.framework.util;

import org.apache.commons.lang3.StringUtils;
import javax.servlet.http.HttpServletRequest;
/**
 * 方法名称：IpUtil
 * 功能描述：获得访问者Ip
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017-08-11 17:53:38
 * 修订记录：
 */
public class IpUtil {
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP。
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            return request.getRemoteAddr();
        }
    }
}