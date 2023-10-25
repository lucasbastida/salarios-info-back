package com.example.salariogod.security;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

@Configuration
@Validated
@ConditionalOnProperty(name = "app-recaptcha.enabled", havingValue = "true")
public class RecaptchaConfig {

    @Bean("recaptchaRestTemplate")
    public RestTemplate recaptchaRestTemplate() {
        return new RestTemplateBuilder().build();
    }

    @Bean
    public RecaptchaService recaptchaService(@Qualifier("recaptchaRestTemplate") RestTemplate restTemplate,
                                             @Value("${app-recaptcha.min-score}") @NotNull @PositiveOrZero @DecimalMax(value = "1") Double minScore,
                                             @Value("${app-recaptcha.secret}") @NotBlank String secret,
                                             @Value("${app-recaptcha.url}") @NotBlank String url) {
        return new RecaptchaService(restTemplate, minScore, secret, url);
    }
}
