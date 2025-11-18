package org.example.ejournal.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "teacher_schedule_slots",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_teacher_schedule_day_period",
                        columnNames = {"teacher_schedule_id", "day_of_week", "period_number"}
                )
        })
public class TeacherScheduleSlot extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "teacher_schedule_id")
    private TeacherSchedule teacherSchedule;

    @NotNull
    @Column(nullable = false)
    private String day;

    @NotNull
    @Column(name = "period_number", nullable = false)
    private Integer periodNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "school_class_id")
    private SchoolClass schoolClass;
}
