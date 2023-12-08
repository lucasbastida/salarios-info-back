package com.example.salariogod.persistence;

import com.example.salariogod.application.domain.TechRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class SalaryRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16-bookworm");

    @Autowired
    private SalaryRepository sut;

    @Test
    void test() {
        final PageRequest pageRequest = PageRequest.of(0, 25);
        final Page<Long> byTechRole = sut.findSalaryIdsByTechRole(TechRole.BACKEND, pageRequest);

        assertThat(byTechRole.getTotalElements()).isZero();
    }
}