package com.schoolmoney.pl.modules.finance.collections.models;

import com.schoolmoney.pl.modules.classes.models.Class;
import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccount;
import lombok.*;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Collection {
    private CollectionId collectionId;
    private String title;
    private String logo;
    private String description;
    private Long goal;
    private FinanceAccount financeAccount;
    private CollectionStatus status;
    private Class aClass;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant goalReachedAt;

    private Boolean isArchived;
    private Instant archivedAt;
}
