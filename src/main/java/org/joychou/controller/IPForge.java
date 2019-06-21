package org.joychou.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author  JoyChou (joychou@joychou.org)
 * @date    2017.12.29
 * @desc    Java获取IP安全代码
 * @detail  关于获取IP不安全代码，详情可查看https://joychou.org/web/how-to-get-real-ip.html
 */

@Controller
@RequestMapping("/ip")
public class IPForge {
    // no any proxy
    @RequestMapping("/noproxy")
    @ResponseBody
    public static String noProxy(HttpServletRequest request) {
        return request.getRemoteAddr();
    }


    /**
     * proxy_set_header X-Real-IP $remote_addr;
     * proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for
     * if code used X-Forwarded-For to get ip, the code must be vulnerable.
     */
    @RequestMapping("/proxy")
    @ResponseBody
    public static String proxy(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotBlank(ip)) {
            return ip;
        }else {
            String remoteAddr = request.getRemoteAddr();
            if (StringUtils.isNotBlank(remoteAddr)) {
                return remoteAddr;
            }
        }
        return "";
    }


}
