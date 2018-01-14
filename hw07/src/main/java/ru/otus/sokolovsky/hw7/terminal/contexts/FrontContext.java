package ru.otus.sokolovsky.hw7.terminal.contexts;

import ru.otus.sokolovsky.hw7.terminal.Terminal;
import ru.otus.sokolovsky.hw7.terminal.actions.Action;
import ru.otus.sokolovsky.hw7.terminal.actions.SwitchToAccountContext;
import ru.otus.sokolovsky.hw7.terminal.actions.SwitchToAdminContext;

public class FrontContext extends Context {

    public FrontContext(Terminal terminal) throws Exception {
        super(terminal);
    }

    protected String prompt() {
        return "ATM";
    }

    @Override
    protected Action[] actions() {
        return new Action[] {
            new SwitchToAccountContext(),
            new SwitchToAdminContext()
        };
    }
}
