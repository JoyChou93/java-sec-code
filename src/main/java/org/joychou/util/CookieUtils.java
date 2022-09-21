package org.joychou.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;


@Slf4j
public class CookieUtils {

    public static boolean deleteCookie(HttpServletResponse res, String cookieName) {
        try {
            Cookie cookie = new Cookie(cookieName, null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            res.addCookie(cookie);
            return true;
        } catch (Exception e) {
            log.error(e.toString());
            return false;
        }
    }
}
