package com.example.salariogod.web.publish;

import com.example.salariogod.application.domain.Payment;
import com.example.salariogod.application.domain.Salary;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PublishSalaryTest {

    @DisplayName("to() converts to domain with expected fields")
    @Test
    void to() {
        final PublishPayment payment = mock(PublishPayment.class);
        when(payment.to()).thenReturn(new Payment());

        final LocalDate now = LocalDate.now();
        final PublishSalary publishSalary = new PublishSalary();
        publishSalary.setPayments(List.of(payment));
        publishSalary.setPaymentDate(now);

        final Salary expected = Salary.builder()
                .payments(List.of(new Payment()))
                .paymentDate(now)
                .build();

        final Salary salary = publishSalary.to();

        assertThat(salary).usingRecursiveComparison().isEqualTo(expected);
        verify(payment).to();
    }
}