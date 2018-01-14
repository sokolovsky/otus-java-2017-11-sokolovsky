package ru.otus.sokolovsky.hw7;


import ru.otus.sokolovsky.hw7.terminal.contexts.FrontContext;
import ru.otus.sokolovsky.hw7.terminal.Terminal;

import java.io.InputStreamReader;
import java.io.PrintWriter;

public class App {

    public static void main(String[] args) throws Exception {

        Terminal terminal = new Terminal(new PrintWriter(System.out), new InputStreamReader(System.in));

        terminal.writeln("Terminal is switching on ...");

        new FrontContext(terminal).run();
    }
}
