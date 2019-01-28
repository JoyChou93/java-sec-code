package org.joychou.controller;

import org.joychou.utils.Security;
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
    protected static String[] urlwhitelist = {"joychou.com", "joychou.me"};


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
     * Desc: 直接访问不限制Referer，非直接访问限制Referer (开发同学喜欢这样进行JSONP测试)
     * URL:  http://localhost:8080/jsonp/emptyReferer?callback=test
     */
    @RequestMapping("/emptyReferer")
    @ResponseBody
    private static String emptyReferer(HttpServletRequest request, HttpServletResponse response) {
        String referer = request.getHeader("referer");
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (null == referer) {
            String callback = request.getParameter("callback");
            return callback + "(" + info + ")";
        } else {
            Security sec = new Security();
            if (!sec.checkSafeUrl(referer, urlwhitelist)) {
                return "Referer is not safe.";
            }
            String callback = request.getParameter("callback");
            return callback + "(" + info + ")";
        }
    }

    // http://localhost:8080/jsonp/sec?callback=test
    @RequestMapping("/sec")
    @ResponseBody
    private static String sec(HttpServletRequest request, HttpServletResponse response) {
        // JSONP的跨域设置
        response.setHeader("Access-Control-Allow-Origin", "*");
        String referer = request.getHeader("referer");
        Security sec = new Security();
        if (!sec.checkSafeUrl(referer, urlwhitelist)) {
            return "Referer is not safe.";
        }
        String callback = request.getParameter("callback");
        return callback + "(" + info + ")";
    }


}