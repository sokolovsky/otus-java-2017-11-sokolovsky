package ru.otus.sokolovsky.hw4;

public class Utils {
    public static String humanReadableByteCount(long bytes) {
        int threshold = 1024;
        if (bytes < threshold) {
            return bytes + " B";
        }
        int exp = (int) (Math.log(bytes) / Math.log(threshold));
        String pre = "KMGTPE".charAt(exp-1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(threshold, exp), pre);
    }

    public static long getUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
}
