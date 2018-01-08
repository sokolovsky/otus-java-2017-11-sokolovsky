package ru.otus.sokolovsky.hw6.terminal.contexts;

import ru.otus.sokolovsky.hw6.terminal.Terminal;
import ru.otus.sokolovsky.hw6.terminal.actions.*;

public class AdminContext extends Context {

    public AdminContext(Terminal terminal) throws Exception {
        super(terminal);
    }

    @Override
    protected String prompt() {
        return "Admin";
    }

    @Override
    protected Action[] actions() {
        return new Action[] {
            new AdminInfoAction(),
            new AdminHistoryAction()
        };
    }
}
