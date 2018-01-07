package ru.otus.sokolovsky.hw6.accounting;

public class Account {
    private String number;
    private int amount;

    Account(String number, int amount) {
        this.number = number;
        this.amount = amount;
    }

    public void credit(int amount) {
        this.amount += amount;
    }

    public void withdraw(int amount) throws WrongOperationException {
        if (amount > this.amount) {
            throw new WrongOperationException();
        }
        this.amount -= amount;
    }

    public int getBalance() {
        return amount;
    }

    public String getNumber() {
        return number;
    }
}

