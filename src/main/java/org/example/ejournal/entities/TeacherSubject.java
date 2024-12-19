package org.example.ejournal.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "teachers_subjects")
public class TeacherSubject extends BaseEntity {
	
	@Column(columnDefinition = "boolean default true")
	private boolean active;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private Teacher teacher;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private Subject subject;
	
	@OneToMany(mappedBy = "teacherSubject")
	private List<TeacherPosition> teacherPositionList;
	
	@OneToMany(mappedBy = "teacherSubject")
	private List<Schedule> scheduleList;
	
	//custom
	public TeacherSubject(Teacher teacher, Subject subject) {
		this.teacher = teacher;
		this.subject = subject;
	}
}