package com.abc.transaction;

import com.abc.DateProvider;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private final BigDecimal amount;
    private final LocalDateTime transactionDate;
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }
    
    public TransactionType getType() {
        return BigDecimal.ZERO.compareTo(amount) > 0 ? TransactionType.WITHDRAWAL : TransactionType.DEPOSIT;
    }
    
    public Transaction(double amount) {
        this(BigDecimal.valueOf(amount));
    }
    
    public Transaction(BigDecimal amount) {
        this.amount = amount;
        this.transactionDate = DateProvider.getInstance().now();
    }
}
