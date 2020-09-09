package org.joychou.controller;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.joychou.security.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
            Files.readAllBytes(Paths.get(imgFile));
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

    @Test
    public void securityTestClassLoader() {
        // 测试classloader相关函数
        ClassLoader c = this.getClass().getClassLoader();
        System.out.println("ClassLoader base dir:" + c.getResource(""));

        // 并不能使用..跳转
        InputStream is = c.getResourceAsStream("/../../../../../../../../../../../../tmp/a.txt");
        assert is == null;

        is = c.getResourceAsStream("../../../../../../../../../../../../tmp/a.txt");
        assert is == null;
    }

    @Test
    public void securityTestFile() {
        // 测试File类相关函数

        // 测试exists方法对..路径的文件怎么处理

        // 系统确实不存在 /tmp/notexistfile 文件

        File file = new File("/tmp/", "notexistfile");
        assert !file.exists();

        file = new File("/tmp/", "../etc/passwd"); //Weak point
        assert file.exists();

        file = new File("/tmp/", "../notexistdir/../etc/passwd"); //Weak point
        assert !file.exists();

        file = new File("/tmp/", "../tmp/../etc/passwd"); //Weak point
        assert file.exists();

        file = new File("/tmp/", FilenameUtils.getName("../etc/passwd"));

        assert !file.exists();

        // 测试isReadable方法是否跟随软链接
        // 实验结论：
        //   isReadable 会跟随软链接
        //   toRealPath 也会跟随软连接，并且转化成真实文件路径；若文件路径不存在会抛出异常
        try {
            //Path path = Paths.get("/etc/", "passwd");

            // touch /tmp/zzz
            // ln -sv /tmp/zzz /tmp/link
            // rm /tmp/zzz

            Path path = Paths.get("/tmp/", "aaa");

            assert !Files.isReadable(path);

            // path.toRealPath();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void securityTestPath() {
        // 测试Paths相关函数
        Path path = Paths.get("/tmp", "../etc/passwd");

        File file = path.toFile();

        Path file_to_path = file.toPath();

        assert "passwd".equals(file.getName());
        assert "/tmp/../etc/passwd".equals(file_to_path.toString());
        assert "/tmp/../etc/passwd".equals(path.toString());
        assert file.exists();

        // 测试resolve函数是否受..影响
        // 实验结论：
        //  1. resolve受..影响
        //  2. resolve函数受绝对路径影响

        // resolve函数参数为绝对路径时，直接就忽略/tmp
        path = Paths.get("/tmp").resolve("/etc/hosts");
        assert path.toFile().exists();
        assert "hosts".equals(path.toFile().getName());  // 对象不同，内容相同，"=="返回false，equals返回true
        assert "/etc/hosts".equals(path.toFile().toString());

        /*
        * /tmp/existsdir目录存在，/tmp/existsfile文件存在，则返回True
        *
        * mkdir /tmp/existsdir
        * touch /tmp/existsfile
        * */
        path = Paths.get("/tmp").resolve("notexistsdir/../notexistsfile");
        assert !path.toFile().exists();

        path = Paths.get("/tmp").resolve("existsdir/../existsfile");
        assert path.toFile().exists();
        assert "existsfile".equals(path.toFile().getName());
        assert "/tmp/existsdir/../existsfile".equals(path.toFile().toString());
    }

    public static void main(String[] argv) throws IOException {
        String aa = new String(Files.readAllBytes(Paths.get("pom.xml")), StandardCharsets.UTF_8);
        System.out.println(aa);
    }
}