package com.judgmentlabs.judgeval.utils;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicBoolean;

public class Logger {
    private static final String RESET = "\033[0m";
    private static final String RED = "\033[31m";
    private static final String YELLOW = "\033[33m";
    private static final String BLUE = "\033[34m";
    private static final String GRAY = "\033[90m";
    private static final String GREEN = "\033[32m";

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public enum Level {
        DEBUG(0, GRAY),
        INFO(1, GRAY),
        WARNING(2, YELLOW),
        ERROR(3, RED),
        CRITICAL(4, RED);

        private final int value;
        private final String color;

        Level(int value, String color) {
            this.value = value;
            this.color = color;
        }

        public int getValue() {
            return value;
        }

        public String getColor() {
            return color;
        }
    }

    private static final AtomicBoolean initialized = new AtomicBoolean(false);
    private static Level currentLevel = Level.INFO;
    private static boolean useColor = true;
    private static PrintStream output = System.out;

    private static void initialize() {
        if (initialized.compareAndSet(false, true)) {
            String noColor = System.getenv("JUDGMENT_NO_COLOR");
            useColor = noColor == null && System.console() != null;
        }
    }

    public static void setLevel(Level level) {
        currentLevel = level;
    }

    public static void setUseColor(boolean useColor) {
        Logger.useColor = useColor;
    }

    public static void setOutput(PrintStream output) {
        Logger.output = output;
    }

    private static void log(Level level, String message) {
        initialize();

        if (level.getValue() < currentLevel.getValue()) {
            return;
        }

        String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
        String formattedMessage =
                String.format("%s - judgeval - %s - %s", timestamp, level.name(), message);

        if (useColor) {
            formattedMessage = level.getColor() + formattedMessage + RESET;
        }

        output.println(formattedMessage);
    }

    public static void debug(String message) {
        log(Level.DEBUG, message);
    }

    public static void info(String message) {
        log(Level.INFO, message);
    }

    public static void warning(String message) {
        log(Level.WARNING, message);
    }

    public static void error(String message) {
        log(Level.ERROR, message);
    }

    public static void critical(String message) {
        log(Level.CRITICAL, message);
    }

    public static void success(String message) {
        String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
        String formattedMessage = String.format("%s - judgeval - SUCCESS - %s", timestamp, message);

        if (useColor) {
            formattedMessage = GREEN + formattedMessage + RESET;
        }

        output.println(formattedMessage);
    }
}
