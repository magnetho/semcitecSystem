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
    @JoinColumn(nullable = false)
    private Classroom classroom;

    @ManyToOne
    @JoinColumn(nullable = false)
    private UserModel user;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;   
}
