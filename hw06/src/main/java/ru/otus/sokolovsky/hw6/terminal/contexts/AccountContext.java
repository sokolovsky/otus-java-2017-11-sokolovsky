package ru.otus.sokolovsky.hw6.terminal.contexts;

import ru.otus.sokolovsky.hw6.accounting.Account;
import ru.otus.sokolovsky.hw6.terminal.Terminal;
import ru.otus.sokolovsky.hw6.terminal.actions.*;

public class AccountContext extends Context {
    private Account account;

    public AccountContext(Terminal terminal, Account account) throws Exception {
        super(terminal);
        this.account = account;
    }

    @Override
    protected String prompt() {
        return String.format("Session of account `%s`", account.getNumber());
    }

    @Override
    protected Action[] actions() {
        return new Action[] {
            new GetAccountBalance(account),
            new PutMoneyIntoAccount(account),
            new GettingMoneyFromAccount(account),
            new GetAccountHistory(account)
        };
    }
}
