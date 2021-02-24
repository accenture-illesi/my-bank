package com.abc.transaction;

import java.time.LocalDateTime;

/**
 * Mock {@link Transaction} class.
 *
 * @implNote Used instead of Mockito.
 */
public class MockTransaction extends Transaction {
    private final LocalDateTime date;
    
    /**
     * Creates a mock {@link Transaction} when testing with given transaction dates.
     */
    public MockTransaction(double amount, LocalDateTime date) {
        super(amount);
        this.date = date;
    }
    
    @Override
    public LocalDateTime getTransactionDate() {
        return date;
    }
}
