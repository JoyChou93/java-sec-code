package org.joychou.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * Solve can't get value in filter by @Value when not using embed tomcat.
 *
 * @author JoyChou @2019-07-24
 */
@Component
public class WebConfig {

    private static String[] callbacks;
    private static Boolean  jsonpReferCheckEnabled = false;
    private static String[] jsonpRefererHost;
    private static String[] referWhitelist;
    private static String[] referUris;
    private static Boolean referSecEnabled = false;
    private static String businessCallback;

    /**
     * application.properties里object自动转jsonp的referer校验开关
     * @param jsonpReferCheckEnabled jsonp校验开关
     */
    @Value("${joychou.security.jsonp.referer.check.enabled}")
    public void setJsonpReferCheckEnabled(Boolean jsonpReferCheckEnabled){
        WebConfig.jsonpReferCheckEnabled = jsonpReferCheckEnabled;
    }
    public static Boolean getJsonpReferCheckEnabled(){
        return jsonpReferCheckEnabled;
    }


    @Value("${joychou.security.jsonp.callback}")
    public void setJsonpCallbacks(String[] callbacks){
        WebConfig.callbacks = callbacks;
    }
    public static String[] getJsonpCallbacks(){
        return callbacks;
    }


    /**
     * application.properties里object自动转jsonp的referer白名单域名
     * @param jsonpRefererHost 白名单域名，仅支持一级域名
     */
    @Value("${joychou.security.jsonp.referer.host}")
    public void setJsonpReferWhitelist(String[] jsonpRefererHost){
        WebConfig.jsonpRefererHost = jsonpRefererHost;
    }
    public static String[] getJsonpReferWhitelist(){
        return jsonpRefererHost;
    }


    @Value("${joychou.security.referer.enabled}")
    public void setReferSecEnabled(Boolean referSecEnabled){
        WebConfig.referSecEnabled = referSecEnabled;
    }
    public static Boolean getReferSecEnabled(){
        return referSecEnabled;
    }


    @Value("${joychou.security.referer.host}")
    public void setReferWhitelist(String[] referWhitelist){
        WebConfig.referWhitelist = referWhitelist;
    }
    public static String[] getReferWhitelist(){
        return referWhitelist;
    }


    @Value("${joychou.security.referer.uri}")
    public void setReferUris(String[] referUris)
    {
        WebConfig.referUris = referUris;
    }
    public static String[] getReferUris(){
        return referUris;
    }


    @Value("${joychou.business.callback}")
    public void setBusinessCallback(String businessCallback){
        WebConfig.businessCallback = businessCallback;
    }
    public static String getBusinessCallback(){
        return businessCallback;
    }

}