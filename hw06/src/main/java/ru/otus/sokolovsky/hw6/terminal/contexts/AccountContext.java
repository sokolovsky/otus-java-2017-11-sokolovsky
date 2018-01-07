package ru.otus.sokolovsky.hw6.terminal.contexts;

import ru.otus.sokolovsky.hw6.accounting.Account;
import ru.otus.sokolovsky.hw6.terminal.Terminal;
import ru.otus.sokolovsky.hw6.terminal.actions.Action;
import ru.otus.sokolovsky.hw6.terminal.actions.GetAccountBalance;

public class AccountContext extends Context {
    private Account account;

    public AccountContext(Terminal terminal, Account account) throws Exception {
        super(terminal);
        this.account = account;
    }

    @Override
    protected String prompt() {
        return String.format("Session %s account /> ", account.getNumber());
    }

    @Override
    protected Action[] actions() {
        return new Action[] {
            new GetAccountBalance(account)
        };
    }
}
