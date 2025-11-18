package org.example.ejournal.repositories;

import org.example.ejournal.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByUser_EmailIgnoreCase(String email);

    // Find teachers in a school who teach a specific subject
    @Query("""
           select t
           from Teacher t
           join t.subjects subj
           where t.school.id = :schoolId and subj.id = :subjectId
           """)
    List<Teacher> findBySchoolAndSubject(@Param("schoolId") Long schoolId,
                                         @Param("subjectId") Long subjectId);

    // For cleanup: any teacher anywhere using that subject?
    @Query("""
           select count(t)
           from Teacher t
           join t.subjects subj
           where subj.id = :subjectId
           """)
    long countTeachersUsingSubject(@Param("subjectId") Long subjectId);

    Set<Teacher> findAllBySchool_Id(Long schoolId);

    Set<Teacher> findAllBySchool_IdAndHeadTeacherTrue(Long schoolId);

    @Query("""
           select t from Teacher t
           join t.user u
           where t.id = :teacherId
           """)
    Optional<Teacher> fetchByIdWithUser(@Param("teacherId") Long teacherId);

}
