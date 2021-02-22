package com.abc;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Customer {
    private static final DecimalFormat FORMAT = new DecimalFormat("¤#,##0.00", DecimalFormatSymbols.getInstance(Locale.US));
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
    
    public double totalInterestEarned() {
        double total = 0;
        for (Account account : accounts) {
            total += account.interestEarned();
        }
        return total;
    }
    
    public String getStatement() {
        StringBuilder statement = new StringBuilder("Statement for ").append(name).append("\n");
        BigDecimal total = BigDecimal.ZERO;
        for (Account account : accounts) {
            statement.append("\n").append(statementForAccount(account)).append("\n");
            total = total.add(account.sumTransactions());
        }
        statement.append("\n").append("Total In All Accounts ").append(toAbsDollars(total));
        return statement.toString();
    }
    
    private String statementForAccount(Account account) {
        StringBuilder statement = new StringBuilder(account.getAccountType().toString()).append("\n");
        BigDecimal total = BigDecimal.ZERO;
        for (Transaction transaction : account.getTransactions()) {
            statement.append("  ").append(transaction.getType())
                    .append(" ").append(toAbsDollars(transaction.getAmount()))
                    .append("\n");
            total = total.add(transaction.getAmount());
        }
        statement.append("Total ").append(toAbsDollars(total));
        return statement.toString();
    }
    
    private String toAbsDollars(BigDecimal amount) {
        return FORMAT.format(amount.abs());
    }
}
