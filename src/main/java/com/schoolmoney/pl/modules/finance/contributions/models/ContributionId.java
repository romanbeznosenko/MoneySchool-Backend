package com.schoolmoney.pl.modules.finance.contributions.models;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor(staticName = "of")
public class ContributionId {
    UUID id;
}
