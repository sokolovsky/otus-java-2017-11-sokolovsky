package ru.otus.sokolovsky.hw6.terminal.actions;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

public class CommandParser {

    private final String name;
    private final Map<String, String> params;
    private final int countTokens;
    private Action action;

    private final String PARAM_TYPE_STRING = "string";
    private final String PARAM_TYPE_INT = "int";

    public CommandParser(String name, Map<String, String> params, Action action) {
        this.name = name;
        this.params = params;
        this.countTokens = params.size() + 1;
        this.action = action;
    }

    public Action parse(String line) throws Exception {
        if (!match(line)) {
            throw new Exception("Action is not matching for this command");
        }

        if (params.size() == 0) {
            return action;
        }

        String[] tokens = line.split("\\s");
        tokens = Arrays.copyOfRange(tokens, 1, tokens.length);

        if (params.size() == 1) {
            Map.Entry<String, String> entry = params.entrySet().iterator().next();
            String valueType = entry.getValue();
            Object v = getValue(tokens[0], valueType);
            getSetupMethod(entry.getKey()).invoke(action, v);
            return action;
        }

        for (String param :tokens) {
            String name = getParamName(param);
            String valueType = params.get(name);
            Object value = getValue(param, valueType);
            getSetupMethod(name).invoke(action, value);
        }
        return action;
    }

    private Method getSetupMethod(String name) throws NoSuchMethodException {
        String ucName = name.substring(0,1).toUpperCase() + name.substring(1);
        return action
                .getClass()
                .getMethod("set" + ucName, Object.class);
    }

    public boolean match(String line) throws Exception {
        String[] tokens = line.split("\\s");
        if (tokens.length != countTokens) {
            return false;
        }
        if (!name.equals(tokens[0].trim())) {
            return false;
        }

        if (params.size() != tokens.length - 1) {
            return false;
        }
        return true;
    }

    private Object getValue(String token, String type) throws Exception {
        switch (type.toLowerCase()) {
            case PARAM_TYPE_STRING:
                return token;
            case PARAM_TYPE_INT:
                return Integer.parseInt(token);
        }
        throw new Exception("Incompatible type of param was used");
    }

    private String getParamName(String param) {
        String[] strings = param.split("=", 1);
        return strings[0];
    }
}
