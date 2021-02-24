package com.abc.customer;

import com.abc.Account;
import com.abc.AccountType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class SummaryGeneratorTest {
    SummaryGenerator underTest;
    
    @Before
    public void init() {
        underTest = new SummaryGenerator();
    }
    
    @Test
    public void pluralization() {
        //given
        for (int i = -3; i < 4; i++) {
            //when
            String actual = i + " " + underTest.pluralize(i, "account");
            String expected = i + " account" + (Math.abs(i) == 1 ? "" : "s");
            //then
            Assert.assertEquals(expected, actual);
        }
    }
    
    @Test
    public void summary() {
        //given
        List<Customer> customers = List.of(new Customer("Bobby")
                        .openAccount(new Account(AccountType.CHECKING).deposit(1000)),
                new Customer("Billy")
                        .openAccount(new Account(AccountType.SAVINGS).deposit(2000).withdraw(500))
                        .openAccount(new Account(AccountType.MAXI_SAVINGS).deposit(5000)));
        //when
        String actual = underTest.customerSummary(customers);
        String expected = "Customer Summary" + System.lineSeparator() +
                " - Bobby (1 account)" + System.lineSeparator() +
                " - Billy (2 accounts)";
        //then
        Assert.assertEquals(expected, actual);
    }
}
