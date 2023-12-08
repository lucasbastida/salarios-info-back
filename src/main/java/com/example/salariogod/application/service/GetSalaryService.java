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
import java.util.Optional;

@Service
public class GetSalaryService {

    private final SalaryRepository salaryRepository;
    private final Integer maxPageSize;

    public GetSalaryService(SalaryRepository salaryRepository,
                            @Value("${app.get-salary-page-size: 25}") Integer maxPageSize) {
        this.salaryRepository = salaryRepository;
        this.maxPageSize = maxPageSize;
    }

    public Page<Salary> getSalary(GetSalaryQuery getSalaryQuery) {
        final Integer size = Optional.ofNullable(getSalaryQuery.getSize()).orElse(maxPageSize);
        final int pageRequestSize = size <= this.maxPageSize ? size : this.maxPageSize;

        final PageRequest pageRequest = PageRequest.of(getSalaryQuery.getPage(), pageRequestSize, Sort.by(Sort.Direction.DESC, "creationInstant"));

        Page<Long> salaryIds;
        if (getSalaryQuery.getTechRole() != null) {
            salaryIds = salaryRepository.findSalaryIdsByTechRole(getSalaryQuery.getTechRole(), pageRequest);
        } else {
            salaryIds = salaryRepository.findAllSalaryIds(pageRequest);
        }

        final List<Long> content = salaryIds.getContent();
        final List<Salary> salaries = salaryRepository.findByIdInOrderByCreationInstant(content);

        return new PageImpl<>(salaries, PageRequest.of(getSalaryQuery.getPage(), maxPageSize), salaryIds.getTotalElements());
    }
}
