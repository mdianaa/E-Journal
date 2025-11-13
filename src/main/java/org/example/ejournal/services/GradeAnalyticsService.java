package org.example.ejournal.services;

import java.math.BigDecimal;

public interface GradeAnalyticsService {

    // средна аритметична оценка за текущия предмет за определени класове в дадено училище
    BigDecimal viewAverageGradeForSubject(long schoolId, String subject, String classNumber);

    // средна аритметична оценка за даден учител
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
