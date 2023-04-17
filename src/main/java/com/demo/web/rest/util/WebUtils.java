package com.demo.web.rest.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class WebUtils {
    public static String getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-Ip");
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
