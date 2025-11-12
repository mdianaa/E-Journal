package org.example.ejournal.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.ejournal.dtos.request.BadNoteDtoRequest;
import org.example.ejournal.dtos.response.BadNoteDtoResponse;
import org.example.ejournal.entities.BadNote;
import org.example.ejournal.entities.Student;
import org.example.ejournal.entities.Subject;
import org.example.ejournal.entities.Teacher;
import org.example.ejournal.repositories.BadNoteRepository;
import org.example.ejournal.repositories.StudentRepository;
import org.example.ejournal.repositories.SubjectRepository;
import org.example.ejournal.repositories.TeacherRepository;
import org.example.ejournal.services.BadNoteService;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BadNoteServiceImpl implements BadNoteService {

    private final BadNoteRepository badNoteRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;

    @Override
    public BadNoteDtoResponse createBadNote(BadNoteDtoRequest req) {
        Subject subject = subjectRepository.findById(req.getSubjectId())
                .orElseThrow(() -> new EntityNotFoundException("Subject with id " + req.getTeacherId() + " cannot be found"));

        Student student = studentRepository.findById(req.getStudentId())
                .orElseThrow(() -> new EntityNotFoundException("Student with id " + req.getTeacherId() + " cannot be found"));

        Teacher teacher = teacherRepository.findById(req.getTeacherId())
                .orElseThrow(() -> new EntityNotFoundException("Teacher with id " + req.getTeacherId() + " cannot be found"));

        BadNote note = new BadNote();
        note.setDay(req.getDay());
        note.setSubject(subject);
        note.setDescription(req.getDescription());
        note.setStudent(student);
        note.setTeacher(teacher);

        BadNote saved = badNoteRepository.save(note);
        return toDto(saved);
    }

    @Override
    public Set<BadNoteDtoResponse> viewAllBadNotesForStudent(long studentId) {
        // TODO check
        return badNoteRepository.findAllByStudent_IdOrderByDayDesc(studentId)
                .stream().map(this::toDto).collect(Collectors.toCollection(java.util.LinkedHashSet::new));
    }

    @Override
    public Set<BadNoteDtoResponse> viewAllBadNotesGivenByTeacher(long teacherId) {
        // TODO check
        return badNoteRepository.findAllByTeacher_IdOrderByDayDesc(teacherId)
                .stream().map(this::toDto).collect(Collectors.toCollection(java.util.LinkedHashSet::new));
    }

    @Override
    public void deleteBadNote(long badNoteId) {
        // TODO check
        BadNote note = badNoteRepository.findById(badNoteId)
                .orElseThrow(() -> new EntityNotFoundException("BadNote with id " + badNoteId + " cannot be found"));

        badNoteRepository.delete(note);
    }

    private BadNoteDtoResponse toDto(BadNote n) {
        BadNoteDtoResponse dto = new BadNoteDtoResponse();
        dto.setId(n.getId());
        dto.setDay(n.getDay());

        var s = n.getSubject();
        if (s != null) {
            dto.setSubjectId(s.getId());
            dto.setSubjectName(s.getName());
        }

        dto.setDescription(n.getDescription());

        var st = n.getStudent();
        if (st != null) {
            dto.setStudentId(st.getId());
            dto.setStudentFullName((st.getUser().getFirstName() + " " + st.getUser().getLastName()).trim());
        }

        var t = n.getTeacher();
        if (t != null) {
            dto.setTeacherId(t.getId());
            dto.setTeacherFullName((t.getUser().getFirstName() + " " + t.getUser().getLastName()).trim());
        }

        return dto;
    }

}