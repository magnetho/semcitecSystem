package com.luanpereira.semcitecsystem.controllers;

import com.luanpereira.semcitecsystem.models.Employee;
import com.luanpereira.semcitecsystem.models.Schedule;
import com.luanpereira.semcitecsystem.services.EmployeeService;
import com.luanpereira.semcitecsystem.services.ScheduleService;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ScheduleService scheduleService;

    private List<Map.Entry<String, String>> states = BrazilianStates.getStates();

    private static String USER_PROFILE_IMG_DIRECTORY;

    public ScheduleController(@Value("${userProfileImgDirectory}") String userProfileImgDirectory) {
        USER_PROFILE_IMG_DIRECTORY = userProfileImgDirectory;
    }

    

    @GetMapping("/list")
    public String listEmployees(Model model) {
        List<Schedule> employeeList = scheduleService.findAll();
        model.addAttribute("scheduleList", employeeList);
        model.addAttribute("contentTitle", "lista de  horarios");
        model.addAttribute("content", "schedulePage");
        return "default";
    }

    @GetMapping("/newEmployee")
    public String newEmployee(Model model) {
        model.addAttribute("states", states);
        model.addAttribute("contentTitle", "Novo Funcionário");
        model.addAttribute("content", "newEmployee");
        return "default";

    }

    @PostMapping("/saveEmployee")
    public String saveNewEmployee(RedirectAttributes redirectAttributes, Model model, Employee employeeData) {
        String successMsg = employeeData.getUuid() == null ? "Funcionário criado com sucesso" : "Funcionário editado com sucesso";

        try {
            this.employeeService.save(employeeData);
            redirectAttributes.addFlashAttribute("successMsg", successMsg);
            return "redirect:/employee/employeeProfile/" + employeeData.getUuid();
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Funcionário já cadastrado");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Erro ao cadastrar funcionário");
        }

        return "redirect:/employee/newEmployee";
    }

    @PostMapping("/saveImg")
    public String saveImg(MultipartFile img, UUID uuid) {
        this.employeeService.findById(uuid).ifPresent(employeeModel -> {
            String filePath = "/userProfileImgDirectory/" + img.getOriginalFilename();
            employeeModel.setImg(filePath);
            this.employeeService.save(employeeModel);
            try {
                Files.copy(img.getInputStream(), Path.of(USER_PROFILE_IMG_DIRECTORY + img.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
                System.out.println(USER_PROFILE_IMG_DIRECTORY + img.getOriginalFilename());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return "redirect:/employee/employeeProfile/" + uuid;
    }
}