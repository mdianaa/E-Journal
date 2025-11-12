package org.example.ejournal.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "absences")
public class Absence extends BaseEntity {

    @Column
    @Size(max = 50)
    @NotNull
    private String day;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Subject subject;

    @Column(columnDefinition = "boolean default false")
    private boolean isExcused;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Student student;
}
