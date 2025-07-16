package com.luanpereira.semcitecsystem.repositories;

import com.luanpereira.semcitecsystem.Dtos.StudentBillReportDTO;
import com.luanpereira.semcitecsystem.models.Period;
import com.luanpereira.semcitecsystem.models.StudentBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface StudentBillRepository extends JpaRepository<StudentBill, UUID> {

    List<StudentBill> findAllByPeriod(Period period);

    List<StudentBill> findByStudent_UuidAndPeriod_UuidOrderByUpdatedAtAsc(UUID studentUuid, UUID periodUuid);

    @Query("""
                select new com.luanpereira.semcitecsystem.Dtos.StudentBillReportDTO(
                    sb.student.name,
                    sb.type,
                    sb.dueDate,
                    sb.baseAmount,
                    sb.discountAmount,
                    sb.additionAmount,
                    sb.paymentValue,
                    sb.finalAmount
                )
                from studentBill sb
                where sb.period.uuid = :periodUuid
                order by sb.student.name, sb.dueDate
            """)
    List<StudentBillReportDTO> findBillsByPeriod(@Param("periodUuid") UUID periodUuid);
}
