package com.abc.account;

import com.abc.transaction.MockTransaction;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class AccountTest {
    private static final double DOUBLE_DELTA = 1e-15;
    private static final int DAYS = LocalDate.now().lengthOfYear();
    Account account;
    
    @Test
    public void sumNoTransactions() {
        //given
        account = Account.newAccount(AccountType.CHECKING);
        //when
        BigDecimal actual = account.sumTransactions();
        //then
        assertEquals(0, BigDecimal.ZERO.compareTo(actual));
    }
    
    @Test
    public void sumConsecutiveTransactions() {
        //given
        account = Account.newAccount(AccountType.CHECKING);
        //when
        BigDecimal actual0 = account.sumTransactions();
        account.deposit(BigDecimal.ONE);
        BigDecimal actual1 = account.sumTransactions();
        account.deposit(BigDecimal.TEN);
        BigDecimal actual11 = account.sumTransactions();
        //then
        assertEquals(0, BigDecimal.ZERO.compareTo(actual0));
        assertEquals(0, BigDecimal.ONE.compareTo(actual1));
        assertEquals(0, BigDecimal.valueOf(11).compareTo(actual11));
    }
    
    @Test
    public void checking() {
        //given
        account = Account.newAccount(AccountType.CHECKING);
        account.deposit(3000.0);
        //when
        account.depositDailyInterest();
        //then
        double actual = account.interestEarned().doubleValue();
        double expected = 3000 * 0.001 / DAYS;
        assertEquals(expected, actual, DOUBLE_DELTA);
    }
    
    @Test
    public void savings() {
        //given
        account = Account.newAccount(AccountType.SAVINGS);
        account.deposit(3000.0);
        //when
        account.depositDailyInterest();
        //then
        double actual = account.interestEarned().doubleValue();
        double expected = (1000 * 0.001 + 2000 * 0.002) / DAYS;
        assertEquals(expected, actual, DOUBLE_DELTA);
    }
    
    @Test
    public void maxiSavingsNoWithdrawals() {
        //given
        account = Account.newAccount(AccountType.MAXI_SAVINGS);
        account.deposit(3000.0);
        //when
        account.depositDailyInterest();
        //then
        double actual = account.interestEarned().doubleValue();
        double expected = 3000 * 0.05 / DAYS;
        assertEquals(expected, actual, DOUBLE_DELTA);
    }
    
    @Test
    public void maxiSavingsExpiredWithdrawals() {
        //given
        account = Account.newAccount(AccountType.MAXI_SAVINGS);
        account.deposit(3000.0);
        account.getTransactions().add(new MockTransaction(-1000, LocalDateTime.now()
                .minus(10, ChronoUnit.DAYS)
                .minus(1, ChronoUnit.MINUTES)));
        //when
        account.depositDailyInterest();
        //then
        double actual = account.interestEarned().doubleValue();
        double expected = 2000 * 0.05 / DAYS;
        assertEquals(expected, actual, DOUBLE_DELTA);
    }
    
    @Test
    public void maxiSavingsWithWithdrawals() {
        //given
        account = Account.newAccount(AccountType.MAXI_SAVINGS);
        account.deposit(3000.0);
        account.withdraw(1000);
        //when
        account.depositDailyInterest();
        //then
        double actual = account.interestEarned().doubleValue();
        double expected = 2000 * 0.001 / DAYS;
        assertEquals(expected, actual, DOUBLE_DELTA);
    }
    
    @Test
    public void zeroWithdraw() {
        //given
        account = Account.newAccount(AccountType.CHECKING);
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
        account = Account.newAccount(AccountType.CHECKING);
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
