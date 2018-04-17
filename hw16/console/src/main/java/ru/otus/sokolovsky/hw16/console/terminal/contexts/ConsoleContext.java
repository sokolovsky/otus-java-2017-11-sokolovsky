package ru.otus.sokolovsky.hw16.console.terminal.contexts;

import ru.otus.sokolovsky.hw16.console.terminal.Terminal;
import ru.otus.sokolovsky.hw16.console.terminal.actions.*;

public abstract class ConsoleContext extends Context {

    public ConsoleContext(Terminal terminal) {
        super(terminal);
    }

    protected String prompt() {
        return "HW16 console";
    }

    @Override
    protected Action[] actions() {
        return new Action[] {
            createStateAction(),
            createServiceUpAction(),
            createIncreaseDbWorkerAction(),
            createDecreaseDbServerAction(),
            createIncreaseWebServerAction(),
            createDecreaseWebServerAction()
        };
    }

    protected abstract ServiceUpAction createServiceUpAction();

    protected abstract IncreaseDbWorkerAction createIncreaseDbWorkerAction();

    protected abstract IncreaseWebServerAction createIncreaseWebServerAction();

    public abstract EnvironmentStateAction createStateAction();

    public abstract DecreaseDbWorkerAction createDecreaseDbServerAction();

    public abstract EnvironmentControlAction createDecreaseWebServerAction() ;
}
