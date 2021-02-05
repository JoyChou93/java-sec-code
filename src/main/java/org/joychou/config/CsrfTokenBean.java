package org.joychou.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * Reference: https://docs.spring.io/spring-security/site/docs/5.0.x/reference/html/csrf.html
 */

@Configuration
public class CsrfTokenBean {

    @Bean
    public CookieCsrfTokenRepository cookieCsrfTokenRepository() {
        return CookieCsrfTokenRepository.withHttpOnlyFalse();
    }
}
