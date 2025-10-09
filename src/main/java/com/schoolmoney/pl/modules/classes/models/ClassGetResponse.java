package com.schoolmoney.pl.modules.classes.models;

import com.schoolmoney.pl.core.user.models.UserResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ClassGetResponse(
        @Schema(description = "Assigned class ID", example = "f146b72e-42e5-4f9b-b12b-060b1133b4ab")
        UUID id,

        @Schema(description = "Class name", example = "4B")
        String name,

        @Schema(description = "Class' treasurer", implementation = UserResponse.class)
        UserResponse treasurer,

        @Schema(description = "Whether current user is treasurer of this class", example = "true")
        Boolean isTreasurer,

        @Schema(description = "Class members count", example = "23")
        Long memberCount
) {
}