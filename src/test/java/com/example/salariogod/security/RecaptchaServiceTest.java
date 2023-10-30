package com.example.salariogod.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecaptchaServiceTest {
    @Captor
    private ArgumentCaptor<HttpEntity<LinkedMultiValueMap<String, String>>> captor;

    @Mock
    private RestTemplate restTemplate;

    private RecaptchaService sut;

    @BeforeEach
    void setUp() {
        sut = new RecaptchaService(restTemplate, 0.5, "secret", "url");
    }

    @Test
    void whenTokenValidationPostFails_thenThrow() {
        when(restTemplate.postForEntity(eq("url"), any(), eq(RecaptchaResponse.class))).thenThrow(RestClientException.class);

        assertThatThrownBy(() -> sut.valid("foo")).isInstanceOf(RuntimeException.class);
    }

    @Test
    void whenTokenValidationHasNoResponse_thenThrowIllegalStateExecption() {
        when(restTemplate.postForEntity(eq("url"), any(), eq(RecaptchaResponse.class))).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        assertThatThrownBy(() -> sut.valid("foo")).isExactlyInstanceOf(IllegalStateException.class);
    }

    @Test
    void whenTokenIsInvalid_thenReturnFalse() {
        final RecaptchaResponse response = new RecaptchaResponse();
        response.setSuccess(true);
        response.setScore(0.4);
        final ResponseEntity<RecaptchaResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);

        when(restTemplate.postForEntity(eq("url"), any(), eq(RecaptchaResponse.class))).thenReturn(responseEntity);

        final boolean actual = sut.valid("foo");

        assertThat(actual).isFalse();
    }

    @Test
    void whenValidToken_thenReturnTrue() {
        final RecaptchaResponse response = new RecaptchaResponse();
        response.setSuccess(true);
        response.setScore(0.5);
        final ResponseEntity<RecaptchaResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);

        when(restTemplate.postForEntity(eq("url"), captor.capture(), eq(RecaptchaResponse.class))).thenReturn(responseEntity);

        final boolean actual = sut.valid("foo");

        assertThat(actual).isTrue();

        final HttpEntity<LinkedMultiValueMap<String, String>> value = captor.getValue();
        final LinkedMultiValueMap<String, String> body = value.getBody();
        assertThat(value.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_FORM_URLENCODED);
        assertThat(body).contains(entry("secret", List.of("secret")), entry("response", List.of("foo")));
    }
}