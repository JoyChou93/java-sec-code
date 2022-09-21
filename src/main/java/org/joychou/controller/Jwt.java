package org.joychou.controller;

import lombok.extern.slf4j.Slf4j;
import org.joychou.util.CookieUtils;
import org.joychou.util.JwtUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 */
@Slf4j
@RestController
@RequestMapping("/jwt")
public class Jwt {

    private static final String COOKIE_NAME = "USER_COOKIE";
    /**
     * http://localhost:8080/jwt/createToken
     * Create jwt token and set token to cookies.
     *
     * @author JoyChou 2022-09-20
     */
    @GetMapping("/createToken")
    public String createToken(HttpServletResponse response, HttpServletRequest request) {
        String loginUser = request.getUserPrincipal().getName();
        log.info("Current login user is " + loginUser);

        CookieUtils.deleteCookie(response, COOKIE_NAME);
        String token = JwtUtils.generateTokenByJavaJwt(loginUser);
        Cookie cookie = new Cookie(COOKIE_NAME, token);

        cookie.setMaxAge(86400);    // 1 DAY
        cookie.setPath("/");
        cookie.setSecure(true);
        response.addCookie(cookie);
        return "Add jwt token cookie successfully. Cookie name is USER_COOKIE";
    }


    /**
     * http://localhost:8080/jwt/getName
     * Get nickname from USER_COOKIE
     *
     * @author JoyChou 2022-09-20
     * @param user_cookie cookie
     * @return nickname
     */
    @GetMapping("/getName")
    public String getNickname(@CookieValue(COOKIE_NAME) String user_cookie) {
        String nickname = JwtUtils.getNicknameByJavaJwt(user_cookie);
        return "Current jwt user is " + nickname;
    }

}
