package com.abc.transaction;

public enum TransactionType {
    WITHDRAWAL("withdrawal"), DEPOSIT("deposit");
    final String name;
    
    TransactionType(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
