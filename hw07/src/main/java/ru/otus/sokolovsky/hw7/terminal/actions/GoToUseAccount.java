package ru.otus.sokolovsky.hw7.terminal.actions;

import ru.otus.sokolovsky.hw7.accounting.Account;
import ru.otus.sokolovsky.hw7.accounting.Accounts;
import ru.otus.sokolovsky.hw7.atm.Machine;
import ru.otus.sokolovsky.hw7.terminal.Terminal;
import ru.otus.sokolovsky.hw7.terminal.contexts.AccountContext;

public class GoToUseAccount implements Action {
    private Account account;
    private Machine machine;

    public GoToUseAccount(Machine machine) {
        this.machine = machine;
    }
    public void setNumber(Object number) {
        account = Accounts.get((String) number);
    }

    @Override
    public void execute(Terminal terminal) {
        if (account == null) {
            throw new RuntimeException("Need to use account number");
        }
        try {
            AccountContext context = new AccountContext(terminal, account, machine);
            context.run();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public String help() {
        return "Switching to account context with particular number of account, e.g. account 1024";
    }

    @Override
    public String formula() {
        return "account <String:number>";
    }
}
