package com.abc;

import java.util.ArrayList;
import java.util.List;

public class Bank {
    private final List<Customer> customers;
    
    public Bank() {
        customers = new ArrayList<Customer>();
    }
    
    public void addCustomer(Customer customer) {
        customers.add(customer);
    }
    
    public String customerSummary() {
        String summary = "Customer Summary";
        for (Customer customer : customers) {
            summary += "\n - " + customer.getName() + " (" + pluralizedCount(customer.getNumberOfAccounts(), "account") + ")";
        }
        return summary;
    }
    
    private String pluralizedCount(int number, String word) {
        return number + " " + (number == 1 ? word : word + "s");
    }
    
    public double totalInterestPaid() {
        double total = 0;
        for (Customer customer : customers) {
            total += customer.totalInterestEarned();
        }
        return total;
    }
    
    //TODO update error handling or safe delete
    public String getFirstCustomer() {
        try {
            return customers.get(0).getName();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }
}
