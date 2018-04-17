package ru.otus.sokolovsky.hw16.console.environment;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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

    private ProcessRunner runProcess(String command, String name) {
        ProcessRunner processRunner = createProcessRunner();
        processRunner.setName(name);
        processRunner.setLogger(createLogger(processRunner.getName()));
        try {
            processRunner.start(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return processRunner;
    }

    private void stopProcess(ProcessRunner processRunner) {
        processRunner.stop();
    }

    @Override
    public void increaseWebService() {
        ProcessRunner webProcess = runProcess(webCommand, "WEB-" + webProcesses.size());
        webProcesses.add(webProcess);
    }

    private Consumer<String> createLogger(String name) {
        return s -> infoHandler.accept(String.format("|%-20s| %s", name, s));
    }

    @Override
    public void decreaseWebService() {
        if (webProcesses.size() == 0) {
            return;
        }
        stopProcess(webProcesses.remove());
    }

    @Override
    public void increaseDbService() {
        ProcessRunner dbProcess = runProcess(dbCommand, "DB-" + dbProcesses.size());
        dbProcesses.add(dbProcess);
    }

    @Override
    public void decreaseDbService() {
        if (dbProcesses.size() == 0) {
            return;
        }
        stopProcess(dbProcesses.remove());
    }

    @Override
    public void setInfoHandler(Consumer<String> consumer) {
        infoHandler = consumer;
    }

    @Override
    public boolean isMessagesSystemRun() {
        return msProcess != null;
    }

    @Override
    public List<String> getRunDbServices() {
        return dbProcesses.stream().map(ProcessRunner::getName).collect(Collectors.toList());
    }

    @Override
    public List<String> getRunWebServices() {
        return webProcesses.stream().map(ProcessRunner::getName).collect(Collectors.toList());
    }
}
