package ru.otus.sokolovsky.hw16.console.runner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApplicationRunner {

    private String command;
    private Process process;

    private final StringBuffer out = new StringBuffer();

    public ApplicationRunner(String command) {
        this.command = command;
    }

    public void start() throws IOException {
        System.out.println("Directory: " + new java.io.File( "." ).getCanonicalPath());
        ProcessBuilder pb = new ProcessBuilder(command.split(" "));
        pb.redirectErrorStream(true);
        process = pb.start();

        StreamListener output = new StreamListener(process.getInputStream(), "OUTPUT", command);
        output.start();
        System.out.println("Message System was started " + process.info() + "and is alive - " + process.isAlive());
    }

    public void stop() {
        process.destroy();
    }

    private class StreamListener extends Thread {
        private final Logger logger;
        private final InputStream is;
        private final String type;

        private StreamListener(InputStream is, String type, String title) {
            this.is = is;
            this.type = type;
            logger = Logger.getLogger(title);
        }

        @Override
        public void run() {
            logger.info("Start listening another program...");
            try (InputStreamReader isr = new InputStreamReader(is)) {
                BufferedReader br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null) {
                    logger.info(line);
//                    out.append(type).append('>').append(line).append('\n');
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        }
    }
}
