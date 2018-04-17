package ru.otus.sokolovsky.hw16.console.terminal;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Scanner;

public class Terminal {

    private final Writer out;
    private final Reader in;
    private final String clearLine;
    private String prompt = "";
    private String lastPrompt;

    public Terminal(Writer out, Reader in) {
        this.out = out;
        this.in = in;
        this.clearLine = generateClearLine();
    }

    private String generateClearLine() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            builder.append('\b');
        }
        return builder.toString();
    }

    public String getLine() throws IOException {
        return getLine(prompt);
    }

    public String getLine(String prompt) throws IOException {
        lastPrompt = prompt + " /> ";
        out.write(lastPrompt);
        out.flush();
        return readLine();
    }

    private String readLine() {
        Scanner scanner = new Scanner(in);
        return scanner.nextLine();
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public void writeln(String line) {
        try {
            out.write(line);
            out.write("\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logLine(String line) {
        try {
            out.write(clearLine);
            out.write(line);
            out.write("\n");
            out.write(lastPrompt);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
