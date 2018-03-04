package ru.otus.sokolovsky.hw14;

import java.util.Arrays;

public class Utils {
    public static void sort(int[] ar, int offset, int len) {
        if (len <= 1) {
            return;
        }
        int base = ar[offset];
        int[] left = new int[len];
        int leftIndex = -1;

        int[] right = new int[len];
        int rightIndex = -1;

        for (int i = offset + 1; i < offset + len; i++) {
            if (ar[i] < base) {
                leftIndex++;
                left[leftIndex] = ar[i];
            }
            if (ar[i] > base) {
                rightIndex++;
                right[rightIndex] = ar[i];
            }
        }
        // try to without stack overflow
        if (leftIndex >= 0) {
            sort(left, 0, leftIndex +  1);
        }
        if (rightIndex >= 0) {
            sort(right, 0, rightIndex + 1);
        }

        // can use one loop, but too many conditionals
        for (int i = 0; i <= leftIndex; i++) {
            ar[i + offset] = left[i];
        }
        ar[offset + leftIndex + 1] = base;
        for (int i = 0; i <= rightIndex; i++) {
            ar[(offset + leftIndex + 2) + i] = right[i];
        }
    }
}
