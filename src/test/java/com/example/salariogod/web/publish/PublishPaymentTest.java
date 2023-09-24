package com.example.salariogod.web.publish;

import com.example.salariogod.application.domain.Payment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PublishPaymentTest {

    @DisplayName("to() converts to domain with expected fields")
    @Test
    void to() {
        PublishPayment publishPayment = new PublishPayment();
        publishPayment.setGross(true);
        publishPayment.setAmount("1");
        publishPayment.setCurrency("USD");

        Payment expected = Payment.builder()
                .gross(true)
                .amount("1")
                .currency("USD")
                .build();

        Payment payment = publishPayment.to();

        assertThat(payment).usingRecursiveComparison().isEqualTo(expected);
    }
}