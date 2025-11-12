package org.example.ejournal.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "grades")
public class Grade extends BaseEntity {

    @Column(precision = 3, scale = 2)
    @NotNull
    private BigDecimal value;

    @ManyToOne(fetch = FetchType.LAZY)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    private Teacher gradedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    private Student student;
}
