package ch.nostromo.tiffanys.commons.utils;

import ch.nostromo.tiffanys.commons.ChessGameException;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LogFormatterTest {

    @Test
    public void testFormatter() {
        LogRecord record = new LogRecord(Level.INFO, "Hello world");
        Instant instant = Instant.now();

        record.setInstant(instant);

        String result = new LogFormatter().format(record);

        assertEquals(new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS").format(Date.from(instant)) + " - [INFO] Hello world" + System.lineSeparator(), result);
    }

    @Test
    public void testFormatterException() {
        LogRecord record = new LogRecord(Level.INFO, "Hello world");
        record.setThrown(new ChessGameException("Failure"));

        String result = new LogFormatter().format(record);

        assertTrue(result.contains("ch.nostromo.tiffanys.commons.ChessGameException: Failure"));
    }


}
