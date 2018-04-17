package ru.otus.sokolovsky.hw16.console.environment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProcessRunner {

    private final StreamListener processListener;
    private Process process;
    private Consumer<String> logger = s -> {};
    private String name;

    public ProcessRunner() {
        processListener = new StreamListener();
    }{}

    public void start(String command) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));

        if (process != null) {
            throw new IllegalStateException("Process has run yet");
        }
        processBuilder.redirectErrorStream(true);
        process = processBuilder.start();

        processListener.setStream(process.getInputStream());
        processListener.start();
    }

    public void stop() {
        process.destroy();
    }

    public void setLogger(Consumer<String> logger) {
        this.logger = logger;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    private class StreamListener extends Thread {
        private InputStream is;

        void setStream(InputStream is) {
            this.is = is;
        }

        @Override
        public void run() {
            if (is == null) {
                throw new IllegalStateException("Need input stream to handle");
            }
            System.out.println("Start interact with program");
            try (InputStreamReader isr = new InputStreamReader(is)) {
                BufferedReader br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null) {
                    logger.accept(line);
                }
            } catch (IOException e) {
                logger.accept(e.getMessage());
            }
        }
    }
}
