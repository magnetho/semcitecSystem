package com.luanpereira.semcitecsystem.services;

import com.luanpereira.semcitecsystem.models.Period;
import com.luanpereira.semcitecsystem.repositories.PeriodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PeriodService {

    @Autowired
    private PeriodRepository periodRepository;

    public Long countItem() {
        return periodRepository.count();
    }

    public Optional<Period> findById(UUID uuid) {
        return this.periodRepository.findById(uuid);
    }

    public List<Period> findAll() {
        return this.periodRepository.findAll();
    }

    public List<Period> findAllOrder() {
        return this.periodRepository.findAllOrdered();
    }


    public Period save(Period periodData) {
      
        return this.periodRepository.save(periodData);
    }
    
    
}
