package ru.otus.sokolovsky.hw14;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

class UtilsTest {

    @Test
    void shouldSortSliceOfArray() {
        int[] ints = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        Utils.sort(ints, 3, 3);
        assertThat(ints, equalTo(new int[]{10, 9, 8, 5, 6, 7, 4, 3, 2, 1}));
    }
    @Test
    void shouldSortFullArray() {
        int[] ints = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        Utils.sort(ints, 0, ints.length);
        assertThat(ints, equalTo(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}));
    }

    @Test
    void shouldSortShuffledArray() {
        int[] ints = {6, 5, 10, 9, 8, 7, 4, 3, 2, 1};
        Utils.sort(ints, 0, ints.length);
        assertThat(ints, equalTo(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}));
    }
}