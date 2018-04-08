package ru.otus.sokolovsky.hw16.console;

import ru.otus.sokolovsky.hw16.console.terminal.Terminal;
import ru.otus.sokolovsky.hw16.console.terminal.contexts.ApplicationContext;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class App {

    final String DB_INSTANCE_NAME = "db";
    final String WEB_INSTANCE_NAME = "web";
    final String MS_INSTANCE_NAME = "ms";

    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = new ApplicationContext(new Terminal(new OutputStreamWriter(System.out), new InputStreamReader(System.in)));
        applicationContext.run();

        // запустить консольное приложение
        // чтение уведомлений в отдельном потоке (от системы сообщений)

        // командная строка в отдельном потоке с возможностью контроля и выдачи программы помощи
        // есть единый объект вывода
    }
}
