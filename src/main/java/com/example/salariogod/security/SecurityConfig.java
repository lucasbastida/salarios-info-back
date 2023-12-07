package com.example.salariogod.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
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
    @Order(1)
    public SecurityFilterChain actuatorFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .securityMatchers(requestMatcherConfigurer -> requestMatcherConfigurer.
                        requestMatchers("/actuator/**", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**"))
                .authorizeHttpRequests(registry -> registry
                        .requestMatchers("/actuator/health")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .httpBasic(Customizer.withDefaults())
                .cors(Customizer.withDefaults())
                .build();
    }

    @Bean
    @Order(2)
    @ConditionalOnProperty(name = "app-recaptcha.enabled", havingValue = "true")
    public SecurityFilterChain recaptcha(HttpSecurity httpSecurity, RecaptchaService recaptchaService) throws Exception {
        return httpSecurity.
                csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest()
                        .permitAll())
                .httpBasic(Customizer.withDefaults())
                .cors(Customizer.withDefaults())
                .addFilterBefore(new RecaptchaV3Filter(recaptchaService), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain noRecaptcha(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.
                csrf(AbstractHttpConfigurer::disable).
                authorizeHttpRequests(authorize -> authorize
                        .anyRequest()
                        .permitAll())
                .httpBasic(Customizer.withDefaults())
                .cors(Customizer.withDefaults())
                .build();
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
