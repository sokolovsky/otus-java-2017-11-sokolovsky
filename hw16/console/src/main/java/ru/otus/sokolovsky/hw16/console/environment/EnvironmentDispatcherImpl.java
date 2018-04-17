package ru.otus.sokolovsky.hw16.console.environment;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

public abstract class EnvironmentDispatcherImpl implements EnvironmentDispatcher {

    private final String msCommand;
    private final String dbCommand;
    private final String webCommand;
    private ProcessRunner msProcess;
    private Queue<ProcessRunner> webProcesses = new LinkedList<>();
    private Queue<ProcessRunner> dbProcesses = new LinkedList<>();
    private Consumer<String> infoHandler = (i) -> {};

    protected abstract ProcessRunner createProcessRunner();

    public EnvironmentDispatcherImpl(String msCommand, String dbCommand, String webCommand) {
        this.msCommand = msCommand;
        this.dbCommand = dbCommand;
        this.webCommand = webCommand;
    }

    @Override
    public void runEnvironment() {
        try {
            runMessageSystem();
            // for warming up
            Thread.sleep(1000);

            increaseDbService();
            // for warming up
            Thread.sleep(1000);

            increaseWebService();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void runMessageSystem() {
        if (msProcess != null) {
            throw new IllegalStateException("Environment has been launched yet.");
        }
        msProcess = createProcessRunner();
        msProcess.setLogger(createLogger("Message System"));
        try {
            msProcess.start(msCommand);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void increaseWebService() {
        ProcessRunner webProcess = createProcessRunner();
        webProcesses.add(webProcess);
        webProcess.setLogger(createLogger("WEB-" + dbProcesses.size()));
        try {
            webProcess.start(webCommand);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Consumer<String> createLogger(String name) {
        return s -> infoHandler.accept(String.format("|%-20s| %s", name, s));
    }

    @Override
    public void decreaseWebService() {
        if (webProcesses.size() == 0) {
            return;
        }
        ProcessRunner processRunner = webProcesses.remove();
        processRunner.stop();
    }

    @Override
    public void increaseDbService() {
        ProcessRunner dbProcess = createProcessRunner();
        dbProcesses.add(dbProcess);
        dbProcess.setLogger(createLogger("DB-" + dbProcesses.size()));
        try {
            dbProcess.start(dbCommand);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void decreaseDbService() {
        if (dbProcesses.size() == 0) {
            return;
        }
        ProcessRunner processRunner = dbProcesses.remove();
        processRunner.stop();
    }

    @Override
    public void setInfoHandler(Consumer<String> consumer) {
        infoHandler = consumer;
    }
}
