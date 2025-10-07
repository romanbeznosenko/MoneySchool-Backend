package com.schoolmoney.pl.modules.classMember.management;

import com.schoolmoney.pl.modules.classMember.models.ClassMemberDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ClassMemberRepository extends
        JpaRepository<ClassMemberDAO, UUID>,
        JpaSpecificationExecutor<ClassMemberDAO> {
}