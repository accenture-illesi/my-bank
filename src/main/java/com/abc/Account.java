package com.abc;

import com.abc.interest.ProgressiveInterest;
import com.abc.interest.WithdrawalTimingInterest;
import com.abc.transaction.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Account {
    private static final ProgressiveInterest CHECKING_INTEREST = new ProgressiveInterest(0.001);
    private static final ProgressiveInterest SAVINGS_INTEREST = new ProgressiveInterest(0.001).next(1000, 0.002);
    private static final WithdrawalTimingInterest MAXI_SAVINGS_INTEREST = new WithdrawalTimingInterest(10, 0.001, 0.05);
    private static final int DAYS_IN_YEAR = LocalDate.now().lengthOfYear();
    private static final ProgressiveInterest DAILY_CHECKING_INTEREST = new ProgressiveInterest(0.001 / DAYS_IN_YEAR);
    private static final ProgressiveInterest DAILY_SAVINGS_INTEREST = new ProgressiveInterest(0.001 / DAYS_IN_YEAR).next(1000, 0.002 / DAYS_IN_YEAR);
    private static final WithdrawalTimingInterest DAILY_MAXI_SAVINGS_INTEREST = new WithdrawalTimingInterest(10, 0.001 / DAYS_IN_YEAR, 0.05 / DAYS_IN_YEAR);
    
    private final AccountType accountType;
    private final List<Transaction> transactions;
    
    private BigDecimal sum;
    
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
            sum = null;
            return this;
        }
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
            sum = null;
            return this;
        }
    }
    
    public BigDecimal dailyInterestEarned() {
        BigDecimal amount = sumTransactions();
        return switch (accountType) {
            case CHECKING -> DAILY_CHECKING_INTEREST.calculate(amount);
            case SAVINGS -> DAILY_SAVINGS_INTEREST.calculate(amount);
            case MAXI_SAVINGS -> DAILY_MAXI_SAVINGS_INTEREST.calculate(amount, transactions);
        };
    }
    
    public BigDecimal interestEarned() {
        BigDecimal amount = sumTransactions();
        return switch (accountType) {
            case CHECKING -> CHECKING_INTEREST.calculate(amount);
            case SAVINGS -> SAVINGS_INTEREST.calculate(amount);
            case MAXI_SAVINGS -> MAXI_SAVINGS_INTEREST.calculate(amount, transactions);
        };
    }
    
    public BigDecimal sumTransactions() {
        if (sum == null) {
            sum = BigDecimal.ZERO;
            for (Transaction transaction : transactions) {
                sum = sum.add(transaction.getAmount());
            }
        }
        return sum;
    }
    
    public AccountType getAccountType() {
        return accountType;
    }
    
    public List<Transaction> getTransactions() {
        return transactions;
    }
}
