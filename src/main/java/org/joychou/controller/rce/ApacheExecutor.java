package org.joychou.controller.rce;;

import org.apache.commons.exec.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ApacheExecutor {
    private static void execCmdWithTimout(String cmd, long timeOutMilliSeconds)
            throws Exception {
        CommandLine commandLine = CommandLine.parse(cmd);
        DefaultExecutor executor = new DefaultExecutor();
        ExecuteWatchdog watchdog = new ExecuteWatchdog(timeOutMilliSeconds);
        executor.setWatchdog(watchdog);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        ExecuteStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);
        executor.setStreamHandler(streamHandler);
        try {
            int exitValue = executor.execute(commandLine);

            if (watchdog.killedProcess()) {
                throw new Exception("time out for command: " + commandLine);
            }
            if (exitValue != 0) {
                throw new Exception("time out for commandxxx: " + commandLine);
            }
        } catch (ExecuteException e) {
            System.out.println(e);

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        Path path = Paths.get("/etc/", "../tmp/password`ping`");
        String test_path = "aaa'bbb";
        test_path = test_path.replace("'", "'\''");
        System.out.println(test_path);

        System.out.println(path);
        try {
            execCmdWithTimout("tar -xvf '/tmp/a.tar\' --exclude=\'' -C '/tmp/iii'", 3000);

            //execCmdWithTimout("unzip /tmp/aaa/a.zip -d /tmp/bbb *php -d /tmp/ccc", 3000);

            //execCmdWithTimout("unzip '/tmp/aaa/a.zip\' -d /tmp/bbb *txt -d /tmp/ccc", 3000);

            //execCmdWithTimout("touch /tmp/asdf", 3000);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
