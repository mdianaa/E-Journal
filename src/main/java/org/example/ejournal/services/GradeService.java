package org.example.ejournal.services;

import org.example.ejournal.dtos.request.GradeDtoRequest;
import org.example.ejournal.dtos.request.StudentDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.dtos.response.GradeDtoResponse;
import org.example.ejournal.enums.SubjectType;

import java.math.BigDecimal;
import java.util.List;

public interface GradeService {

    GradeDtoResponse createGrade(GradeDtoRequest grade, long subjectId, long studentId);

    GradeDtoResponse editGrade(long gradeId, GradeDtoRequest gradeDto);

    public List<GradeDtoResponse> getGradesForStudent(long studentId);

    List<GradeDtoResponse> showAllGradesAsStudent();

    List<GradeDtoResponse> showAllGradesAsParent(long studentI);

    List<GradeDtoResponse> showAllGradesAsTeacher(long studentId);

    List<GradeDtoResponse> showAllGradesAsHeadmaster(long studentId);
    
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
}
