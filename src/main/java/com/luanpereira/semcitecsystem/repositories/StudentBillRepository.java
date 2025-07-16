package com.luanpereira.semcitecsystem.repositories;

import com.luanpereira.semcitecsystem.models.Period;
import com.luanpereira.semcitecsystem.models.StudentBill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface StudentBillRepository extends JpaRepository<StudentBill, UUID> {

    List<StudentBill> findAllByPeriod(Period period);
    List<StudentBill> findByStudent_UuidAndPeriod_UuidOrderByUpdatedAtAsc(UUID studentUuid, UUID periodUuid);
}

