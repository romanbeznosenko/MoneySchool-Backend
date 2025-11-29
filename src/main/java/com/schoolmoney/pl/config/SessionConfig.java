package com.schoolmoney.pl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.SessionIdGenerator;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
public class SessionConfig {
    @Bean
    public SessionIdGenerator sessionIdGenerator() {
        return new SessionIdGeneratorConfig();
    }

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("DFSESSIONID");
        serializer.setCookiePath("/");
        serializer.setUseHttpOnlyCookie(true);
        serializer.setSameSite("None");
        serializer.setUseSecureCookie(true);
        // Remove domain pattern to allow cookies to work with the exact backend host
        // This enables cross-origin cookie sharing when using withCredentials: true
        serializer.setCookieMaxAge(7 * 24 * 60 * 60);

        return serializer;
    }
}

