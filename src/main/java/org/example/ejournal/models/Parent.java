package org.example.ejournal.models;

import jakarta.persistence.*;
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
@Table(name = "parents")
public class Parent extends User {

    @OneToMany(mappedBy = "parent", targetEntity = Student.class, fetch = FetchType.EAGER)
    private Set<Student> children;

    @ManyToOne
    private School school;

    @Override
    public String toString() {
        return super.toString() + "Parent{" +
                "children=" + children +
                ", school=" + school +
                '}';
    }
}
