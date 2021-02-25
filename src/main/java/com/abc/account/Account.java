package com.abc.account;

import com.abc.transaction.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Account {
    protected static final int DAYS_IN_YEAR = LocalDate.now().lengthOfYear();
    
    private final List<Transaction> transactions;
    
    private BigDecimal sum;
    
    public Account() {
        this.transactions = new ArrayList<>();
    }
    
    public static Account newAccount(AccountType accountType) {
        return switch (accountType) {
            case CHECKING -> new CheckingAccount();
            case SAVINGS -> new SavingsAccount();
            case MAXI_SAVINGS -> new MaxiSavingsAccount();
        };
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
    
    //FIXME sum up daily interests
    public abstract BigDecimal interestEarned();
    
    public abstract BigDecimal dailyInterestEarned();
    
    public BigDecimal sumTransactions() {
        if (sum == null) {
            sum = BigDecimal.ZERO;
            for (Transaction transaction : transactions) {
                sum = sum.add(transaction.getAmount());
            }
        }
        return sum;
    }
    
    public abstract AccountType getAccountType();
    /*{return accountType;}*/
    
    public List<Transaction> getTransactions() {
        return transactions;
    }
}
