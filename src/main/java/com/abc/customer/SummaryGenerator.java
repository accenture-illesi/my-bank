package com.abc.customer;

import java.util.List;

public class SummaryGenerator {
    public String customerSummary(List<Customer> customers) {
        StringBuilder summary = new StringBuilder("Customer Summary");
        for (Customer customer : customers) {
            summary.append(customerSummary(customer));
        }
        return summary.toString();
    }
    
    private StringBuilder customerSummary(Customer customer) {
        int accounts = customer.getNumberOfAccounts();
        return new StringBuilder(System.lineSeparator()).append(" - ").append(customer.getName()).append(" (")
                .append(accounts).append(pluralize(accounts, " account")).append(")");
    }
    
    protected String pluralize(int amount, String word) {
        int i = Math.abs(amount);
        return word + (i == 0 || i > 1 ? "s" : "");
    }
}
