package com.example.salariogod.web.publish;

import com.example.salariogod.application.domain.Payment;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PublishPayment {
    @NotBlank
    private String amount;
    @NotBlank
    private String currency;
    private boolean gross;

    public Payment to() {
        return Payment.builder()
                .amount(this.amount)
                .gross(this.gross)
                .currency(this.currency)
                .build();
    }
}
