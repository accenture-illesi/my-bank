package com.abc.customer;

import com.abc.Account;
import com.abc.AccountType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StatementGeneratorTest {
    public static final String EXPECTED = "Statement for Henry" + System.lineSeparator() +
            System.lineSeparator() +
            "Checking Account" + System.lineSeparator() +
            "  deposit $100.00" + System.lineSeparator() +
            "Total $100.00" + System.lineSeparator() +
            System.lineSeparator() +
            "Savings Account" + System.lineSeparator() +
            "  deposit $4,000.00" + System.lineSeparator() +
            "  withdrawal $200.00" + System.lineSeparator() +
            "Total $3,800.00" + System.lineSeparator() +
            System.lineSeparator() +
            "Total In All Accounts $3,900.00";
    private StatementGenerator underTest;
    
    @Before
    public void init() {
        underTest = new StatementGenerator();
    }
    
    @Test
    public void testCustomerStatement() {
        //given
        Customer henry = givenCustomer();
        //when
        String actual = underTest.getStatement(henry);
        //then
        assertEquals(EXPECTED, actual);
    }
    
    private Customer givenCustomer() {
        Account checkingAccount = new Account(AccountType.CHECKING);
        Account savingsAccount = new Account(AccountType.SAVINGS);
        Customer henry = new Customer("Henry").openAccount(checkingAccount).openAccount(savingsAccount);
        checkingAccount.deposit(100.0);
        savingsAccount.deposit(4000.0);
        savingsAccount.withdraw(200.0);
        return henry;
    }
}
