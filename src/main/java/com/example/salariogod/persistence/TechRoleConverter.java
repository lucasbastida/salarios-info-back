package com.example.salariogod.persistence;

import com.example.salariogod.application.domain.TechRole;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TechRoleConverter extends AbstractEnumConverter<TechRole, Short> {
    private TechRoleConverter() {
        super(TechRole.class);
    }
}
