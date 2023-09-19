package com.example.salariogod.application.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
public class Salary {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToMany
    private List<Payment> payments;

    @Column(nullable = false)
    private Instant creationInstant;

    @Column(nullable = false)
    private LocalDate paymentDate;

    @Column
    private String techPosition;
}
