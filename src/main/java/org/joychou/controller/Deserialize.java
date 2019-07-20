package org.joychou.controller;

import org.apache.commons.lang.StringUtils;
import org.joychou.security.AntObjectInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;

/**
 * Deserialize RCE using Commons-Collections gadget.
 *
 * @author JoyChou @2018-06-14
 */
@RestController
@RequestMapping("/deserialize")
public class Deserialize {


    private static Logger logger= LoggerFactory.getLogger(Deserialize.class);

    /**
     * java -jar ysoserial.jar CommonsCollections5 "open -a Calculator" | base64
     * Add the result to rememberMe cookie.
     *
     * http://localhost:8080/deserialize/rememberMe/vul
     */
    @RequestMapping("/rememberMe/vul")
    public static String rememberMeVul(HttpServletRequest request)
            throws IOException, ClassNotFoundException {

        Cookie[] cookies = request.getCookies();
        String rememberMe = "";

        if (null == cookies) {
            logger.info("No cookies.");
        } else {
            for (Cookie cookie : cookies) {
                if ( cookie.getName().equals("rememberMe") ) {
                    rememberMe = cookie.getValue();
                }
            }
        }

        if (StringUtils.isBlank(rememberMe) ) {
            return "No rememberMe cookie. Right?";
        }

        byte[] decoded = Base64.getDecoder().decode(rememberMe);
        ByteArrayInputStream bytes = new ByteArrayInputStream(decoded);
        ObjectInputStream in = new ObjectInputStream(bytes);
        in.readObject();
        in.close();

        return "Are u ok?";
    }

    /**
     * Check deserialize class using black list.
     *
     * http://localhost:8080/deserialize/rememberMe/security
     */
    @RequestMapping("/rememberMe/security")
    public static String rememberMeBlackClassCheck(HttpServletRequest request)
            throws IOException, ClassNotFoundException {

        Cookie[] cookies = request.getCookies();
        String rememberMe = "";

        if (null == cookies) {
            logger.info("No cookies in /rememberMe/security");
        } else {
            for (Cookie cookie : cookies) {
                if ( cookie.getName().equals("rememberMe") ) {
                    rememberMe = cookie.getValue();
                }
            }
        }

        if (StringUtils.isBlank(rememberMe) ) {
            return "No rememberMe cookie. Right?";
        }

        byte[] decoded = Base64.getDecoder().decode(rememberMe);
        ByteArrayInputStream bytes = new ByteArrayInputStream(decoded);
        AntObjectInputStream in = new AntObjectInputStream(bytes);
        in.readObject();
        in.close();

        return "I'm very OK.";
    }
}
