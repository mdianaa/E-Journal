package org.example.ejournal.services;

import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.models.*;

import java.util.Map;

public interface SchoolService {

    SchoolDtoRequest createSchool(SchoolDtoRequest schoolDto);

    School viewSchoolInfo(long schoolId);

    // средна аритметична оценка за текущия предмет за определени класове (само 12-ти, само 11-ти...)
    Map<Subject, Grade> viewAverageGradePerSubject(long schoolId, long schoolClassId);

    Map<Teacher, Grade> viewAverageGradePerTeacher(long schoolId, long teacherId);

    // Трябва да се покаже  средния успех на учениците  във всеки клас, за всеки учител, всеки предмет и всяко училище.
    // Освен това трябва да се покажат и броят на двойките, тройките, четворките, петиците и шестиците отново
    // във всеки клас, всеки предмет, всеки учител и училище.

    void deleteSchool(long schoolId);

}
