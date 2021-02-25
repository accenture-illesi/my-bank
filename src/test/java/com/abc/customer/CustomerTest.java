package com.abc.customer;

import com.abc.account.Account;
import com.abc.account.AccountType;
import org.junit.Test;

import static org.junit.Assert.*;

public class CustomerTest {
    @Test
    public void testCustomerStatement() {
        //given
        Account checkingAccount = Account.newAccount(AccountType.CHECKING);
        Account savingsAccount = Account.newAccount(AccountType.SAVINGS);
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
        Customer oscar = new Customer("Oscar").openAccount(Account.newAccount(AccountType.SAVINGS));
        assertEquals(1, oscar.getNumberOfAccounts());
    }
    
    @Test
    public void testTwoAccount() {
        Customer oscar = new Customer("Oscar")
                .openAccount(Account.newAccount(AccountType.SAVINGS));
        oscar.openAccount(Account.newAccount(AccountType.CHECKING));
        assertEquals(2, oscar.getNumberOfAccounts());
    }
    
    @Test
    public void testThreeAccounts() {
        Customer oscar = new Customer("Oscar")
                .openAccount(Account.newAccount(AccountType.SAVINGS))
                .openAccount(Account.newAccount(AccountType.CHECKING))
                .openAccount(Account.newAccount(AccountType.CHECKING));
        assertEquals(3, oscar.getNumberOfAccounts());
    }
}
