package com.abc;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class BankTest {
    private static final double DOUBLE_DELTA = 1e-15;
    private Bank bank;
    
    @Before
    public void init() {
        bank = new Bank();
    }
    
    @Test
    public void customerSummary() {
        Customer john = new Customer("John");
        john.openAccount(new Account(AccountType.CHECKING));
        bank.addCustomer(john);
        
        assertEquals("Customer Summary\n - John (1 account)", bank.customerSummary());
    }
    
    @Test
    public void checkingAccount() {
        Account checkingAccount = new Account(AccountType.CHECKING);
        Customer bill = new Customer("Bill").openAccount(checkingAccount);
        bank.addCustomer(bill);
        
        checkingAccount.deposit(100.0);
        
        assertEquals(0.1, bank.totalInterestPaid(), DOUBLE_DELTA);
    }
    
    @Test
    public void savings_account() {
        Account account = new Account(AccountType.SAVINGS);
        bank.addCustomer(new Customer("Bill").openAccount(account));
    
        account.deposit(1500.0);
    
        assertEquals(2.0, bank.totalInterestPaid(), DOUBLE_DELTA);
    }
    
    @Test
    public void maxi_savings_account() {
        Account account = new Account(AccountType.MAXI_SAVINGS);
        bank.addCustomer(new Customer("Bill").openAccount(account));
        
        account.deposit(3000.0);
        
        assertEquals(170.0, bank.totalInterestPaid(), DOUBLE_DELTA);
    }
    
    @Test
    public void transfer() {
        //given
        Account from = new Account(AccountType.SAVINGS);
        from.deposit(1000);
        
        Account to = new Account(AccountType.CHECKING);
        to.deposit(1000);
        
        //when
        bank.transfer(from, to, 100);
        
        //then
        assertEquals(0, BigDecimal.valueOf(900).compareTo(from.sumTransactions()));
        assertEquals(0, BigDecimal.valueOf(1100).compareTo(to.sumTransactions()));
    }
    
    @Test
    public void transferFail() {
        //given
        Account from = new Account(AccountType.SAVINGS);
        from.deposit(50);
        
        Account to = new Account(AccountType.CHECKING);
        to.deposit(50);
        
        //when
        try {
            bank.transfer(from, to, 100);
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
        //then
        assertEquals(0, BigDecimal.valueOf(50).compareTo(from.sumTransactions()));
        assertEquals(0, BigDecimal.valueOf(50).compareTo(to.sumTransactions()));
        assertEquals(0, from.sumTransactions().compareTo(to.sumTransactions()));
    }
}
