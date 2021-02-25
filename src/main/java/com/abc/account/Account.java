package com.abc.account;

import com.abc.transaction.InterestTransaction;
import com.abc.transaction.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Account {
    protected static final int DAYS_IN_YEAR = LocalDate.now().lengthOfYear();
    
    private final List<Transaction> transactions = new ArrayList<>();
    
    private BigDecimal sum;
    
    public static Account newAccount(AccountType accountType) {
        return switch (accountType) {
            case CHECKING -> new CheckingAccount();
            case SAVINGS -> new SavingsAccount();
            case MAXI_SAVINGS -> new MaxiSavingsAccount();
        };
    }
    
    public Account deposit(double amount) {
        return deposit(BigDecimal.valueOf(amount), false);
    }
    
    public Account deposit(BigDecimal amount) {
        return deposit(amount, false);
    }
    
    /**
     * Utility method, to calculate and apply daily interest to the account.
     */
    public void depositDailyInterest() {
        depositInterest(dailyInterestEarned());
    }
    
    private void depositInterest(BigDecimal amount) {
        deposit(amount, true);
    }
    
    private Account deposit(BigDecimal amount, boolean isInterest) {
        if (BigDecimal.ZERO.compareTo(amount) >= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        } else {
            transactions.add(isInterest ? new InterestTransaction(amount) : new Transaction(amount));
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
    
    /**
     * Sums the total interest earned.
     *
     * @return sum of all interests
     */
    public BigDecimal interestEarned() {
        return transactions.stream().filter(t -> t instanceof InterestTransaction)
                .map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * Calculates the daily interest for the account.
     */
    protected abstract BigDecimal dailyInterestEarned();
    
    public BigDecimal sumTransactions() {
        if (sum == null) {
            sum = transactions.stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        return sum;
    }
    
    public abstract AccountType getAccountType();
    
    public List<Transaction> getTransactions() {
        return transactions;
    }
}
