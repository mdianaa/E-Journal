package org.example.ejournal.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(
        name = "school_classes",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_class_school_classname_period",
                        columnNames = {
                                "school_id",
                                "class_name",
                                "school_year_start",
                                "school_year_end"
                        }
                )
        }
)
public class SchoolClass extends BaseEntity {

    @Column
    @Size(min = 2, max = 3)
    @NotNull
    private String className;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "head_teacher_id")
    private Teacher headTeacher;

    @OneToMany(mappedBy = "schoolClass", fetch = FetchType.LAZY)
    private Set<Student> students;

    @ManyToOne(fetch = FetchType.LAZY)
    private School school;

    @Column(name = "school_year_start", nullable = false)
    private LocalDate schoolYearStart;

    @Column(name = "school_year_end", nullable = false)
    private LocalDate schoolYearEnd;

    @Column(nullable = false)
    private boolean deactivated = false;

}
