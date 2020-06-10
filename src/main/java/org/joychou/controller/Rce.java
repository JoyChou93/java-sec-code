package org.joychou.controller;

import org.junit.Test;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Java code execute
 *
 * @author JoyChou @ 2018-05-24
 */
@RestController
@RequestMapping("/rce")
public class Rce {

    @GetMapping("/exec")
    public String CommandExec(String cmd) {
        Runtime run = Runtime.getRuntime();
        StringBuilder sb = new StringBuilder();

        try {
            Process p = run.exec(cmd);
            BufferedInputStream in = new BufferedInputStream(p.getInputStream());
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));
            String tmpStr;

            while ((tmpStr = inBr.readLine()) != null) {
                sb.append(tmpStr);
            }

            if (p.waitFor() != 0) {
                if (p.exitValue() == 1)
                    return "Command exec failed!!";
            }

            inBr.close();
            in.close();
        } catch (Exception e) {
            return "Except";
        }
        return sb.toString();
    }

    public String CommandExec1(String cmd) {
        StringBuilder sb = new StringBuilder();

        try {
            java.lang.Runtime rt = java.lang.Runtime.getRuntime();
            String cmds[] = {"/bin/sh", "-c", cmd};

            Process ps = rt.exec(cmds);
            //取得命令结果的输出流
            InputStream fis = ps.getInputStream();
            //用一个读输出流类去读
            InputStreamReader isr = new InputStreamReader(fis);
            //用缓冲器读行
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            //直到读完为止
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            int value = ps.waitFor();
            System.out.println(value);
        } catch (Exception e) {
            return "Except";
        }
        return sb.toString();
    }

    @Test
    /**
     * 参考：https://blog.spoock.com/2018/11/25/getshell-bypass-exec/
     *
     * 实验结论：
     * 1. java.lang.Runtime.getRuntime().exec(字符串)  字符串会被分割成数组
     * 2. String cmds[] = {"/bin/sh","-c",cmd};  如果是数组形式，cmd参数可以任意构造
     */
    public void SecurityTest() {
        String output = CommandExec("whoami && ls");
        assert "Command exec failed!!".equals(output);

        output = CommandExec("whoami");
        assert ! "Command exec failed!!".equals(output);

        // Linux下${IFS}绕过exec的token分析
        // https://blog.spoock.com/2018/11/25/getshell-bypass-exec/
        output = CommandExec("whoami{IFS}&&${IFS}ls");
        assert ! "Command exec failed!!".equals(output);

        output = CommandExec1("whoami && cat /etc/passwd");
        assert output.contains("root");
    }
}

