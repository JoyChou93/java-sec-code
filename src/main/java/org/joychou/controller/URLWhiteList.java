package org.joychou.controller;


import com.google.common.net.InternetDomainName;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * date: 2018年08月23日
 * author: JoyChou
 * desc: URL白名单绕过
 */

@Controller
@RequestMapping("/url")
public class URLWhiteList {


    private String urlwhitelist = "joychou.com";


    // 绕过方法bypassjoychou.com
    @RequestMapping("/endswith")
    @ResponseBody
    public String endsWith(HttpServletRequest request) throws Exception{
        String url = request.getParameter("url");
        System.out.println(url);
        URL u = new URL(url);
        String host = u.getHost().toLowerCase();
        String rootDomain = InternetDomainName.from(host).topPrivateDomain().toString();

        if (rootDomain.endsWith(urlwhitelist)) {
            return "URL is legal";
        } else {
            return "URL is illegal";
        }
    }

    // 绕过方法joychou.com.bypass.com  bypassjoychou.com
    @RequestMapping("/contains")
    @ResponseBody
    public String contains(HttpServletRequest request) throws Exception{
        String url = request.getParameter("url");
        URL u = new URL(url);
        String host = u.getHost().toLowerCase();
        String rootDomain = InternetDomainName.from(host).topPrivateDomain().toString();

        if (rootDomain.contains(urlwhitelist)) {
            return "URL is legal";
        } else {
            return "URL is illegal";
        }
    }

    // 绕过方法bypassjoychou.com，代码功能和endsWith一样/
    @RequestMapping("/regex")
    @ResponseBody
    public String regex(HttpServletRequest request) throws Exception{
        String url = request.getParameter("url");
        URL u = new URL(url);
        String host = u.getHost().toLowerCase();
        String rootDomain = InternetDomainName.from(host).topPrivateDomain().toString();

        Pattern p = Pattern.compile("joychou\\.com");
        Matcher m = p.matcher(rootDomain);
        if (m.find()) {
            return "URL is legal";
        } else {
            return "URL is illegal";
        }
    }


    @RequestMapping("/indexof")
    @ResponseBody
    public String indexOf(HttpServletRequest request) throws Exception{
        String url = request.getParameter("url");
        // indexof返回-1，表示没有匹配到字符串
        if (-1 == url.indexOf(urlwhitelist)) {
            return "URL is illegal";
        } else {
            return "URL is legal";
        }
    }

    // URL类getHost方法被绕过造成的安全问题
    // 绕过姿势：http://localhost:8080/url/urlVul?url=http://www.taobao.com%23@joychou.com/, URL类getHost为joychou.com
    // 直接访问http://www.taobao.com#@joychou.com/，浏览器请求的是www.taobao.com
    @RequestMapping("/urlVul")
    @ResponseBody
    public String urlVul(HttpServletRequest request) throws Exception{
        String url = request.getParameter("url");
        System.out.println("url:  " + url);
        URL u = new URL(url);
        // 判断是否是http(s)协议
        if (!u.getProtocol().startsWith("http") && !u.getProtocol().startsWith("https")) {
            return "URL is not http or https";
        }
        String host = u.getHost().toLowerCase();
        System.out.println("host:  " + host);
        // 如果非顶级域名后缀会报错
        String rootDomain = InternetDomainName.from(host).topPrivateDomain().toString();

        if (rootDomain.equals(urlwhitelist)) {
            return "URL is legal";
        } else {
            return "URL is illegal";
        }
    }


    // 安全代码
    @RequestMapping("/seccode")
    @ResponseBody
    public String seccode(HttpServletRequest request) throws Exception{
        String url = request.getParameter("url");
        System.out.println("url:  " + url);
        URI uri = new URI(url);
        URL u = new URL(url);
        // 判断是否是http(s)协议
        if (!u.getProtocol().startsWith("http") && !u.getProtocol().startsWith("https")) {
            return "URL is not http or https";
        }
        // 使用uri获取host
        String host = uri.getHost().toLowerCase();
        System.out.println("host:  " + host);

        // 如果非顶级域名后缀会报错
        String rootDomain = InternetDomainName.from(host).topPrivateDomain().toString();

        if (rootDomain.equals(urlwhitelist)) {
            return "URL is legal";
        } else {
            return "URL is illegal";
        }
    }
}
