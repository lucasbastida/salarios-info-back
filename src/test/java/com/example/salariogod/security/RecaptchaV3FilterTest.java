package com.example.salariogod.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecaptchaV3FilterTest {

    @Mock
    private RecaptchaService recaptchaService;

    @InjectMocks
    private RecaptchaV3Filter sut;

    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    @Mock
    private FilterChain filterChainMock;

    @MethodSource("httpMethodsExcludingPost")
    @ParameterizedTest
    void whenNotPostRequest_thenDontValidate(HttpMethod httpMethod) throws ServletException, IOException {
        when(requestMock.getMethod()).thenReturn(httpMethod.name());

        sut.doFilter(requestMock, responseMock, filterChainMock);

        verify(requestMock, never()).getHeader("recaptcha");
        verify(recaptchaService, never()).valid(anyString());
        verify(filterChainMock).doFilter(requestMock, responseMock);
    }

    @Test
    void whenRecaptchaServiceThrowsException_thenThrow() {
    }

    @Test
    void whenRecaptchaTokenIsInvalid_thenThrowBadCredentialsException() {

    }

    @Test
    void whenRecaptchaPassed_thenContinueFilterChain() {

    }

    private static Stream<HttpMethod> httpMethodsExcludingPost() {
        return Arrays.stream(HttpMethod.values()).filter(httpMethod -> !httpMethod.matches("POST"));
    }
}