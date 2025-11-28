package com.schoolmoney.pl.modules.classMember.management;

import com.schoolmoney.pl.core.student.models.StudentDAO;
import com.schoolmoney.pl.modules.classMember.models.ClassMemberDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ClassMemberRepository extends
        JpaRepository<ClassMemberDAO, UUID>,
        JpaSpecificationExecutor<ClassMemberDAO> {
    List<ClassMemberDAO> findByStudentAndIsConfirmedAndIsArchived(StudentDAO student, Boolean isConfirmed, Boolean isArchived);

    @Query("SELECT CASE WHEN COUNT(cm) > 0 THEN true ELSE false END FROM ClassMemberDAO cm WHERE cm.aClass.id = :classId AND cm.student.id = :studentId")
    boolean existsByClassIdAndStudentId(@Param("classId") UUID classId, @Param("studentId") UUID studentId);
}