package org.example.ejournal.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "bad_notes")
public class BadNote extends BaseEntity {

    @Column
    @NotNull
    @Size(max = 50)
    private String day;

    @ManyToOne
    @NotNull
    private Subject subject;

    @Column
    @NotNull
    private String description;

    @ManyToOne
    @NotNull
    private Student student;

    @ManyToOne
    @NotNull
    private Teacher teacher;

}
