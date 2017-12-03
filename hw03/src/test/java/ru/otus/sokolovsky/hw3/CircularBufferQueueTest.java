package ru.otus.sokolovsky.hw3;

import org.junit.Test;
import java.util.Arrays;
import java.util.Iterator;

import static org.junit.Assert.*;

public class CircularBufferQueueTest {

    @Test
    public void warmup() {
        CircularBufferQueue<Integer> queue = new CircularBufferQueue<Integer>(10);
        boolean areEquals = Arrays.equals(queue.toArray(), new Integer[0]);
        assertTrue(areEquals);
    }

    @Test
    public void retrievePoll() {
        CircularBufferQueue<Integer> queue = new CircularBufferQueue<Integer>(10);
        queue.add(1);
        queue.add(2);
        queue.add(3);

        assertEquals(3, queue.size());

        assertEquals(Integer.valueOf(1), queue.poll());
        assertEquals(Integer.valueOf(2), queue.poll());
        assertEquals(Integer.valueOf(3), queue.poll());
        assertEquals(null, queue.poll());
        assertEquals(null, queue.poll());
    }

    @Test
    public void cycleWriting() {
        CircularBufferQueue<Integer> queue = new CircularBufferQueue<Integer>(3);
        queue.add(1);
        queue.add(2);
        queue.add(3);
        queue.add(4);
        queue.add(5);

        assertEquals(Integer.valueOf(3), queue.poll());
        assertEquals(Integer.valueOf(4), queue.poll());
        assertEquals(Integer.valueOf(5), queue.poll());
    }

    @Test
    public void iteratorWalking() {
        CircularBufferQueue<Integer> queue = new CircularBufferQueue<Integer>(3);
        queue.add(1);
        queue.add(2);
        queue.add(3); // 1
        queue.add(4); // 2
        queue.add(5); // 3

        int i = 0;
        for (Integer number: queue) {
            assertEquals(Integer.valueOf(i++ + 3), number);
        }
    }

    @Test
    public void iteratorRemoving() {
        int removeItem = 3;
        CircularBufferQueue<Integer> queue = new CircularBufferQueue<Integer>(5);
        queue.add(1);
        queue.add(2);
        queue.add(3);
        queue.add(4);
        queue.add(5);

        int counter = 0;
        Iterator<Integer> iterator = queue.iterator();
        while (iterator.hasNext()) {
            iterator.next();
            if (counter == removeItem) {
                iterator.remove();
            }
            counter++;
        }
        assertEquals(4, queue.size());
        assertEquals(Integer.valueOf(5), queue.getByIndex(3));
    }

    @Test
    public void removingSimpleQueue() {
        CircularBufferQueue<Integer> queue = new CircularBufferQueue<Integer>(3);
        queue.add(1);
        queue.add(2);
        queue.add(3);

        queue.remove(2);

        assertEquals(2, queue.size());
        assertEquals(Integer.valueOf(1), queue.poll());
        assertEquals(Integer.valueOf(3), queue.poll());
    }

    @Test
    public void removingOverflowedQueue() {
        CircularBufferQueue<Integer> queue = new CircularBufferQueue<Integer>(5);
        queue.add(1);
        queue.add(2);
        queue.add(3);
        queue.add(4);
        queue.add(5);

        queue.poll(); // 1 -> _
        queue.poll(); // 2 -> _
        // {_, _, 3, 4, 5}

        queue.add(6); // {6, _, 3, 4, 5}
        queue.remove(4); // {6, _, _, 3, 5}

        assertEquals(queue.size(), 3);

        assertEquals(Integer.valueOf(3), queue.poll()); // {6, _, _, _, 5}
        assertEquals(Integer.valueOf(5), queue.poll()); // {6, _, _, _, _}
        assertEquals(Integer.valueOf(6), queue.poll()); // {_, _, _, _, _}

        assertEquals(0, queue.size());
    }

    @Test
    public void removingLeftSideOverflowedQueue() {
        CircularBufferQueue<Integer> queue = new CircularBufferQueue<Integer>(7);
        queue.add(1);
        queue.add(2);
        queue.add(3);
        queue.add(4);
        queue.add(5);
        queue.add(6);
        queue.add(7);

        // {1, 2, 3, 4, 5, 6, 7}
        queue.poll();
        queue.poll();
        queue.poll();
        queue.poll();

        // {_, _, _, _, 5, 6, 7}
        queue.add(8);
        queue.add(9);
        queue.add(10);

        // {8, 9, 10, _, 5, 6, 7}
        queue.remove(9);
        // {8, 10, _, _, 5, 6, 7}

        assertEquals(5, queue.size());

        assertEquals(Integer.valueOf(5), queue.poll());
        assertEquals(Integer.valueOf(6), queue.poll());
        assertEquals(Integer.valueOf(7), queue.poll());
        assertEquals(Integer.valueOf(8), queue.poll());
        assertEquals(Integer.valueOf(10), queue.poll());
        assertTrue(queue.isEmpty());
    }
}
