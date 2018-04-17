package ru.otus.sokolovsky.hw16.console.terminal.actions;

import ru.otus.sokolovsky.hw16.console.environment.EnvironmentDispatcher;
import ru.otus.sokolovsky.hw16.console.terminal.Terminal;

import java.util.List;

public class EnvironmentStateAction extends EnvironmentControlAction {
    @Override
    public void execute(Terminal terminal) {
        EnvironmentDispatcher dispatcher = getEnvironmentDispatcher();
        if (!dispatcher.isMessagesSystemRun()) {
            terminal.writeln("Message System: not active");
            return;
        }

        terminal.writeln("Message System: has been running");

        List<String> runWebServices = dispatcher.getRunWebServices();
        terminal.writeln(String.format("Web: %s (%d)", String.join(", ", runWebServices), runWebServices.size()));

        List<String> runDbServices = dispatcher.getRunDbServices();
        terminal.writeln(String.format("Db: %s (%d)", String.join(", ", runDbServices), runDbServices.size()));
    }

    @Override
    public String help() {
        return "Shows state of environment run instances";
    }

    @Override
    public String formula() {
        return "state";
    }
}
