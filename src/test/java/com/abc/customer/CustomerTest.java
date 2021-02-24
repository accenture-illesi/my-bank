package com.abc.customer;

import com.abc.Account;
import com.abc.AccountType;
import org.junit.Test;

import static org.junit.Assert.*;

public class CustomerTest {
    @Test
    public void testCustomerStatement() {
        //given
        Account checkingAccount = new Account(AccountType.CHECKING);
        Account savingsAccount = new Account(AccountType.SAVINGS);
        Customer henry = new Customer("Henry").openAccount(checkingAccount).openAccount(savingsAccount);
        checkingAccount.deposit(100.0);
        savingsAccount.deposit(4000.0);
        savingsAccount.withdraw(200.0);
        
        //when
        String actual = henry.getStatement();
        
        //then
        assertFalse(actual.isBlank());
        assertTrue(actual.contains("Statement for Henry"));
    }
    
    @Test
    public void testOneAccount() {
        Customer oscar = new Customer("Oscar").openAccount(new Account(AccountType.SAVINGS));
        assertEquals(1, oscar.getNumberOfAccounts());
    }
    
    @Test
    public void testTwoAccount() {
        Customer oscar = new Customer("Oscar")
                .openAccount(new Account(AccountType.SAVINGS));
        oscar.openAccount(new Account(AccountType.CHECKING));
        assertEquals(2, oscar.getNumberOfAccounts());
    }
    
    @Test
    public void testThreeAccounts() {
        Customer oscar = new Customer("Oscar")
                .openAccount(new Account(AccountType.SAVINGS))
                .openAccount(new Account(AccountType.CHECKING))
                .openAccount(new Account(AccountType.CHECKING));
        assertEquals(3, oscar.getNumberOfAccounts());
    }
}
