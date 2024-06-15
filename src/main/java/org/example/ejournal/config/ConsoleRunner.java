package org.example.ejournal.config;

import org.example.ejournal.dtos.request.*;
import org.example.ejournal.enums.*;
import org.example.ejournal.services.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class ConsoleRunner implements CommandLineRunner {

    private final AbsenceService absenceService;
    private final GradeService gradeService;
    private final HeadmasterService headmasterService;
    private final ParentService parentService;
    private final ScheduleService scheduleService;
    private final SchoolClassService schoolClassService;
    private final SchoolService schoolService;
    private final StudentService studentService;
    private final SubjectService subjectService;
    private final TeacherService teacherService;

    public ConsoleRunner(AbsenceService absenceService, GradeService gradeService, HeadmasterService headmasterService, ParentService parentService, ScheduleService scheduleService, SchoolClassService schoolClassService, SchoolService schoolService, StudentService studentService, SubjectService subjectService, TeacherService teacherService) {
        this.absenceService = absenceService;
        this.gradeService = gradeService;
        this.headmasterService = headmasterService;
        this.parentService = parentService;
        this.scheduleService = scheduleService;
        this.schoolClassService = schoolClassService;
        this.schoolService = schoolService;
        this.studentService = studentService;
        this.subjectService = subjectService;
        this.teacherService = teacherService;
    }

    @Override
    public void run(String... args) {

        // creation

        SchoolDtoRequest schoolDtoRequest = new SchoolDtoRequest("SELS", "bul. Ivan Vazov");
        schoolService.createSchool(schoolDtoRequest);

        SubjectDtoRequest english = new SubjectDtoRequest(SubjectType.ENGLISH);
        SubjectDtoRequest math = new SubjectDtoRequest(SubjectType.MATH);

        subjectService.createSubject(english, schoolDtoRequest);
        subjectService.createSubject(math, schoolDtoRequest);

        Set<SubjectDtoRequest> subjects = new HashSet<>();
        subjects.add(english);
        subjects.add(math);

        HeadmasterDtoRequest headmasterDtoRequest = new HeadmasterDtoRequest("Martin", "Stoyanov", "+359 876 256", RoleType.HEADMASTER);
        headmasterService.createHeadmaster(headmasterDtoRequest, schoolDtoRequest);

        TeacherDtoRequest teacherDtoRequest1 = new TeacherDtoRequest("Ivan", "Petrov", "+359 345 678", "Lozenetz", RoleType.TEACHER);
        TeacherDtoRequest teacherDtoRequest2 = new TeacherDtoRequest("Maria", "Ivanova", "+359 675 353", "Vitosha", RoleType.TEACHER);

        teacherService.createTeacher(teacherDtoRequest1, schoolDtoRequest, subjects);
        teacherService.createTeacher(teacherDtoRequest2, schoolDtoRequest, subjects);

        SchoolClassDtoRequest schoolClassDtoRequest = new SchoolClassDtoRequest("11A");

        schoolClassService.createClass(schoolClassDtoRequest, teacherDtoRequest2, schoolDtoRequest);

        ParentDtoRequest parentDtoRequest1 = new ParentDtoRequest("Ivana", "Marinova", "+359 368 354", RoleType.PARENT);
        ParentDtoRequest parentDtoRequest2 = new ParentDtoRequest("Mihail", "Dimitrov", "+359 463 152", RoleType.PARENT);

        parentService.createParent(parentDtoRequest1, schoolDtoRequest);
        parentService.createParent(parentDtoRequest2, schoolDtoRequest);

        StudentDtoRequest studentDtoRequest1 = new StudentDtoRequest("Ivaylo", "Marinov", "Serdika", "+359 243 578", RoleType.STUDENT);
        StudentDtoRequest studentDtoRequest2 = new StudentDtoRequest("Iva", "Dimitrov", "Studentski grad", "+359 536 183", RoleType.STUDENT);
        StudentDtoRequest studentDtoRequest3 = new StudentDtoRequest("Marin", "Dimitrov", "Studentski grad", "+359 354 263", RoleType.STUDENT);

        studentService.createStudent(studentDtoRequest1, schoolDtoRequest, schoolClassDtoRequest, parentDtoRequest1);
        studentService.createStudent(studentDtoRequest2, schoolDtoRequest, schoolClassDtoRequest, parentDtoRequest2);
        studentService.createStudent(studentDtoRequest3, schoolDtoRequest, schoolClassDtoRequest, parentDtoRequest2);

        ScheduleDtoRequest monday = new ScheduleDtoRequest(WeekDay.MONDAY, SemesterType.FIRST, ShiftType.FIRST, PeriodType.FIRST);
        ScheduleDtoRequest tuesday = new ScheduleDtoRequest(WeekDay.TUESDAY, SemesterType.FIRST, ShiftType.FIRST, PeriodType.SECOND);
        ScheduleDtoRequest wednesday = new ScheduleDtoRequest(WeekDay.WEDNESDAY, SemesterType.FIRST, ShiftType.FIRST, PeriodType.FIFTH);
        ScheduleDtoRequest thursday = new ScheduleDtoRequest(WeekDay.THURSDAY, SemesterType.FIRST, ShiftType.FIRST, PeriodType.FIRST);
        ScheduleDtoRequest friday = new ScheduleDtoRequest(WeekDay.FRIDAY, SemesterType.FIRST, ShiftType.FIRST, PeriodType.SIXTH);

        scheduleService.createSchedule(monday, schoolClassDtoRequest, math);
        scheduleService.createSchedule(tuesday, schoolClassDtoRequest, math);
        scheduleService.createSchedule(wednesday, schoolClassDtoRequest, english);
        scheduleService.createSchedule(thursday, schoolClassDtoRequest, english);
        scheduleService.createSchedule(friday, schoolClassDtoRequest, english);

        AbsenceDtoRequest absenceDtoRequest = new AbsenceDtoRequest(WeekDay.MONDAY);
        absenceService.createAbsence(absenceDtoRequest, teacherDtoRequest1, studentDtoRequest1, english);

        GradeDtoRequest gradeDtoRequest1 = new GradeDtoRequest(BigDecimal.valueOf(5));
        GradeDtoRequest gradeDtoRequest2 = new GradeDtoRequest(BigDecimal.valueOf(5));

        gradeService.createGrade(gradeDtoRequest1, teacherDtoRequest2, math, studentDtoRequest1);
        gradeService.createGrade(gradeDtoRequest2, teacherDtoRequest2, math, studentDtoRequest2);

        // visualize

//        System.out.println(schoolService.viewSchoolInfo(1L));
//        Set<SubjectDtoResponse> subjectsToPrint = subjectService.viewAllSubjectsInSchool(1L);
//        subjectsToPrint.forEach(System.out::println);
//        System.out.println(headmasterService.viewHeadmaster(1L));
//        Set<ParentDtoResponse> parentsToPrint = parentService.viewAllParentsInSchool(1L);
//        parentsToPrint.forEach(System.out::println);
//        System.out.println(scheduleService.viewScheduleForDay("monday", "11A", "first"));
//        System.out.println(studentService.viewStudent(6L));
//        Set<StudentDtoResponse> studentsToPrint = studentService.showAllStudentsInSchool(1L);
//        studentsToPrint.forEach(System.out::println);
//        studentService.showAllAbsencesForStudent(6L).forEach(System.out::println);
//        studentService.showAllGradesForSubject(6L, math).forEach(System.out::println);
//        System.out.println(teacherService.viewTeacher(2L));
//        teacherService.viewAllTeachersInSchool(1L).forEach(System.out::println);

//        System.out.println(gradeService.viewGradeCountInSchoolClass(BigDecimal.valueOf(5), 1L));
//        System.out.println(gradeService.viewGradeCountForSubject(BigDecimal.valueOf(5), 2L));
//        System.out.println(gradeService.viewGradeCountForTeacher(BigDecimal.valueOf(5), 3L));
//        System.out.println(gradeService.viewGradeCountInSchool(BigDecimal.valueOf(5), 1L));

//        System.out.println(gradeService.viewAverageGradeForSubject(1L, math.getSubjectType(), "11A"));
//        System.out.println(gradeService.viewAverageGradeForTeacher(3L));
//        System.out.println(gradeService.viewAverageGradeForSchool(1L));
//        System.out.println(gradeService.viewAverageGradeForStudent(6L));


        // edit



        // delete

//        absenceService.deleteAbsence(1L);
//        headmasterService.deleteHeadmaster(1L);
//        parentService.deleteParent(4L);
//        schoolClassService.deleteClass(1L);
//        schoolService.deleteSchool(1L);
//        studentService.withdrawStudent(6L);
//        teacherService.deleteTeacher(2L);
//        subjectService.deleteSubject(1L, 1L); //TODO: delete subject doesn't work properly
//        scheduleService.deleteSchedule(1L);


        // methods for registration work properly
        // methods for visualization work properly
        // methods for upload/ edit
        // methods for delete work properly (only subject doesn't)

    }
}
