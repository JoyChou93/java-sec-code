package org.joychou.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: JoyChou (joychou@joychou.org)
 * @date:   2017.12.28
 * @desc:   Java url redirect
 */


@Controller
@RequestMapping("/urlRedirect")
public class URLRedirect {

    @GetMapping("/redirect")
    public String redirect(@RequestParam("url") String url) {
        return "redirect:" + url;
    }
    /**
     * @disc: 存在URL重定向漏洞
     * @fix: 添加URL白名单 https://github.com/JoyChou93/trident/blob/master/src/main/java/CheckURL.java
     */
    @RequestMapping("/setHeader")
    @ResponseBody
    public static void setHeader(HttpServletRequest request, HttpServletResponse response){
        String url = request.getParameter("url");
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY); // 301 redirect
        response.setHeader("Location", url);
    }

    /**
     * @disc: 存在URL重定向漏洞
     * @fix: 添加URL白名单 https://github.com/JoyChou93/trident/blob/master/src/main/java/CheckURL.java
     */
    @RequestMapping("/sendRedirect")
    @ResponseBody
    public static void sendRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String url = request.getParameter("url");
        response.sendRedirect(url); // 302 redirect
    }


    /**
     * @usage: http://localhost:8080/urlRedirect/forward?url=/urlRedirect/test
     * @disc: 安全代码，没有URL重定向漏洞。
     */
    @RequestMapping("/forward")
    @ResponseBody
    public static void forward(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String url = request.getParameter("url");
        RequestDispatcher rd =request.getRequestDispatcher(url);
        try{
            rd.forward(request, response);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/test")
    @ResponseBody
    public static String test() {
        return "test";
    }
}
