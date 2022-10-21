package org.joychou.service;


import org.springframework.http.HttpHeaders;

public interface HttpService {

    String RequestHttp(String url, HttpHeaders headers);

    String RequestHttpBanRedirects(String url, HttpHeaders headers);
}
