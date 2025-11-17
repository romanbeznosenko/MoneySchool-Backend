package com.schoolmoney.pl.modules.finance.collections.models;

import com.schoolmoney.pl.modules.classes.models.ClassGetResponse;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CollectionResponse(
        UUID collectionId,
        String title,
        String description,
        String logo,
        ClassGetResponse aClass
) {
}
