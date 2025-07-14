package com.luanpereira.semcitecsystem.services;

import com.luanpereira.semcitecsystem.models.Employee;
import com.luanpereira.semcitecsystem.models.Schedule;
import com.luanpereira.semcitecsystem.repositories.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    public Long countItem() {
        return scheduleRepository.count();
    }

    public Optional<Schedule> findById(UUID uuid) {
        return this.scheduleRepository.findById(uuid);
    }

    public List<Schedule> findAll() {
        return this.scheduleRepository.findAll();
    }

    public Schedule save(Schedule scheduleData) {
      
        return this.scheduleRepository.save(scheduleData);
    }
    
    public Schedule saveDescription(Schedule scheduleData) {
        Schedule schedule = this.scheduleRepository.findById(scheduleData.getUuid()).get();
        return this.scheduleRepository.save(schedule);
    }
}
