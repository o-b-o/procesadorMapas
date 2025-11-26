package com.proyecto;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private static final String LOG_FILE = "analizador.log";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private Logger() {}

    /**
     * Logs a message to the console and to a persistent log file.
     * This method is synchronized to be thread-safe.
     *
     * @param level   The severity level of the message (INFO, WARN, ERROR).
     * @param message The message to be logged.
     */
    public static synchronized void log(LogLevel level, String message) {
        String timestamp = dateFormat.format(new Date());
        String threadName = Thread.currentThread().getName();
        String logEntry = String.format("[%s] [%s] [%s]: %s", timestamp, threadName, level, message);

        // Always print to the console
        if (level == LogLevel.ERROR) {
            System.err.println(logEntry);
        } else {
            System.out.println(logEntry);
        }

        // Write to the log file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(logEntry);
            writer.newLine();
        } catch (IOException e) {
            // If logging fails, we're in big trouble.
            // Print the logging failure to the console.
            System.err.println("CRITICAL: Failed to write to log file!");
            e.printStackTrace();
        }
    }
}
