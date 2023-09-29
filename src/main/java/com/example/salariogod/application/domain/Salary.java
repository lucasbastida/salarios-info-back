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
    private Contract contractType;

    @Column
    private String comment;

    @Column(nullable = false)
    private TechRole techRole;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "salary")
    private OtherRole otherRole;
}
