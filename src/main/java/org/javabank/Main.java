package org.javabank;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello from JavaBank ATM! Version control with Git.");

        try {
            int result = 10/0;
        } catch (ArithmeticException e) {
            System.out.println("You can't divide by zero!");
        }
    }
}