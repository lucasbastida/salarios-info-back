package com.example.salariogod.web.publish;

import com.example.salariogod.application.domain.Salary;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PublishRequestTest {

    @DisplayName("to() converts to domain with expected fields")
    @Test
    void to() {
        final PublishSalary mockSalary = mock(PublishSalary.class);
        when(mockSalary.to()).thenReturn(new Salary());

        final PublishRequest request = new PublishRequest();
        request.setSalaries(List.of(mockSalary));

        final List<Salary> expected = List.of(new Salary());

        final List<Salary> salaries = request.to();

        assertThat(salaries).usingRecursiveComparison().isEqualTo(expected);
        verify(mockSalary).to();
    }
}