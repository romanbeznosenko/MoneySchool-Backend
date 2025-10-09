package com.schoolmoney.pl.modules.finance.financeAccount.management;

import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccountDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface FinanceAccountRepository extends JpaRepository<FinanceAccountDAO, UUID> {
    @Query(value = "SELECT * FROM finance_account WHERE owner_id = :ownerId", nativeQuery = true)
    Optional<FinanceAccountDAO> findByOwnerId(@Param("ownerId") UUID ownerId);

}