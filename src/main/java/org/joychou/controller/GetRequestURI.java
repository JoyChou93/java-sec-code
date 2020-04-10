package org.joychou.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * The difference between getRequestURI and getServletPath.
 * 由于Spring Security的<code>antMatchers("/css/**", "/js/**")</code>未使用getRequestURI，所以登录不会被绕过。
 * <p>
 * Details: https://joychou.org/web/security-of-getRequestURI.html
 * <p>
 * Poc:
 * http://localhost:8080/css/%2e%2e/exclued/vuln
 * http://localhost:8080/css/..;/exclued/vuln
 * http://localhost:8080/css/..;bypasswaf/exclued/vuln
 *
 * @author JoyChou @2020-03-28
 */

@RestController
@RequestMapping("uri")
public class GetRequestURI {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(value = "/exclued/vuln")
    public String exclued(HttpServletRequest request) {

        String[] excluedPath = {"/css/**", "/js/**"};
        String uri = request.getRequestURI(); // Security: request.getServletPath()
        PathMatcher matcher = new AntPathMatcher();

        logger.info("getRequestURI: " + uri);
        logger.info("getServletPath: " + request.getServletPath());

        for (String path : excluedPath) {
            if (matcher.match(path, uri)) {
                return "You have bypassed the login page.";
            }
        }
        return "This is a login page >..<";
    }
}
