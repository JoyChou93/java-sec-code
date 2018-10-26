package org.joychou.controller;

import org.joychou.utils.Security;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: JoyChou
 * @date:   2018年10月24日
 * @desc:   https://github.com/JoyChou93/java-sec-code/wiki/CORS
 */

@Controller
@RequestMapping("/cors")
public class CORS {

    protected static String info = "{\"name\": \"JoyChou\", \"phone\": \"18200001111\"}";
    protected static String[] urlwhitelist = {"joychou.com", "joychou.me"};

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

    @CrossOrigin("*")
    @RequestMapping("/vuls3")
    @ResponseBody
    private static String vuls3(HttpServletResponse response) {
        return info;
    }

    @RequestMapping("/sec")
    @ResponseBody
    private static String seccode(HttpServletRequest request, HttpServletResponse response) {
        String origin = request.getHeader("Origin");
        Security sec = new Security();
        Boolean origin_safe = false;

        // 如果origin为空，表示是同域过来的请求或者浏览器直接发起的请求，这种直接放过，没有安全问题。
        if (origin == null) {
            origin_safe = true;
        }else if (sec.checkSafeUrl(origin, urlwhitelist)) {
            origin_safe = true;
        }

        if (!origin_safe) {
            return "Origin is not safe.";
        }
        response.setHeader("Access-Control-Allow-Origin", "*");
        // response.setHeader("Access-Control-Allow-Methods", "POST, GET");
        // response.setHeader("Access-Control-Allow-Credentials", "true");
        return info;
    }


}