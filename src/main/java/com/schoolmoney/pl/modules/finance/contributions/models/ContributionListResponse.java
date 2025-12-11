package com.schoolmoney.pl.modules.finance.contributions.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
public record ContributionListResponse(
        @Schema(description = "Total count of contributions", example = "15")
        Long count,

        @Schema(description = "Total amount of all contributions", example = "750.00")
        Double totalAmount,

        @Schema(description = "List of contributions")
        List<ContributionResponse> data
) {
}
