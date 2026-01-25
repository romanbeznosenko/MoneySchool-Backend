package com.schoolmoney.pl.modules.finance.financeAccount.management;

public class NotTreasurerException extends RuntimeException {
    public NotTreasurerException() {
        super("Only treasurer accounts can perform this operation");
    }
}
