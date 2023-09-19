package com.example.salariogod.persistence;

import com.example.salariogod.application.domain.Salary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaryRepository extends JpaRepository<Salary, Long> {
}
