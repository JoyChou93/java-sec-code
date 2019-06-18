package org.joychou.controller;


import com.google.common.net.InternetDomainName;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
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


    private String urlwhitelist[] = {"joychou.org", "joychou.com"};

    /**
     * @desc 绕过方法bypassjoychou.org
     * @usage http://localhost:8080/url/endswith?url=http://aaajoychou.org
     */
    @RequestMapping("/endswith")
    @ResponseBody
    public String endsWith(HttpServletRequest request) throws Exception{
        String url = request.getParameter("url");
        URL u = new URL(url);
        String host = u.getHost().toLowerCase();

        for (String domain: urlwhitelist){
            if (host.endsWith(domain)) {
                return "Good url.";
            }
        }
        return "Bad url.";
    }

    /**
     * @desc 绕过方法joychou.org.bypass.com  bypassjoychou.org
     * @usage http://localhost:8080/url/contains?url=http://joychou.org.bypass.com http://bypassjoychou.org
     */
    @RequestMapping("/contains")
    @ResponseBody
    public String contains(HttpServletRequest request) throws Exception{
        String url = request.getParameter("url");
        URL u = new URL(url);
        String host = u.getHost().toLowerCase();

        for (String domain: urlwhitelist){
            if (host.contains(domain)) {
                return "Good url.";
            }
        }
        return "Bad url.";
    }


    /**
     * @desc 绕过方法bypassjoychou.org，代码功能和endsWith一样
     * @usage http://localhost:8080/url/regex?url=http://aaajoychou.org
     */
    @RequestMapping("/regex")
    @ResponseBody
    public String regex(HttpServletRequest request) throws Exception{
        String url = request.getParameter("url");
        URL u = new URL(url);
        String host = u.getHost().toLowerCase();

        Pattern p = Pattern.compile("joychou\\.org$");
        Matcher m = p.matcher(host);
        if (m.find()) {
            return "Good url.";
        } else {
            return "Bad url.";
        }
    }


    /**
     * @desc 绕过方法joychou.org.bypass.com  bypassjoychou.org，代码功能和 contains 一样
     * @usage http://localhost:8080/url/indexof?url=http://joychou.org.bypass.com http://bypassjoychou.org
     */
    @RequestMapping("/indexof")
    @ResponseBody
    public String indexOf(HttpServletRequest request) throws Exception{
        String url = request.getParameter("url");
        URL u = new URL(url);
        String host = u.getHost();
        // indexOf为-1，表示没有匹配到字符串
        for (String domain: urlwhitelist){
            if (host.indexOf(domain) != -1) {
                return "Good url.";
            }
        }
        return "Bad url.";
    }

    /**
     * @desc 用java.net.URL类的getHost被绕过情况
     * @usage https://github.com/JoyChou93/java-sec-code/wiki/SecurityUtil-whtielist-Bypass
     */
    @RequestMapping("/url_bypass")
    @ResponseBody
    public String url_bypass(HttpServletRequest request) throws Exception{
        String url = request.getParameter("url");
        System.out.println("url:  " + url);
        URL u = new URL(url);
        // 判断是否是http(s)协议
        if (!u.getProtocol().startsWith("http") && !u.getProtocol().startsWith("https")) {
            return "Url is not http or https";
        }
        String host = u.getHost().toLowerCase();
        System.out.println("host:  " + host);

        if (host.endsWith("." + urlwhitelist)) {
            return "Good url.";
        } else {
            return "Bad url.";
        }
    }


    /**
     * @desc 利用InternetDomainName库获取一级域名的安全代码 (一级域名白名单)
     */
    @RequestMapping("/seccode")
    @ResponseBody
    public String seccode(HttpServletRequest request) throws Exception{
        String url = request.getParameter("url");

        URI uri = new URI(url);
        // 判断是否是http(s)协议
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return "SecurityUtil is not http or https";
        }

        // 使用uri获取host
        String host = uri.getHost().toLowerCase();

        // 如果非顶级域名后缀会报错
        String rootDomain = InternetDomainName.from(host).topPrivateDomain().toString();

        if (rootDomain.equals(urlwhitelist)) {
            return "Good url.";
        } else {
            return "Bad url.";
        }
    }

    /**
     * @desc 自定义一级域名白名单
     * @usage http://localhost:8080/url/seccode1?url=http://aa.taobao.com
     */
    @RequestMapping("/seccode1")
    @ResponseBody
    public String seccode1(HttpServletRequest request) throws Exception{

        // 定义一级域名白名单list，用endsWith加上.判断
        String whiteDomainlists[] = {"taobao.com", "tmall.com"};
        String url = request.getParameter("url");

        URI uri = new URI(url);
        // 判断是否是http(s)协议
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return "SecurityUtil is not http or https";
        }

        // 使用uri获取host
        String host = uri.getHost().toLowerCase();

        for (String domain: whiteDomainlists){
            if (host.endsWith("." + domain)) {
                return "Good url.";
            }
        }

        return "Bad url.";
    }

    /**
     * @desc 自定义多级域名白名单
     * @usage http://localhost:8080/url/seccode2?url=http://ccc.bbb.taobao.com
     */
    @RequestMapping("/seccode2")
    @ResponseBody
    public String seccode2(HttpServletRequest request) throws Exception{
        // 定义多级域名白名单，判断使用equals
        String whiteDomainlists[] = {"aaa.taobao.com", "ccc.bbb.taobao.com"};
        String url = request.getParameter("url");

        URI uri = new URI(url);
        // 判断是否是http(s)协议
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return "SecurityUtil is not http or https";
        }
        // 使用uri获取host
        String host = uri.getHost().toLowerCase();

        for (String domain: whiteDomainlists){
            if (host.equals(domain)) {
                return "Good url.";
            }
        }
        return "Bad url.";
    }

    /**
     * @desc 自定义多级域名白名单
     * @usage http://localhost:8080/url/seccode3?url=http://ccc.bbb.taobao.com
     */
    @RequestMapping("/seccode3")
    @ResponseBody
    public String seccode3(HttpServletRequest request) throws Exception{

        // 定义多级域名白名单
        ArrayList<String> whiteDomainlists = new ArrayList<String>();
        whiteDomainlists.add("bbb.taobao.com");
        whiteDomainlists.add("ccc.bbb.taobao.com");

        String url = request.getParameter("url");
        URI uri = new URI(url);

        // 判断是否是http(s)协议
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return "SecurityUtil is not http or https";
        }
        // 使用uri获取host
        String host = uri.getHost().toLowerCase();

        if (whiteDomainlists.indexOf(host) != -1) {
            return "Good url.";
        }
        return "Bad url.";
    }
}
