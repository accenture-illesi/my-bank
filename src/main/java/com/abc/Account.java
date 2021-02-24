package com.abc;

import com.abc.interest.ProgressiveInterest;
import com.abc.interest.WithdrawalTimingInterest;
import com.abc.transaction.Transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Account {
    public static final ProgressiveInterest CHECKING_INTEREST = new ProgressiveInterest(0.001);
    public static final ProgressiveInterest SAVINGS_INTEREST = new ProgressiveInterest(0.001).next(1000, 0.002);
    public static final WithdrawalTimingInterest MAXI_SAVINGS_INTEREST = new WithdrawalTimingInterest(10, 0.001, 0.05);
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
        switch (accountType) {
            case CHECKING:
                return CHECKING_INTEREST.calculate(amount);
            case SAVINGS:
                return SAVINGS_INTEREST.calculate(amount);
            case MAXI_SAVINGS:
                return MAXI_SAVINGS_INTEREST.calculate(amount, transactions);
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
