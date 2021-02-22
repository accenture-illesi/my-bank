package com.abc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Account {
    private final AccountType accountType;
    private final List<Transaction> transactions;
    
    public Account(AccountType accountType) {
        this.accountType = accountType;
        this.transactions = new ArrayList<>();
    }
    
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        } else {
            transactions.add(new Transaction(amount));
        }
    }
    
    public void withdraw(double amount) {
        //TODO check for negative balance?
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        } else {
            transactions.add(new Transaction(-amount));
        }
    }
    
    public double interestEarned() {
        //TODO use BigDecimal
        double amount = sumTransactions().doubleValue();
        //TODO improve readability/maintainability (move logic into AccountType?) (utility class?)
        switch (accountType) {
            case SAVINGS:
                if (amount <= 1000) {
                    return amount * 0.001;
                } else {
                    return /*1000x0.001*/ 1 + (amount - 1000) * 0.002;
                }
            case MAXI_SAVINGS:
                if (amount <= 1000) {
                    return amount * 0.02;
                } else if (amount <= 2000) {
                    return /*1000 x 0.02*/ 20 + (amount - 1000) * 0.05;
                } else {
                    return /*(1000x0.02)+((2000-1000)x0.05)=20+50*/ 70 + (amount - 2000) * 0.1;
                }
            case CHECKING:
                return 0 + amount * 0.001;
            default:
                throw new IllegalStateException("Unexpected value: " + accountType);
        }
    }
    
    public BigDecimal sumTransactions() {
        //TODO eager summing (new field)?
        BigDecimal amount = BigDecimal.ZERO;
        for (Transaction transaction : transactions) {
            amount = amount.add(transaction.getAmount());
        }
        return amount;
    }
    
    public AccountType getAccountType() {
        return accountType;
    }
    
    public List<Transaction> getTransactions() {
        return transactions;
    }
}
