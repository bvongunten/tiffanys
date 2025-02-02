package ch.nostromo.tiffanys.commons.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.LogRecord;

/**
 * Java Util Logging Formatter (Date / time / Log level / message /  (throwable)
 */
public class LogFormatter extends java.util.logging.Formatter {

    @Override
    public synchronized String format(LogRecord record) {
        StringBuilder result = new StringBuilder();

        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS");

        result.append(dateFormat.format(new Date(record.getMillis()))).append(" - ");

        result.append("[");
        result.append(record.getLevel().getName());
        result.append("] ");

        result.append(formatMessage(record));

        result.append(System.lineSeparator());

        if (record.getThrown() != null) {
            try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
                record.getThrown().printStackTrace(pw);
                result.append(sw);
            } catch (Exception ignored) {
                // Ignored
            }
        }

        return result.toString();
    }
}
