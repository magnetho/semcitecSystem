package com.luanpereira.semcitecsystem.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "studentBill")
public class StudentBill {

    @Id
    @GeneratedValue
    private UUID uuid;

    @ManyToOne
    @JoinColumn(nullable = true)
    private Inscription inscription;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Period period;
    private LocalDate dueDate;
    private BigDecimal baseAmount;       
    private BigDecimal discountAmount;    
    private BigDecimal additionAmount;     
    private BigDecimal finalAmount;  
    private BigDecimal paymentValue;
    @Enumerated(EnumType.ORDINAL)
    private PaymentType paymentType;
    private LocalDate paymentDate;
    @Enumerated(EnumType.ORDINAL)
    private BillType type;
    @Column(length = 500)
    private String observation;
}

