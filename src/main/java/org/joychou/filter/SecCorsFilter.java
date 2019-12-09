package org.joychou.filter;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 由于CorsFilter和spring security冲突，所以改为下面的代码。
 * CorsFilter可以参考config/CorsConfig2的代码。
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecCorsFilter extends CorsFilter {

    public SecCorsFilter() {
        super(configurationSource());
    }

    private static UrlBasedCorsConfigurationSource configurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://test.joychou.org");
        config.addAllowedOrigin("https://test.joychou.org");
        config.addAllowedHeader("*");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/cors/sec/corsFitler", config);

        return source;
    }
}