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
            throw new IllegalArgumentException("amount must be greater than zero");
        } else {
            transactions.add(new Transaction(amount));
        }
    }
    
    public void withdraw(double amount) {
        //TODO check for negative balance?
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be greater than zero");
        } else {
            transactions.add(new Transaction(-amount));
        }
    }
    
    public double interestEarned() {
        double amount = sumTransactions();
        //TODO move logic into AccountType
        switch (accountType) {
            case SAVINGS:
                if (amount <= 1000) {
                    return amount * 0.001;
                } else {
                    return 1 + (amount - 1000) * 0.002;
                }
                //TODO add super-savings account
//            case SUPER_SAVINGS:
//                if (amount <= 4000)
//                    return 20;
            case MAXI_SAVINGS:
                if (amount <= 1000) {
                    return amount * 0.02;
                }
                if (amount <= 2000) {
                    return 20 + (amount - 1000) * 0.05;
                }
                return 70 + (amount - 2000) * 0.1;
            default:
                return amount * 0.001;
        }
    }
    
    public double sumTransactions() {
        //TODO eager summing (new field)?
        BigDecimal amount = BigDecimal.ZERO;
        for (Transaction transaction : transactions) {
            amount = amount.add(transaction.getAmount());
        }
        return amount.doubleValue(); // TODO keep as BigDecimal
    }
    
    public AccountType getAccountType() {
        return accountType;
    }
    
    public List<Transaction> getTransactions() {
        return transactions;
    }
}
