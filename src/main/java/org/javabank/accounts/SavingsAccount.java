package org.javabank.accounts;

public class SavingsAccount extends Account {
    public SavingsAccount(String accountId, String pin, double initialBalance) {
        super(accountId, pin, initialBalance);
    }

    @Override
    public void applyAccountSpecificRules() {
        // Reglas específicas para cuentas de ahorro.
    }
}

