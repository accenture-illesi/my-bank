package com.abc;

import java.math.BigDecimal;
import java.util.Date;

public class Transaction {
    private final BigDecimal amount;
    
    private final Date transactionDate;
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public double getAmountAsDouble() {
        return amount.doubleValue();
    }
    
    public Date getTransactionDate() {
        return transactionDate;
    }
    
    public Transaction(double amount) {
        this.amount = BigDecimal.valueOf(amount);
        this.transactionDate = DateProvider.getInstance().now();
    }
    
    public Transaction(BigDecimal amount) {
        this.amount = amount;
        this.transactionDate = DateProvider.getInstance().now();
    }
}
