package ru.otus.sokolovsky.hw3;

import java.util.*;

public class CircularBufferQueue <E> implements Queue<E> {
    private Object[] internalArray;
    private boolean notOverflowed = false;

    private int size = 0;
    private int head = 0;

    public CircularBufferQueue (int volume, boolean notOverflowed) {
        internalArray = new Object[volume];
        this.notOverflowed = notOverflowed;
    }

    public CircularBufferQueue (int volume) {
        internalArray = new Object[volume];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public E getByIndex(int index) {
        if (index >= size() || index < 0) {
            throw new RuntimeException("Index is not bounded");
        }
        int internalIndex = head + index;
        if (internalIndex >= internalArray.length) {
            internalIndex = internalIndex % internalArray.length;
        }
        return (E) internalArray[internalIndex];
    }

    public int getIndex(E e) {
        for (int i = 0; i < size; i++) {
            if (e == getByIndex(i)) {
                return i;
            }
        }
        return -1;
    }

    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == getByIndex(i)) {
                return true;
            }
        }
        return false;
    }

    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int pointer = 0;
            private int given = -1;
            public boolean hasNext() {
                return size() > pointer;
            }

            public E next() {
                E res = getByIndex(pointer);
                given = pointer;
                pointer++;
                return res;
            }

            public void remove() {
                if (given < 0) {
                    return;
                }
                E e = getByIndex(given);
                CircularBufferQueue.this.remove(e);
                pointer = given;
                given = -1;
            }
        };
    }

    public Object[] toArray() {
        Object[] result = new Object[size()];
        for (int i = 0; i < size(); i++) {
            result[i] = getByIndex(i);
        }
        return result;
    }

    public <T1> T1[] toArray(T1[] a) {
        Object[] array = this.toArray();
        if (a.length < size()) {
            return (T1[]) Arrays.<Object, Object>copyOf(array, size, a.getClass());
        }
        System.arraycopy(array, 0, a, 0, size);
        if (a.length > size())
            a[size] = null;
        return a;
    }

    public boolean add(E e) {
        int volume = internalArray.length;

        if (notOverflowed && size() + 1 > volume) {
            throw new RuntimeException("Volume of queue was oveflowed");
        }

        int position = head + size;

        if (position >= volume) {
            position = position - volume;
            if (position == head) {
                head++;
            }
            if (head >= volume) {
                head = head - volume;
            }
        }
        internalArray[position] = e;

        size++;
        if (size() > volume) {
            size = volume;
        }
        return true;
    }

    public boolean remove(Object o) {
        int index = getIndex((E) o);
        if (index < 0) {
            return false;
        }

        int arrayIndex = head + index;
        if (arrayIndex >= internalArray.length) {
            arrayIndex = arrayIndex - internalArray.length;
        }

        Object[] sourceArray = internalArray;
        if (head + size() <= internalArray.length) {
            // glue right side //  - - 1 2 /3/ 4 -
            System.arraycopy(sourceArray, arrayIndex + 1, internalArray, arrayIndex, size() - index - 1);
        } else if (arrayIndex >=head && arrayIndex < internalArray.length) {
            // glue two sides // 5 6 - - 1 2 /3/ 4
            System.arraycopy(sourceArray, head, internalArray, head + 1, arrayIndex - head);
            head++;
        } else if (arrayIndex < head) {
            // glue two sides // 5 /6/ 7  - - - 1 2 3 4
            System.arraycopy(sourceArray, arrayIndex + 1, internalArray, arrayIndex, size() - index - 1);
        }
        size--;
        // a hole can appear
        return false;
    }

    public boolean containsAll(Collection<?> c) {
        for(Object e: c) {
            if (!contains(e)) {
                return false;
            }
        }
        return true;
    }

    public boolean addAll(Collection<? extends E> c) {
        for(E e: c) {
            add(e);
        }
        return true;
    }

    public boolean removeAll(Collection<?> c) {
        for (Object e: c) {
            if (!contains(e)) {
                continue;
            }
            remove(e);
        }
        return true;
    }

    public boolean retainAll(Collection<?> c) {
        return false;
    }

    public void clear() {
        internalArray = new Object[internalArray.length];
        size = 0;
        head = 0;
    }

    public boolean offer(E e) {
        return add(e);
    }

    public E remove() {
        if (size() == 0) {
            throw new NoSuchElementException();
        }
        return poll();
    }

    public E poll() {
        if (size() == 0) {
            return null;
        }
        E res = (E) internalArray[head];
        head++;

        if (head >= internalArray.length) {
            head = head - internalArray.length;
        }
        size--;

        return res;
    }

    public E element() {
        if (size() == 0) {
            throw new NoSuchElementException();
        }
        return peek();
    }

    public E peek() {
        if (size() == 0) {
            return null;
        }
        return getByIndex(0);
    }
}
