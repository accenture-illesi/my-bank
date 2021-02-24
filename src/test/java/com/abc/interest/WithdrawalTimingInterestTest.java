package com.abc.interest;

import com.abc.transaction.MockTransaction;
import com.abc.transaction.Transaction;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class WithdrawalTimingInterestTest {
    private static final double DOUBLE_DELTA = 1e-15;
    private WithdrawalTimingInterest underTest;
    private static final BigDecimal TWO_THOUSAND = BigDecimal.valueOf(2000);
    private static final BigDecimal THREE_THOUSAND = BigDecimal.valueOf(3000);
    
    @Before
    public void init() {
        underTest = new WithdrawalTimingInterest(10, 0.001, 0.05);
    }
    
    @Test
    public void noWithdrawals() {
        //given
        List<Transaction> transactions = List.of(new Transaction(3000));
        //when
        double actual = underTest.calculate(THREE_THOUSAND, transactions).doubleValue();
        //then
        double expected = 3000 * 0.05;
        assertEquals(expected, actual, DOUBLE_DELTA);
    }
    
    @Test
    public void expiredWithdrawals() {
        //given
        List<Transaction> transactions = List.of(new Transaction(3000),
                new MockTransaction(-1000, LocalDateTime.now()
                        .minus(10, ChronoUnit.DAYS)
                        .minus(1, ChronoUnit.MINUTES)));
        //when
        double actual = underTest.calculate(TWO_THOUSAND, transactions).doubleValue();
        //then
        double expected = 2000 * 0.05;
        assertEquals(expected, actual, DOUBLE_DELTA);
    }
    
    @Test
    public void withWithdrawals() {
        //given
        List<Transaction> transactions = List.of(new Transaction(3000), new Transaction(-1000));
        //when
        double actual = underTest.calculate(TWO_THOUSAND, transactions).doubleValue();
        //then
        double expected = 2000 * 0.001;
        assertEquals(expected, actual, DOUBLE_DELTA);
    }
}
