package com.luanpereira.semcitecsystem.controllers;

import com.luanpereira.semcitecsystem.Dtos.StudentBillReportDTO;
import com.luanpereira.semcitecsystem.models.BillType;
import com.luanpereira.semcitecsystem.models.PaymentType;
import com.luanpereira.semcitecsystem.models.Period;
import com.luanpereira.semcitecsystem.models.Schedule;
import com.luanpereira.semcitecsystem.models.StatusPeriod;
import com.luanpereira.semcitecsystem.models.StudentBill;
import com.luanpereira.semcitecsystem.models.StudentModel;
import com.luanpereira.semcitecsystem.services.FinanceService;
import com.luanpereira.semcitecsystem.services.PeriodService;
import com.luanpereira.semcitecsystem.services.ScheduleService;
import com.luanpereira.semcitecsystem.services.StudentBillService;
import com.luanpereira.semcitecsystem.services.StudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/finance")
public class FinanceController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private PeriodService periodService;

    @Autowired
    private FinanceService financeService;

    @Autowired
    private StudentBillService studentBillService;
    @Autowired
    private StudentService studentService;

    private static String USER_PROFILE_IMG_DIRECTORY;

    public FinanceController(@Value("${userProfileImgDirectory}") String userProfileImgDirectory) {
        USER_PROFILE_IMG_DIRECTORY = userProfileImgDirectory;
    }

    @GetMapping("/student/{uuid}")
    public String studentBill(@PathVariable UUID uuid, Model model,
            @RequestParam(name = "period", required = false) UUID periodUuid) {
        StudentModel student = this.studentService.findById(uuid);
        List<Period> periods = periodService.findAllOrder();
        LocalDate now = LocalDate.now();
        int currentMonth = now.getMonthValue();
        int currentYear = now.getYear();

        Period selectedPeriod;

        if (periodUuid != null) {
            selectedPeriod = periods.stream()
                    .filter(p -> p.getUuid().equals(periodUuid))
                    .findFirst()
                    .orElse(null);
        } else {
            selectedPeriod = periods.stream()
                    .filter(p -> p.getMonth() == currentMonth && p.getYear() == currentYear)
                    .findFirst()
                    .orElse(periods.isEmpty() ? null : periods.get(0));
        }

        List<StudentBill> studentBills = studentBillService.GetByStudentAndPeriod(uuid, selectedPeriod.getUuid());

        BigDecimal total = studentBills.stream()
                .map(StudentBill::getFinalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("totalBills", total);
        model.addAttribute("periods", periods);
        model.addAttribute("periodSelect", selectedPeriod);
        model.addAttribute("studentBills", studentBills);
        model.addAttribute("student", student);
        model.addAttribute("contentTitle", "Financeiro");
        model.addAttribute("content", "studentBill");
        return "default";
    }

    @PostMapping("/student-bill")
    public String SaveStudentBill(RedirectAttributes redirectAttributes, @ModelAttribute StudentBill bill) {

        switch (bill.getType()) {
            case DESCONTO:
                bill.setDiscountAmount(bill.getBaseAmount());
                bill.setAdditionAmount(BigDecimal.ZERO);
                bill.setPaymentValue(BigDecimal.ZERO);
                bill.setBaseAmount(BigDecimal.ZERO);
                break;
            case MATRÍCULA:
            case TAXA_MATERIAL:
            case MENSALIDADE:
                bill.setBaseAmount(bill.getBaseAmount());
                bill.setAdditionAmount(BigDecimal.ZERO);
                bill.setPaymentValue(BigDecimal.ZERO);
                bill.setDiscountAmount(BigDecimal.ZERO);
                break;

            case PAGAMENTO:

                bill.setPaymentValue(bill.getBaseAmount());
                bill.setAdditionAmount(BigDecimal.ZERO);
                bill.setDiscountAmount(BigDecimal.ZERO);
                bill.setBaseAmount(BigDecimal.ZERO);
                break;
            default:
                bill.setBaseAmount(bill.getBaseAmount());
                bill.setAdditionAmount(BigDecimal.ZERO);
                bill.setPaymentValue(BigDecimal.ZERO);
                bill.setDiscountAmount(BigDecimal.ZERO);
                break;
        }

        bill.setCurrentBalance();
        studentBillService.Save(bill);
        redirectAttributes.addFlashAttribute("successMsg", "Lançamento criado com sucesso!");
        return "redirect:/finance/student/" + bill.getStudent().getUuid() + "?period=" + bill.getPeriod().getUuid();

    }

    @PostMapping("/student-bill/edit")
    public String EditStudentBill(RedirectAttributes redirectAttributes, @ModelAttribute StudentBill bill) {

        bill.setCurrentBalance();
        studentBillService.Save(bill);
        redirectAttributes.addFlashAttribute("successMsg", "Lançamento criado com sucesso!");
        return "redirect:/finance/student/" + bill.getStudent().getUuid() + "?period=" + bill.getPeriod().getUuid();

    }

    @PostMapping("/pay")
    public String paymentBill(RedirectAttributes redirectAttributes,
            @RequestParam("selectedBills") List<UUID> selectedBills,
            @RequestParam("paymentDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate paymentDate,
            @RequestParam("paymentType") PaymentType paymentType,
            @RequestParam("studentUuid") UUID studentUuid,
            @RequestParam("period") UUID periodUuid,
            Model model) {

        if (selectedBills.isEmpty()) {
            model.addAttribute("error", "Nenhum lançamento foi selecionado.");
            return "redirect:/finance/student/" + studentUuid + "?period=" + periodUuid;
        }

        for (UUID billId : selectedBills) {
            StudentBill bill = studentBillService.FindById(billId)
                    .orElseThrow(() -> new RuntimeException("Lançamento não encontrado: " + billId));

            bill.setPaymentDate(paymentDate);
            bill.setPaymentType(paymentType);
            bill.setPaymentBalance();
            bill.setCurrentBalance();

            studentBillService.Save(bill);
        }

        redirectAttributes.addFlashAttribute("successMsg", "Lançamento criado com sucesso!");
        return "redirect:/finance/student/" + studentUuid + "?period=" + periodUuid;
    }

    @GetMapping("/period/list")
    public String listPeriod(Model model) {
        List<Period> periodList = periodService.findAllOrder();
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
        return "redirect:/finance/period/list";
    }

    @GetMapping("/generate/{id}")
    public String editarSchedule(RedirectAttributes redirectAttributes, @PathVariable UUID id, Model model) {

        try {
            Period period = periodService.findById(id).orElseThrow();
            Integer total = financeService.generateStudentBillsForPeriod(period);
            period.setStatus(StatusPeriod.CALCULADO);
            periodService.save(period);
            redirectAttributes.addFlashAttribute("successMsg", total + " lançamentos gerados com sucesso");
            return "redirect:/finance/period/list";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "erro no calculo");
            return "redirect:/finance/period/list";

        }

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

    @GetMapping("/report/bills")
    public String getStudentBillReport(@RequestParam(name = "period", required = false) UUID periodUuid, Model model) {

        List<Period> periods = periodService.findAllOrder();
        LocalDate now = LocalDate.now();
        int currentMonth = now.getMonthValue();
        int currentYear = now.getYear();
        Period selectedPeriod;

        if (periodUuid != null) {
            selectedPeriod = periods.stream()
                    .filter(p -> p.getUuid().equals(periodUuid))
                    .findFirst()
                    .orElse(null);
        } else {
            selectedPeriod = periods.stream()
                    .filter(p -> p.getMonth() == currentMonth && p.getYear() == currentYear)
                    .findFirst()
                    .orElse(periods.isEmpty() ? null : periods.get(0));
        }

        var reportList = studentBillService.getStudentBillsByPeriod(selectedPeriod.getUuid());

        // Total geral (finalAmount)
        BigDecimal totalFinal = reportList.stream()
                .map(StudentBillReportDTO::finalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("periodSelect", selectedPeriod);
        model.addAttribute("bills", reportList);
        model.addAttribute("totalFinal", totalFinal);
        model.addAttribute("contentTitle", "Relatorio");
        model.addAttribute("content", "studentBillReport");
        return "default";
    }
}