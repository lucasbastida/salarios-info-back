package com.example.salariogod.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain publicFilterChain(HttpSecurity httpSecurity,
                                                 @Autowired(required = false) RecaptchaService recaptchaService) throws Exception {
        HttpSecurity security = httpSecurity
                .csrf(Customizer.withDefaults())
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers(HttpMethod.POST, "api/v1/salary").permitAll()
                );
        if (recaptchaService != null) {
            security.addFilterBefore(new RecaptchaV3Filter(recaptchaService), UsernamePasswordAuthenticationFilter.class);
        }
        return security.build();
    }
}
