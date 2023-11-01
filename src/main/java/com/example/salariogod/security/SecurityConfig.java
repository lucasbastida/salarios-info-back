package com.example.salariogod.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain publicFilterChain(HttpSecurity httpSecurity,
                                                 @Autowired(required = false) RecaptchaService recaptchaService) throws Exception {
        httpSecurity.authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/actuator/**")
                                .authenticated()
                                .anyRequest()
                                .permitAll())
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable);

        if (recaptchaService != null) {
            httpSecurity.addFilterBefore(new RecaptchaV3Filter(recaptchaService), UsernamePasswordAuthenticationFilter.class);
        }

        return httpSecurity.build();
    }
}
