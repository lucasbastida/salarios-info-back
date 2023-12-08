package com.example.salariogod.persistence;

import com.example.salariogod.application.domain.Salary;
import com.example.salariogod.application.domain.TechRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SalaryRepository extends JpaRepository<Salary, Long> {

    @Query("select s.id from Salary s where s.techRole = :techRole")
    Page<Long> findSalaryIdsByTechRole(TechRole techRole, Pageable pageable);

    @Query("select s.id from Salary s")
    Page<Long> findAllSalaryIds(Pageable pageable);

    @Query("from Salary s join fetch s.payments where s.id in :salaryIds")
    List<Salary> findByIdInOrderByCreationInstant(List<Long> salaryIds);
}
