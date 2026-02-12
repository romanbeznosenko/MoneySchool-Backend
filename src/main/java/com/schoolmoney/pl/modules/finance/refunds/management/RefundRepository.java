package com.schoolmoney.pl.modules.finance.refunds.management;

import com.schoolmoney.pl.modules.finance.refunds.models.RefundDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RefundRepository extends JpaRepository<RefundDAO, UUID> {

    @Query("SELECT COALESCE(SUM(r.amount), 0) FROM RefundDAO r WHERE r.collection.id = :collectionId")
    Long sumByCollectionId(@Param("collectionId") UUID collectionId);
}
