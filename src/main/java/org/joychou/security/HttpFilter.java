package org.joychou.security;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.joychou.config.WebConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;


/**
 * Check referer for all GET requests with callback parameters.
 * If the check of referer fails, a 403 forbidden error page will be returned.
 *
 * Still need to add @ServletComponentScan annotation in Application.java.
 *
 */
@WebFilter(filterName = "referFilter", urlPatterns = "/*")
public class HttpFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    private final Logger logger= LoggerFactory.getLogger(HttpFilter.class);

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String refer = request.getHeader("referer");
        PathMatcher matcher = new AntPathMatcher();
        boolean isMatch = false;
        for (String uri: WebConfig.getReferUris()) {
            if ( matcher.match (uri, request.getRequestURI()) ) {
                isMatch = true;
                break;
            }
        }

        if (isMatch) {
            if (WebConfig.getReferSecEnabled()) {
                // Check referer for all GET requests with callback parameters.
                for (String callback: WebConfig.getCallbacks()) {
                    if (request.getMethod().equals("GET") && StringUtils.isNotBlank(request.getParameter(callback)) ){
                        // If the check of referer fails, a 403 forbidden error page will be returned.
                        if (!SecurityUtil.checkURLbyEndsWith(refer, WebConfig.getReferWhitelist())){
                            logger.info("[-] URL: " + request.getRequestURL() + "?" + request.getQueryString() + "\t"
                                    + "Referer: " + refer);
                            response.sendRedirect("https://test.joychou.org/error3.html");
                            return;
                        }
                    }
                }
            }
        }



        filterChain.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }
}
