package com.abc;

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
    
    public String getType() {
        return BigDecimal.ZERO.compareTo(amount) > 0 ? "withdrawal" : "deposit";
    }
    
    public Transaction(double amount) {
        this.amount = BigDecimal.valueOf(amount);
        this.transactionDate = DateProvider.getInstance().now();
    }
    
    public Transaction(BigDecimal amount) {
        this.amount = amount;
        this.transactionDate = DateProvider.getInstance().now();
    }
    
    protected Transaction(double amount, LocalDateTime date) {
        this.amount = BigDecimal.valueOf(amount);
        this.transactionDate = date;
    }
}
