package com.schoolmoney.pl.core.accountActivation.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AccountActivationRequest(
        @NotBlank
        @NotNull
        @Schema(description = "Account activation code", example = "7312/iii-lll-eee-zzz-nnn-aaa-kkk-ooo-wwww")
        String code) {
}
