package com.schoolmoney.pl.modules.classes.models;

import lombok.Builder;

import java.util.List;

@Builder
public record ClassGetPageResponse(
        long count,
        List<ClassGetResponse> data
) {
}
