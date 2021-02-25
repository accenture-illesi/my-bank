package com.abc.account;

import com.abc.interest.WithdrawalTimingInterest;

import java.math.BigDecimal;

public class MaxiSavingsAccount extends Account {
    private static final WithdrawalTimingInterest MAXI_SAVINGS_INTEREST = new WithdrawalTimingInterest(10, 0.001, 0.05);
    private static final WithdrawalTimingInterest DAILY_MAXI_SAVINGS_INTEREST = new WithdrawalTimingInterest(10, 0.001 / DAYS_IN_YEAR, 0.05 / DAYS_IN_YEAR);
    
    @Override
    public BigDecimal interestEarned() {
        return MAXI_SAVINGS_INTEREST.calculate(sumTransactions(), getTransactions());
    }
    
    @Override
    public BigDecimal dailyInterestEarned() {
        return DAILY_MAXI_SAVINGS_INTEREST.calculate(sumTransactions(), getTransactions());
    }
    
    @Override
    public AccountType getAccountType() {
        return AccountType.MAXI_SAVINGS;
    }
}
