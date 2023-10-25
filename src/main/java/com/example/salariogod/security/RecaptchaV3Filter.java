package com.example.salariogod.security;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class RecaptchaV3Filter extends OncePerRequestFilter {

    private final RecaptchaService recaptchaService;

    public RecaptchaV3Filter(RecaptchaService recaptchaService) {
        this.recaptchaService = recaptchaService;
    }

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain) throws ServletException, IOException {
        if (request.getMethod().equals("POST")) {
            final String token = request.getHeader("recaptcha");
            if (!recaptchaService.valid(token)) {
                throw new BadCredentialsException("invalid recaptcha");
            }
        }
        filterChain.doFilter(request, response);
    }
}
