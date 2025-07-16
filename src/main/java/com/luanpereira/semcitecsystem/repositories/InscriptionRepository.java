package com.luanpereira.semcitecsystem.repositories;

import com.luanpereira.semcitecsystem.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InscriptionRepository extends JpaRepository<Inscription, UUID> {

    @Query(value = "SELECT NEXTVAL(inscription_code_seq)", nativeQuery = true)
    int getNextValFromSequence();
    Optional<Inscription> findByStudentAndCourse(StudentModel student, CourseModel course);

    List <Inscription> findByClassroomAndStatus(Classroom classroom, Status status);

    List<Inscription> findByStatus(Status status);

    List<Inscription> findByStudent(StudentModel student);

    List<Inscription> findByClassroom(Classroom classroom);

    List<Inscription> findByCourse(CourseModel course);

    List<Inscription> findByClassroomUuid(UUID uuid);
}
