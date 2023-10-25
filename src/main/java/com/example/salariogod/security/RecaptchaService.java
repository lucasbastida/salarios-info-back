package com.example.salariogod.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class RecaptchaService {

    private final RestTemplate restTemplate;
    private final Double minScore;
    private final String secretKey;
    private final String url;

    public RecaptchaService(RestTemplate restTemplate,
                            Double minScore,
                            String secretKey,
                            String url) {
        this.restTemplate = restTemplate;
        this.minScore = minScore;
        this.secretKey = secretKey;
        this.url = url;
    }

    public boolean valid(String token) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        final LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("secret", secretKey);
        map.add("response", token);
        final HttpEntity<LinkedMultiValueMap<String, String>> entity = new HttpEntity<>(map, httpHeaders);

        final ResponseEntity<RecaptchaResponse> response = restTemplate.postForEntity(url, entity, RecaptchaResponse.class);
        final RecaptchaResponse body = response.getBody();

        if (body == null) {
            throw new IllegalStateException("recaptcha response body is null");
        }

        if (body.isInvalid(this.minScore)) {
            log.info("Invalid recaptcha token: {} error-codes: {} score: {}", token, body.getErrorCodes(), body.getScore());
            return false;
        }

        return true;
    }
}
