package com.example.salariogod.web.publish;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = PublishSalaryValidator.class)
public @interface ValidPublishSalary {
    String message() default "otherRole must be empty when techRole is not OTHER";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
