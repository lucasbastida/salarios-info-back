package com.example.salariogod.web.publish;

import com.example.salariogod.application.domain.Contract;
import com.example.salariogod.application.domain.Payment;
import com.example.salariogod.application.domain.Salary;
import jakarta.validation.Valid;
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
public class PublishSalary {
    @NotEmpty
    private List<@Valid PublishPayment> payments;
    @NotNull
    private LocalDate paymentDate;
    @NotNull
    private Contract contract;
    private String techPosition;

    public Salary to() {
        final List<Payment> paymentList = this.payments.stream()
                .map(PublishPayment::to)
                .toList();

        return Salary.builder()
                .paymentDate(this.paymentDate)
                .payments(paymentList)
                .contractType(contract)
                .build();
    }
}
