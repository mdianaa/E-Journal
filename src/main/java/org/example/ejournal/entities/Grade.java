package org.example.ejournal.entities;

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
public class Grade extends BaseEntity {

    @Column(precision = 3, scale = 2, nullable = false)
    private BigDecimal value;

    @ManyToOne
    private Subject subject;

    @ManyToOne
    private Teacher gradedByTeacher;

    @ManyToOne
    private Student student;

    // grades for the student for the particular class (10th, 11th ect.)
    @ManyToOne
    private SchoolClass schoolClass;
}
