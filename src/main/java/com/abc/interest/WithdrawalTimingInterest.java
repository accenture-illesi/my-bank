package com.abc.interest;

import com.abc.DateProvider;
import com.abc.transaction.Transaction;
import com.abc.transaction.TransactionType;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class WithdrawalTimingInterest {
    private final int days;
    private final BigDecimal rateWithin;
    private final BigDecimal rateOver;
    
    public WithdrawalTimingInterest(int days, double rateWithin, double rateOver) {
        this.days = days;
        this.rateWithin = BigDecimal.valueOf(rateWithin);
        this.rateOver = BigDecimal.valueOf(rateOver);
    }
    
    public BigDecimal calculate(BigDecimal amount, List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            if (isWithinDays(transaction) && TransactionType.WITHDRAWAL == transaction.getType()) {
                return amount.multiply(rateWithin);
            }
        }
        return amount.multiply(rateOver);
    }
    
    private boolean isWithinDays(Transaction transaction) {
        return transaction.getTransactionDate()
                .isAfter(DateProvider.getInstance().now()
                        .minus(days, ChronoUnit.DAYS));
    }
}
