package com.example.salariogod.web.get;

import com.example.salariogod.application.domain.Payment;
import com.example.salariogod.application.domain.Salary;
import com.example.salariogod.web.get.SalaryResponse.PaymentResponse;
import jakarta.annotation.Nonnull;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SalaryResponseAssembler implements RepresentationModelAssembler<Salary, SalaryResponse> {
    @Override
    public @Nonnull SalaryResponse toModel(@Nonnull Salary entity) {
        final List<PaymentResponse> payments = new ArrayList<>();
        final SalaryResponse response = new SalaryResponse();

        response.setContract(entity.getContract());
        response.setTechRole(entity.getTechRole());

        for (Payment payment : entity.getPayments()) {
            final PaymentResponse paymentResponse = new PaymentResponse();
            paymentResponse.setAmount(payment.getAmount());
            paymentResponse.setCurrency(payment.getCurrency());
            paymentResponse.setGross(paymentResponse.isGross());
            payments.add(paymentResponse);
        }

        response.setPayments(payments);
        response.setSubmittedAt(entity.getCreationInstant());
        response.setMonthPayed(entity.getPaymentDate().getMonth());

        return response;
    }
}
