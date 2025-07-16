package com.luanpereira.semcitecsystem.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "studentBill")
public class StudentBill {

    @Id
    @GeneratedValue
    private UUID uuid;

    @ManyToOne
    @JoinColumn(nullable = false)
    private StudentModel student;
    @ManyToOne
    @JoinColumn(nullable = true)
    private Inscription inscription;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Period period;
    private LocalDate dueDate;
    @Column(nullable = false)
    private BigDecimal baseAmount = BigDecimal.ZERO;
    @Column(nullable = false)
    private BigDecimal discountAmount = BigDecimal.ZERO;
    @Column(nullable = false)
    private BigDecimal additionAmount = BigDecimal.ZERO;
    @Column(nullable = false)
    private BigDecimal finalAmount = BigDecimal.ZERO;
    @Column(nullable = false)
    private BigDecimal paymentValue = BigDecimal.ZERO;
    @Enumerated(EnumType.ORDINAL)
    private PaymentType paymentType;
    private LocalDate paymentDate;
    @Enumerated(EnumType.ORDINAL)
    private BillType type;
    @Column(length = 500)
    private String observation;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public BigDecimal getCurrentBalance() {
        return baseAmount
                .subtract(discountAmount)
                .add(additionAmount)
                .subtract(paymentValue);
    }

    public void setPaymentBalance() {
        setPaymentValue(baseAmount
                .subtract(discountAmount)
                .add(additionAmount));
    }

    public void setCurrentBalance() {
        setFinalAmount(baseAmount
                .subtract(discountAmount)
                .add(additionAmount)
                .subtract(paymentValue));
    }

}
