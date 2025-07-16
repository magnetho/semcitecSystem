package com.luanpereira.semcitecsystem.services;

import com.luanpereira.semcitecsystem.models.CourseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticsService {

    @Autowired
    private StudentService studentService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private UserService userService;
    @Autowired
    private InscriptionService inscriptionService;

    public List<Statistics> semcitecStatistics() {
        List<CourseModel> allCourses = this.courseService.findAll();
        List<Statistics> statistics = new ArrayList<>();
        allCourses.stream()
                .forEach(course -> {
                    statistics.add(new Statistics(course.getName(), inscriptionService.findByCourse(course).stream().count()));
                });
        return statistics;
    }

    @Data
    @AllArgsConstructor
    public static class Statistics {
        private String name;
        private Long quantity;
    }
}
