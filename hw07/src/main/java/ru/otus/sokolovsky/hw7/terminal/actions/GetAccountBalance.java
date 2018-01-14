package ru.otus.sokolovsky.hw7.terminal.actions;

import ru.otus.sokolovsky.hw7.accounting.Account;
import ru.otus.sokolovsky.hw7.terminal.Terminal;

public class GetAccountBalance implements Action {

    private Account account;

    public GetAccountBalance(Account account) {
        this.account = account;
    }

    @Override
    public void execute(Terminal terminal) {
        terminal.writeln(String.format("Balance of account is - %d", account.getBalance()));
    }

    @Override
    public String help() {
        return "Shows balance of account number";
    }

    @Override
    public String formula() {
        return "balance";
    }
}
