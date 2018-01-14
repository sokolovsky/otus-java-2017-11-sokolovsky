package ru.otus.sokolovsky.hw7.atm;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public class Machine {
    private List<String> history;
    private SortedMap<Note, Integer> cells;

    private int amount = 0;

    public Machine(State state) {
        setState(state);
    }

    public Machine() {
        reset();
    }

    public Machine setState(State state) {
        Objects.requireNonNull(state);
        reset();
        for (Map.Entry<Note, Integer> entry : state.getCells().entrySet()) {
            putMoney(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public State createState() {
        State state = new State();
        state.setCells(Collections.unmodifiableMap(cells));
        return state;
    }

    private void reset() {
        cells = new TreeMap<>((n1, n2) -> (n1.getAmount() - n2.getAmount()) * -1);
        history = new LinkedList<>();
        amount = 0;
    }

    public void putMoney(Note note, int noteCount) {
        putInCell(note, noteCount);
        amount += noteCount * note.getAmount();
        writeToHistory("putting", noteCount);
    }

    private synchronized boolean prepareNotesSet(int sum, Map<Note, Integer> container) {
        container.clear();
        if (amount < sum) {
            return false;
        }

        int remain = sum;
        for (Map.Entry<Note, Integer> entry : cells.entrySet()) {
            Note note = entry.getKey();
            int satisfiedCount = remain / note.getAmount();
            int cellCount = entry.getValue();
            if (satisfiedCount > cellCount) {
                satisfiedCount = cellCount;
            }
            if (satisfiedCount == 0) {
                continue;
            }
            remain -= satisfiedCount * note.getAmount();
            container.put(note, satisfiedCount);
            if (remain == 0) {
                break;
            }
        }
        boolean res = remain == 0;
        if (!res) {
            container.clear();
        }
        return res;
    }

    public boolean canGetSum(int sum) {
        return prepareNotesSet(sum, new HashMap<>());
    }

    public Map<Note, Integer> getMoney(int amount) throws Exception {
        if (this.amount < amount) {
            throw new Exception("Not enough money in ATM");
        }

        Map<Note, Integer> result = new TreeMap<>();
        if (!prepareNotesSet(amount, result)) {
            throw new Exception(String.format("Getting money with sum `%,d` is impossible", amount));
        }
        this.amount -= amount;
        for (Map.Entry<Note, Integer> entry : result.entrySet()) {
            int remain = cells.get(entry.getKey()) - entry.getValue();
            cells.put(entry.getKey(), remain);
        }

        writeToHistory("taking", amount);
        return result;

    }

    private void writeToHistory(String operation, int sum) {
        history.add(String.format(
                "%tD %s of %,d",
                LocalDateTime.now(),
                operation,
                sum
            )
        );
    }

    private void putInCell(Note note, int noteCount) {
        cells.putIfAbsent(note, 0);
        int count = cells.get(note) + noteCount;
        cells.put(note, count);
    }

    public List<String> history() {
        return history;
    }

    public int getAmount() {
        return amount;
    }

    public Map<Note, Integer> getCellsInfo() {
        return Collections.unmodifiableMap(cells);
    }

    public Note getMinNote() {
        return cells.keySet().stream()
                .filter((Note n) -> cells.get(n) > 0)
                .min(Comparator.comparingInt(Note::getAmount))
                .orElse(null);
    }

    public class State implements Serializable {
        private Map<Note, Integer> cells;

        void setCells(Map<Note, Integer> cells) {
            this.cells = cells;
        }

        Map<Note, Integer> getCells() {
            return cells;
        }
    }
}
