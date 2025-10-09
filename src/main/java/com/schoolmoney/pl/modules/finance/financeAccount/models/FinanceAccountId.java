package com.schoolmoney.pl.modules.finance.financeAccount.models;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor(staticName = "of")
public class FinanceAccountId {
    UUID id;
}
