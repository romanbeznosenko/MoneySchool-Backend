package com.schoolmoney.pl.modules.finance.refunds.management;

public class RefundAmountExceedsCollectedException extends RuntimeException {
    public RefundAmountExceedsCollectedException() {
        super("Refund amount exceeds collected amount");
    }
}
