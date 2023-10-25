package com.example.salariogod.security;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RecaptchaResponse {
    private boolean success;
    private Double score;
    private String action;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZZ")
    @JsonAlias("challenge_ts")
    private ZonedDateTime challengeTimestamp;
    private String hostname;
    @JsonAlias("error-codes")
    private String errorCodes;

    public boolean isInvalid(Double minScore) {
        return !this.success || score < minScore;
    }
}
