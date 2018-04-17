package ru.otus.sokolovsky.hw16.db.provider;

import org.springframework.core.io.ClassPathResource;
import ru.otus.sokolovsky.hw16.db.myorm.SqlExecutor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Arrays;

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
                System.out.println(String.format("File %s loaded", file));
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
        try {
            ClassPathResource resource = new ClassPathResource(fileName);
            InputStream in = resource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (NoSuchFileException e) {
            System.out.println(e.getReason());
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
