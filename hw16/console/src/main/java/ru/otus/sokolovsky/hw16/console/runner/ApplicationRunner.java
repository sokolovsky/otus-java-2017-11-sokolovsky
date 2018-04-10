package ru.otus.sokolovsky.hw16.console.runner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApplicationRunner {

    private final StreamListener processListener;
    private Process process;
    private final ProcessBuilder processBuilder;

    public ApplicationRunner(String command) {
        processBuilder = new ProcessBuilder(command.split(" "));
        processListener = new StreamListener(command);
    }

    public void start() throws IOException {
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

    public void afterStart(String event, Runnable f) {
        processListener.addEventCaller(event, f);
    }

    private class StreamListener extends Thread {
        private final Logger logger;
        private InputStream is;
        private Map<String, List<Runnable>> afterStartCalls = new HashMap<>();

        private StreamListener(String title) {
            logger = Logger.getLogger(title);
        }

        void setStream(InputStream is) {
            this.is = is;
        }

        void addEventCaller(String event, Runnable caller) {
            afterStartCalls.putIfAbsent(event, new LinkedList<>());
            afterStartCalls.get(event).add(caller);
        }

        void runCallersIfPresent(String event) {
            List<Runnable> functions = afterStartCalls.get(event);
            if (functions == null) {
                return;
            }
            functions.forEach(Runnable::run);
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
                    logger.info(line);
                    runCallersIfPresent(line);
//                    out.append(type).append('>').append(line).append('\n');
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        }
    }
}
