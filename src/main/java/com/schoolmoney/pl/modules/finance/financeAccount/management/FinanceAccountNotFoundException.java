package com.schoolmoney.pl.modules.finance.financeAccount.management;

public class FinanceAccountNotFoundException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "Finance account not found";
    public FinanceAccountNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
