package ch.nostromo.tiffanys.uci.utils.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.LogRecord;

public class LogFormatter extends java.util.logging.Formatter {
    private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
    
    Date dat = new Date();
    private Object[] args = new Object[1];
    private String lineSeparator = System.getProperty("line.separator");

    @Override
    public synchronized String format(LogRecord record) {
    	StringBuilder sb = new StringBuilder();
        this.dat.setTime(record.getMillis());
        this.args[0] = this.dat;
        
        sb.append(dateFormat.format(new Date(record.getMillis()))).append(" - ");
        
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
            } catch (Exception ignored) {
                // Ignored
            }
        }
        return sb.toString();
    }
}
