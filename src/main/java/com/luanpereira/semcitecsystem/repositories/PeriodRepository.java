package com.luanpereira.semcitecsystem.repositories;

import com.luanpereira.semcitecsystem.models.Period;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PeriodRepository extends JpaRepository<Period, UUID> {

    List<Period> findAllByOrderByYearDescMonthDesc();

    @Query("SELECT p FROM period p ORDER BY p.year DESC, p.month DESC")
    List<Period> findAllOrdered();
}
