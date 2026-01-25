package com.schoolmoney.pl.modules.finance.refunds.management;

import com.schoolmoney.pl.modules.finance.refunds.models.RefundDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RefundRepository extends JpaRepository<RefundDAO, UUID> {
}
