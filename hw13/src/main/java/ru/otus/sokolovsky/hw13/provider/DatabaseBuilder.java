package ru.otus.sokolovsky.hw13.provider;

import ru.otus.sokolovsky.hw13.myorm.SqlExecutor;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class DatabaseBuilder {

    private String[] createSqlFiles;
    private String[] dropSqlFiles;
    private SqlExecutor executor;

    public DatabaseBuilder(String createSqlFiles, String dropSqlFiles, SqlExecutor executor) {
        this.createSqlFiles = Arrays.stream(createSqlFiles.split(",")).map(String::trim).toArray(String[]::new);
        this.dropSqlFiles = Arrays.stream(dropSqlFiles.split(",")).map(String::trim).toArray(String[]::new);
        this.executor = executor;
    }

    public void createTables() {
        Arrays.stream(createSqlFiles).forEach((file) -> {
            try {
                executor.execUpdate(getFileContent(file));
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage() + " " + file);
            }
        });
    }

    public void dropTables() {
        Arrays.stream(dropSqlFiles).forEach((file) -> {
            try {
                executor.execUpdate(getFileContent(file));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String getFileContent(String fileName) {
        StringBuilder result = new StringBuilder("");
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(fileName)).getFile());

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }
            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }
}
