package org.joychou.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joychou.utils.Security;

/**
 * @author: JoyChou
 * @date:   2018年10月24日
 * @desc:   只要Access-Control-Allow-Origin为*，或者可被绕过，就存在CORS跨域
 */

@Controller
@RequestMapping("/cors")
public class CORS {

    protected static String info = "{\"name\": \"JoyChou\", \"phone\": \"18200001111\"}";
    protected static String[] urlwhitelist = {"joychou.com", "joychou.me"};

    /**
     *
     * @param request
     * @param response
     * @desc: 当origin为空，即直接访问的情况下，response的header中不会出现Access-Control-Allow-Origin
     */
    @RequestMapping("/vuls1")
    @ResponseBody
    private static String vuls1(HttpServletRequest request, HttpServletResponse response) {
        // 获取Header中的Origin
        String origin = request.getHeader("origin");

        response.setHeader("Access-Control-Allow-Origin", origin); // 设置Origin值为Header中获取到的
        // response.setHeader("Access-Control-Allow-Methods", "POST, GET");
        // response.setHeader("Access-Control-Allow-Credentials", "true");  // cookie
        return info;
    }

    @RequestMapping("/vuls2")
    @ResponseBody
    private static String vuls2(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        // response.setHeader("Access-Control-Allow-Methods", "POST, GET");
        // response.setHeader("Access-Control-Allow-Credentials", "true");
        return info;
    }

    @RequestMapping("/sec")
    @ResponseBody
    private static String seccode(HttpServletRequest request, HttpServletResponse response) {
        String origin = request.getHeader("Origin");
        Security sec = new Security();
        if (!sec.checkSafeUrl(origin, urlwhitelist)) {
            return "Origin is not safe.";
        }
        response.setHeader("Access-Control-Allow-Origin", "*");
        // response.setHeader("Access-Control-Allow-Methods", "POST, GET");
        // response.setHeader("Access-Control-Allow-Credentials", "true");
        return info;
    }


}