package org.example.ejournal.models;

import jakarta.persistence.*;
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
public class Grade extends BasicEntity {

    @Column(precision = 1, scale = 1, nullable = false)
    private BigDecimal value;

    @OneToOne
    private Subject subject;

    @OneToOne
    private Teacher gradedByTeacher;

    @ManyToOne
    private Student student;
}
