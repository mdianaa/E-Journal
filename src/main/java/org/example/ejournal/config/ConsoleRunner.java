package org.example.ejournal.config;

import org.example.ejournal.dtos.request.*;
import org.example.ejournal.enums.*;
import org.example.ejournal.models.School;
import org.example.ejournal.models.Subject;
import org.example.ejournal.services.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
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

        ScheduleDtoRequest monday = new ScheduleDtoRequest(WeekDay.MONDAY, SemesterType.FIRST, ShiftType.FIRST);
        ScheduleDtoRequest tuesday = new ScheduleDtoRequest(WeekDay.TUESDAY, SemesterType.FIRST, ShiftType.FIRST);
        ScheduleDtoRequest wednesday = new ScheduleDtoRequest(WeekDay.WEDNESDAY, SemesterType.FIRST, ShiftType.FIRST);
        ScheduleDtoRequest thursday = new ScheduleDtoRequest(WeekDay.THURSDAY, SemesterType.FIRST, ShiftType.FIRST);
        ScheduleDtoRequest friday = new ScheduleDtoRequest(WeekDay.FRIDAY, SemesterType.FIRST, ShiftType.FIRST);

        //TODO: Map isn't represented in the DB...

        Map<LocalDate, SubjectDtoRequest> scheduleSubjects = new HashMap<>();
        scheduleSubjects.putIfAbsent(LocalDate.now(), english);
        scheduleSubjects.putIfAbsent(LocalDate.of(2024, 5, 5), math);

        scheduleService.createSchedule(monday, schoolClassDtoRequest, scheduleSubjects);
        scheduleService.createSchedule(tuesday, schoolClassDtoRequest, scheduleSubjects);
        scheduleService.createSchedule(wednesday, schoolClassDtoRequest, scheduleSubjects);
        scheduleService.createSchedule(thursday, schoolClassDtoRequest, scheduleSubjects);
        scheduleService.createSchedule(friday, schoolClassDtoRequest, scheduleSubjects);

        AbsenceDtoRequest absenceDtoRequest = new AbsenceDtoRequest(WeekDay.MONDAY);
        absenceService.createAbsence(absenceDtoRequest, teacherDtoRequest1, studentDtoRequest1, english);

        GradeDtoRequest gradeDtoRequest = new GradeDtoRequest(BigDecimal.valueOf(5));
        gradeService.createGrade(gradeDtoRequest, teacherDtoRequest2, math, studentDtoRequest3);

        // visualize

//        System.out.println(schoolService.viewSchoolInfo(1L));

//        Set<Subject> subjectsToPrint = subjectService.showAllSubjectsInSchool(1L);
//        subjectsToPrint.forEach(System.out::println);

//        System.out.println(headmasterService.viewHeadmaster(1L));

//        teacherService.showAllTeachersInSchool(1L).forEach(System.out::println); //TODO: dto response to be created

//        System.out.println(teacherService.viewTeacher(2L));  //TODO: dto response to be created

//        parentService.viewAllParentsInSchool(1L).forEach(System.out::println);  //TODO: dto response to be created

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

//        System.out.println("running");

        // methods for registration work properly (only schedule doesn't)
        // methods for upload/ edit
        // methods for delete (only subject doesn't)

    }
}
