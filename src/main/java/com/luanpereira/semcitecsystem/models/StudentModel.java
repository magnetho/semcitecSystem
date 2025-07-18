package com.luanpereira.semcitecsystem.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "student")
public class StudentModel {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID uuid;

    private String name;

    @Column(unique = true)
    private String cpf;

    private LocalDate birthday;
    private String gender;
    private String phone1;
    private String phone2;
    private String email;

    @ColumnDefault(value = "'s/n'")
    @Column(length = 20)
    private String houseNumber;

    private String street;
    private String neighborhood;
    private String city;
    private String state;
    private String zip_code;

    @Column(columnDefinition = "TEXT")
    private String obs;

    private String img;

    // Novos Campos
    @Column(nullable = false)
    private String motherName;

    private String fatherName;

    private String originSchool;

    private String grade; // série escolar

    private String shift; // turno

    private Boolean hasDisability;

    private String disability;

    private Boolean usesMedication;

    private String medication;

    private Boolean hasHealthProblem;

    private String healthProblem;

    private String emergencyContactName;

    private String emergencyContactPhone;

    private Boolean allowImageUse;

    private String referralSource;
}