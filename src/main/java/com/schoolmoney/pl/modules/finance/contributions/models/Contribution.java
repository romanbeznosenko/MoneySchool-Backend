package com.schoolmoney.pl.modules.finance.contributions.models;

import com.schoolmoney.pl.core.student.models.Student;
import com.schoolmoney.pl.core.user.models.User;
import com.schoolmoney.pl.modules.finance.collections.models.Collection;
import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contribution {
    private ContributionId contributionId;
    private Collection collection;
    private Student student;
    private User payer;
    private FinanceAccount sourceAccount;
    private Double amount;
    private String note;
    private ContributionStatus status;

    private Instant createdAt;
    private Instant processedAt;
    private Instant updatedAt;
}
