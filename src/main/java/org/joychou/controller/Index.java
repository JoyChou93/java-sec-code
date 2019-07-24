package org.joychou.controller;


import com.alibaba.fastjson.JSON;
import org.apache.catalina.util.ServerInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


/**
 * Index page
 *
 * @author JoyChou @2018-05-28
 */
@Controller
public class Index {

    @RequestMapping("/appInfo")
    @ResponseBody
    public static String appInfo(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        Map<String, String> m = new HashMap<>();

        m.put("tomcat_version", ServerInfo.getServerInfo());
        m.put("username", username);
        m.put("login", "success");
        m.put("app_name", "java security code");
        m.put("java_version", System.getProperty("java.version"));
        m.put("fastjson_version", JSON.VERSION);

        // covert map to string
        return JSON.toJSONString(m);
    }

    @RequestMapping("/")
    public String redirect() {
        return "redirect:/index";
    }

    @RequestMapping("/index")
    public static String index(Model model, HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        model.addAttribute("user", username);
        return "index";
    }
}
