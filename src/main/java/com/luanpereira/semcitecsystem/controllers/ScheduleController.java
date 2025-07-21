package com.luanpereira.semcitecsystem.controllers;

import com.luanpereira.semcitecsystem.models.Classroom;
import com.luanpereira.semcitecsystem.models.Schedule;
import com.luanpereira.semcitecsystem.models.UserModel;
import com.luanpereira.semcitecsystem.services.ClassroomService;
import com.luanpereira.semcitecsystem.services.ScheduleService;
import com.luanpereira.semcitecsystem.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Time;
import java.time.DayOfWeek;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private UserService userService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ClassroomService classroomService;

    private static String USER_PROFILE_IMG_DIRECTORY;

    public ScheduleController(@Value("${userProfileImgDirectory}") String userProfileImgDirectory) {
        USER_PROFILE_IMG_DIRECTORY = userProfileImgDirectory;
    }

    @GetMapping("/list")
    public String lisSchedule(Model model) {
        List<Schedule> employeeList = scheduleService.findAll();
        model.addAttribute("scheduleList", employeeList);
        model.addAttribute("contentTitle", "Lista de Horários");
        model.addAttribute("content", "schedulePage");
        return "default";
    }

    @GetMapping("/new")
    public String newSchedule(Model model) {

        List<UserModel> employeeList = userService.findAll();
        List<Classroom> classList = classroomService.findAllOrderedByName();

        model.addAttribute("schedule", new Schedule());
        model.addAttribute("employeeList", employeeList);
        model.addAttribute("weekDays", DayOfWeek.values());
        model.addAttribute("classList", classList);
        model.addAttribute("contentTitle", "Atribuir Horário");
        model.addAttribute("content", "newSchedule");
        return "default";
    }

    @GetMapping("/edit/{id}")
    public String editarSchedule(@PathVariable UUID id, Model model) {
        Schedule schedule = scheduleService.findById(id).orElseThrow();
        model.addAttribute("schedule", schedule);
        model.addAttribute("classList", classroomService.findAllOrderedByName());
        model.addAttribute("employeeList", userService.findAll());
        model.addAttribute("weekDays", DayOfWeek.values());
        model.addAttribute("contentTitle", "Editar Horário");
        model.addAttribute("content", "newSchedule");
        return "default";
    }

    @PostMapping("/save")
    public String saveStudent(RedirectAttributes redirectAttributes, Schedule schedule) {
        String successMsg = schedule.getUuid() == null ? "Horário atribuido com sucesso"
                : "Horário editado com sucesso";
        try {
            this.scheduleService.save(schedule);
            redirectAttributes.addFlashAttribute("successMsg", successMsg);
            return "redirect:/schedule/list";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Algo deu errado");
        }
        return "redirect:/schedule/new";
    }
    

}