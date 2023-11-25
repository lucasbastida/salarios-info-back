package com.example.salariogod.persistence;

import com.example.salariogod.application.domain.Salary;
import com.example.salariogod.application.domain.TechRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SalaryRepository extends JpaRepository<Salary, Long> {

    @Query("from Salary s join fetch s.payments where s.techRole = :techRole")
    Page<Salary> findByTechRole(TechRole techRole, Pageable pageable);

}
