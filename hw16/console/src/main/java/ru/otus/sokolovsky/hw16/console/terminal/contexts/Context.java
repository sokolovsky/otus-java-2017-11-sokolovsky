package ru.otus.sokolovsky.hw16.console.terminal.contexts;

import ru.otus.sokolovsky.hw16.console.terminal.Terminal;
import ru.otus.sokolovsky.hw16.console.terminal.actions.*;
import ru.otus.sokolovsky.hw16.console.terminal.actions.ActionDefinitionException;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

abstract class Context {
    private Terminal terminal;
    private List<CommandParser> parsers = new LinkedList<>();

    public Context(Terminal terminal) {
        this.terminal = terminal;
    }

    private void initActionParsers() {
        LinkedList<Action> actionsList = new LinkedList<>(Arrays.asList(actions()));
        actionsList.add(new HelpAction(actionsList));

        for (Action action : actionsList) {
            try {
                parsers.add(ActionAnalyzer.getCommandParser(action));
            } catch (ActionDefinitionException e) {
                e.printStackTrace();
                throw new IllegalStateException("Wrong definition of context", e);
            }
        }
    }

    public void run() {
        initActionParsers();

        Action action = null;
        while (!(action instanceof QuitAction)) {
            terminal().setPrompt(prompt());
            String line;
            try {
                line = terminal().getLine();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            if (line.trim().equals("")) {
                continue;
            }
            action = null;
            for (CommandParser parser : parsers) {
                try {
                    if (parser.match(line)) {
                        action = parser.parse(line);
                        action.execute(terminal());
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            if (action == null) {
                terminal().writeln(String.format("Command `%s` is not supported", line));
            }
        }
    }

    private Terminal terminal() {
        return terminal;
    }

    protected abstract String prompt();
    protected abstract Action[] actions();
}
