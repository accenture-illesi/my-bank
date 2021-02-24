package com.abc.transaction;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TransactionTest {
    @Test
    public void withdrawal() {
        //given
        Transaction transaction = new Transaction(-1);
        //when
        TransactionType actual = transaction.getType();
        //then
        assertEquals(TransactionType.WITHDRAWAL, actual);
    }
    
    @Test
    public void deposit() {
        //given
        Transaction transaction = new Transaction(1);
        //when
        TransactionType actual = transaction.getType();
        //then
        assertEquals(TransactionType.DEPOSIT, actual);
    }
    
    @Test
    public void correctDate() {
        //given
        Transaction transaction = new Transaction(1);
        //when
        LocalDateTime actual = transaction.getTransactionDate();
        //then
        assertTrue(LocalDateTime.now().minus(1, ChronoUnit.MINUTES).isBefore(actual));
        assertTrue(LocalDateTime.now().plus(1, ChronoUnit.MINUTES).isAfter(actual));
    }
}
