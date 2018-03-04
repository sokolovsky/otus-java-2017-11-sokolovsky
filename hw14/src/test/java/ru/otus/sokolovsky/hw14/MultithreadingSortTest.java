package ru.otus.sokolovsky.hw14;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

class MultithreadingSortTest {

    @Test
    void shouldSortWithinOneThread() {
        MultithreadingSort sort = new MultithreadingSort(1);
        int[] ints = {4, 5, 6, 2, 3, 1};
        sort.execute(ints);
        assertThat(ints, equalTo(new int[]{1,2,3,4,5,6}));
    }

    @Test
    void shouldSortWithinMultipleTreads() {
        MultithreadingSort sort = new MultithreadingSort(4);
        int[] ints = {4, 5, 6, 2, 3, 1};
        sort.execute(ints);
        assertThat(ints, equalTo(new int[]{1,2,3,4,5,6}));
    }

    @Test
    void shouldProcessBigArray() {
        int[] ints = IntStream.rangeClosed(0, 1000_000).toArray();
        shuffleArray(ints);

        MultithreadingSort sort = new MultithreadingSort(1);
        sort.execute(ints);

        for (int i = 0; i <= 1000_000; i++) {
            assertThat(ints[i], is(i));
        }
    }

    private static void shuffleArray(int[] array) {
        int index, temp;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }
}
