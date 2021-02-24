package com.abc.customer;

import com.abc.Account;
import com.abc.transaction.Transaction;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class StatementGenerator {
    private static final DecimalFormat FORMAT = new DecimalFormat("¤#,##0.00", DecimalFormatSymbols.getInstance(Locale.US));
    
    public String getStatement(Customer customer) {
        StringBuilder statement = new StringBuilder("Statement for ").append(customer.getName()).append(System.lineSeparator());
        BigDecimal total = BigDecimal.ZERO;
        for (Account account : customer.getAccounts()) {
            statement.append(System.lineSeparator()).append(statementForAccount(account)).append(System.lineSeparator());
            total = total.add(account.sumTransactions());
        }
        statement.append(System.lineSeparator()).append("Total In All Accounts ").append(toAbsDollars(total));
        return statement.toString();
    }
    
    private String statementForAccount(Account account) {
        StringBuilder statement = new StringBuilder(account.getAccountType().toString()).append(System.lineSeparator());
        BigDecimal total = BigDecimal.ZERO;
        for (Transaction transaction : account.getTransactions()) {
            statement.append("  ").append(transaction.getType())
                    .append(" ").append(toAbsDollars(transaction.getAmount()))
                    .append(System.lineSeparator());
            total = total.add(transaction.getAmount());
        }
        statement.append("Total ").append(toAbsDollars(total));
        return statement.toString();
    }
    
    private String toAbsDollars(BigDecimal amount) {
        return FORMAT.format(amount.abs());
    }
}
