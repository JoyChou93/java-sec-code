package org.joychou.security;

import org.springframework.http.MediaType;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.commons.lang.StringUtils;


/**
 * usage: 对所有带有callback参数的get请求做referer校验，如果校验失败返回403页面
 * desc:  除了以下代码，还需要在Application.java中添加@ServletComponentScan注解
 */
@WebFilter(filterName = "referSecCheck", urlPatterns = "/*")
public class secFilter implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String refer = request.getHeader("referer");
        String referWhitelist[] = {"joychou.org", "joychou.com"};

        // get method and includes callback parameter
        System.out.println(request.getMethod());
        if (request.getMethod().equals("GET") && StringUtils.isNotBlank(request.getParameter("callback")) ){
            // if check referer failed, display 403 forbidden page.
            if (!SecurityUtil.checkURLbyEndsWith(refer, referWhitelist)){
                response.setContentType(MediaType.TEXT_HTML_VALUE); // content-type: text/html
                response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 forbidden
                response.getWriter().write("Referer check failed. 403 Forbidden.");  // response
                return;
            }
        }

        filterChain.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }
}
