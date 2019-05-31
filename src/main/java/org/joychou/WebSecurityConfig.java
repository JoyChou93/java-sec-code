package org.joychou;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    RequestMatcher csrfRequestMatcher = new RequestMatcher() {

        // 配置不需要CSRF校验的请求方式
        private Pattern allowedMethods =
                Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");

        @Override
        public boolean matches(HttpServletRequest request) {
            // CSRF disabled on allowedMethod
            // false表示不校验csrf
            return !(allowedMethods.matcher(request.getMethod()).matches());
        }

    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // http.csrf().disable()  // 去掉csrf校验
        // 默认token存在session里，用CookieCsrfTokenRepository改为token存在cookie里。
        // 但存在后端多台服务器情况，session不能同步的问题，所以一般使用cookie模式。
        http.csrf()
                .requireCsrfProtectionMatcher(csrfRequestMatcher)
                .ignoringAntMatchers("/xxe/**", "/fastjon/**")  // 不进行csrf校验的uri，多个uri使用逗号分隔
                .csrfTokenRepository(new CookieCsrfTokenRepository());
        // http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }
}