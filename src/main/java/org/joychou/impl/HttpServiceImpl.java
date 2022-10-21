package org.joychou.impl;


import org.joychou.service.HttpService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpMethod;

import javax.annotation.Resource;

@Service
public class HttpServiceImpl implements HttpService {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private RestTemplate restTemplateBanRedirects;

    /**
     * Http request by RestTemplate. Only support HTTP protocol. <p>
     * Redirects: GET HttpMethod follow redirects by default, other HttpMethods do not follow redirects.<p>
     * User-Agent: Java/1.8.0_102 <p>
     */
    public String RequestHttp(String url, HttpHeaders headers) {
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> re = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return re.getBody();
    }

    /**
     * Http request by RestTemplate. Only support HTTP protocol. <p>
     * Redirects: Disable followRedirects.<p>
     * User-Agent: Java/1.8.0_102 <p>
     */
    public String RequestHttpBanRedirects(String url, HttpHeaders headers) {
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> re = restTemplateBanRedirects.exchange(url, HttpMethod.GET, entity, String.class);
        return re.getBody();
    }
}
