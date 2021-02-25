package com.abc;

import com.abc.account.Account;
import com.abc.account.AccountType;
import com.abc.customer.Customer;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class BankTest {
    private static final double DOUBLE_DELTA = 1e-15;
    /**
     * A slightly less strict delta, to compare compound interests over multiple iterations of calculations.
     *
     * @implNote {@link BigDecimal} is used for the actual calculations, but only double for the tests.
     */
    private static final double LOWER_DELTA = 1e-11;
    private Bank underTest;
    private static final int DAYS = LocalDate.now().lengthOfYear();
    
    @Before
    public void init() {
        underTest = new Bank();
    }
    
    @Test
    public void customerSummary() {
        Customer john = new Customer("John");
        john.openAccount(Account.newAccount(AccountType.CHECKING));
        underTest.addCustomer(john);
        
        assertEquals("Customer Summary" + System.lineSeparator() + " - John (1 account)", underTest.customerSummary());
    }
    
    @Test
    public void checkingAccount() {
        // given
        Account checkingAccount = Account.newAccount(AccountType.CHECKING);
        Customer bill = new Customer("Bill").openAccount(checkingAccount);
        underTest.addCustomer(bill);
        checkingAccount.deposit(100.0);
        
        //when
        underTest.accrueInterest();
        
        //then
        double actual = underTest.totalInterestPaid().doubleValue();
        double expected = 100 * 0.001 / DAYS;
        assertEquals(expected, actual, DOUBLE_DELTA);
    }
    
    @Test
    public void savings_account() {
        // given
        Account account = Account.newAccount(AccountType.SAVINGS);
        underTest.addCustomer(new Customer("Bill").openAccount(account));
        account.deposit(1500.0);
        
        //when
        underTest.accrueInterest();
        
        //then
        double actual = underTest.totalInterestPaid().doubleValue();
        double expected = (1000 * 0.001 + 500 * 0.002) / DAYS;
        assertEquals(expected, actual, DOUBLE_DELTA);
    }
    
    @Test
    public void maxi_savings_account() {
        //given
        Account account = Account.newAccount(AccountType.MAXI_SAVINGS);
        underTest.addCustomer(new Customer("Bill").openAccount(account));
        account.deposit(3000.0);
        
        //when
        underTest.accrueInterest();
        
        //then
        double actual = underTest.totalInterestPaid().doubleValue();
        double expected = 3000 * 0.05 / DAYS;
        assertEquals(expected, actual, DOUBLE_DELTA);
    }
    
    @Test
    public void transfer() {
        //given
        Account from = Account.newAccount(AccountType.SAVINGS);
        from.deposit(1000);
        
        Account to = Account.newAccount(AccountType.CHECKING);
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
        Account from = Account.newAccount(AccountType.SAVINGS);
        from.deposit(50);
        
        Account to = Account.newAccount(AccountType.CHECKING);
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
        double actual = actualBalance().doubleValue();
        double expected = expectedBalance(days);
        assertEquals(expected, actual, DOUBLE_DELTA);
    }
    
    @Test
    public void accrueMultipleInterests() {
        //given
        int days = 100;
        completeAccounts();
        //when
        for (int i = 0; i < days; i++) {
            underTest.accrueInterest();
        }
        //then
        double actual = actualBalance().doubleValue();
        double expected = expectedBalance(days);
        assertEquals(expected, actual, LOWER_DELTA);
    }
    
    private void completeAccounts() {
        underTest.addCustomer(new Customer("Arthur")
                .openAccount(Account.newAccount(AccountType.CHECKING).deposit(1000))
                .openAccount(Account.newAccount(AccountType.SAVINGS).deposit(100).withdraw(50)));
        underTest.addCustomer(new Customer("Charlie")
                .openAccount(Account.newAccount(AccountType.MAXI_SAVINGS).deposit(1000)));
    }
    
    private BigDecimal actualBalance() {
        BigDecimal actual = BigDecimal.ZERO;
        for (Customer customer : underTest.getCustomers()) {
            for (Account account : customer.getAccounts()) {
                actual = actual.add(account.sumTransactions());
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
