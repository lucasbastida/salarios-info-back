package com.example.salariogod.web.publish;

import com.example.salariogod.application.domain.TechRole;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

public class PublishSalaryValidator implements ConstraintValidator<ValidPublishSalary, PublishSalary> {
    @Override
    public boolean isValid(PublishSalary value, ConstraintValidatorContext context) {
        return TechRole.OTHER.equals(value.getTechRole()) || !StringUtils.hasText(value.getOtherRole());
    }
}
