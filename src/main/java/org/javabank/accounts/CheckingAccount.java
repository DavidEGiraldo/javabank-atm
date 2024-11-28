package org.javabank.accounts;

public class CheckingAccount extends Account {
    public CheckingAccount(String accountId, String pin, double initialBalance) {
        super(accountId, pin, initialBalance);
    }

    @Override
    public void applyAccountSpecificRules() {
        // Reglas específicas para cuentas corrientes.
    }
}
