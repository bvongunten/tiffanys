package ch.nostromo.tiffanys.commons.logging;

import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

@UtilityClass
public class LogUtils {

    public static void initializeLogging(Level consoleLevel, Level logfileLevel, String logDirectory, String logFile, String rootLogger) {

        try {
            LogManager.getLogManager().reset();
            Logger root = Logger.getLogger(rootLogger);
            root.setLevel(Level.FINEST);
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
        } catch (Exception e) {
            throw new RuntimeException("Unable to configure logging, because of error " + e.getMessage(), e);
        }
    }

    public static void initializeLogging(Level consoleLevel, Level logfileLevel, String logDirectory, String logFile) {
        initializeLogging(consoleLevel, logfileLevel, logDirectory, logFile, "ch.nostromo.tiffanys");
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



