package com.schoolmoney.pl.modules.parentStudent.management;

import com.schoolmoney.pl.modules.parentStudent.models.ParentStudentDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ParentStudentRepository extends JpaRepository<ParentStudentDAO, UUID> {
}