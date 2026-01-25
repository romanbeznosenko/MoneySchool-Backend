package com.schoolmoney.pl.modules.finance.collections.management;

public class NoFundsToTransferException extends RuntimeException {
    public NoFundsToTransferException() {
        super("Collection has no funds to transfer");
    }
}
