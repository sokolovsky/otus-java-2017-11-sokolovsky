package ru.otus.sokolovsky.hw16.console.terminal.contexts;

import ru.otus.sokolovsky.hw16.console.terminal.Terminal;
import ru.otus.sokolovsky.hw16.console.terminal.actions.Action;
import ru.otus.sokolovsky.hw16.console.terminal.actions.ServiceUpAction;
import ru.otus.sokolovsky.hw16.console.terminal.actions.IncreaseDbWorkerAction;
import ru.otus.sokolovsky.hw16.console.terminal.actions.IncreaseWebServerAction;

public abstract class ConsoleContext extends Context {

    public ConsoleContext(Terminal terminal) throws Exception {
        super(terminal);
    }

    protected String prompt() {
        return "HW16 console";
    }

    @Override
    protected Action[] actions() {
        return new Action[] {
            createServiceUpAction(),
            createIncreaseDbWorkerAction(),
            createIncreaseWebServerAction(),
        };
    }

    protected abstract ServiceUpAction createServiceUpAction();

    protected abstract IncreaseDbWorkerAction createIncreaseDbWorkerAction();

    protected abstract IncreaseWebServerAction createIncreaseWebServerAction();
}
