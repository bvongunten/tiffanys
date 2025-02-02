package ch.nostromo.tiffanys.commons.utils;

import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

@UtilityClass
public class LogUtils {

    private static final String ROOT_LOGGER = "ch.nostromo.tiffanys";

    /**
     * Fire up console and file logging
     */
    public static void initializeConsoleAndFileLogging(Level consoleLevel, Level logfileLevel, String logFile) {
        resetLogging();
        addConsoleHandler(consoleLevel);
        addFileHandler(logfileLevel, logFile);
    }

    /**
     * Fire up console logging
     */
    public static void initializeConsoleLogging(Level consoleLevel) {
        resetLogging();
        addConsoleHandler(consoleLevel);
    }

    /**
     * Fire up file logging
     */
    public static void initializeFileLogging(Level logfileLevel, String logFile) {
        resetLogging();
        addFileHandler(logfileLevel, logFile);
    }

    private static void resetLogging() {
        LogManager.getLogManager().reset();
        Logger root = Logger.getLogger(ROOT_LOGGER);
        root.setLevel(Level.FINEST);
        LogManager.getLogManager().addLogger(root);
    }

    private static void addConsoleHandler(Level consoleLevel) {
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(consoleLevel);
        consoleHandler.setFormatter(new LogFormatter());
        LogManager.getLogManager().getLogger("").addHandler(consoleHandler);
    }

    private static void addFileHandler(Level logfileLevel, String logFile) {
        try {
            FileHandler fileHandler = new FileHandler(getLogFile(logFile).getAbsolutePath());
            fileHandler.setFormatter(new LogFormatter());
            fileHandler.setLevel(logfileLevel);
            LogManager.getLogManager().getLogger("").addHandler(fileHandler);
        } catch (IOException e) {
            throw new RuntimeException("Unable to configure logging, because of error " + e.getMessage(), e);
        }
    }

    public static File getLogFile(String logfileName) {
        File logDirectory = new File(Constants.HOME_DIRECTORY);
        if (!logDirectory.exists()) {
            if (!logDirectory.mkdirs()) {
                throw new RuntimeException("Unable to create directory " + logDirectory);
            }
        }

        return new File(logDirectory, logfileName);
    }
}



