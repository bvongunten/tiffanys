package ch.nostromo.tiffanys.commons.uci.utils;

/**
 * Simple Console writer, to print out line to system out
 */
public class UciConsoleWriter {

    @SuppressWarnings("java:S106")
    public void println(String string) {
        System.out.println(string);
        System.out.flush();
    }

}
