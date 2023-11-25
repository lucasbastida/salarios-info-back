package com.example.salariogod.web.get;

import com.example.salariogod.application.domain.Salary;
import com.example.salariogod.application.domain.TechRole;
import com.example.salariogod.application.service.GetSalaryQuery;
import com.example.salariogod.application.service.GetSalaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GetSalaryController {

    private final GetSalaryService getSalaryService;
    private final SalaryResponseAssembler salaryResponseAssembler;
    private final PagedResourcesAssembler<Salary> salaryPagedResourcesAssembler;

    @GetMapping("/v1/salaries")
    public PagedModel<SalaryResponse> getSalaries(@RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                  @RequestParam(name = "techRole", required = false) TechRole techRole) {

        final GetSalaryQuery query = GetSalaryQuery.builder()
                .page(page)
                .techRole(techRole)
                .build();

        final Page<Salary> salary = getSalaryService.getSalary(query);

        return salaryPagedResourcesAssembler.toModel(salary, salaryResponseAssembler);
    }
}
