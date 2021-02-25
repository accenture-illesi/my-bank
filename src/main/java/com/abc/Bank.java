package com.abc;

import com.abc.account.Account;
import com.abc.customer.Customer;
import com.abc.customer.SummaryGenerator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Bank {
    private static final SummaryGenerator SUMMARY_GENERATOR = new SummaryGenerator();
    private final List<Customer> customers;
    
    public Bank() {
        customers = new ArrayList<>();
    }
    
    public void addCustomer(Customer customer) {
        customers.add(customer);
    }
    
    public String customerSummary() {
        return SUMMARY_GENERATOR.customerSummary(customers);
    }
    
    public BigDecimal totalInterestPaid() {
        BigDecimal total = BigDecimal.ZERO;
        for (Customer customer : customers) {
            total = total.add(customer.totalInterestEarned());
        }
        return total;
    }
    
    public void transfer(Account from, Account to, double amount) {
        transfer(from, to, BigDecimal.valueOf(amount));
    }
    
    public void transfer(Account from, Account to, BigDecimal amount) {
        // TODO check permissions (authentication & authorization)
        try {
            from.withdraw(amount);
            to.deposit(amount);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to transfer between accounts", e);
        }
    }
    
    /**
     * Applies daily interest to each account.
     *
     * @apiNote This method should be called from the main application at the beginning of each day, to correctly apply interests.<br/>
     * If the interest is not calculated at the correct time, it may give different results.<br/>
     * (e.g. if the interest rate is based on expiring transactions)
     */
    public void accrueInterest() {
        customers.stream().map(Customer::getAccounts).flatMap(Collection::stream)
                .forEach(Account::depositDailyInterest);
    }
    
    public List<Customer> getCustomers() {
        return customers;
    }
}
