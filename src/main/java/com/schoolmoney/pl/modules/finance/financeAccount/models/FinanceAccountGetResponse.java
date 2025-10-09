package com.schoolmoney.pl.modules.finance.financeAccount.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@Builder
public record FinanceAccountGetResponse(
        @Schema(description = "Finance account assigned ID", example = "f7109a73-2926-43f3-a681-18f7984a0228")
        UUID id,

        @Schema(description = "Finance account IBAN", example = "PL61 1090 1014 0000 0712 1633 3975")
        String IBAN,

        @Schema(description = "Finance account balance", example = "23.5")
        Double balance,

        @Schema(description = "Finance account isTreasurerAccount flag", example = "true")
        Boolean isTreasurerAccount
) {
}
