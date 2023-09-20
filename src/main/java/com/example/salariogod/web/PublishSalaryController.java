package com.example.salariogod.web;

import com.example.salariogod.application.domain.Payment;
import com.example.salariogod.application.domain.Salary;
import com.example.salariogod.application.service.PublishSalaryService;
import com.example.salariogod.web.PublishSalaryRequest.PaymentRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PublishSalaryController {

    private final PublishSalaryService publishSalaryService;

    @PostMapping("/v1/salary")
    public ResponseEntity<Void> publish(@RequestBody @Valid PublishSalaryRequest request) {
        final List<Salary> salaries = convert(request);
        publishSalaryService.publish(salaries);
        return ResponseEntity.ok().build();
    }

    private static List<Salary> convert(PublishSalaryRequest request) {
        return request.getSalaries().stream()
                .map(salaryRequest -> Salary.builder()
                        .paymentDate(salaryRequest.getPaymentDate())
                        .techPosition(salaryRequest.getTechPosition())
                        .creationInstant(Instant.now())
                        .payments(convert(salaryRequest.getPayments()))
                        .build())
                .toList();
    }

    private static List<Payment> convert(List<PaymentRequest> payments) {
        return payments.stream()
                .map(paymentRequest -> Payment.builder()
                        .taxed(paymentRequest.isTaxed())
                        .amount(paymentRequest.getAmount())
                        .currency(paymentRequest.getCurrency())
                        .build())
                .toList();
    }
}
