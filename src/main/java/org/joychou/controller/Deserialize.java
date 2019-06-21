package org.joychou.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * @author JoyChou (joychou@joychou.org)
 * @Date   2018年06月14日
 * @Desc   该应用必须有Commons-Collections包才能利用反序列化命令执行。
 */

@Controller
@RequestMapping("/deserialize")
public class Deserialize {

    @RequestMapping("/test")
    @ResponseBody
    public static String deserialize_test(HttpServletRequest request) throws Exception{
        try {
            InputStream iii = request.getInputStream();
            ObjectInputStream in = new ObjectInputStream(iii);
            in.readObject();  // 触发漏洞
            in.close();
            return "test";
        }catch (Exception e){
            return "exception";
        }
    }
}
