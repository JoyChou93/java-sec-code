package org.joychou.controller;



import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;


/**
 * Spring Security CVE-2022-22978 <p>
 * <a href="https://github.com/JoyChou93/java-sec-code/wiki/CVE-2022-22978">漏洞相关wiki</a>
 * @author JoyChou @2023-01-212
 */

public class Dotall {


    /**
     * <a href="https://github.com/spring-projects/spring-security/compare/5.5.6..5.5.7">官方spring-security修复commit记录</a>
     */
    public static void main(String[] args) throws Exception{
        Pattern vuln_pattern = Pattern.compile("/black_path.*");
        Pattern sec_pattern = Pattern.compile("/black_path.*", Pattern.DOTALL);

        String poc = URLDecoder.decode("/black_path%0a/xx", StandardCharsets.UTF_8.toString());
        System.out.println("Poc: " + poc);
        System.out.println("Not dotall: " + vuln_pattern.matcher(poc).matches());    // false，非dotall无法匹配\r\n
        System.out.println("Dotall: " + sec_pattern.matcher(poc).matches());         // true，dotall可以匹配\r\n
    }
}
