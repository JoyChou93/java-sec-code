package org.joychou.controller;

import org.apache.commons.codec.binary.Base64;
import org.joychou.security.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;


@RestController
public class PathTraversal {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * http://localhost:8080/path_traversal/vul?filepath=../../../../../etc/passwd
     */
    @GetMapping("/path_traversal/vul")
    public String getImage(String filepath) throws IOException {
        return getImgBase64(filepath);
    }

    @GetMapping("/path_traversal/sec")
    public String getImageSec(String filepath) throws IOException {
        if (SecurityUtil.pathFilter(filepath) == null) {
            logger.info("Illegal file path: " + filepath);
            return "Bad boy. Illegal file path.";
        }
        return getImgBase64(filepath);
    }

    private String getImgBase64(String imgFile) throws IOException {

        logger.info("Working directory: " + System.getProperty("user.dir"));
        logger.info("File path: " + imgFile);

        File f = new File(imgFile);
        if (f.exists() && !f.isDirectory()) {
            byte[] data = Files.readAllBytes(Paths.get(imgFile));
            return new String(Base64.encodeBase64(data));
        } else {
            return "File doesn't exist or is not a file.";
        }
    }

    private boolean testNewFile(String imgFile) {
        try {
            byte[] data = Files.readAllBytes(Paths.get(imgFile));
            return true;
        } catch (IOException e) {
            System.out.println("File doesn't exist " + imgFile);
        }
        return false;
    }

    @Test
    public void securityTest1() {
        // Files.readAllBytes(Paths.get(imgFile))会验证每一级的目录、文件是否存在

        // 存在/tmp/a文件
        // 存在/tmp/x目录
        // 不存在/tmp/xxx目录或者文件

        assert testNewFile("/tmp/x/../a");  // 可以读
        assert !testNewFile("/tmp/a/../a");  // 文件不存在
        assert !testNewFile("/tmp/xxxx/../a");  // 文件不存在
        assert testNewFile("/tmp/../tmp/a");  // 可以读
        assert testNewFile("/tmp/../../../../etc/passwd");  // 可以读
    }

    @Test
    public void securityTest2() {
        // 由于Paths判断非预期行为，可能导致任意目录、文件操作
        Path inputPath = Paths.get("/tmp", "../etc/passwd");
        Path webPath = Paths.get("/", "tmp");
        assert inputPath.startsWith(webPath);  // 预期是false，实际是true
    }

    public static void main(String[] argv) throws IOException {
        String aa = new String(Files.readAllBytes(Paths.get("pom.xml")), StandardCharsets.UTF_8);
        System.out.println(aa);
    }
}