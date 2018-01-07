package ru.otus.sokolovsky.hw6;


import ru.otus.sokolovsky.hw6.terminal.contexts.FrontContext;
import ru.otus.sokolovsky.hw6.terminal.Terminal;

import java.io.InputStreamReader;
import java.io.PrintWriter;

public class App {

    public static void main(String[] args) throws Exception {

        Terminal terminal = new Terminal(new PrintWriter(System.out), new InputStreamReader(System.in));

        terminal.writeln("Terminal is starting...");

        new FrontContext(terminal).run();
    }
}