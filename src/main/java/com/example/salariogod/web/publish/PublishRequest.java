package com.example.salariogod.web.publish;

import com.example.salariogod.application.domain.Salary;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PublishRequest {

    @NotEmpty
    @Valid
    private List<PublishSalary> salaries;

    public List<Salary> to() {
        return salaries.stream()
                .map(PublishSalary::to)
                .toList();
    }
}
