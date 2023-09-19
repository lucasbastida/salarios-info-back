package com.example.salariogod.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PublishSalaryRequest {

    @NotEmpty
    private List<@Valid SalaryRequest> salaries;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SalaryRequest {
        @NotEmpty
        private List<PaymentRequest> payments;
        @NotNull
        private LocalDate paymentDate;
        private String techPosition;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PaymentRequest {
        @NotBlank
        private String amount;
        @NotBlank
        private String currency;
        private boolean taxed;
    }
}
