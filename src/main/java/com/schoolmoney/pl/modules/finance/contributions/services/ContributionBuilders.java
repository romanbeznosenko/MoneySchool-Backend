package com.schoolmoney.pl.modules.finance.contributions.services;

import com.schoolmoney.pl.core.student.models.Student;
import com.schoolmoney.pl.core.user.models.User;
import com.schoolmoney.pl.modules.finance.collections.models.Collection;
import com.schoolmoney.pl.modules.finance.contributions.models.Contribution;
import com.schoolmoney.pl.modules.finance.contributions.models.ContributionId;
import com.schoolmoney.pl.modules.finance.contributions.models.ContributionStatus;
import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccount;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ContributionBuilders {

    public static Contribution buildContribution(
            Collection collection,
            Student student,
            User payer,
            FinanceAccount sourceAccount,
            Double amount,
            String note
    ) {
        return Contribution.builder()
                .contributionId(ContributionId.of(null))
                .collection(collection)
                .student(student)
                .payer(payer)
                .sourceAccount(sourceAccount)
                .amount(amount)
                .note(note)
                .status(ContributionStatus.PENDING)
                .createdAt(Instant.now())
                .processedAt(null)
                .updatedAt(Instant.now())
                .build();
    }

    public static Contribution buildCompletedContribution(
            Collection collection,
            Student student,
            User payer,
            FinanceAccount sourceAccount,
            Double amount,
            String note
    ) {
        Instant now = Instant.now();
        return Contribution.builder()
                .contributionId(ContributionId.of(null))
                .collection(collection)
                .student(student)
                .payer(payer)
                .sourceAccount(sourceAccount)
                .amount(amount)
                .note(note)
                .status(ContributionStatus.COMPLETED)
                .createdAt(now)
                .processedAt(now)
                .updatedAt(now)
                .build();
    }
}
