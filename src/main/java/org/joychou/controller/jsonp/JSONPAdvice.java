package org.joychou.controller.jsonp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

// AbstractJsonpResponseBodyAdvice will be removed as of Spring Framework 5.1, use CORS instead.
// Since Spring Framework 4.1
// Springboot 2.1.0 RELEASE use spring framework 5.1.2
@ControllerAdvice
public class JSONPAdvice extends AbstractJsonpResponseBodyAdvice {

    // method of using @Value in constructor
    public JSONPAdvice(@Value("${joychou.security.jsonp.callback}") String[] callback) {
        super(callback);  // Can set multiple paramNames
    }

}
