package org.example.ejournal.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "subjects")
public class Subject extends BaseEntity {

    @Column
    @NotNull
    @Size(max = 50)
    private String name;

    @ManyToMany(mappedBy = "subjects", fetch = FetchType.LAZY)
    private Set<Teacher> teachers;
}
