package ru.otus.sokolovsky.hw2.execution;

public interface MeasureCase {

    /**
     * Should create data with strong link.
     * For example it could be field of object
     */
    void createData();

    /**
     * Should return description of case.
     */
    String description();

    /**
     * Should return count of created records
     */
    default int count() {
        return 1;
    }
}
