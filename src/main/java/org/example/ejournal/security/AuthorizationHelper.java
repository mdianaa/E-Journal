package org.example.ejournal.security;

import lombok.RequiredArgsConstructor;
import org.example.ejournal.entities.*;
import org.example.ejournal.repositories.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Component("authz")
@RequiredArgsConstructor
// additional authorization for the users
public class AuthorizationHelper {

    private final HeadmasterRepository headmasterRepository;
    private final TeacherRepository teacherRepository;
    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final AbsenceRepository absenceRepository;
    private final BadNoteRepository badNoteRepository;
    private final ScheduleRepository scheduleRepository;

    public boolean isHeadmaster(Authentication authentication, Long headmasterId) {
        String email = principalEmail(authentication);
        if (!StringUtils.hasText(email) || headmasterId == null) return false;

        Headmaster headmaster = headmasterRepository.findByUser_Email(email).isPresent()
                ? headmasterRepository.findByUser_Email(email).get() : null;

        return headmaster != null && headmasterId.equals(headmaster.getId());
    }

    public boolean isHeadmasterOfSchool(Authentication authentication, Long schoolId) {
        String email = principalEmail(authentication);
        if (!StringUtils.hasText(email) || schoolId == null) return false;

        Optional<Headmaster> headmasterOpt = headmasterRepository.findByUser_Email(email);
        if (headmasterOpt.isEmpty()) return false;

        Headmaster headmaster = headmasterOpt.get();

        if (headmaster.getSchool() == null) return false;

        return schoolId.equals(headmaster.getSchool().getId());
    }

    public boolean isHeadmasterOfStudent(Authentication authentication, Long studentId) {
        String email = principalEmail(authentication);
        if (!StringUtils.hasText(email) || studentId == null) return false;

        Headmaster headmaster = headmasterRepository.findByUser_Email(email).orElse(null);
        if (headmaster == null || headmaster.getSchool() == null) return false;

        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null || student.getSchool() == null) return false;

