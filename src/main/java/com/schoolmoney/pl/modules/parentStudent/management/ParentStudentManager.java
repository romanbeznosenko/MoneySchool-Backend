package com.schoolmoney.pl.modules.parentStudent.management;

import com.schoolmoney.pl.modules.parentStudent.models.ParentStudentDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParentStudentManager {
    private final ParentStudentRepository parentStudentRepository;

    public ParentStudentDAO saveToDatabase(ParentStudentDAO parentStudent) {
        return parentStudentRepository.save(parentStudent);
    }
}