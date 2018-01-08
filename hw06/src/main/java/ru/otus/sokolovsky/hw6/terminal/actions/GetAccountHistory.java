package ru.otus.sokolovsky.hw6.terminal.actions;

import ru.otus.sokolovsky.hw6.accounting.Account;
import ru.otus.sokolovsky.hw6.terminal.Terminal;

public class GetAccountHistory implements Action {

    private Account account;

    public GetAccountHistory(Account account) {
        this.account = account;
    }

    @Override
    public void run(Terminal terminal) {
        for (String record : account.history()) {
            terminal.writeln(String.format("Balance of account is - %d", account.getBalance()));
        }
        terminal.writeln("----");
        terminal.writeln(String.format("Total: %d", account.history().size()));
    }

    @Override
    public String help() {
        return "Shows operations history of account";
    }

    @Override
    public String formula() {
        return "history";
    }
}
