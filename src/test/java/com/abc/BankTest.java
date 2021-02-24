package com.abc;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class BankTest {
    //TODO improve accuracy of calculations
    private static final double DOUBLE_DELTA = 1e-10;
    private Bank underTest;
    
    @Before
    public void init() {
        underTest = new Bank();
    }
    
    @Test
    public void customerSummary() {
        Customer john = new Customer("John");
        john.openAccount(new Account(AccountType.CHECKING));
        underTest.addCustomer(john);
        
        assertEquals("Customer Summary\n - John (1 account)", underTest.customerSummary());
    }
    
    @Test
    public void checkingAccount() {
        Account checkingAccount = new Account(AccountType.CHECKING);
        Customer bill = new Customer("Bill").openAccount(checkingAccount);
        underTest.addCustomer(bill);
        
        checkingAccount.deposit(100.0);
    
        assertEquals(0.1, underTest.totalInterestPaid(), DOUBLE_DELTA);
    }
    
    @Test
    public void savings_account() {
        Account account = new Account(AccountType.SAVINGS);
        underTest.addCustomer(new Customer("Bill").openAccount(account));
    
        account.deposit(1500.0);
    
        assertEquals(2.0, underTest.totalInterestPaid(), DOUBLE_DELTA);
    }
    
    @Test
    public void maxi_savings_account() {
        Account account = new Account(AccountType.MAXI_SAVINGS);
        underTest.addCustomer(new Customer("Bill").openAccount(account));
    
        account.deposit(3000.0);
    
        double expected = 3000 * 0.05;
        assertEquals(expected, underTest.totalInterestPaid(), DOUBLE_DELTA);
    }
    
    @Test
    public void transfer() {
        //given
        Account from = new Account(AccountType.SAVINGS);
        from.deposit(1000);
        
        Account to = new Account(AccountType.CHECKING);
        to.deposit(1000);
        
        //when
        underTest.transfer(from, to, 100);
        
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
            underTest.transfer(from, to, 100);
            fail("Should throw exception");
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
        //then
        assertEquals(0, BigDecimal.valueOf(50).compareTo(from.sumTransactions()));
        assertEquals(0, BigDecimal.valueOf(50).compareTo(to.sumTransactions()));
        assertEquals(0, from.sumTransactions().compareTo(to.sumTransactions()));
    }
    
    @Test
    public void accrueInterest() {
        //given
        int days = 1;
        completeAccounts();
        //when
        for (int i = 0; i < days; i++) {
            underTest.accrueInterest();
        }
        //then
        double actual = actualBalance();
        double expected = expectedBalance(days);
        assertEquals(expected, actual, DOUBLE_DELTA);
    }
    
    @Test
    public void accrueMultipleInterests() {
        //given
        int days = 2;
        completeAccounts();
        //when
        for (int i = 0; i < days; i++) {
            underTest.accrueInterest();
        }
        //then
        double actual = actualBalance();
        double expected = expectedBalance(days);
        assertEquals(expected, actual, DOUBLE_DELTA);
    }
    
    private void completeAccounts() {
        underTest.addCustomer(new Customer("Arthur")
                .openAccount(new Account(AccountType.CHECKING).deposit(1000))
                .openAccount(new Account(AccountType.SAVINGS).deposit(100).withdraw(50)));
        underTest.addCustomer(new Customer("Charlie")
                .openAccount(new Account(AccountType.MAXI_SAVINGS).deposit(1000)));
    }
    
    private double actualBalance() {
        double actual = 0.0;
        for (Customer customer : underTest.getCustomers()) {
            for (Account account : customer.getAccounts()) {
                actual += account.sumTransactions().doubleValue();
            }
        }
        return actual;
    }
    
    private double expectedBalance(int days) {
        int daysInYear = LocalDate.now().lengthOfYear();
        return 1000 * Math.pow(1 + (0.001 / daysInYear), days)
                + 50 * Math.pow(1 + (0.001 / daysInYear), days)
                + 1000 * Math.pow(1 + (0.05 / daysInYear), days);
    }
}
