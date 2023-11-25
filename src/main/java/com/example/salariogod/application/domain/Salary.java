package com.example.salariogod.application.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
public class Salary {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Payment> payments;

    @Column(nullable = false)
    private Instant creationInstant;

    @Column(nullable = false)
    private LocalDate paymentDate;

    @Column(nullable = false)
    @Enumerated(value = EnumType.ORDINAL)
    private Contract contract;

    @Column(nullable = false)
    private TechRole techRole;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "salary_id")
    private List<OtherRole> otherRole;

    @Column
    private String comment;
}
