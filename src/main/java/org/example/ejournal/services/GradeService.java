package org.example.ejournal.services;

import org.example.ejournal.dtos.request.GradeDtoRequest;
import org.example.ejournal.dtos.request.StudentDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.dtos.response.GradeDtoResponse;
import org.example.ejournal.enums.SubjectType;

import java.math.BigDecimal;

public interface GradeService {

    GradeDtoResponse createGrade(GradeDtoRequest grade, TeacherDtoRequest teacherDto, SubjectDtoRequest subjectDto, StudentDtoRequest studentDto);

    GradeDtoResponse editGrade(long gradeId, GradeDtoRequest gradeDto);

    // средна аритметична оценка за текущия предмет за определени класове (само 12-ти 'A', само 11-ти 'A'...) в дадено училище
    //BigDecimal viewAverageGradeForSubject(long schoolId, SubjectType subject, String classNumber);
    
    BigDecimal viewAverageGradeForSubject(long schoolId, String subjectName, String classNumber);
    
    // средна аритметична оценка за деден учител
    BigDecimal viewAverageGradeForTeacher(long teacherId);

    // средна аритметична оценка за даден ученик
    BigDecimal viewAverageGradeForStudent(long studentId);

    // средна аритметична оценка на дадено училище
    BigDecimal viewAverageGradeForSchool(long schoolId);

    // броя на оценките в даден клас
    int viewGradeCountInSchoolClass(BigDecimal grade, long schoolClassId);

    // броя на оценките за даден предмет
    int viewGradeCountForSubject(BigDecimal grade, long subjectId);

    // броя на оценките при даден учител
    int viewGradeCountForTeacher(BigDecimal grade, long teacherId);

    // броя на оценките в дадено училище
    int viewGradeCountInSchool(BigDecimal grade, long schoolId);

    // средна оценка на определен ученик за определен срок
//    BigDecimal viewAverageGradeForSubjectPerSemester(long subjectId, String semester, long studentId);
}
