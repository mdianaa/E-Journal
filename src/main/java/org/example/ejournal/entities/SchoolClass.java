package org.example.ejournal.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ejournal.enums.ClassSection;
import org.example.ejournal.enums.GradeLevel;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
    @Entity
    @Table(name = "school_classes")
    public class SchoolClass extends BaseEntity {
        
        @Enumerated(EnumType.STRING)
        @Column(name = "grade_level", nullable = false)
        private GradeLevel gradeLevel;  // Enum for grade level (10th)
        
        @Enumerated(EnumType.STRING)
        @Column(name = "class_section", nullable = false)
        private ClassSection classSection;  // Enum for class section (A, B, C, etc.)
        
        @OneToOne(optional = false)
        @JoinColumn(name = "head_teacher_id")
        private Teacher headTeacher;
        
        @OneToMany(mappedBy = "currentSchoolClass", targetEntity = Student.class, fetch = FetchType.EAGER)
        private Set<Student> students;
        
        @OneToMany(mappedBy = "schoolClass")
        private Set<TeacherPosition> teacherPositionSet;
        
        @ManyToOne
        private School school;
    
    // New field to track academic year
    @ManyToOne(optional = false)
    @JoinColumn(name = "academic_year_id")
    private AcademicYear academicYear;
        
    }
