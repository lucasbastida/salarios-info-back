package com.example.salariogod.application.service;

import com.example.salariogod.application.domain.Salary;
import com.example.salariogod.persistence.SalaryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

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

        Page<Long> salaryIds;
        if (getSalaryQuery.getTechRole() != null) {
            salaryIds = salaryRepository.findSalaryIdsByTechRole(getSalaryQuery.getTechRole(), pageRequest);
        } else {
            salaryIds = salaryRepository.findAllSalaryIds(pageRequest);
        }

        final List<Long> content = salaryIds.getContent();
        final List<Salary> salaries = salaryRepository.findByIdInOrderByCreationInstant(content);

        return new PageImpl<>(salaries, PageRequest.of(getSalaryQuery.getPage(), pageSize), salaries.size());
    }
}
