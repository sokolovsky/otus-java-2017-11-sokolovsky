package ru.otus.sokolovsky.hw7.terminal.contexts;

import ru.otus.sokolovsky.hw7.accounting.Account;
import ru.otus.sokolovsky.hw7.atm.Machine;
import ru.otus.sokolovsky.hw7.terminal.Terminal;
import ru.otus.sokolovsky.hw7.terminal.actions.*;

public class AccountContext extends Context {
    private Account account;
    private Machine machine;

    public AccountContext(Terminal terminal, Account account, Machine machine) throws Exception {
        super(terminal);
        this.account = account;
        this.machine = machine;
    }

    @Override
    protected String prompt() {
        return String.format("Session of account `%s`", account.getNumber());
    }

    @Override
    protected Action[] actions() {
        return new Action[] {
            new GetAccountBalance(account),
            new PutMoneyIntoAccount(account, machine),
            new GettingMoneyFromAccount(account, machine),
            new GetAccountHistory(account)
        };
    }
}
