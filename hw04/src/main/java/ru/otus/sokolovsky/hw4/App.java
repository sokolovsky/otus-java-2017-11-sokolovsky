package ru.otus.sokolovsky.hw4;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import static java.lang.management.ManagementFactory.*;

/**
 * 
 params
    * logname - filename of log file for writing current directory
 */
public class App {
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());
        System.out.println("CPU cores count: " + Runtime.getRuntime().availableProcessors());

        String logfile = "default.log";
        if (args.length > 0 && args[0].length() > 0) {
            logfile = args[0];
        }
        PrintStream stream = new PrintStream(new FileOutputStream("./" + logfile));

        GCRunsMeasurer GCReporter = new GCRunsMeasurer(getGarbageCollectorMXBeans(), stream);
        Thread reporterThread = new Thread(GCReporter);
        reporterThread.start();
        long oomTotalMemory;

        LeakedProcess leakedProcess = new LeakedProcess(System.out);
        try {
            leakedProcess.run();
        } catch (OutOfMemoryError e) {
            oomTotalMemory = Runtime.getRuntime().totalMemory();
            leakedProcess.erase();
            String mem = Utils.humanReadableByteCount(oomTotalMemory);

            RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
            long uptimeOfSec = rb.getUptime() / 1000;

            System.out.printf("\nMemory (%s) was run out, time %d sec\n", mem, uptimeOfSec);
            GCReporter.publishCommonMeasure();
            throw e;
        } finally {
            reporterThread.interrupt();
            stream.close();
        }
    }
}
