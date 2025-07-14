package com.luanpereira.semcitecsystem.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Inscription {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID uuid;
    @Column(unique = true, nullable = false, length = 14)
    private String InscriptionCode;
    @ManyToOne
    @JoinColumn(name = "student_uuid", nullable = false)
    private StudentModel student;
    @ManyToOne
    @JoinColumn(name = "course_uuid", nullable = false)
    private CourseModel course;
    @ManyToOne
    @JoinColumn(name = "classroom_uuid", nullable = false)
    private Classroom classroom;
    @ManyToOne
    @JoinColumn(name = "author_uuid", nullable = false)
    private UserModel author;
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ATIVO'")
    private Status status;
    @CreationTimestamp
    private LocalDate inscriptionDate;
    private BigDecimal monthlyValue; 
    private BigDecimal discountValue; 
    private Integer dueDay;
    @Column(columnDefinition = "TEXT")
    public String observation;
}
