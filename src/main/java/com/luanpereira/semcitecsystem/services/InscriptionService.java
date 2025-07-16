package com.luanpereira.semcitecsystem.services;

import com.luanpereira.semcitecsystem.models.*;
import com.luanpereira.semcitecsystem.repositories.InscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class InscriptionService {
    @Autowired
    private InscriptionRepository inscriptionRepository;

    public Inscription save(Inscription inscription) {
        return this.inscriptionRepository.save(inscription);
    }

    public Inscription findByStudentAndCourse(StudentModel student, CourseModel course) {
        return inscriptionRepository.findByStudentAndCourse(student, course).orElse(null);
    }

    public List<Inscription> findByStatus(Status status) {
        return inscriptionRepository.findByStatus(status);
    }

    public List<Inscription> findByStudent(StudentModel student) {
        return inscriptionRepository.findByStudent(student);
    }

    public List<Inscription> findByClassroom(Classroom classroom) {
        return inscriptionRepository.findByClassroom(classroom);
    }

    public List<Inscription> findByClassroomUuid(UUID uuid) {
        return inscriptionRepository.findByClassroomUuid(uuid);
    }

    public Inscription findById(UUID uuid) {
        return this.inscriptionRepository.findById(uuid).get();
    }

    public int getNextValFromSequence() {
        return this.inscriptionRepository.getNextValFromSequence();
    }

    public List<Inscription> findActiveInscriptionsByClassroom(Classroom classroom) {
        return this.inscriptionRepository.findByClassroomAndStatus(classroom, Status.ATIVO);
    }

    public List<Inscription> findByCourse(CourseModel course) {
        return this.inscriptionRepository.findByCourse(course);
    }
}
