package com.luanpereira.semcitecsystem.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luanpereira.semcitecsystem.models.BillType;
import com.luanpereira.semcitecsystem.models.Inscription;
import com.luanpereira.semcitecsystem.models.Period;
import com.luanpereira.semcitecsystem.models.Status;
import com.luanpereira.semcitecsystem.models.StudentBill;
import com.luanpereira.semcitecsystem.repositories.InscriptionRepository;
import com.luanpereira.semcitecsystem.repositories.StudentBillRepository;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FinanceService {

    @Autowired
    private InscriptionRepository inscriptionRepository;
    @Autowired
    private StudentBillRepository studentBillRepository;

 

    @Transactional
    public Integer generateStudentBillsForPeriod(Period period) {
    // 1. Buscar todas as inscrições ativas
    List<Inscription> activeInscriptions = inscriptionRepository.findByStatus(Status.ATIVO);

    // 2. Buscar todos os lançamentos já existentes no período
    List<StudentBill> existingBills = studentBillRepository.findAllByPeriod(period);
    Set<UUID> alreadyLaunchedInscriptions = existingBills.stream()
        .filter(b -> b.getInscription() != null)
        .map(b -> b.getInscription().getUuid())
        .collect(Collectors.toSet());

    // 3. Montar nova lista de lançamentos
    List<StudentBill> billsToCreate = new ArrayList<>();

    for (Inscription inscription : activeInscriptions) {
        if (alreadyLaunchedInscriptions.contains(inscription.getUuid())) continue;

        StudentBill bill = new StudentBill();
        bill.setInscription(inscription);
        bill.setPeriod(period);
        bill.setStudent(inscription.getStudent());

        // Calcular data de vencimento baseado no ano/mes do período + dia do vencimento da inscrição
        int dueDay = inscription.getDueDay() != null ? inscription.getDueDay() : 10; // fallback
        LocalDate dueDate = LocalDate.of(period.getYear(), period.getMonth(), 
                            Math.min(dueDay, YearMonth.of(period.getYear(), period.getMonth()).lengthOfMonth()));
        bill.setDueDate(dueDate);

        // Preencher valores
        BigDecimal base = inscription.getMonthlyValue() != null ? inscription.getMonthlyValue() : BigDecimal.ZERO;
        BigDecimal discount = inscription.getDiscountValue() != null ? inscription.getDiscountValue() : BigDecimal.ZERO;
        BigDecimal addition = BigDecimal.ZERO;

        bill.setBaseAmount(base);
        bill.setDiscountAmount(discount);
        bill.setAdditionAmount(addition);
        bill.setFinalAmount(base.subtract(discount).add(addition));

        bill.setType(BillType.MENSALIDADE);
        bill.setObservation("Lançado automaticamente");

        billsToCreate.add(bill);
    }

    // 4. Salvar tudo de uma vez
    Integer total = billsToCreate.size();
    studentBillRepository.saveAll(billsToCreate);
    return total;
}

   
}
