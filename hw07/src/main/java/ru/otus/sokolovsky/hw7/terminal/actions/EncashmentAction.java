package ru.otus.sokolovsky.hw7.terminal.actions;

import ru.otus.sokolovsky.hw7.events.Dispatcher;
import ru.otus.sokolovsky.hw7.events.Event;
import ru.otus.sokolovsky.hw7.terminal.Terminal;

public class EncashmentAction implements Action {
    @Override
    public void execute(Terminal terminal) {
        terminal.writeln("Clear starting...");
        Dispatcher.getInstance().trigger(new Event("encashment"));
        terminal.writeln("Done");
    }

    @Override
    public String help() {
        return "Clears states of everything machines";
    }

    @Override
    public String formula() {
        return "refresh";
    }
}
