package com.schoolmoney.pl.modules.finance.collections.models;

import lombok.Builder;

import java.util.List;

@Builder
public record CollectionPageResponse(
        long count,
        List<CollectionResponse> data
) {
}
