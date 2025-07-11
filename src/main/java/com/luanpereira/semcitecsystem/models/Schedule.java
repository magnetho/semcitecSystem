package com.luanpereira.semcitecsystem.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "schedule")
public class Schedule {
    @Id
    @GeneratedValue
    private UUID uuid;

    @ManyToOne
    private Classroom classroom;

    @ManyToOne
    private UserModel user;

    @Enumerated(EnumType.ORDINAL)
    private DayOfWeek dayOfWeek; // Enum do Java

    private LocalTime startTime;
    private LocalTime endTime;   
}
