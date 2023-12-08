package com.example.salariogod.application.service;

import com.example.salariogod.application.domain.TechRole;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetSalaryQuery {
    private int page;
    private Integer size;
    private TechRole techRole;
}
