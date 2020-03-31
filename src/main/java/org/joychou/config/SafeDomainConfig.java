package org.joychou.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 为了不要每次调用都解析safedomain的xml，所以将解析动作放在Bean里。
 */
@Configuration
public class SafeDomainConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(SafeDomainConfig.class);

    @Bean // @Bean代表将safeDomainParserf方法返回的对象装配到SpringIOC容器中
    public SafeDomainParser safeDomainParser() {
        try {
            LOGGER.info("SafeDomainParser bean inject successfully!!!");
            return new SafeDomainParser();
        } catch (Exception e) {
            LOGGER.error("SafeDomainParser is null " + e.getMessage(), e);
        }
        return null;
    }

}

