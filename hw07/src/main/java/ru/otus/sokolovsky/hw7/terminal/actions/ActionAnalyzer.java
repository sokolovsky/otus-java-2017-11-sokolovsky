package ru.otus.sokolovsky.hw7.terminal.actions;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionAnalyzer {
    public static CommandParser getCommandParser(Action action) throws ActionDefinitionException {
        String formula = action.formula();

        final String[] tokens = formula.split("\\s", 2);
        if (tokens.length < 1) {
            throw new ActionDefinitionException("Action name is empty");
        }

        String name = tokens[0];

        Map<String, String> params = new HashMap<>();
        Pattern pattern = Pattern.compile("<(\\w+):(\\w+)>");
        if (tokens.length > 1) {
            Matcher matcher = pattern.matcher(tokens[1]);
            while (matcher.find()) {
                params.put(matcher.group(2), matcher.group(1));
            }
        }
        return new CommandParser(name, params, action);
    }
}
