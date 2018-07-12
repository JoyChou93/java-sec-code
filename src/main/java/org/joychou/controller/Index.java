package org.joychou.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



/**
 * @author: JoyChou (joychou@joychou.org)
 * @date:   2018.05.28
 * @desc:   Index Page
 */

@Controller
public class Index {
    @RequestMapping("/")
    @ResponseBody
    public static String index() {
        return "Welcome to java sec code home page by JoyChou(joychou@joychou.org)";
    }
}
