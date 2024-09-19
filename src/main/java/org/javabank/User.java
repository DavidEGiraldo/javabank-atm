package org.javabank;

public class User {
    private final String username;
    private final String pin;

    public User(String username, String pin) {
        this.username = username;
        this.pin = pin;
    }

    public String getUsername() {
        return username;
    }

    public String getPin() {
        return pin;
    }
}
