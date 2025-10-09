package com.schoolmoney.pl.modules.finance.financeAccount.management;

public class FinanceAccountOwnerMismatchException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "User is not the owner of this finance account";
    public FinanceAccountOwnerMismatchException() {
        super(DEFAULT_MESSAGE);
    }
}
