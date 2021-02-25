package com.abc.account;

import com.abc.interest.ProgressiveInterest;

import java.math.BigDecimal;

public class CheckingAccount extends Account {
    @Deprecated
    private static final ProgressiveInterest CHECKING_INTEREST = new ProgressiveInterest(0.001);
    private static final ProgressiveInterest DAILY_CHECKING_INTEREST = new ProgressiveInterest(0.001 / DAYS_IN_YEAR);
    
    @Override
    protected BigDecimal dailyInterestEarned() {
        return DAILY_CHECKING_INTEREST.calculate(sumTransactions());
    }
    
    @Override
    public AccountType getAccountType() {
        return AccountType.CHECKING;
    }
}
