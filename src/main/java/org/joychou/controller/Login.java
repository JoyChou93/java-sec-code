package org.joychou.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
public class Login {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {

        String username = request.getUserPrincipal().getName();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        String[] deleteCookieKey = {"JSESSIONID", "remember-me"}; // delete cookie
        for (String key : deleteCookieKey) {
            Cookie cookie = new Cookie(key, null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        if (null == request.getUserPrincipal()) {
            logger.info("USER " + username + " LOGOUT SUCCESS.");
        } else {
            logger.info("User " + username + " logout failed. Please try again.");
        }

        return "redirect:/login?logout";
    }

}
