package ru.otus.sokolovsky.hw7.terminal.actions;

import ru.otus.sokolovsky.hw7.accounting.Account;
import ru.otus.sokolovsky.hw7.atm.Machine;
import ru.otus.sokolovsky.hw7.atm.Note;
import ru.otus.sokolovsky.hw7.terminal.Terminal;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PutMoneyIntoAccount implements Action {

    private Account account;
    private Machine machine;
    private static Map<Integer, Note> notesChoice;

    static {
        notesChoice = new HashMap<>();
        int i = 1;
        for (Note note : Note.values()) {
            notesChoice.put(i++, note);
        }
    }

    public PutMoneyIntoAccount(Account account, Machine machine) {
        this.account = account;
        this.machine = machine;
    }

    @Override
    public String help() {
        return "Puts money into account";
    }

    @Override
    public String formula() {
        return "put";
    }

    @Override
    public void execute(Terminal terminal) {
        terminal.writeln("Putting money");
        int credit = 0;
        while (true) {
            terminal.writeln("Chose the next notes:");
            for (Map.Entry<Integer, Note> entry : notesChoice.entrySet()) {
                int n = entry.getKey();
                Note note = entry.getValue();
                terminal.writeln(String.format("%,8d - %d", note.getAmount(), n));
            }
            terminal.writeln("Type `q` for exit");

            NoteBatch noteBatch = null;
            try {
                noteBatch = handleNoteBatch(terminal);
            } catch (WrongInputException e) {
                continue;
            } catch (QuitHandleException e) {
                break;
            }

            machine.putMoney(noteBatch.getNote(), noteBatch.getCount());

            int money = noteBatch.getCount() * noteBatch.getNote().getAmount();
            terminal.writeln(String.format("Put %d", money));
            credit += money;
        }
        if (credit > 0) {
            account.credit(credit);
            terminal.writeln(String.format("Money %d was put into account `%s`", credit, account.getNumber()));
        }
    }

    private NoteBatch handleNoteBatch(Terminal terminal) throws WrongInputException, QuitHandleException {
        int numberOfNote;
        Note note;
        try {
            String line = terminal.getLine("enter the number").trim();
            if (line.equals("q")) {
                throw new QuitHandleException();
            }
            numberOfNote = Integer.parseInt(line);
            note = notesChoice.get(numberOfNote);
            if (note == null) {
                terminal.writeln("Wrong note number");
                throw new WrongInputException();
            }
        } catch (NumberFormatException e) {
            terminal.writeln("Wrong symbols");
            throw new WrongInputException();
        } catch (IOException e) {
            throw new WrongInputException();
        }

        int count = 0;
        try {
            String line = terminal.getLine(String.format("enter count of `%,d` notes", note.getAmount()));
            count = Integer.parseInt(line);
        } catch (NumberFormatException e) {
            terminal.writeln("Wrong count number");
            throw new WrongInputException();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (count == 0) {
            throw new WrongInputException();
        }
        return new NoteBatch(note, count);
    }

    class NoteBatch {
        private final Note note;
        private final int count;

        NoteBatch(Note note, int count) {
            this.note = note;
            this.count = count;
        }

        int getCount() {
            return count;
        }

        Note getNote() {
            return note;
        }
    }

    private class QuitHandleException extends Exception {
    }

    private class WrongInputException extends Exception {
    }
}
