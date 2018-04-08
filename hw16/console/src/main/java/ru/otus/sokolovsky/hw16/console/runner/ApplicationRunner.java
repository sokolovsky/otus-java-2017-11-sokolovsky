package ru.otus.sokolovsky.hw16.console.runner;

import java.io.IOException;

public class ApplicationRunner {

    private String command;
    private Process process;

    public ApplicationRunner(String command) {
        this.command = command;
    }

    public void start() throws IOException {
        ProcessBuilder pb = new ProcessBuilder(command.split(" "));
        pb.redirectErrorStream(true);
        process = pb.start();
    }

    public void stop() {
        process.destroy();
    }
}
