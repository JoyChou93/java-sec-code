package org.joychou.security;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
                                        throws ServletException, IOException {

        logger.info("USER " + authentication.getName()+ " LOGIN SUCCESS.");

        SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
        String originUrl = "";
        try {
            originUrl = savedRequest.getRedirectUrl();
        } catch (Exception e) {
            logger.debug(e.toString());
        }

        if (savedRequest != null) {
            logger.info("Original url is: " + originUrl);
        }

        Map<String, String> content = new HashMap<>();
        content.put("code", "0");
        content.put("message", "Login success");
        content.put("redirectUrl", originUrl);
        // 直接进行sendRedirect到登录前的url，会重定向失败。具体原因可google ajax and sendRedirect
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(JSON.toJSONString(content));
    }
}
