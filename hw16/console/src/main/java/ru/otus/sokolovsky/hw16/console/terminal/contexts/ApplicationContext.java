package ru.otus.sokolovsky.hw16.console.terminal.contexts;

import ru.otus.sokolovsky.hw16.console.terminal.Terminal;
import ru.otus.sokolovsky.hw16.console.terminal.actions.Action;
import ru.otus.sokolovsky.hw16.console.terminal.actions.ServiceUpAction;
import ru.otus.sokolovsky.hw16.console.terminal.actions.IncreaseDbWorkerAction;
import ru.otus.sokolovsky.hw16.console.terminal.actions.IncreaseWebServerAction;

public class ApplicationContext extends Context {

    public ApplicationContext(Terminal terminal) throws Exception {
        super(terminal);
    }

    protected String prompt() {
        return "HW16 console";
    }

    @Override
    protected Action[] actions() {
        return new Action[] {
            new ServiceUpAction(),
            new IncreaseDbWorkerAction(),
            new IncreaseWebServerAction(),
        };
    }
}
