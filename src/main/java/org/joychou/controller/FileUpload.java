package org.joychou.controller;

import com.fasterxml.uuid.Generators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


/**
 * @author  JoyChou (joychou@joychou.org)
 * @date    2018.08.15
 * @desc    File upload
 */

@Controller
@RequestMapping("/file")
public class FileUpload {

    // Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "/tmp/";
    private final Logger logger= LoggerFactory.getLogger(this.getClass());

    @GetMapping("/")
    public String index() {
        return "upload"; // return upload.html page
    }

    @GetMapping("/pic")
    public String uploadPic() {
        return "uploadPic"; // return uploadPic.html page
    }

    @PostMapping("/upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            // 赋值给uploadStatus.html里的动态参数message
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:/file/status";
        }

        try {
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + UPLOADED_FOLDER + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "upload failed");
            e.printStackTrace();
            return "redirect:/file/status";
        }

        return "redirect:/file/status";
    }

    // only upload picture
    @PostMapping("/upload/picture")
    public String uploadPicture(@RequestParam("file") MultipartFile multifile,
                                   RedirectAttributes redirectAttributes) throws Exception{
        if (multifile.isEmpty()) {
            // 赋值给uploadStatus.html里的动态参数message
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:/file/status";
        }

        String fileName = multifile.getOriginalFilename();
        String Suffix = fileName.substring(fileName.lastIndexOf(".")); // 获取文件后缀名
        String mimeType = multifile.getContentType(); // 获取MIME类型
        File excelFile = convert(multifile);

        // 判断文件后缀名是否在白名单内
        String picSuffixList[] = {".jpg", ".png", ".jpeg", ".gif", ".bmp"};  // 后缀名白名单
        Boolean suffixFlag = false;
        for (String white_suffix : picSuffixList) {
            if (Suffix.toLowerCase().equals(white_suffix)) {
                suffixFlag = true;
                break;
            }
        }

        if (!suffixFlag) {
            logger.error("[-] Suffix error: " + Suffix);
        }

        String mimeTypeBlackList[] = {"text/html"}; // 不允许传html

        Boolean mimeBlackFlag = false;
        for (String blackMimeType : mimeTypeBlackList) {
            if (mimeType.equalsIgnoreCase(blackMimeType) ) {
                mimeBlackFlag = true;
                logger.error("[-] Mime type error: " + mimeType);
                break;
            }
        }

        boolean isImageFlag = isImage(excelFile);

        if( !isImageFlag ){
            logger.error("[-] File is not Image");
        }
        if ( !suffixFlag || mimeBlackFlag || !isImageFlag ) {
            redirectAttributes.addFlashAttribute("message", "illeagl picture");
            deleteFile(excelFile);
            return "redirect:/file/status";
        }


        try {
            // Get the file and save it somewhere
            byte[] bytes = multifile.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + multifile.getOriginalFilename());
            Files.write(path, bytes);

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + UPLOADED_FOLDER + multifile.getOriginalFilename() + "'");

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "upload failed");
            e.printStackTrace();
            deleteFile(excelFile);
            return "redirect:/file/status";
        }

        deleteFile(excelFile);
        return "redirect:/file/status";
    }

    @GetMapping("/status")
    public String uploadStatus() {
        return "uploadStatus";
    }

    private void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 不建议使用transferTo，因为原始的MultipartFile会被覆盖
     * https://stackoverflow.com/questions/24339990/how-to-convert-a-multipart-file-to-file
     *
     * @param multiFile
     * @return
     */
    private File convert(MultipartFile multiFile) throws Exception {
        String fileName = multiFile.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        UUID uuid = Generators.timeBasedGenerator().generate();

        File convFile = new File(UPLOADED_FOLDER + uuid + suffix);
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multiFile.getBytes());
        fos.close();
        return convFile;
    }

    /**
     * Check if the file is a picture.
     *
     * @param file
     * @return
     */
    public static boolean isImage(File file) throws IOException {
        BufferedImage bi = ImageIO.read(file);
        if (bi == null) {
            return false;
        }
        return true;
    }
}
