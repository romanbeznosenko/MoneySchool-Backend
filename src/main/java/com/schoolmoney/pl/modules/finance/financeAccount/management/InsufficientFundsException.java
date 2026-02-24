package com.schoolmoney.pl.modules.finance.financeAccount.management;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException() {
        super("Insufficient funds in account");
    }
}
