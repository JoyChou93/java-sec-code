package org.joychou.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 * @author: JoyChou
 * @Date:   2018年06月14日
 * @Desc：  将根目录的poc放到/tmp/poc就能在mac上弹计算器。该应用必须有Commons-Collections包才能利用反序列化。
 */

@Controller
@RequestMapping("/deserialize")
public class Deserialize {

    @RequestMapping("/test")
    @ResponseBody
    public static String deserialize_test(HttpServletRequest request) throws Exception{
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("/tmp/poc"));
            in.readObject();  // 触发漏洞
            in.close();
            return "test";
        }catch (Exception e){
            return "exception";
        }

    }
}
