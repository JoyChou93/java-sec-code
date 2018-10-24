package org.joychou.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@RequestMapping("/fastjson")
public class Fastjson {

    @RequestMapping(value = "deserialize", method = {RequestMethod.POST })
    @ResponseBody
    public static String Deserialize(@RequestBody String params) {
        // 如果Content-Type不设置application/json格式，post数据会被url编码
        System.out.println(params);
        try {
            // 将post提交的string转换为json
            JSONObject ob = JSON.parseObject(params);
            return ob.get("name").toString();
        }catch (Exception e){
            e.printStackTrace();
            return e.toString();
        }
    }

    public static void main(String[] args){
        String str = "{\"name\": \"fastjson\"}";
        JSONObject jo = JSON.parseObject(str);
        System.out.println(jo.get("name"));  // fastjson
    }
}
