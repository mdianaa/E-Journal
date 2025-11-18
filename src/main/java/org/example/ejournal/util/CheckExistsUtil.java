package org.example.ejournal.util;

import org.example.ejournal.repositories.*;

public final class CheckExistsUtil {

    public static void checkIfHeadmasterExists(HeadmasterRepository headmasterRepository, Long headmasterId) {
        if (headmasterRepository.findById(headmasterId).isEmpty()) {
            throw new IllegalArgumentException("Headmaster with id " + headmasterId + " not found");
        }
    }

    public static void checkIfTeacherExists(TeacherRepository teacherRepository, Long teacherId) {
        if (teacherRepository.findById(teacherId).isEmpty()) {
            throw new IllegalArgumentException("Teacher with id " + teacherId + " not found");
        }
    }

    public static void checkIfStudentExists(StudentRepository studentRepository, Long studentId) {
        if (studentRepository.findById(studentId).isEmpty()) {
            throw new IllegalArgumentException("Student with id " + studentId + " not found");
        }
    }

    public static void checkIfParentExists(ParentRepository parentRepository, Long patientId) {
        if (parentRepository.findById(patientId).isEmpty()) {
            throw new IllegalArgumentException("Parent with id " + parentRepository + " not found");
        }
    }

    public static void checkIfBadNoteExists(BadNoteRepository badNoteRepository, Long badNoteId) {
        if (badNoteRepository.findById(badNoteId).isEmpty()) {
            throw new IllegalArgumentException("Bad note with id " + badNoteId + " not found");
        }
    }

    public static void checkIfSchoolExists(SchoolRepository schoolRepository, Long schoolId) {
        if (schoolRepository.findById(schoolId).isEmpty()) {
            throw new IllegalArgumentException("School with id " + schoolId + " not found");
        }
    }

    public static void checkIfSchoolClassExists(SchoolClassRepository schoolClassRepository, Long schoolClassId) {
        if (schoolClassRepository.findById(schoolClassId).isEmpty()) {
            throw new IllegalArgumentException("School class with id " + schoolClassId + " not found");
        }
    }

    public static void checkIfSubjectExists(SubjectRepository subjectRepository, Long subjectId) {
        if (subjectRepository.findById(subjectId).isEmpty()) {
            throw new IllegalArgumentException("Subject with id " + subjectId + " not found");
        }
    }
}
