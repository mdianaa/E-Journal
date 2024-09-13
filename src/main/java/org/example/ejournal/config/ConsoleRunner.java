package org.example.ejournal.config;

import org.example.ejournal.dtos.request.*;
import org.example.ejournal.dtos.response.ParentDtoResponse;
import org.example.ejournal.dtos.response.StudentDtoResponse;
import org.example.ejournal.dtos.response.SubjectDtoResponse;
import org.example.ejournal.dtos.response.TeacherDtoResponse;
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
    private final UserAuthenticationService userAuthenticationService;

    public ConsoleRunner(AbsenceService absenceService, GradeService gradeService, HeadmasterService headmasterService, ParentService parentService, ScheduleService scheduleService, SchoolClassService schoolClassService, SchoolService schoolService, StudentService studentService, SubjectService subjectService, TeacherService teacherService, UserAuthenticationService userAuthenticationService) {
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
        this.userAuthenticationService = userAuthenticationService;
    }

    @Override
    public void run(String... args) {

        // creation

        // create ADMIN

      /* UserRegisterDtoRequest adminRegisterDtoRequest = new UserRegisterDtoRequest("admin","admin",RoleType.ADMIN);
        userAuthenticationService.register(adminRegisterDtoRequest);

        // create other users

        SchoolDtoRequest schoolDtoRequest = new SchoolDtoRequest("SELS", "bul. Ivan Vazov");
        schoolService.createSchool(schoolDtoRequest);

        SubjectDtoRequest english = new SubjectDtoRequest("ENGLISH");
        SubjectDtoRequest math = new SubjectDtoRequest("MATH");

        subjectService.createSubject(english, schoolDtoRequest);
        subjectService.createSubject(math, schoolDtoRequest);

        Set<SubjectDtoRequest> subjects = new HashSet<>();
        subjects.add(english);
        subjects.add(math);*/

//        HeadmasterDtoRequest headmasterDtoRequest = new HeadmasterDtoRequest("Martin", "Stoyanov", "+359 876 256");
//        UserRegisterDtoRequest userRegisterDtoRequest1 = new UserRegisterDtoRequest("martin", "12345", RoleType.HEADMASTER);
//        headmasterService.createHeadmaster(headmasterDtoRequest, schoolDtoRequest, userRegisterDtoRequest1);
////        TeacherDtoRequest teacherDtoRequest1 = new TeacherDtoRequest("Georgi","Petrov","088823232","Lozenetz");
//        UserRegisterDtoRequest userRegisterDtoRequest2 = new UserRegisterDtoRequest("ivan", "12345", RoleType.TEACHER);
//        TeacherDtoRequest teacherDtoRequest2 = new TeacherDtoRequest("Penka","Shtereva","08880099232","adres?");
//        UserRegisterDtoRequest userRegisterDtoRequest3 = new UserRegisterDtoRequest("maria", "12345", RoleType.TEACHER);

//        AdminRegisterDtoRequest teacherRegisterNew1 =  new AdminRegisterDtoRequest("Georgi","Petrov","088823232",RoleType.TEACHER, "goshko","parola1");
//        AdminRegisterDtoRequest teacherRegisterNew2 =  new AdminRegisterDtoRequest("Penka","Shtereva","08880099232",RoleType.TEACHER, "penka1","parola2");
//
//        TeacherDtoResponse TteacherRegisterNew2 = teacherService.createTeacher(teacherRegisterNew2, schoolDtoRequest.getName());
//
//        SchoolClassDtoRequest schoolClassDtoRequest = new SchoolClassDtoRequest("11A");
//
//
//        schoolClassService.createClass(schoolClassDtoRequest, teacherDtoRequest1, schoolDtoRequest);
//
//        ParentDtoRequest parentDtoRequest1 = new ParentDtoRequest("Ivana", "Marinova", "+359 368 354");
//        UserRegisterDtoRequest userRegisterDtoRequest4 = new UserRegisterDtoRequest("ivana", "12345", RoleType.PARENT);
//        ParentDtoRequest parentDtoRequest2 = new ParentDtoRequest("Mihail", "Dimitrov", "+359 463 152");
//        UserRegisterDtoRequest userRegisterDtoRequest5 = new UserRegisterDtoRequest("mihail", "12345", RoleType.PARENT);
//
//        parentService.createParent(parentDtoRequest1, schoolDtoRequest, userRegisterDtoRequest4);
//        parentService.createParent(parentDtoRequest2, schoolDtoRequest, userRegisterDtoRequest5);
//
//        StudentDtoRequest studentDtoRequest1 = new StudentDtoRequest("Ivaylo", "Marinov", "Serdika", "+359 243 578");
//        UserRegisterDtoRequest userRegisterDtoRequest6 = new UserRegisterDtoRequest("ivaylo", "12345", RoleType.STUDENT);
//        StudentDtoRequest studentDtoRequest2 = new StudentDtoRequest("Iva", "Dimitrov", "Studentski grad", "+359 536 183");
//        UserRegisterDtoRequest userRegisterDtoRequest7 = new UserRegisterDtoRequest("iva", "12345", RoleType.STUDENT);
//        StudentDtoRequest studentDtoRequest3 = new StudentDtoRequest("Marin", "Dimitrov", "Studentski grad", "+359 354 263");
//        UserRegisterDtoRequest userRegisterDtoRequest8 = new UserRegisterDtoRequest("marin", "12345", RoleType.STUDENT);
//
//        studentService.createStudent(studentDtoRequest1, schoolDtoRequest, schoolClassDtoRequest, parentDtoRequest1, userRegisterDtoRequest6);
//        studentService.createStudent(studentDtoRequest2, schoolDtoRequest, schoolClassDtoRequest, parentDtoRequest2, userRegisterDtoRequest7);
//        studentService.createStudent(studentDtoRequest3, schoolDtoRequest, schoolClassDtoRequest, parentDtoRequest2, userRegisterDtoRequest8);
//
//        ScheduleDtoRequest monday = new ScheduleDtoRequest(WeekDay.MONDAY, SemesterType.FIRST, ShiftType.FIRST, PeriodType.FIRST);
//        ScheduleDtoRequest tuesday = new ScheduleDtoRequest(WeekDay.TUESDAY, SemesterType.FIRST, ShiftType.FIRST, PeriodType.SECOND);
//        ScheduleDtoRequest wednesday = new ScheduleDtoRequest(WeekDay.WEDNESDAY, SemesterType.FIRST, ShiftType.FIRST, PeriodType.FIFTH);
//        ScheduleDtoRequest thursday = new ScheduleDtoRequest(WeekDay.THURSDAY, SemesterType.FIRST, ShiftType.FIRST, PeriodType.FIRST);
//        ScheduleDtoRequest friday = new ScheduleDtoRequest(WeekDay.FRIDAY, SemesterType.FIRST, ShiftType.FIRST, PeriodType.SIXTH);
//
//        scheduleService.createSchedule(monday, schoolClassDtoRequest, math);
//        scheduleService.createSchedule(tuesday, schoolClassDtoRequest, math);
//        scheduleService.createSchedule(wednesday, schoolClassDtoRequest, english);
//        scheduleService.createSchedule(thursday, schoolClassDtoRequest, english);
//        scheduleService.createSchedule(friday, schoolClassDtoRequest, english);
//
//        AbsenceDtoRequest absenceDtoRequest = new AbsenceDtoRequest(WeekDay.MONDAY);
//        absenceService.createAbsence(absenceDtoRequest, teacherDtoRequest1, studentDtoRequest1, english);
//
//        GradeDtoRequest gradeDtoRequest1 = new GradeDtoRequest(BigDecimal.valueOf(5));
//        GradeDtoRequest gradeDtoRequest2 = new GradeDtoRequest(BigDecimal.valueOf(5));
//
//        gradeService.createGrade(gradeDtoRequest1, teacherDtoRequest2, math, studentDtoRequest1);
//        gradeService.createGrade(gradeDtoRequest2, teacherDtoRequest2, math, studentDtoRequest2);

        // visualize

//        System.out.println(schoolService.viewSchoolInfo(1L));
//        Set<SubjectDtoResponse> subjectsToPrint = subjectService.viewAllSubjectsInSchool(1L);
//        subjectsToPrint.forEach(System.out::println);
//        System.out.println(headmasterService.viewHeadmaster(1L));
//        Set<ParentDtoResponse> parentsToPrint = parentService.viewAllParentsInSchool(1L);
//        parentsToPrint.forEach(System.out::println);
//        //System.out.println(scheduleService.viewScheduleForDay("monday", "11A", "first"));
//        System.out.println(studentService.viewStudent(6L));
//        Set<StudentDtoResponse> studentsToPrint = studentService.showAllStudentsInSchool(1L);
//        studentsToPrint.forEach(System.out::println);
//        studentService.showAllAbsencesForStudent(6L).forEach(System.out::println);
//        studentService.showAllGradesForSubject(6L, math).forEach(System.out::println);
//        System.out.println(teacherService.viewTeacher(2L));
//        teacherService.viewAllTeachersInSchool(1L).forEach(System.out::println);
//
//        System.out.println(gradeService.viewGradeCountInSchoolClass(BigDecimal.valueOf(5), 1L));
//        System.out.println(gradeService.viewGradeCountForSubject(BigDecimal.valueOf(5), 2L));
//        System.out.println(gradeService.viewGradeCountForTeacher(BigDecimal.valueOf(5), 3L));
//        System.out.println(gradeService.viewGradeCountInSchool(BigDecimal.valueOf(5), 1L));
//
//        System.out.println(gradeService.viewAverageGradeForSubject(1L, "MATH", "11A"));
//        System.out.println(gradeService.viewAverageGradeForTeacher(3L));
//        System.out.println(gradeService.viewAverageGradeForSchool(1L));
//        System.out.println(gradeService.viewAverageGradeForStudent(6L));
//

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
