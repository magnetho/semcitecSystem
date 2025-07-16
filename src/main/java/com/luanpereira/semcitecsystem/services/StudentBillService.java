package com.luanpereira.semcitecsystem.services;


import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luanpereira.semcitecsystem.models.StudentBill;
import com.luanpereira.semcitecsystem.repositories.StudentBillRepository;



@Service
public class StudentBillService {

   
    @Autowired
    private StudentBillRepository studentBillRepository;

    
    public List<StudentBill> GetByStudentAndPeriod(UUID studentUuid, UUID periodUuid){

        return studentBillRepository.findByStudent_UuidAndPeriod_UuidOrderByUpdatedAtAsc(studentUuid,periodUuid);
        
    }

    public StudentBill Save(StudentBill studentBill){
        return studentBillRepository.save(studentBill);
    }



    
}
