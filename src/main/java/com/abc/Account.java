package com.abc;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Account {
    private final AccountType accountType;
    private final List<Transaction> transactions;
    
    public Account(AccountType accountType) {
        this.accountType = accountType;
        this.transactions = new ArrayList<>();
    }
    
    public Account deposit(double amount) {
        return deposit(BigDecimal.valueOf(amount));
    }
    
    public Account deposit(BigDecimal amount) {
        if (BigDecimal.ZERO.compareTo(amount) >= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        } else {
            transactions.add(new Transaction(amount));
        }
        return this;
    }
    
    public Account withdraw(double amount) {
        return withdraw(BigDecimal.valueOf(amount));
    }
    
    public Account withdraw(BigDecimal amount) {
        if (BigDecimal.ZERO.compareTo(amount) >= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        } else if (sumTransactions().compareTo(amount) < 0) {
            //TODO allow overdraft?
            throw new IllegalArgumentException("Account does not have enough balance");
        } else {
            transactions.add(new Transaction(amount.negate()));
        }
        return this;
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
                for (Transaction transaction : transactions) {
                    if (isWithinTenDays(transaction) && "withdrawal".equalsIgnoreCase(transaction.getType())) {
                        return amount * 0.001;
                    }
                }
                return amount * 0.05;
            case CHECKING:
                return 0 + amount * 0.001;
            default:
                throw new IllegalStateException("Unexpected value: " + accountType);
        }
    }
    
    private boolean isWithinTenDays(Transaction transaction) {
        return transaction.getTransactionDate()
                .isAfter(DateProvider.getInstance().now()
                        .minus(10, ChronoUnit.DAYS));
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
