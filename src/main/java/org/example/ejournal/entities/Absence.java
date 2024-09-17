package org.example.ejournal.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ejournal.enums.WeekDay;

import java.time.LocalDateTime;

/**
 * The type Absence.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "absences")
public class Absence extends BaseEntity {
    @Column
    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    private WeekDay day;
    
    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;
    
    @Column(columnDefinition = "boolean default false")
    private boolean isExcused;
    
    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;
    
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    // record the year when the grade was given
    @Column(name = "academic_year", nullable = false)
    private String academicYear;

    // absences for a particular grade (10th, 11th)
    @ManyToOne
    private SchoolClass schoolClass;

}
