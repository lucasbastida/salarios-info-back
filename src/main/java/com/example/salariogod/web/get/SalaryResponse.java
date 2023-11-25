package com.example.salariogod.web.get;

import com.example.salariogod.application.domain.Contract;
import com.example.salariogod.application.domain.TechRole;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;
import java.time.Month;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalaryResponse extends RepresentationModel<SalaryResponse> {

    private Instant submittedAt;

    private Month monthPayed;

    private Contract contract;

    private TechRole techRole;

    private List<PaymentResponse> payments;

    @Getter
    @Setter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class PaymentResponse {
        private String amount;

        private String currency;

        private boolean gross;
    }
}
