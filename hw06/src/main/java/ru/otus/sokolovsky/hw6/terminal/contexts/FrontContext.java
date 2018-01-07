package ru.otus.sokolovsky.hw6.terminal.contexts;

import ru.otus.sokolovsky.hw6.terminal.Terminal;
import ru.otus.sokolovsky.hw6.terminal.actions.Action;
import ru.otus.sokolovsky.hw6.terminal.actions.SwitchToAccountContext;

public class FrontContext extends Context {

    public FrontContext(Terminal terminal) throws Exception {
        super(terminal);
    }

    protected String prompt() {
        return "ATM /> ";
    }

    @Override
    protected Action[] actions() {
        return new Action[] {
            new SwitchToAccountContext()
        };
    }
}
