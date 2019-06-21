package org.joychou.controller;


import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;


/**
 * @author  JoyChou (joychou@joychou.org)
 * @date    2018.05.28
 * @desc    Index Page
 */

@Controller
public class Index {
    @RequestMapping("/")
    @ResponseBody
    public static String index() {
        Map m = new HashMap();
        m.put("app_name", "java_vul_code");
        m.put("java_version", System.getProperty("java.version"));
        m.put("fastjson_version", JSON.VERSION);

        // covert map to string
        return JSON.toJSONString(m);
    }
}
