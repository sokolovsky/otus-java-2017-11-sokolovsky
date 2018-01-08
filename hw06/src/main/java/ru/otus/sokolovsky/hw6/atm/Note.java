package ru.otus.sokolovsky.hw6.atm;

public enum Note implements Comparable<Note> {
    DOZEN (10),
    HALF_OF_HUNDRED (50),
    ONE_HUNDRED (100),
    COUPLE_OF_HUNDRED (200),
    HALF_OF_THOUSAND (500),
    ONE_THOUSAND (1_000),
    COUPLE_OF_THOUSAND (2_000),
    FIVE_THOUSAND (5_000);

    private int amount;

    private Note(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
