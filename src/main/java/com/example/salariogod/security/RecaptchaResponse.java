package com.example.salariogod.security;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RecaptchaResponse {
    private Boolean success;
    private Double score;
    private String action;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @JsonAlias("challenge_ts")
    private Instant challengeTimestamp;
    private String hostname;
    @JsonAlias("error-codes")
    private List<String> errorCodes;

    public boolean failed(Double minScore) {
        return !this.success || score == null || score < minScore;
    }
}
