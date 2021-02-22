package com.abc;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class AccountTest {
    private static final double DOUBLE_DELTA = 1e-15;
    Account account;
    
    @Before
    public void init() {
    }
    
    @Test
    public void checking() {
        //given
        account = new Account(AccountType.CHECKING);
        account.deposit(3000.0);
        //when
        double actual = account.interestEarned();
        //then
        double expected = 3000 * 0.001;
        assertEquals(expected, actual, DOUBLE_DELTA);
    }
    
    @Test
    public void savings() {
        //given
        account = new Account(AccountType.SAVINGS);
        account.deposit(3000.0);
        //when
        double actual = account.interestEarned();
        //then
        double expected = 1000 * 0.001 + 2000 * 0.002;
        assertEquals(expected, actual, DOUBLE_DELTA);
    }
    
    @Test
    public void maxiSavingsNoWithdrawals() {
        //given
        account = new Account(AccountType.MAXI_SAVINGS);
        account.deposit(3000.0);
        //when
        double actual = account.interestEarned();
        //then
        double expected = 3000 * 0.05;
        assertEquals(expected, actual, DOUBLE_DELTA);
    }
    
    @Test
    public void maxiSavingsExpiredWithdrawals() {
        //given
        account = new Account(AccountType.MAXI_SAVINGS);
        account.deposit(3000.0);
        account.getTransactions().add(new Transaction(-1000, LocalDateTime.now()
                .minus(10, ChronoUnit.DAYS)
                .minus(1, ChronoUnit.MINUTES)));
        //when
        double actual = account.interestEarned();
        //then
        double expected = 2000 * 0.05;
        assertEquals(expected, actual, DOUBLE_DELTA);
    }
    
    @Test
    public void maxiSavingsWithWithdrawals() {
        //given
        account = new Account(AccountType.MAXI_SAVINGS);
        account.deposit(3000.0);
        account.withdraw(1000);
        //when
        double actual = account.interestEarned();
        //then
        double expected = 2000 * 0.001;
        assertEquals(expected, actual, DOUBLE_DELTA);
    }
    
    @Test
    public void zeroWithdraw() {
        //given
        account = new Account(AccountType.CHECKING);
        account.deposit(1000);
        try {
            //when
            account.withdraw(BigDecimal.ZERO);
            fail("Should throw exception");
        } catch (Throwable e) {
            //then
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }
    
    @Test
    public void notEnoughBalance() {
        //given
        account = new Account(AccountType.CHECKING);
        account.deposit(1000);
        try {
            //when
            account.withdraw(2000);
            fail("Should throw exception");
        } catch (Throwable e) {
            //then
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }
}
