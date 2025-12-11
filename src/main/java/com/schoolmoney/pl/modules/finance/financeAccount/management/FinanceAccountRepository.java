package com.schoolmoney.pl.modules.finance.financeAccount.management;

import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccountDAO;
import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FinanceAccountRepository extends JpaRepository<FinanceAccountDAO, UUID> {
    @Query(value = "SELECT * FROM finance_account WHERE owner_id = :ownerId", nativeQuery = true)
    Optional<FinanceAccountDAO> findByOwnerId(@Param("ownerId") UUID ownerId);

    @Query("SELECT fa FROM FinanceAccountDAO fa WHERE fa.owner.id = :ownerId AND fa.accountType = :accountType")
    Optional<FinanceAccountDAO> findByOwnerIdAndAccountType(
            @Param("ownerId") UUID ownerId,
            @Param("accountType") FinanceAccountType accountType
    );

    @Query("SELECT fa FROM FinanceAccountDAO fa WHERE fa.accountType = :accountType")
    List<FinanceAccountDAO> findByAccountType(@Param("accountType") FinanceAccountType accountType);

    @Query("SELECT fa FROM FinanceAccountDAO fa WHERE fa.collection.id = :collectionId")
    Optional<FinanceAccountDAO> findByCollectionId(@Param("collectionId") UUID collectionId);
}