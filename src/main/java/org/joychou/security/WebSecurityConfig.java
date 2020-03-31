package org.joychou.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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

    private RequestMatcher csrfRequestMatcher = new RequestMatcher() {

        @Override
        public boolean matches(HttpServletRequest request) {

            // 配置需要CSRF校验的请求方式，
            HashSet<String> allowedMethods = new HashSet<>(Arrays.asList(csrfMethod));
            // return false表示不校验csrf
            if (!csrfEnabled) {
                return false;
            }
            return allowedMethods.contains(request.getMethod());
        }

    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 默认token存在session里，用CookieCsrfTokenRepository改为token存在cookie里。
        // 但存在后端多台服务器情况，session不能同步的问题，所以一般使用cookie模式。
        http.csrf()
                .requireCsrfProtectionMatcher(csrfRequestMatcher)
                .ignoringAntMatchers(csrfExcludeUrl)  // 不进行csrf校验的uri，多个uri使用逗号分隔
                .csrfTokenRepository(new CookieCsrfTokenRepository());
        http.exceptionHandling().accessDeniedHandler(new CsrfAccessDeniedHandler());
        // http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());«

        http.cors();

        // spring security login settings
        http.authorizeRequests()
                .antMatchers("/css/**", "/js/**").permitAll() // permit static resources
                .anyRequest().authenticated().and() // any request authenticated except above static resources
                .formLogin().loginPage("/login").permitAll() // permit all to access /login page
                .successHandler(new LoginSuccessHandler())
                .failureHandler(new LoginFailureHandler()).and()
                .logout().logoutUrl("/logout").permitAll().and()
                // tomcat默认JSESSION会话有效时间为30分钟，所以30分钟不操作会话将过期。为了解决这一问题，引入rememberMe功能。
                .rememberMe();
    }

    /**
     * Global cors configure
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource()
    {
        // Set cors origin white list
        ArrayList<String> allowOrigins = new ArrayList<>();
        allowOrigins.add("joychou.org");
        allowOrigins.add("https://test.joychou.me"); // 区分http和https，并且默认不会拦截同域请求。

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowOrigins);
        configuration.setAllowCredentials(true);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/cors/sec/httpCors", configuration); // ant style
        return source;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("joychou").password("joychou123").roles("USER").and()
                .withUser("admin").password("admin123").roles("USER", "ADMIN");
    }
}


