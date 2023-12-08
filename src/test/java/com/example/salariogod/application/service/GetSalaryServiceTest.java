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
    void getSalary_raisesExceptionWhenRepositoryFailsFindingIds(GetSalaryQuery query) {
        lenient().when(salaryRepository.findSalaryIdsByTechRole(any(), any())).thenThrow(RuntimeException.class);
        lenient().when(salaryRepository.findAllSalaryIds(any(PageRequest.class))).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> sut.getSalary(query)).isInstanceOf(RuntimeException.class);
    }

    @ParameterizedTest
    @MethodSource("validQueries")
    void getSalary_raisesExceptionWhenRepositoryFailsFindingSalaries(GetSalaryQuery query) {
        final List<Long> ids = List.of(1L);
        final PageImpl<Long> pagedIdsMock = new PageImpl<>(ids, PageRequest.of(0, 25), ids.size());

        lenient().when(salaryRepository.findSalaryIdsByTechRole(any(), any())).thenReturn(pagedIdsMock);
        lenient().when(salaryRepository.findAllSalaryIds(any(PageRequest.class))).thenReturn(pagedIdsMock);
        when(salaryRepository.findByIdInOrderByCreationInstant(ids)).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> sut.getSalary(query)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void getSalary_whenQueryHasTechRole_then_searchByTechRole() {
        GetSalaryQuery query = new GetSalaryQuery(1, 25, TechRole.BACKEND);

        final List<Long> ids = List.of(1L);
        final PageImpl<Long> pagedIdsMock = new PageImpl<>(ids, PageRequest.of(0, 25), ids.size());

        Page<Salary> expectedPage = new PageImpl<>(List.of(new Salary()), PageRequest.of(1, 25), 1);

        when(salaryRepository.findSalaryIdsByTechRole(eq(TechRole.BACKEND), any())).thenReturn(pagedIdsMock);
        when(salaryRepository.findByIdInOrderByCreationInstant(ids)).thenReturn(List.of(new Salary()));

        Page<Salary> result = sut.getSalary(query);

        assertEquals(expectedPage, result);

        verify(salaryRepository, times(1)).findSalaryIdsByTechRole(eq(TechRole.BACKEND), pageRequestArgumentCaptor.capture());
        verify(salaryRepository, never()).findAllSalaryIds(any(PageRequest.class));

        final PageRequest pageRequest = pageRequestArgumentCaptor.getValue();
        assertThat(pageRequest.getPageSize()).isEqualTo(PAGE_SIZE);
        final Sort.Order orderFor = pageRequest.getSort().getOrderFor("creationInstant");
        assertThat(orderFor).isNotNull();
        assertThat(orderFor.getDirection()).isEqualTo(Sort.Direction.DESC);
    }

    @Test
    void getSalary_whenQueryOnlyHasPage_then_searchAll() {
        GetSalaryQuery query = new GetSalaryQuery(1, 25, null);

        final List<Long> ids = List.of(1L);
        final PageImpl<Long> pagedIdsMock = new PageImpl<>(ids, PageRequest.of(0, 25), ids.size());

        Page<Salary> expectedPage = new PageImpl<>(List.of(new Salary()), PageRequest.of(1, 25), 1);

        when(salaryRepository.findAllSalaryIds(any(PageRequest.class))).thenReturn(pagedIdsMock);
        when(salaryRepository.findByIdInOrderByCreationInstant(ids)).thenReturn(List.of(new Salary()));

        Page<Salary> result = sut.getSalary(query);

        assertEquals(expectedPage, result);

        verify(salaryRepository, never()).findSalaryIdsByTechRole(eq(TechRole.BACKEND), any());
        verify(salaryRepository, times(1)).findAllSalaryIds(pageRequestArgumentCaptor.capture());

        final PageRequest pageRequest = pageRequestArgumentCaptor.getValue();
        assertThat(pageRequest.getPageSize()).isEqualTo(PAGE_SIZE);
        final Sort.Order orderFor = pageRequest.getSort().getOrderFor("creationInstant");
        assertThat(orderFor).isNotNull();
        assertThat(orderFor.getDirection()).isEqualTo(Sort.Direction.DESC);
    }
}