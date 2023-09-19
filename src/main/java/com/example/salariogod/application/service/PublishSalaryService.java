package com.example.salariogod.application.service;

import com.example.salariogod.application.domain.Salary;
import com.example.salariogod.persistence.SalaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PublishSalaryService {

    private final SalaryRepository salaryRepository;

    public void publish(List<Salary> salaries) {
        salaryRepository.saveAll(salaries);
    }

}
