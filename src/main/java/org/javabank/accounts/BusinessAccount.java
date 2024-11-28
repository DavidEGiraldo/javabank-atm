package org.javabank.accounts;

public class BusinessAccount extends Account {
    public BusinessAccount(String accountId, String pin, double initialBalance) {
        super(accountId, pin, initialBalance);
    }

    @Override
    public void applyAccountSpecificRules() {
        // Reglas específicas para cuentas empresariales.
    }
}

