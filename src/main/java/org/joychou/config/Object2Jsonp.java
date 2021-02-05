package org.joychou.config;

import org.apache.commons.lang.StringUtils;
import org.joychou.security.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * <code>AbstractJsonpResponseBodyAdvice</code> will be removed as of Spring Framework 5.1, use CORS instead.
 * Since Spring Framework 4.1. Springboot 2.1.0 RELEASE use spring framework 5.1.2
 */
@ControllerAdvice
public class Object2Jsonp extends AbstractJsonpResponseBodyAdvice {

    private final String[] callbacks;
    private final Logger logger= LoggerFactory.getLogger(this.getClass());


    // method of using @Value in constructor
    public Object2Jsonp(@Value("${joychou.security.jsonp.callback}") String[] callbacks) {
        super(callbacks);  // Can set multiple paramNames
        this.callbacks = callbacks;
    }


    // Check referer
    @Override
    protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType,
                                           MethodParameter returnType, ServerHttpRequest req,
                                           ServerHttpResponse res) {

        HttpServletRequest request = ((ServletServerHttpRequest)req).getServletRequest();
        HttpServletResponse response = ((ServletServerHttpResponse)res).getServletResponse();

        String realJsonpFunc = getRealJsonpFunc(request);
        // 如果url带callback，且校验不安全后
        if ( StringUtils.isNotBlank(realJsonpFunc) ) {
            jsonpReferHandler(request, response);
        }
        super.beforeBodyWriteInternal(bodyContainer, contentType, returnType, req, res);
    }

    /**
     * @return 获取实际jsonp的callback
     */
    private String getRealJsonpFunc(HttpServletRequest req) {

        String reqCallback = null;
        for (String callback: this.callbacks) {
            reqCallback = req.getParameter(callback);
            if(StringUtils.isNotBlank(reqCallback)) {
                break;
            }
        }
        return reqCallback;
    }

    // 校验Jsonp的Referer
    private void jsonpReferHandler(HttpServletRequest request, HttpServletResponse response) {

        String refer = request.getHeader("referer");
        String url = request.getRequestURL().toString();
        String query = request.getQueryString();

        // 如果jsonp校验的开关为false，不校验
        if ( !WebConfig.getJsonpReferCheckEnabled() ) {
            return;
        }

        // 校验jsonp逻辑，如果不安全，返回forbidden
        if (SecurityUtil.checkURL(refer) == null ){
            logger.error("[-] URL: " + url + "?" + query + "\t" + "Referer: " + refer);
            try{
                // 使用response.getWriter().write后，后续写入jsonp后还会继续使用response.getWriteer()，导致报错
//                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                response.getWriter().write(" Referer check error.");
//                response.flushBuffer();
                response.sendRedirect(Constants.ERROR_PAGE);
            } catch (Exception e){
                logger.error(e.toString());
            }

        }
    }
}
