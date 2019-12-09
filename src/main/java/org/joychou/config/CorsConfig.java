package org.joychou.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
public class CorsConfig
{
    @Bean
    public WebMvcConfigurer corsConfigurer()
    {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // 设置cors origin白名单。区分http和https，并且默认不会拦截同域请求。
                String[] allowOrigins = {"http://test.joychou.org", "https://test.joychou.org"};

                registry.addMapping("/cors/sec/webMvcConfigurer")
                        .allowedOrigins(allowOrigins)
                        .allowedMethods("GET", "POST")
                        .allowCredentials(true);
            }
        };
    }
}