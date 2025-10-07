package com.schoolmoney.pl.modules.classes.management;

import com.schoolmoney.pl.core.user.models.UserDAO;
import com.schoolmoney.pl.modules.classes.models.ClassDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ClassRepository extends JpaRepository<ClassDAO, UUID> {
    Page<ClassDAO> findAll(Specification<ClassDAO> spec, Pageable pageable);

    @Query("""
        SELECT DISTINCT c FROM ClassDAO c
        WHERE c.id IN (
            SELECT cm.aClass.id FROM ClassMemberDAO cm
            WHERE cm.student.parent.id = :parentId
            AND cm.isArchived = false
        )
        """)
    Page<ClassDAO> findByParentId(@Param("parentId") UUID parentId, Pageable pageable);

    @Query("""
        SELECT DISTINCT cm.student.parent 
        FROM ClassMemberDAO cm
        WHERE cm.aClass.id = :classId
        AND cm.isConfirmed = true
        AND cm.isArchived = false
        """)
    List<UserDAO> findParentsByClassId(@Param("classId") UUID classId);
}