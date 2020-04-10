package org.joychou.controller;


import org.joychou.security.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The vulnerability code and security code of Java url whitelist.
 * The security code is checking url whitelist.
 *
 * @author JoyChou (joychou@joychou.org)
 * @version 2018.08.23
 */

@RestController
@RequestMapping("/url")
public class URLWhiteList {


    private String domainwhitelist[] = {"joychou.org", "joychou.com"};
    private static final Logger logger = LoggerFactory.getLogger(URLWhiteList.class);

    /**
     * bypass poc: bypassjoychou.org
     * http://localhost:8080/url/vuln/endswith?url=http://aaajoychou.org
     */
    @GetMapping("/vuln/endsWith")
    public String endsWith(@RequestParam("url") String url) {

        String host = SecurityUtil.gethost(url);

        for (String domain : domainwhitelist) {
            if (host.endsWith(domain)) {
                return "Good url.";
            }
        }
        return "Bad url.";
    }


    /**
     * It's the same with <code>indexOf</code>.
     * <p>
     * http://localhost:8080/url/vuln/contains?url=http://joychou.org.bypass.com
     * http://localhost:8080/url/vuln/contains?url=http://bypassjoychou.org
     */
    @GetMapping("/vuln/contains")
    public String contains(@RequestParam("url") String url) {

        String host = SecurityUtil.gethost(url);

        for (String domain : domainwhitelist) {
            if (host.contains(domain)) {
                return "Good url.";
            }
        }
        return "Bad url.";
    }


    /**
     * bypass poc: bypassjoychou.org. It's the same with endsWith.
     * http://localhost:8080/url/vuln/regex?url=http://aaajoychou.org
     */
    @GetMapping("/vuln/regex")
    public String regex(@RequestParam("url") String url) {

        String host = SecurityUtil.gethost(url);
        Pattern p = Pattern.compile("joychou\\.org$");
        Matcher m = p.matcher(host);

        if (m.find()) {
            return "Good url.";
        } else {
            return "Bad url.";
        }
    }


    /**
     * The bypass of using <code>java.net.URL</code> to getHost.
     * <p>
     * Bypass poc1: curl -v 'http://localhost:8080/url/vuln/url_bypass?url=http://evel.com%5c@www.joychou.org/a.html'
     * Bypass poc2: curl -v 'http://localhost:8080/url/vuln/url_bypass?url=http://evil.com%5cwww.joychou.org/a.html'
     * <p>
     * More details: https://github.com/JoyChou93/java-sec-code/wiki/URL-whtielist-Bypass
     */
    @GetMapping("/vuln/url_bypass")
    public String url_bypass(String url) {

        logger.info("url:  " + url);

        if (!SecurityUtil.isHttp(url)) {
            return "Url is not http or https";
        }

        String host = SecurityUtil.gethost(url);
        logger.info("host:  " + host);

        // endsWith .
        for (String domain : domainwhitelist) {
            if (host.endsWith("." + domain)) {
                return "Good url.";
            }
        }

        return "Bad url.";
    }


    /**
     * 一级域名白名单 First-level host whitelist.
     * http://localhost:8080/url/sec/endswith?url=http://aa.joychou.org
     */
    @GetMapping("/sec/endswith")
    public String sec_endswith(@RequestParam("url") String url) {

        String whiteDomainlists[] = {"joychou.org", "joychou.com"};

        if (!SecurityUtil.isHttp(url)) {
            return "SecurityUtil is not http or https";
        }

        String host = SecurityUtil.gethost(url);

        // endsWith .
        for (String domain : whiteDomainlists) {
            if (host.endsWith("." + domain)) {
                return "Good url.";
            }
        }

        return "Bad url.";
    }

    /**
     * 多级域名白名单 Multi-level host whitelist.
     * http://localhost:8080/url/sec/multi_level_hos?url=http://ccc.bbb.joychou.org
     */
    @GetMapping("/sec/multi_level_host")
    public String sec_multi_level_host(@RequestParam("url") String url) {
        String whiteDomainlists[] = {"aaa.joychou.org", "ccc.bbb.joychou.org"};

        if (!SecurityUtil.isHttp(url)) {
            return "SecurityUtil is not http or https";
        }

        String host = SecurityUtil.gethost(url);

        // equals
        for (String domain : whiteDomainlists) {
            if (host.equals(domain)) {
                return "Good url.";
            }
        }
        return "Bad url.";
    }


    /**
     * 多级域名白名单 Multi-level host whitelist.
     * http://localhost:8080/url/sec/array_indexOf?url=http://ccc.bbb.joychou.org
     */
    @GetMapping("/sec/array_indexOf")
    public String sec_array_indexOf(@RequestParam("url") String url) {

        // Define muti-level host whitelist.
        ArrayList<String> whiteDomainlists = new ArrayList<>();
        whiteDomainlists.add("bbb.joychou.org");
        whiteDomainlists.add("ccc.bbb.joychou.org");

        if (!SecurityUtil.isHttp(url)) {
            return "SecurityUtil is not http or https";
        }

        String host = SecurityUtil.gethost(url);

        if (whiteDomainlists.indexOf(host) != -1) {
            return "Good url.";
        }
        return "Bad url.";
    }

}
