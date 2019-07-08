package org.joychou.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.RequestMatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;


/**
 * Congifure csrf
 *
 */
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${joychou.security.csrf.enabled}")
    private Boolean csrfEnabled = false;

    @Value("${joychou.security.csrf.exclude.url}")
    private String[] csrfExcludeUrl;

    @Value("${joychou.security.csrf.method}")
    private String[] csrfMethod = {"POST"};

    RequestMatcher csrfRequestMatcher = new RequestMatcher() {

        @Override
        public boolean matches(HttpServletRequest request) {

            // 配置需要CSRF校验的请求方式，
            HashSet<String> allowedMethods = new HashSet<String>(Arrays.asList(csrfMethod));
            // return false表示不校验csrf
            if (!csrfEnabled) {
                return false;
            }
            return allowedMethods.contains(request.getMethod());
        }

    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // http.csrf().disable()  // 去掉csrf校验
        // 默认token存在session里，用CookieCsrfTokenRepository改为token存在cookie里。
        // 但存在后端多台服务器情况，session不能同步的问题，所以一般使用cookie模式。
        http.csrf()
                .requireCsrfProtectionMatcher(csrfRequestMatcher)
                .ignoringAntMatchers(csrfExcludeUrl)  // 不进行csrf校验的uri，多个uri使用逗号分隔
                .csrfTokenRepository(new CookieCsrfTokenRepository());
        // 自定义csrf校验失败的代码，默认是返回403错误页面
        http.exceptionHandling().accessDeniedHandler(new CsrfAccessDeniedHandler());
        // http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }
}