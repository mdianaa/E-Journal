package org.example.ejournal.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "bad_notes")
public class BadNote extends BaseEntity{
    
    @Column
    private LocalDateTime dateTime;

    @Column
    private String description;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Teacher teacher;

    // record the year when the grade was given
    @Column(name = "academic_year", nullable = false)
    private String academicYear;

    // bad notes for a particular grade (10th, 11th)
    @ManyToOne
    private SchoolClass schoolClass;

}
