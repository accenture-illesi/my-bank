package com.abc.customer;

import com.abc.Account;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Customer {
    private static final StatementGenerator STATEMENT_GENERATOR = new StatementGenerator();
    private final String name;
    private final List<Account> accounts;
    
    public Customer(String name) {
        this.name = name;
        this.accounts = new ArrayList<>();
    }
    
    public String getName() {
        return name;
    }
    
    public Customer openAccount(Account account) {
        accounts.add(account);
        return this;
    }
    
    public int getNumberOfAccounts() {
        return accounts.size();
    }
    
    public BigDecimal totalInterestEarned() {
        BigDecimal total = BigDecimal.ZERO;
        for (Account account : accounts) {
            total = total.add(account.interestEarned());
        }
        return total;
    }
    
    public String getStatement() {
        return STATEMENT_GENERATOR.getStatement(this);
    }
    
    public List<Account> getAccounts() {
        return accounts;
    }
}
