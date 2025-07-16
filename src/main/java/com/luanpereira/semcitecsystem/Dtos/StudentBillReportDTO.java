package com.luanpereira.semcitecsystem.Dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.luanpereira.semcitecsystem.models.BillType;

public record StudentBillReportDTO(
        String studentName,
        BillType type,
        LocalDate dueDate,
        BigDecimal baseAmount,
        BigDecimal discountAmount,
        BigDecimal additionAmount,
        BigDecimal paymentValue,
        BigDecimal finalAmount) {
}
