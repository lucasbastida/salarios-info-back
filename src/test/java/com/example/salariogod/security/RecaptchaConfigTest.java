package com.example.salariogod.security;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.executable.ExecutableValidator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Method;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RecaptchaConfigTest {

    @Nested
    class ValidationTest {

        private static ValidatorFactory validatorFactory;
        private static ExecutableValidator executableValidator;

        @AfterAll
        static void afterAll() {
            validatorFactory.close();
        }

        @BeforeAll
        static void beforeAll() {
            validatorFactory = Validation.buildDefaultValidatorFactory();
            executableValidator = validatorFactory.getValidator().forExecutables();
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(doubles = 1.1)
        void whenMinScoreIsInvalid_thenViolateConstraint(Double minScore) throws NoSuchMethodException {
            final RecaptchaConfig recaptchaConfig = new RecaptchaConfig();
            final Method recaptchaService = RecaptchaConfig.class.getMethod("recaptchaService", RestTemplate.class, Double.class, String.class, String.class);
            final Object[] objects = {null, minScore, "secret", "url"};

            final Set<ConstraintViolation<RecaptchaConfig>> constraintViolations = executableValidator.validateParameters(recaptchaConfig, recaptchaService, objects);

            assertThat(constraintViolations).isNotEmpty();
            assertThat(RecaptchaConfig.class.getAnnotation(Validated.class)).isNotNull();
        }
    }

    @Nested
    class ExpectedBeanTest {
        ApplicationContextRunner runner = new ApplicationContextRunner().withUserConfiguration(RecaptchaConfig.class);

        @Test
        void whenRecaptchaDisabled_thenNoBeans() {
            runner.run(context -> assertThat(context)
                    .doesNotHaveBean(RecaptchaService.class)
                    .doesNotHaveBean("recaptchaRestTemplate"));
        }

        @Test
        void whenRecaptchaEnabled_thenCreateRecaptchaBeans() {
            runner.withPropertyValues(
                            "app-recaptcha.enabled=true",
                            "app-recaptcha.min-score=0.1",
                            "app-recaptcha.secret=adsf-123",
                            "app-recaptcha.url=https://google.com")
                    .run(context -> assertThat(context)
                            .hasBean("recaptchaRestTemplate")
                            .hasSingleBean(RecaptchaService.class));
        }
    }
}