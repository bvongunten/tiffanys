package ch.nostromo.tiffanys.commons.uci.utils;

/**
 * Exception occured during uci processing
 */
public class UciException extends RuntimeException {

    public UciException(String s) {
        super(s);
    }

    public UciException(String s, Throwable throwable) {
        super(s, throwable);
    }

}
