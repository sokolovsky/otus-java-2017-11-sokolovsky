package ru.otus.sokolovsky.hw7.accounting;

import java.util.HashMap;
import java.util.Map;

public class Accounts {

    private static Map<String, Account> accounts = new HashMap<>();

    public static Account get(String number) {
        Account account = accounts.get(number);
        if (account == null) {
            account = new Account(number, 0);
            accounts.put(number, account);
        }
        return account;
    }
}
