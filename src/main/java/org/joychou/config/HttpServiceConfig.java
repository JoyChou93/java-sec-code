package org.joychou.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;


class CustomClientHttpRequestFactory extends SimpleClientHttpRequestFactory {


    @Override
    protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
        super.prepareConnection(connection, httpMethod);
        // Use custom ClientHttpRequestFactory to set followRedirects false.
        connection.setInstanceFollowRedirects(false);
    }
}

@Configuration
public class HttpServiceConfig {

    @Bean
    public RestTemplate restTemplateBanRedirects(RestTemplateBuilder builder) {
        return builder.requestFactory(CustomClientHttpRequestFactory.class).build();
    }


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

}