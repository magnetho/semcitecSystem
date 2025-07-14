package com.luanpereira.semcitecsystem.repositories;

import com.luanpereira.semcitecsystem.models.Period;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PeriodRepository extends JpaRepository<Period, UUID> {
}
