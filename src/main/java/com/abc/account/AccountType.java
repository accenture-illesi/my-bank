package com.abc.account;

public enum AccountType {
    CHECKING("Checking Account"),
    SAVINGS("Savings Account"),
    MAXI_SAVINGS("Maxi Savings Account");
    
    private final String name;
    
    AccountType(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
