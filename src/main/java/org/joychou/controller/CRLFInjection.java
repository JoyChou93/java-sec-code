package org.joychou.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author  JoyChou (joychou@joychou.org)
 * @date    2018.01.03
 * @desc    Java 1.7/1.8 no CRLF vuls (test in Java 1.7/1.8)
 */

@Controller
@RequestMapping("/crlf")
public class CRLFInjection {

    @RequestMapping("/safecode")
    @ResponseBody
    private static void crlf(HttpServletRequest request, HttpServletResponse response) {
        response.addHeader("test1", request.getParameter("test1"));
        response.setHeader("test2", request.getParameter("test2"));
        String author = request.getParameter("test3");
        Cookie cookie = new Cookie("test3", author);
        response.addCookie(cookie);
    }
}
