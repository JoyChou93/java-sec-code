package org.joychou.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * author: JoyChou (joychou@joychou.org)
 * date:   2017.12.28
 * desc:   Java url redirect vuls code
 * fix:    Add url white list (https://github.com/JoyChou93/trident/blob/master/src/main/java/CheckURL.java)
 */


@Controller
@RequestMapping("/urlRedirect")
public class URLRedirect {

    @RequestMapping("/setHeader")
    @ResponseBody
    public static void setHeader(HttpServletRequest request, HttpServletResponse response){
        String url = request.getParameter("url");
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY); // 301 redirect
        response.setHeader("Location", url);
    }

    @RequestMapping("/sendRedirect")
    @ResponseBody
    public static void sendRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String url = request.getParameter("url");
        response.sendRedirect(url); // 302 redirect
    }


    /**
     * usage: http://localhost:8080/urlRedirect/forward?url=/urlRedirect/test
     * disc: safe code, no url redirect vul
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
