package com.example.salariogod.application.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "other_role")
public class OtherRole {
    @Id
    private Long id;

    @Column(nullable = false, length = 75)
    private String name;

    @OneToOne
    @JoinColumn(name = "salary_id", nullable = false)
    private Salary salary;
}
