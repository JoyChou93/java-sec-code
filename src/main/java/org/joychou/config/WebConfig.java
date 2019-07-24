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

    private static Boolean referSecEnabled = false;
    private static String[] callbacks;
    private static String[] referWhitelist;
    private static String[] referUris;

    @Value("${joychou.security.referer.enabled}")
    public void setReferSecEnabled(Boolean referSecEnabled){
        WebConfig.referSecEnabled = referSecEnabled;
    }
    public static Boolean getReferSecEnabled(){
        return referSecEnabled;
    }


    @Value("${joychou.security.jsonp.callback}")
    public void setCallbacks(String[] callbacks){
        WebConfig.callbacks = callbacks;
    }
    public static String[] getCallbacks(){
        return callbacks;
    }


    @Value("${joychou.security.referer.hostwhitelist}")
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
}