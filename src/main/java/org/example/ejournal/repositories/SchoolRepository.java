package org.example.ejournal.repositories;

import org.example.ejournal.dtos.response.SchoolDtoResponse;
import org.example.ejournal.models.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {

    Optional<School> findByName(String name);

//    @Query(value = "SELECT new org.example.ejournal.dtos.response.SchoolDtoResponse(sch.name, sch.address, sch.headmaster.lastName) " +
//            "FROM School sch " +
//            "WHERE sch.id =: schoolId")
//    Optional<SchoolDtoResponse> findSchoolById(long schoolId);
}
