package ru.otus.sokolovsky.hw7.terminal.actions;

import ru.otus.sokolovsky.hw7.terminal.Terminal;
import ru.otus.sokolovsky.hw7.terminal.contexts.AdminContext;

public class SwitchToAdminContext implements Action {

    @Override
    public void run(Terminal terminal) {
        try {
            AdminContext context = new AdminContext(terminal);
            context.run();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public String help() {
        return "Switching to admin context for managing";
    }

    @Override
    public String formula() {
        return "admin";
    }
}
