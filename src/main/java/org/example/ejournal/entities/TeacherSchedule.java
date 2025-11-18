package org.example.ejournal.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "teacher_schedules")
public class TeacherSchedule extends BaseEntity {

    // all lessons for teacher X on semester Y with shift Z

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @NotNull
    @Column(nullable = false)
    private String semester;

    @NotNull
    @Column(nullable = false)
    private String shift;

    @OneToMany(
            mappedBy = "teacherSchedule",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<TeacherScheduleSlot> slots = new LinkedHashSet<>();
}
