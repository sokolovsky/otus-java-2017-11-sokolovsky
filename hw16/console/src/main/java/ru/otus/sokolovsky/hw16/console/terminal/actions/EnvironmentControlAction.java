package ru.otus.sokolovsky.hw16.console.terminal.actions;

import ru.otus.sokolovsky.hw16.console.environment.EnvironmentDispatcher;

public abstract class EnvironmentControlAction implements Action {
    private EnvironmentDispatcher envDispatcher;

    public void setEnvironmentDispatcher(EnvironmentDispatcher envDispatcher) {
        this.envDispatcher = envDispatcher;
    }

    public EnvironmentDispatcher getEnvironmentDispatcher() {
        return envDispatcher;
    }
}
