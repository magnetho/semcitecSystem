package com.luanpereira.semcitecsystem.models;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "period")
public class Period {
    @Id
    @GeneratedValue
    private UUID uuid;

    private int month; 
    private int year;
    @Enumerated(EnumType.ORDINAL)
    private StatusPeriod status;  

    public String getDisplayName() {
        return Month.of(month).getDisplayName(TextStyle.FULL, Locale.forLanguageTag("pt"))
               + "/" + year;
    }
}
