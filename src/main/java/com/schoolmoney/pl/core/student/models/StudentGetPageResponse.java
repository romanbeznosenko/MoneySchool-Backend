package com.schoolmoney.pl.core.student.models;

import lombok.Builder;

import java.util.List;

@Builder
public record StudentGetPageResponse(
        long count,
        List<StudentGetResponse> data
) {
}
