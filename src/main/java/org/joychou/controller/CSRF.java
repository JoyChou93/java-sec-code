package org.joychou.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author  JoyChou (joychou@joychou.org)
 * @date    2019.05.31
 * @desc    check csrf using spring-security
 * @usage   Access http://localhost:8080/csrf/ -> click submit
 */


@Controller
@RequestMapping("/csrf")
public class CSRF {

    @GetMapping("/")
    public String index() {
        return "csrfTest";
    }

    @PostMapping("/post")
    @ResponseBody
    public String post() {
        return "CSRF passed.";
    }
}
