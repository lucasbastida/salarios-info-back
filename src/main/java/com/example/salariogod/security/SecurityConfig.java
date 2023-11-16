package com.example.salariogod.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain publicFilterChain(HttpSecurity httpSecurity,
                                                 @Autowired(required = false) RecaptchaService recaptchaService) throws Exception {
        httpSecurity.
                csrf(AbstractHttpConfigurer::disable).
                authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/actuator/health")
                                .permitAll()
                                .requestMatchers("/actuator/**")
                                .authenticated()
                                .anyRequest()
                                .permitAll())
                .httpBasic(Customizer.withDefaults())
                .cors(Customizer.withDefaults());

        if (recaptchaService != null) {
            httpSecurity.addFilterBefore(new RecaptchaV3Filter(recaptchaService), UsernamePasswordAuthenticationFilter.class);
        }

        return httpSecurity.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
