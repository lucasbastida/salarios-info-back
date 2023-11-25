package com.example.salariogod.web.publish;

import com.example.salariogod.application.domain.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ValidPublishSalary
public class PublishSalary {
    @NotEmpty
    private List<@Valid PublishPayment> payments;

    @NotNull
    private LocalDate paymentDate;

    @NotNull
    private Contract contract;

    @NotNull
    private TechRole techRole;

    private String otherRole;

    public Salary to() {
        final List<Payment> paymentList = this.payments.stream()
                .map(PublishPayment::to)
                .toList();

        final Salary salary = Salary.builder()
                .paymentDate(this.paymentDate)
                .payments(paymentList)
                .contract(this.contract)
                .techRole(this.techRole)
                .build();

        if (TechRole.OTHER.equals(this.techRole) && StringUtils.hasText(this.otherRole)) {
            final OtherRole other = new OtherRole();
            other.setName(this.otherRole);
            salary.setOtherRole(List.of(other));
        }

        return salary;
    }
}
