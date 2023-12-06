package com.example.salariogod.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @Test
    void whenRecaptchaServiceThrowsException_thenThrow() {
        when(recaptchaService.valid(anyString())).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> sut.doFilter(requestMock, responseMock, filterChainMock))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void whenRecaptchaTokenIsInvalid_thenThrowBadCredentialsException() {
        when(requestMock.getHeader(anyString())).thenReturn("foo");
        when(recaptchaService.valid(anyString())).thenReturn(false);

        assertThatThrownBy(() -> sut.doFilter(requestMock, responseMock, filterChainMock)).isExactlyInstanceOf(BadCredentialsException.class);
    }

    @Test
    void whenRecaptchaPassed_thenContinueFilterChain() throws ServletException, IOException {
        when(requestMock.getHeader(anyString())).thenReturn("foo");
        when(recaptchaService.valid(anyString())).thenReturn(true);

        sut.doFilter(requestMock, responseMock, filterChainMock);

        verify(requestMock).getHeader("recaptcha");
        verify(recaptchaService).valid("foo");
        verify(filterChainMock).doFilter(requestMock, responseMock);
    }
}