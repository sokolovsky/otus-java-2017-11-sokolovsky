package ru.otus.sokolovsky.hw7.terminal.actions;

import ru.otus.sokolovsky.hw7.accounting.Account;
import ru.otus.sokolovsky.hw7.terminal.Terminal;

public class GetAccountHistory implements Action {

    private Account account;

    public GetAccountHistory(Account account) {
        this.account = account;
    }

    @Override
    public void execute(Terminal terminal) {
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
