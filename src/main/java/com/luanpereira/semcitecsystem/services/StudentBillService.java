package com.luanpereira.semcitecsystem.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luanpereira.semcitecsystem.Dtos.StudentBillReportDTO;
import com.luanpereira.semcitecsystem.models.StudentBill;
import com.luanpereira.semcitecsystem.repositories.StudentBillRepository;

@Service
public class StudentBillService {

    @Autowired
    private StudentBillRepository studentBillRepository;

    public List<StudentBill> GetByStudentAndPeriod(UUID studentUuid, UUID periodUuid) {

        return studentBillRepository.findByStudent_UuidAndPeriod_UuidOrderByUpdatedAtAsc(studentUuid, periodUuid);

    }

    public Optional<StudentBill> FindById(UUID studentUuid) {
        return studentBillRepository.findById(studentUuid);
    }

    public StudentBill Save(StudentBill studentBill) {
        return studentBillRepository.save(studentBill);
    }

    public List<StudentBillReportDTO> getStudentBillsByPeriod(UUID periodUuid) {
        return studentBillRepository.findBillsByPeriod(periodUuid);
    }

}
