package org.joychou.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/test")
public class Test {

    @RequestMapping(value = "/")
    public String Index(HttpServletResponse response, String empId) {

        System.out.println(empId);
        Cookie cookie = new Cookie("XSRF-TOKEN", "123");
        cookie.setDomain("taobao.com");
        cookie.setMaxAge(-1); // forever time
        response.addCookie(cookie);
        return "success";
    }


    @RequestMapping(value = "/aa")
    public void test(HttpServletResponse response, String empId) {

        System.out.println(empId);
        Cookie cookie = new Cookie("XSRF-TOKEN", "123");
        cookie.setDomain("taobao.com");
        cookie.setMaxAge(-1); // forever time
        response.addCookie(cookie);
    }
}
