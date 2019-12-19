package org.joychou.controller;

import org.joychou.security.SecurityUtil;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.joychou.controller.jsonp.JSONP;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author  JoyChou (joychou@joychou.org)
 * @date    2018.10.24
 * @desc    https://github.com/JoyChou93/java-sec-code/wiki/CORS
 */

@RestController
@RequestMapping("/cors")
public class Cors {

    protected static String info = "{\"name\": \"JoyChou\", \"phone\": \"18200001111\"}";
    protected static String[] urlwhitelist = {"joychou.com", "joychou.me"};

    @RequestMapping("/vuln/origin")
    private static String vuls1(HttpServletRequest request, HttpServletResponse response) {
        String origin = request.getHeader("origin");
        response.setHeader("Access-Control-Allow-Origin", origin); // 设置Origin值为Header中获取到的
        response.setHeader("Access-Control-Allow-Credentials", "true");  // cookie
        return info;
    }

    @RequestMapping("/vuln/setHeader")
    private static String vuls2(HttpServletResponse response) {
        // 后端设置Access-Control-Allow-Origin为*的情况下，跨域的时候前端如果设置withCredentials为true会异常
        response.setHeader("Access-Control-Allow-Origin", "*");
        return info;
    }


    @CrossOrigin("*")
    @RequestMapping("/vuln/crossOrigin")
    private static String vuls3(HttpServletResponse response) {
        return info;
    }


    // https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/config/webMvcConfigurer.java
    @RequestMapping("/sec/webMvcConfigurer")
    public CsrfToken getCsrfToken_01(CsrfToken token) {
        return token;
    }


    // https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/security/WebSecurityConfig.java
    @RequestMapping("/sec/httpCors")
    public CsrfToken getCsrfToken_02(CsrfToken token) {
        return token;
    }


    // https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/filter/SecCorsFilter.java
    @RequestMapping("/sec/corsFitler")
    public CsrfToken getCsrfToken_03(CsrfToken token) {
        return token;
    }


    // https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/filter/CorsFilter.java
    @RequestMapping("/sec/Filter")
    public CsrfToken getCsrfToken_04(CsrfToken token) {
        return token;
    }


    // http://localhost:8080/cors/sec/checkOrigin
    @RequestMapping("/sec/checkOrigin")
    public String seccode(HttpServletRequest request, HttpServletResponse response) {
        String origin = request.getHeader("Origin");

        // 如果origin不为空并且origin不在白名单内，认定为不安全。
        // 如果origin为空，表示是同域过来的请求或者浏览器直接发起的请求。
        if ( origin != null && SecurityUtil.checkURLbyEndsWith(origin, urlwhitelist) == null ) {
            return "Origin is not safe.";
        }
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return JSONP.getUserInfo2JsonStr(request);
    }


}