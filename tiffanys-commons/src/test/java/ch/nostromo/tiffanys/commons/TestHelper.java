package ch.nostromo.tiffanys.commons;

import ch.nostromo.tiffanys.commons.move.Move;
import org.junit.Before;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;
import java.util.logging.*;

import static org.junit.Assert.fail;

public class TestHelper {

    private Level logLevel = Level.SEVERE;

    protected Logger logger = Logger.getLogger(getClass().getName());

    @Before
    public void setUp() throws Exception {
        LogManager.getLogManager().reset();
        Logger root = Logger.getLogger("ch.nostromo");
        root.setLevel(logLevel);
        LogManager.getLogManager().addLogger(root);

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new LoggingConsoleFormatter());
        consoleHandler.setLevel(logLevel);
        LogManager.getLogManager().getLogger("").addHandler(consoleHandler);
    }

    class LoggingConsoleFormatter extends java.util.logging.Formatter {

        Date dat = new Date();
        private Object args[] = new Object[1];
        private String lineSeparator = System.getProperty("line.separator");

        @Override
        public synchronized String format(LogRecord record) {
            StringBuffer sb = new StringBuffer();
            this.dat.setTime(record.getMillis());
            this.args[0] = this.dat;

            sb.append("[");
            sb.append(record.getLevel().getName());
            sb.append("] ");

            String message = formatMessage(record);

            sb.append(message);

            sb.append(this.lineSeparator);

            if (record.getThrown() != null) {
                try {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    record.getThrown().printStackTrace(pw);
                    pw.close();
                    sb.append(sw.toString());
                } catch (Throwable ignored) {
                    // Ignored
                }
            }
            return sb.toString();
        }
    }

    public void checkAndDeleteMove(List<Move> moves, Move moveToCheck) {
        if (!moves.remove(moveToCheck)) {
            fail("Move : " + moveToCheck + " not found!");
        }
    }

    public void checkRemainingMoves(List<Move> moves) {
        if (moves.size() > 0) {
            for (Move move : moves) {
                logger.severe("Move remaining: " + move);
            }

            fail("Moves remaining: " + moves.size());
        }
    }

}
