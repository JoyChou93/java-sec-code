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


/**
 * @author JoyChou @ 2020-01-13
 * 专门为object自动转jsonp功能配置的filter
 */

@WebFilter(filterName = "jsonpFilter", urlPatterns = "/*")
public class JsonpFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    private final Logger logger= LoggerFactory.getLogger(this.getClass());


    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String refer = request.getHeader("referer");
        String[] jsonpReferWhitelist = WebConfig.getJsonpReferWhitelist();
        StringBuffer url = request.getRequestURL();
        String query = request.getQueryString();

        // 如果不满足jsonp校验逻辑，则不校验
        if ( !check(request) ) {
            filterChain.doFilter(req, res);
            return ;
        }

        // 校验jsonp逻辑，如果不安全，返回forbidden
        if (SecurityUtil.checkUrlByGuava(refer, jsonpReferWhitelist) == null ){
            logger.error("[-] URL: " + url + "?" + query + "\t" + "Referer: " + refer);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("forbidden");
            response.flushBuffer();
            return ;
        }

        // 正常的refer，继续业务逻辑。
        filterChain.doFilter(req, res);

    }

    /**
     *
     * @return true要进行jsonp安全校验，false不进行jsonp安全校验
     */
    private boolean check(HttpServletRequest req) {

        // 如果jsonp校验的开关为false，不校验
        if ( !WebConfig.getJsonpReferCheckEnabled() ) {
            return false;
        }

        // 只校验GET请求
        if ( !"GET".equals(req.getMethod()) ) {
            return false;
        }

        // 只校验带配置里的callback参数请求
        String reqCallback = null;
        for (String callback: WebConfig.getJsonpCallbacks()) {
            reqCallback = req.getParameter(callback);
            if(StringUtils.isNotBlank(reqCallback)) {
                break;
            }
        }
        if (StringUtils.isBlank(reqCallback)){
            return false;
        }


        return true;

    }
    @Override
    public void destroy() {

    }
}
