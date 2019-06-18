package org.joychou.controller;

import org.joychou.security.SecurityUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author  JoyChou
 * @date    2018年10月24日
 */

@Controller
@RequestMapping("/jsonp")
public class JSONP {

    protected static String info = "{\"name\": \"JoyChou\", \"phone\": \"18200001111\"}";
    protected static String[] urlwhitelist = {"joychou.com", "joychou.org"};


    // http://localhost:8080/jsonp/referer?callback=test
    @RequestMapping("/referer")
    @ResponseBody
    private static String referer(HttpServletRequest request, HttpServletResponse response) {
        // JSONP的跨域设置
        response.setHeader("Access-Control-Allow-Origin", "*");
        String callback = request.getParameter("callback");
        return callback + "(" + info + ")";
    }

    /**
     * 直接访问不限制Referer，非直接访问限制Referer (开发同学喜欢这样进行JSONP测试)
     * http://localhost:8080/jsonp/emptyReferer?callback=test
     *
     */
    @RequestMapping("/emptyReferer")
    @ResponseBody
    private static String emptyReferer(HttpServletRequest request, HttpServletResponse response) {
        String referer = request.getHeader("referer");
        response.setHeader("Access-Control-Allow-Origin", "*");

        // 如果referer不为空，并且referer不在安全域名白名单内，return error
        // 导致空referer就会绕过校验。开发同学为了方便测试，不太喜欢校验空Referer
        if (null != referer && !SecurityUtil.checkURLbyEndsWith(referer, urlwhitelist)) {
            return "error";
        }

        String callback = request.getParameter("callback");
        return callback + "(" + info + ")";
    }

    // http://localhost:8080/jsonp/sec?callback=test
    @RequestMapping("/sec")
    @ResponseBody
    private static String sec(HttpServletRequest request, HttpServletResponse response) {
        // JSONP的跨域设置
        response.setHeader("Access-Control-Allow-Origin", "*");
        String referer = request.getHeader("referer");

        if (!SecurityUtil.checkURLbyEndsWith(referer, urlwhitelist)) {
            return "error";
        }

        String callback = request.getParameter("callback");
        return callback + "(" + info + ")";
    }


}