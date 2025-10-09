package com.schoolmoney.pl.modules.finance.financeAccount.management;

public class FinanceAccountAlreadyExistingException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "User already have finance account";
    public FinanceAccountAlreadyExistingException() {
        super(DEFAULT_MESSAGE);
    }
}
