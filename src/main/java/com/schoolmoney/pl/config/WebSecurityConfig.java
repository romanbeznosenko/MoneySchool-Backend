package com.schoolmoney.pl.config;

import com.schoolmoney.pl.filters.TrackHeadersFilter;
import com.schoolmoney.pl.filters.RequestLogFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    private static final String[] WHITE_LIST_URL = {
            "/v3/api-docs",
            "/v3/api-docs.yaml",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/auth/**",
            "/error/**",
            "/ws/**",
            "/csrf"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, TrackHeadersFilter trackHeadersFilter, RequestLogFilter requestLogFilter) throws Exception {
        httpSecurity.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
        httpSecurity.addFilterBefore(requestLogFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterBefore(trackHeadersFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.cors(Customizer.withDefaults());

        httpSecurity.csrf(csrf -> csrf.disable());

        httpSecurity.authorizeHttpRequests(auth -> {

            auth.requestMatchers(HttpMethod.OPTIONS, "/**")
                    .permitAll();

            for (String urlPatter : WHITE_LIST_URL) {
                auth.requestMatchers(urlPatter)
                        .permitAll();
            }

            auth.requestMatchers("/api/**")
                    .authenticated();

        });

        httpSecurity.exceptionHandling(exceptionHandling ->
                exceptionHandling.authenticationEntryPoint(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        );

        // Configure logout
        httpSecurity.logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessHandler((request, response, authentication) -> response.setStatus(HttpServletResponse.SC_OK))
                .invalidateHttpSession(true)
                .deleteCookies("DFSESSIONID")
                .deleteCookies("CSRF-TOKEN")
                .permitAll()
        );

        return httpSecurity.build();
    }
}