        return headmaster.getSchool().getId().equals(student.getSchool().getId());
    }

    public boolean isHeadmasterOfTeacher(Authentication authentication, Long teacherId) {
        String email = principalEmail(authentication);
        if (!StringUtils.hasText(email) || teacherId == null) return false;

        Headmaster headmaster = headmasterRepository.findByUser_Email(email).orElse(null);
        if (headmaster == null || headmaster.getSchool() == null) return false;

        return teacherRepository.existsByIdAndSchool_Id(
                teacherId,
                headmaster.getSchool().getId()
        );
    }

    public boolean isHeadmasterOfSchoolClass(Authentication authentication, Long schoolClassId) {
        String email = principalEmail(authentication);
        if (!StringUtils.hasText(email) || schoolClassId == null) return false;

        Headmaster headmaster = headmasterRepository.findByUser_Email(email).orElse(null);
        if (headmaster == null || headmaster.getSchool() == null) return false;

        SchoolClass schoolClass = schoolClassRepository.findById(schoolClassId).orElse(null);
        if (schoolClass == null || schoolClass.getSchool() == null) return false;

        return headmaster.getSchool().getId().equals(schoolClass.getSchool().getId());
    }

    public boolean isHeadmasterOfSchedule(Authentication authentication, Long scheduleId) {
        String email = principalEmail(authentication);
        if (!StringUtils.hasText(email) || scheduleId == null) return false;

        Headmaster headmaster = headmasterRepository.findByUser_Email(email).orElse(null);
        if (headmaster == null || headmaster.getSchool() == null) return false;

        return scheduleRepository.existsByIdAndSchoolClass_School_Id(
                scheduleId,
                headmaster.getSchool().getId()
        );
    }

    public boolean canHeadmasterExcuseAbsence(Authentication authentication, Long absenceId) {
        String email = principalEmail(authentication);
        if (!StringUtils.hasText(email) || absenceId == null) return false;

        Headmaster headmaster = headmasterRepository.findByUser_Email(email).orElse(null);
        if (headmaster == null || headmaster.getSchool() == null) return false;

        return absenceRepository.existsByIdAndStudent_School_Id(
                absenceId,
                headmaster.getSchool().getId()
        );
    }

    public boolean canHeadmasterManageBadNote(Authentication authentication, Long badNoteId) {
        String email = principalEmail(authentication);
        if (!StringUtils.hasText(email) || badNoteId == null) return false;

        Headmaster headmaster = headmasterRepository.findByUser_Email(email).orElse(null);
        if (headmaster == null || headmaster.getSchool() == null) return false;

        return badNoteRepository.existsByIdAndStudent_School_Id(
                badNoteId,
                headmaster.getSchool().getId()
        );
    }

    public boolean isTeacher(Authentication authentication, Long teacherId) {
        String email = principalEmail(authentication);
        if (!StringUtils.hasText(email) || teacherId == null) return false;

        Teacher teacher = teacherRepository.findByUser_Email(email).isPresent()
                ? teacherRepository.findByUser_Email(email).get() : null;

        return teacher != null && teacherId.equals(teacher.getId());
    }

    public boolean isTeacherOfSchoolClass(Authentication authentication, Long schoolClassId) {
        String email = principalEmail(authentication);
        if (!StringUtils.hasText(email) || schoolClassId == null) return false;

        Teacher teacher = teacherRepository.findByUser_Email(email).orElse(null);
        if (teacher == null || teacher.getSchool() == null) return false;

        SchoolClass schoolClass = schoolClassRepository.findById(schoolClassId).orElse(null);
        if (schoolClass == null || schoolClass.getSchool() == null) return false;

        return teacher.getSchool().getId().equals(schoolClass.getSchool().getId());
    }

    public boolean isHeadTeacherOfClass(Authentication authentication, Long schoolClassId) {
        String email = principalEmail(authentication);
        if (!StringUtils.hasText(email) || schoolClassId == null) return false;

        Teacher teacher = teacherRepository.findByUser_Email(email).orElse(null);
        if (teacher == null) return false;

        SchoolClass schoolClass = schoolClassRepository.findById(schoolClassId).orElse(null);
        if (schoolClass == null || schoolClass.getHeadTeacher() == null) return false;

        return teacher.getId().equals(schoolClass.getHeadTeacher().getId());
    }

    public boolean isTeacherOfStudent(Authentication authentication, Long studentId) {
        String email = principalEmail(authentication);
        if (!StringUtils.hasText(email) || studentId == null) return false;

        Teacher teacher = teacherRepository.findByUser_Email(email).orElse(null);
        if (teacher == null || teacher.getSchool() == null) return false;

        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null || student.getSchool() == null) return false;

        return teacher.getSchool().getId().equals(student.getSchool().getId());
    }

    public boolean isStudent(Authentication authentication, Long studentId) {
        String email = principalEmail(authentication);
        if (!StringUtils.hasText(email) || studentId == null) return false;

        return studentRepository.findByUser_Email(email)
                .map(s -> studentId.equals(s.getId()))
                .orElse(false);
    }

    public boolean isStudentInSchoolClass(Authentication authentication, Long schoolClassId) {
        String email = principalEmail(authentication);
        if (!StringUtils.hasText(email) || schoolClassId == null) return false;

        Student student = studentRepository.findByUser_Email(email).orElse(null);
        if (student == null || student.getSchoolClass() == null) return false;

        return schoolClassId.equals(student.getSchoolClass().getId());
    }

    public boolean isStudentOfSchool(Authentication authentication, Long schoolId) {
        String email = principalEmail(authentication);
        if (!StringUtils.hasText(email) || schoolId == null) return false;

        Student student = studentRepository.findByUser_Email(email).orElse(null);
        if (student == null || student.getSchool() == null) return false;

        return schoolId.equals(student.getSchool().getId());
    }

    public boolean isStudentOfClass(Authentication authentication, Long classId) {
        String email = principalEmail(authentication);
        if (!StringUtils.hasText(email) || classId == null) return false;

        Student student = studentRepository.findByUser_Email(email).orElse(null);
        if (student == null || student.getSchoolClass() == null) return false;

        return classId.equals(student.getSchoolClass().getId());
    }

    public boolean isParent(Authentication authentication, Long parentId) {
        String email = principalEmail(authentication);
        if (!StringUtils.hasText(email) || parentId == null) return false;

        Parent parent = parentRepository.findByUser_Email(email).isPresent()
                ? parentRepository.findByUser_Email(email).get() : null;

        return parent != null && parentId.equals(parent.getId());
    }

    public boolean isParentOfStudent(Authentication authentication, Long studentId) {
        String email = principalEmail(authentication);
        if (!StringUtils.hasText(email) || studentId == null) return false;

        Parent parent = parentRepository.findByUser_Email(email).orElse(null);
        if (parent == null) return false;

        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null || student.getParent() == null) return false;

        return parent.getId().equals(student.getParent().getId());
    }

    public boolean isParentOfStudentInSchoolClass(Authentication authentication, Long schoolClassId) {
        String email = principalEmail(authentication);
        if (!StringUtils.hasText(email) || schoolClassId == null) return false;

        Parent parent = parentRepository.findByUser_Email(email).orElse(null);
        if (parent == null) return false;

        return studentRepository.existsByParent_IdAndSchoolClass_Id(parent.getId(), schoolClassId);
    }

    private String principalEmail(Authentication authentication) {
        return (authentication == null) ? null : authentication.getName();
    }
}