package com.schoolmoney.pl.core.student.management;

import com.schoolmoney.pl.core.student.models.StudentDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<StudentDAO, UUID>, JpaSpecificationExecutor<StudentDAO> {
    List<StudentDAO> findByParentIdAndIsArchivedFalse(UUID parentId);
}