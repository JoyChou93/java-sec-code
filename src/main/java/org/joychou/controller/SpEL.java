package org.joychou.controller;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * SpEL Injection.
 * @author JoyChou @2019-01-17
 */
@RestController
public class SpEL {

    /**
     * Use Spel to execute cmd. <p>
     * T(java.lang.Runtime).getRuntime().exec("open -a Calculator")
     */
    @RequestMapping("/spel/vuln1")
    public String spel_vuln1(String value) {
        ExpressionParser parser = new SpelExpressionParser();
        return parser.parseExpression(value).getValue().toString();
    }

    /**
     * Use Spel to execute cmd. <p>
     * #{T(java.lang.Runtime).getRuntime().exec('open -a Calculator')}
     * Exploit must add <code>#{}</code> if using TemplateParserContext.
     */
    @RequestMapping("spel/vuln2")
    public String spel_vuln2(String value) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        SpelExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(value, new TemplateParserContext());
        Object x = expression.getValue(context);    // trigger vulnerability point
        return x.toString();   // response
    }

    /**
     * Use SimpleEvaluationContext to fix.
     */
    @RequestMapping("spel/sec")
    public String spel_sec(String value) {
        SimpleEvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding().build();
        SpelExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(value, new TemplateParserContext());
        Object x = expression.getValue(context);
        return x.toString();
    }

    public static void main(String[] args) {
        ExpressionParser parser = new SpelExpressionParser();
        String expression = "1+1";
        String result = parser.parseExpression(expression).getValue().toString();
        System.out.println(result);
    }

}

