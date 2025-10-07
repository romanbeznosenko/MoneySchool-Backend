package com.schoolmoney.pl.core.student.management;

import com.schoolmoney.pl.core.student.models.StudentDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StudentRepository extends JpaRepository<StudentDAO, UUID> {
    Page<StudentDAO> findAll(Specification<StudentDAO> spec, Pageable pageable);
}