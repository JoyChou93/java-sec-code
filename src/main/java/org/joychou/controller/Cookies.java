package org.joychou.controller;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.joychou.util.WebUtils;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.util.WebUtils.getCookie;

@RestController
@RequestMapping("/cookie")
public class Cookies {

    private static String NICK = "nick";

    @RequestMapping(value = "/vuln01")
    public String vuln01(HttpServletRequest req) {
        String nick = WebUtils.getCookieValueByName(req, NICK); // key code
        return "Cookie nick: " + nick;
    }


    @RequestMapping(value = "/vuln02")
    public String vuln02(HttpServletRequest req) {
        String nick = null;
        Cookie[] cookie = req.getCookies();

        if (cookie != null) {
            nick = getCookie(req, NICK).getValue();  // key code
        }

        return "Cookie nick: " + nick;
    }


    @RequestMapping(value = "/vuln03")
    public String vuln03(HttpServletRequest req) {
        String nick = null;
        Cookie cookies[] = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // key code. Equals can also be equalsIgnoreCase.
                if (NICK.equals(cookie.getName())) {
                    nick = cookie.getValue();
                }
            }
        }
        return "Cookie nick: " + nick;
    }


    @RequestMapping(value = "/vuln04")
    public String vuln04(HttpServletRequest req) {
        String nick = null;
        Cookie cookies[] = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase(NICK)) {  // key code
                    nick = cookie.getValue();
                }
            }
        }
        return "Cookie nick: " + nick;
    }


    @RequestMapping(value = "/vuln05")
    public String vuln05(@CookieValue("nick") String nick) {
        return "Cookie nick: " + nick;
    }


    @RequestMapping(value = "/vuln06")
    public String vuln06(@CookieValue(value = "nick") String nick) {
        return "Cookie nick: " + nick;
    }

}
