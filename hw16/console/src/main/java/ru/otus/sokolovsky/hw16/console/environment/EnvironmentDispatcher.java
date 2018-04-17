package ru.otus.sokolovsky.hw16.console.environment;

import java.util.List;
import java.util.function.Consumer;

public interface EnvironmentDispatcher {
    void runEnvironment();

    void increaseWebService();

    void decreaseWebService();

    void increaseDbService();

    void decreaseDbService();

    void setInfoHandler(Consumer<String> consumer);

    boolean isMessagesSystemRun();

    List<String> getRunDbServices();

    List<String> getRunWebServices();

    void shutdown();
}
