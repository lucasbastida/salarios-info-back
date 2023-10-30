package com.example.salariogod.security;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class RecaptchaResponseTest {

    @MethodSource("failedRecaptchaResponseSource")
    @ParameterizedTest
    void failedRecaptchaResponse(boolean success, Double score) {
        final RecaptchaResponse response = new RecaptchaResponse();
        response.setSuccess(success);
        response.setScore(score);

        final boolean failed = response.failed(0.5);

        assertThat(failed).isTrue();
    }

    @ValueSource(doubles = {0.5, 0.6, 1, 2})
    @ParameterizedTest
    void successfulRecaptchaResponse(double score) {
        final RecaptchaResponse response = new RecaptchaResponse();
        response.setSuccess(true);
        response.setScore(score);

        final boolean failed = response.failed(0.5);

        assertThat(failed).isFalse();
    }

    private static Stream<Arguments> failedRecaptchaResponseSource() {
        return Stream.of(
                Arguments.of(false, 0.6),
                Arguments.of(false, null),
                Arguments.of(true, 0.3),
                Arguments.of(true, null)
        );
    }
}