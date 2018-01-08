package ru.otus.sokolovsky.hw6.accounting;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class Account {
    private String number;
    private int amount;
    private List<String> history = new LinkedList<>();

    Account(String number, int amount) {
        this.number = number;
        this.amount = amount;
    }

    public List<String> history() {
        return history;
    }

    public void writeToHistory(String operation, int amount) {
        history.add(String.format("%tD / %s / %,d", LocalDateTime.now(), operation, amount));
    }

    public void withdraw(int amount) throws WrongOperationException {
        if (amount > this.amount) {
            throw new WrongOperationException();
        }
        this.amount -= amount;
        writeToHistory("withdraw", amount);
    }

    public void credit(int amount) {
        this.amount += amount;
        writeToHistory("credit", amount);
    }

    public int getBalance() {
        return amount;
    }

    public String getNumber() {
        return number;
    }
}

