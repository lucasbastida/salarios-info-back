package com.example.salariogod.application.service;

import com.example.salariogod.application.domain.Salary;
import com.example.salariogod.persistence.SalaryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class GetSalaryService {

    private final SalaryRepository salaryRepository;
    private final Integer pageSize;

    public GetSalaryService(SalaryRepository salaryRepository,
                            @Value("${app.get-salary-page-size: 25}") Integer pageSize) {
        this.salaryRepository = salaryRepository;
        this.pageSize = pageSize;
    }

    public Page<Salary> getSalary(GetSalaryQuery getSalaryQuery) {
        final PageRequest pageRequest = PageRequest.of(getSalaryQuery.getPage(), pageSize, Sort.by(Sort.Direction.DESC, "creationInstant"));

        if (getSalaryQuery.getTechRole() != null) {
            return salaryRepository.findByTechRole(getSalaryQuery.getTechRole(), pageRequest);
        } else {
            return salaryRepository.findAllFetchPayments(pageRequest);
        }
    }
}
