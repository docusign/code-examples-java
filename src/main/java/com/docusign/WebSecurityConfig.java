package com.docusign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

import com.docusign.core.security.CustomAuthenticationFailureHandler;

@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    public DSConfiguration dsConfiguration;

    @Bean
    public RequestCache requestCache() {
        return new HttpSessionRequestCache();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> {
                    try {
                        authorize
                                .antMatchers("/", "/error**", "/assets/**", "/ds/mustAuthenticate**",
                                        "/ds/authenticate**", "/ds/selectApi**", "/con001", "/pkce")
                                .permitAll()
                                .anyRequest().authenticated()
                                .and()
                                .exceptionHandling()
                                .authenticationEntryPoint(
                                        new LoginUrlAuthenticationEntryPoint("/ds/mustAuthenticate"));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .requestCache().requestCache(requestCache()).and()
                .oauth2Login(login -> login.failureHandler(new CustomAuthenticationFailureHandler()))
                .oauth2Client(Customizer.withDefaults())
                .logout(logout -> logout
                        .logoutSuccessUrl("/"))
                .csrf().disable();

        return http.build();
    }
}