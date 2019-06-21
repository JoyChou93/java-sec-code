package org.joychou.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author  JoyChou (joychou@joychou.org)
 * @date    2018.05.24
 * @desc    Java code execute
 * @fix     过滤造成命令执行的参数
 */

@Controller
@RequestMapping("/rce")
public class Rce {

    @RequestMapping("/exec")
    @ResponseBody
    public String CommandExec(HttpServletRequest request) {
        String cmd = request.getParameter("cmd").toString();
        Runtime run = Runtime.getRuntime();
        String lineStr = "";

        try {
            Process p = run.exec(cmd);
            BufferedInputStream in = new BufferedInputStream(p.getInputStream());
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));
            String tmpStr;

            while ((tmpStr = inBr.readLine()) != null) {
                lineStr += tmpStr + "\n";
                System.out.println(tmpStr);
            }

            if (p.waitFor() != 0) {
                if (p.exitValue() == 1)
                    return "command exec failed";
            }

            inBr.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "Except";
        }
        return lineStr;
    }
}

