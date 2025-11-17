package com.schoolmoney.pl.modules.finance.collections.management;

import com.schoolmoney.pl.modules.finance.collections.models.CollectionDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CollectionRepository extends JpaRepository<CollectionDAO, UUID> {
    Page<CollectionDAO> findAll(Specification<CollectionDAO> spec, Pageable pageable);
}