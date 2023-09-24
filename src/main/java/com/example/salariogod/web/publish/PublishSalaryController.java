package com.example.salariogod.web.publish;

import com.example.salariogod.application.domain.Salary;
import com.example.salariogod.application.service.PublishSalaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PublishSalaryController {

    private final PublishSalaryService publishSalaryService;

    @PostMapping("/v1/salary")
    public ResponseEntity<Void> publish(@RequestBody @Valid PublishRequest request) {
        final List<Salary> salaries = request.to();
        publishSalaryService.publish(salaries);
        return ResponseEntity.ok().build();
    }
}
