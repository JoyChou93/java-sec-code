package org.joychou.controller.jsonp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;


@ControllerAdvice
public class JSONPAdvice extends AbstractJsonpResponseBodyAdvice {

    // method of using @Value in constructor
    public JSONPAdvice(@Value("${joychou.security.jsonp.callback}") String[] callback) {
        super(callback);  // Can set multiple paramNames
    }

}
