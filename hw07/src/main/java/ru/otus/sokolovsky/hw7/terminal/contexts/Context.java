package ru.otus.sokolovsky.hw7.terminal.contexts;

import ru.otus.sokolovsky.hw7.terminal.actions.ActionDefinitionException;
import ru.otus.sokolovsky.hw7.terminal.Terminal;
import ru.otus.sokolovsky.hw7.terminal.actions.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

abstract class Context {
    private Terminal terminal;
    private List<CommandParser> parsers = new LinkedList<>();

    public Context(Terminal terminal) throws Exception {
        this.terminal = terminal;
    }

    private void initActionParsers() throws Exception {
        LinkedList<Action> actionsList = new LinkedList<>(Arrays.asList(actions()));
        actionsList.add(new QuitAction());
        actionsList.add(new HelpAction(actionsList));

        for (Action action : actionsList) {
            try {
                parsers.add(ActionAnalyzer.getCommandParser(action));
            } catch (ActionDefinitionException e) {
                e.printStackTrace();
                throw new Exception("Wrong definition of context", e);
            }
        }
    }

    public void run() throws Exception {
        initActionParsers();

        Action action = null;
        while (action == null || action.getClass() != QuitAction.class) {
            terminal().setPrompt(prompt());
            String line = terminal().getLine();
            if (line.trim().equals("")) {
                continue;
            }
            action = null;
            for (CommandParser parser : parsers) {
                if (parser.match(line)) {
                    action = parser.parse(line);
                    action.execute(terminal());
                    break;
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
