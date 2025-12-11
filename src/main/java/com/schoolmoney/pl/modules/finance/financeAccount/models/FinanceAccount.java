package com.schoolmoney.pl.modules.finance.financeAccount.models;

import com.schoolmoney.pl.core.user.models.User;
import com.schoolmoney.pl.modules.finance.collections.models.Collection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinanceAccount {
    private FinanceAccountId financeAccountId;
    private String IBAN;
    private Double balance;
    private FinanceAccountType accountType;
    private User owner;
    private Collection collection;
    private Boolean isTreasureAccount;
    private Instant createdAt;
    private Instant updatedAt;
    private Boolean isArchived;
    private Instant archivedAt;
}
