package com.example.salariogod.application.service;

import com.example.salariogod.application.domain.Salary;
import com.example.salariogod.application.domain.TechRole;
import com.example.salariogod.persistence.SalaryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetSalaryServiceTest {

    private static final int PAGE_SIZE = 25;

    @Mock
    private SalaryRepository salaryRepository;

    private GetSalaryService sut;

    @Captor
    private ArgumentCaptor<PageRequest> pageRequestArgumentCaptor;

    @BeforeEach
    void setUp() {
        sut = new GetSalaryService(salaryRepository, PAGE_SIZE);
    }

    private static Stream<GetSalaryQuery> validQueries() {
        return Stream.of(
                GetSalaryQuery.builder().page(1).techRole(TechRole.BACKEND).build(),
                GetSalaryQuery.builder().page(1).build()
        );
    }

    @ParameterizedTest
    @MethodSource("validQueries")
    void getSalary_raisesExceptionWhenRepositoryFails(GetSalaryQuery query) {
        lenient().when(salaryRepository.findByTechRole(any(), any())).thenThrow(RuntimeException.class);
        lenient().when(salaryRepository.findAll(any(PageRequest.class))).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> sut.getSalary(query)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void getSalary_whenQueryHasTechRole_then_searchByTechRole() {
        GetSalaryQuery query = new GetSalaryQuery(1, TechRole.BACKEND);

        Page<Salary> expectedPage = new PageImpl<>(List.of(new Salary()));

        when(salaryRepository.findByTechRole(eq(TechRole.BACKEND), any())).thenReturn(expectedPage);

        Page<Salary> result = sut.getSalary(query);

        assertEquals(expectedPage, result);

        verify(salaryRepository, times(1)).findByTechRole(eq(TechRole.BACKEND), pageRequestArgumentCaptor.capture());
        verify(salaryRepository, never()).findAll(any(PageRequest.class));

        final PageRequest pageRequest = pageRequestArgumentCaptor.getValue();
        assertThat(pageRequest.getPageSize()).isEqualTo(PAGE_SIZE);
        final Sort.Order orderFor = pageRequest.getSort().getOrderFor("creationInstant");
        assertThat(orderFor).isNotNull();
        assertThat(orderFor.getDirection()).isEqualTo(Sort.Direction.DESC);
    }

    @Test
    void getSalary_whenQueryOnlyHasPage_then_searchAll() {
        GetSalaryQuery query = new GetSalaryQuery(1, null);

        Page<Salary> expectedPage = new PageImpl<>(List.of(new Salary()));

        when(salaryRepository.findAll(any(PageRequest.class))).thenReturn(expectedPage);

        Page<Salary> result = sut.getSalary(query);

        assertEquals(expectedPage, result);

        verify(salaryRepository, never()).findByTechRole(eq(TechRole.BACKEND), any());
        verify(salaryRepository, times(1)).findAll(pageRequestArgumentCaptor.capture());

        final PageRequest pageRequest = pageRequestArgumentCaptor.getValue();
        assertThat(pageRequest.getPageSize()).isEqualTo(PAGE_SIZE);
        final Sort.Order orderFor = pageRequest.getSort().getOrderFor("creationInstant");
        assertThat(orderFor).isNotNull();
        assertThat(orderFor.getDirection()).isEqualTo(Sort.Direction.DESC);
    }
}