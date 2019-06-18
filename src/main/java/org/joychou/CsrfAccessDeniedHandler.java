package org.joychou;


import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CsrfAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * @desc 返回自定义拦截页面
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType(MediaType.TEXT_HTML_VALUE); // content-type: text/html
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 forbidden
        response.getWriter().write("CSRF check failed by JoyChou.");  // response
    }

}

