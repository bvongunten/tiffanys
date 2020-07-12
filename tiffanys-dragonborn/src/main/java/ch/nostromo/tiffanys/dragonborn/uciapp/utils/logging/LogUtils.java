package ch.nostromo.tiffanys.dragonborn.uciapp.utils.logging;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

public class LogUtils {

    public static void initializeLogging(Level consoleLevel, Level logfileLevel, String logDirectory, String logFile)
            throws SecurityException, IOException {

        LogManager.getLogManager().reset();
        Logger root = Logger.getLogger("ch.nostromo.tiffanys");
        root.setLevel(Level.ALL);
        LogManager.getLogManager().addLogger(root);

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(consoleLevel);
        consoleHandler.setFormatter(new LogFormatter());
        LogManager.getLogManager().getLogger("").addHandler(consoleHandler);

        if (logFile != null) {
            FileHandler fileHandler = new FileHandler(getLogFile(logDirectory, logFile).getAbsolutePath());
            fileHandler.setFormatter(new LogFormatter());
            fileHandler.setLevel(logfileLevel);
            LogManager.getLogManager().getLogger("").addHandler(fileHandler);
        }
    }

    public static File getLogDirectory(String logDirectory) {
        File result = new File(logDirectory);
        result.mkdirs();
        return result;
    }

    public static File getLogFile(String logDirectory, String logfileName) {
        return new File(getLogDirectory(logDirectory), logfileName);
    }
}



