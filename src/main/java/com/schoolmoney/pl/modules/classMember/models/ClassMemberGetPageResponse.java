package com.schoolmoney.pl.modules.classMember.models;

import lombok.Builder;

import java.util.List;

@Builder
public record ClassMemberGetPageResponse(
        long count,
        List<ClassMemberGetResponse> data
) {
}
