package org.joychou.controller.jsonp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.netflix.ribbon.proxy.annotation.Http;
import org.joychou.security.SecurityUtil;
import org.springframework.http.MediaType;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.joychou.config.WebConfig;
import org.joychou.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;


/**
 * @author  JoyChou (joychou@joychou.org) @ 2018.10.24
 * https://github.com/JoyChou93/java-sec-code/wiki/JSONP
 */


@RestController
@RequestMapping("/jsonp")
public class JSONP {

    private static String[] urlwhitelist = {"joychou.com", "joychou.org"};
    private static String callback = WebConfig.getBusinessCallback();

    // get current login username
    public static String getUserInfo2JsonStr(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();

        String username = principal.getName();

        Map<String, String> m = new HashMap<>();
        m.put("Username", username);

        return JSON.toJSONString(m);
    }

    /**
     * Set the response content-type to application/javascript.
     * <p>
     * http://localhost:8080/jsonp/vuln/referer?callback_=test
     */
    @RequestMapping(value = "/vuln/referer", produces = "application/javascript")
    public String referer(HttpServletRequest request) {
        String callback = request.getParameter(this.callback);
        return WebUtils.json2Jsonp(callback, getUserInfo2JsonStr(request));
    }

    /**
     * Direct access does not check Referer, non-direct access check referer.
     * Developer like to do jsonp testing like this.
     * <p>
     * http://localhost:8080/jsonp/vuln/emptyReferer?callback_=test
     */
    @RequestMapping(value = "/vuln/emptyReferer", produces = "application/javascript")
    public String emptyReferer(HttpServletRequest request) {
        String referer = request.getHeader("referer");

        if (null != referer && SecurityUtil.checkURLbyEndsWith(referer, urlwhitelist) == null) {
            return "error";
        }
        String callback = request.getParameter(this.callback);
        return WebUtils.json2Jsonp(callback, getUserInfo2JsonStr(request));
    }

    /**
     * Adding callback or cback on parameter can automatically return jsonp data.
     * http://localhost:8080/jsonp/vuln/advice?callback=test
     * http://localhost:8080/jsonp/vuln/advice?_callback=test
     *
     * @return Only return object, AbstractJsonpResponseBodyAdvice can be used successfully.
     * Such as JSONOjbect or JavaBean. String type cannot be used.
     */
    @RequestMapping(value = "/vuln/advice", produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONObject advice(HttpServletRequest request) {
        return JSON.parseObject(getUserInfo2JsonStr(request));
    }


    /**
     * http://localhost:8080/jsonp/vuln/mappingJackson2JsonView?callback=test
     * Reference: https://p0sec.net/index.php/archives/122/ from p0
     * Affected version:  java-sec-code test case version: 4.3.6
     *     - Spring Framework 5.0 to 5.0.6
     *     - Spring Framework 4.1 to 4.3.17
     */
    @RequestMapping(value = "/vuln/mappingJackson2JsonView", produces = MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView mappingJackson2JsonView(HttpServletRequest req) {
        ModelAndView view = new ModelAndView(new MappingJackson2JsonView());
        Principal principal = req.getUserPrincipal();
        view.addObject("username", principal.getName() );
        return view;
    }


    /**
     * Safe code.
     * http://localhost:8080/jsonp/sec?callback_=test
     */
    @RequestMapping(value = "/sec/checkReferer", produces = "application/javascript")
    public String safecode(HttpServletRequest request) {
        String referer = request.getHeader("referer");

        if (SecurityUtil.checkURLbyEndsWith(referer, urlwhitelist) == null) {
            return "error";
        }
        String callback = request.getParameter(this.callback);
        return WebUtils.json2Jsonp(callback, getUserInfo2JsonStr(request));
    }


    @GetMapping("/getToken")
    public CsrfToken getCsrfToken(CsrfToken token) {
        return token;
    }



}