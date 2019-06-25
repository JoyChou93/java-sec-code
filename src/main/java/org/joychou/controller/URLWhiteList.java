package org.joychou.controller;


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
 * The vulnerability code and security code of Java url whitelist.
 * The security code is checking url whitelist.
 *
 * @author    JoyChou (joychou@joychou.org)
 * @version   2018.08.23
 */

@Controller
@RequestMapping("/url")
public class URLWhiteList {


    private String domainwhitelist[] = {"joychou.org", "joychou.com"};

    /**
     * bypass poc: bypassjoychou.org
     * http://localhost:8080/url/endswith?url=http://aaajoychou.org
     *
     */
    @RequestMapping("/endswith")
    @ResponseBody
    public String endsWith(HttpServletRequest request) throws Exception{
        String url = request.getParameter("url");
        URL u = new URL(url);
        String host = u.getHost().toLowerCase();

        for (String domain: domainwhitelist){
            if (host.endsWith(domain)) {
                return "Good url.";
            }
        }
        return "Bad url.";
    }

    /**
     * bypass poc: joychou.org.bypass.com or bypassjoychou.org.
     * http://localhost:8080/url/contains?url=http://joychou.org.bypass.com http://bypassjoychou.org
     *
     */
    @RequestMapping("/contains")
    @ResponseBody
    public String contains(HttpServletRequest request) throws Exception{
        String url = request.getParameter("url");
        URL u = new URL(url);
        String host = u.getHost().toLowerCase();

        for (String domain: domainwhitelist){
            if (host.contains(domain)) {
                return "Good url.";
            }
        }
        return "Bad url.";
    }


    /**
     * bypass poc: bypassjoychou.org. It's the same with endsWith.
     * http://localhost:8080/url/regex?url=http://aaajoychou.org
     *
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
     * bypass poc: joychou.org.bypass.com or bypassjoychou.org. It's the same with contains.
     * http://localhost:8080/url/indexof?url=http://joychou.org.bypass.com http://bypassjoychou.org
     *
     */
    @RequestMapping("/indexof")
    @ResponseBody
    public String indexOf(HttpServletRequest request) throws Exception{
        String url = request.getParameter("url");
        URL u = new URL(url);
        String host = u.getHost();
        // If indexOf returns -1, it means that no string was found.
        for (String domain: domainwhitelist){
            if (host.indexOf(domain) != -1) {
                return "Good url.";
            }
        }
        return "Bad url.";
    }

    /**
     * The bypass of using java.net.URL to getHost.
     *
     * Bypass poc1: curl -v 'http://localhost:8080/url/url_bypass?url=http://evel.com%5c@www.joychou.org/a.html'
     * Bypass poc2: curl -v 'http://localhost:8080/url/url_bypass?url=http://evil.com%5cwww.joychou.org/a.html'
     *
     * Detail: https://github.com/JoyChou93/java-sec-code/wiki/URL-whtielist-Bypass
     */
    @RequestMapping("/url_bypass")
    @ResponseBody
    public String url_bypass(HttpServletRequest request) throws Exception{
        String url = request.getParameter("url");
        System.out.println("url:  " + url);
        URL u = new URL(url);

        if (!u.getProtocol().startsWith("http") && !u.getProtocol().startsWith("https")) {
            return "Url is not http or https";
        }

        String host = u.getHost().toLowerCase();
        System.out.println("host:  " + host);

        // endsWith .
        for (String domain: domainwhitelist){
            if (host.endsWith("." + domain)) {
                return "Good url.";
            }
        }

        return "Bad url.";
    }



    /**
     * First-level host whitelist.
     * http://localhost:8080/url/seccode1?url=http://aa.taobao.com
     *
     */
    @RequestMapping("/seccode1")
    @ResponseBody
    public String seccode1(HttpServletRequest request) throws Exception{

        String whiteDomainlists[] = {"taobao.com", "tmall.com"};
        String url = request.getParameter("url");

        URI uri = new URI(url);
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return "SecurityUtil is not http or https";
        }

        String host = uri.getHost().toLowerCase();

        // endsWith .
        for (String domain: whiteDomainlists){
            if (host.endsWith("." + domain)) {
                return "Good url.";
            }
        }

        return "Bad url.";
    }

    /**
     * Muti-level host whitelist.
     * http://localhost:8080/url/seccode2?url=http://ccc.bbb.taobao.com
     *
     */
    @RequestMapping("/seccode2")
    @ResponseBody
    public String seccode2(HttpServletRequest request) throws Exception{
        String whiteDomainlists[] = {"aaa.taobao.com", "ccc.bbb.taobao.com"};
        String url = request.getParameter("url");

        URI uri = new URI(url);
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return "SecurityUtil is not http or https";
        }
        String host = uri.getHost().toLowerCase();

        // equals
        for (String domain: whiteDomainlists){
            if (host.equals(domain)) {
                return "Good url.";
            }
        }
        return "Bad url.";
    }

    /**
     * Muti-level host whitelist.
     * http://localhost:8080/url/seccode3?url=http://ccc.bbb.taobao.com
     *
     */
    @RequestMapping("/seccode3")
    @ResponseBody
    public String seccode3(HttpServletRequest request) throws Exception{

        // Define muti-level host whitelist.
        ArrayList<String> whiteDomainlists = new ArrayList<String>();
        whiteDomainlists.add("bbb.taobao.com");
        whiteDomainlists.add("ccc.bbb.taobao.com");

        String url = request.getParameter("url");
        URI uri = new URI(url);

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return "SecurityUtil is not http or https";
        }
        String host = uri.getHost().toLowerCase();
        if (whiteDomainlists.indexOf(host) != -1) {
            return "Good url.";
        }
        return "Bad url.";
    }
}
