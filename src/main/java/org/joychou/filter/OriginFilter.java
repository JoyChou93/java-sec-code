package org.joychou.filter;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.joychou.security.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 推荐使用该全局方案修复Cors跨域漏洞，因为可以校验一级域名。
 *
 * @author JoyChou @ 2019.12.19
 */
@WebFilter(filterName = "OriginFilter", urlPatterns = "/cors/sec/originFilter")
public class OriginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String origin = request.getHeader("Origin");
        logger.info("[+] Origin: " + origin + "\tCurrent url:" + request.getRequestURL());

        // 以file协议访问html，origin为字符串的null，所以依然会走安全check逻辑
        if (origin != null && SecurityUtil.checkURL(origin) == null) {
            logger.error("[-] Origin check error. " + "Origin: " + origin +
                    "\tCurrent url:" + request.getRequestURL());
            response.setStatus(response.SC_FORBIDDEN);
            response.getWriter().println("Invaid cors config by joychou.");
            return;
        }

        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTION");

        filterChain.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }
}
