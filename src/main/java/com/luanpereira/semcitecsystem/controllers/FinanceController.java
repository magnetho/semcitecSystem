package com.luanpereira.semcitecsystem.controllers;

import com.luanpereira.semcitecsystem.models.Classroom;
import com.luanpereira.semcitecsystem.models.Employee;
import com.luanpereira.semcitecsystem.models.Period;
import com.luanpereira.semcitecsystem.models.Schedule;
import com.luanpereira.semcitecsystem.models.StatusPeriod;
import com.luanpereira.semcitecsystem.models.StudentModel;
import com.luanpereira.semcitecsystem.models.UserModel;
import com.luanpereira.semcitecsystem.services.ClassroomService;
import com.luanpereira.semcitecsystem.services.EmployeeService;
import com.luanpereira.semcitecsystem.services.PeriodService;
import com.luanpereira.semcitecsystem.services.ScheduleService;
import com.luanpereira.semcitecsystem.services.UserService;
import com.luanpereira.semcitecsystem.utils.BrazilianStates;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/finance")
public class FinanceController {

    @Autowired
    private UserService userService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private PeriodService periodService;

    @Autowired
    private ClassroomService classroomService;


    private static String USER_PROFILE_IMG_DIRECTORY;

    public FinanceController(@Value("${userProfileImgDirectory}") String userProfileImgDirectory) {
        USER_PROFILE_IMG_DIRECTORY = userProfileImgDirectory;
    }

    

    @GetMapping("/period/list")
    public String listPeriod(Model model) {
        List<Period> periodList = periodService.findAll();
        model.addAttribute("periodList", periodList);
        model.addAttribute("contentTitle", "Lista de Períodos");
        model.addAttribute("content", "periodPage");
        return "default";
    }

     @GetMapping("/period/new")
    public String newPeriod(Model model) {
        model.addAttribute("contentTitle", "Criar Período");
        model.addAttribute("content", "newPeriod");
        return "default";
    }

    @PostMapping("/period/create")
    public String createPeriod(RedirectAttributes redirectAttributes, Period period) {
        period.setStatus(StatusPeriod.ABERTO);
       this.periodService.save(period);
            redirectAttributes.addFlashAttribute("successMsg", "Período criado com sucesso!");
        return "redirect:/finance/period/list" ;
    }

    @GetMapping("/list")
    public String listEmployees(Model model) {
        List<Schedule> employeeList = scheduleService.findAll();
        model.addAttribute("scheduleList", employeeList);
        model.addAttribute("contentTitle", "Lista de Horários");
        model.addAttribute("content", "schedulePage");
        return "default";
    }

    @GetMapping("/new")
    public String newEmployee(Model model) {

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
        String successMsg = schedule.getUuid() == null ? "Horário atribuido com sucesso" : "Horário editado com sucesso";
        try {
            this.scheduleService.save(schedule);
            redirectAttributes.addFlashAttribute("successMsg", successMsg);
            return "redirect:/schedule/list" ;
        
        }    catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Algo deu errado");
        }
        return "redirect:/schedule/new";
    }

 
}