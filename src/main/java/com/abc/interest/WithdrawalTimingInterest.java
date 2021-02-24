package com.abc.interest;

import com.abc.DateProvider;
import com.abc.transaction.Transaction;

import java.time.temporal.ChronoUnit;
import java.util.List;

public class WithdrawalTimingInterest {
    private final int days;
    private final double rateWithin;
    private final double rateOver;
    
    public WithdrawalTimingInterest(int days, double rateWithin, double rateOver) {
        this.days = days;
        this.rateWithin = rateWithin;
        this.rateOver = rateOver;
    }
    
    public double calculate(double amount, List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            if (isWithinDays(transaction) && "withdrawal".equalsIgnoreCase(transaction.getType())) {
                return amount * rateWithin;
            }
        }
        return amount * rateOver;
    }
    
    private boolean isWithinDays(Transaction transaction) {
        return transaction.getTransactionDate()
                .isAfter(DateProvider.getInstance().now()
                        .minus(days, ChronoUnit.DAYS));
    }
}
