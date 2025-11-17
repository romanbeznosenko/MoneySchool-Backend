package com.schoolmoney.pl.modules.finance.collections.models;

import com.schoolmoney.pl.modules.classes.models.Class;
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
    private Class aClass;

    private Instant createdAt;
    private Instant updatedAt;

    private Boolean isArchived;
    private Instant archivedAt;
}
