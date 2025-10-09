package com.schoolmoney.pl.modules.finance.financeAccount.management;

public class FinanceAccountForbiddenCreationException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "User can not create a new finance account";
    public FinanceAccountForbiddenCreationException() {
        super(DEFAULT_MESSAGE);
    }
}
