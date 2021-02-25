package com.abc.account;

import com.abc.interest.ProgressiveInterest;

import java.math.BigDecimal;

public class SavingsAccount extends Account {
    @Deprecated
    private static final ProgressiveInterest SAVINGS_INTEREST = new ProgressiveInterest(0.001).next(1000, 0.002);
    private static final ProgressiveInterest DAILY_SAVINGS_INTEREST = new ProgressiveInterest(0.001 / DAYS_IN_YEAR).next(1000, 0.002 / DAYS_IN_YEAR);
    
    @Override
    protected BigDecimal dailyInterestEarned() {
        SAVINGS_INTEREST.calculate(sumTransactions());
        return DAILY_SAVINGS_INTEREST.calculate(sumTransactions());
    }
    
    @Override
    public AccountType getAccountType() {
        return AccountType.SAVINGS;
    }
}
