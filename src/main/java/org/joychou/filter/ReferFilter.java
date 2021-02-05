package org.joychou.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.joychou.config.WebConfig;
import org.joychou.security.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;


/**
 * Check referer for all GET requests with callback parameters.
 * If the check of referer fails, a 403 forbidden error page will be returned.
 * <p>
 * Still need to add @ServletComponentScan annotation in Application.java.
 */
@WebFilter(filterName = "referFilter", urlPatterns = "/*")
public class ReferFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String refer = request.getHeader("referer");
        PathMatcher matcher = new AntPathMatcher();
        boolean isMatch = false;

        // 获取要校验Referer的Uri
        for (String uri : WebConfig.getReferUris()) {
            if (matcher.match(uri, request.getRequestURI())) {
                isMatch = true;
                break;
            }
        }

        if (!isMatch) {
            filterChain.doFilter(req, res);
            return;
        }

        if (!WebConfig.getReferSecEnabled()) {
            filterChain.doFilter(req, res);
            return;
        }

        // Check referer for all GET requests with callback parameters.

        String reqCallback = request.getParameter(WebConfig.getBusinessCallback());
        if ("GET".equals(request.getMethod()) && StringUtils.isNotBlank(reqCallback)) {
            // If the check of referer fails, a 403 forbidden error page will be returned.
            if (SecurityUtil.checkURL(refer) == null) {
                logger.info("[-] URL: " + request.getRequestURL() + "?" + request.getQueryString() + "\t"
                        + "Referer: " + refer);
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write(" Referer check error.");
                response.flushBuffer();
                return;
            }
        }


        filterChain.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }
}