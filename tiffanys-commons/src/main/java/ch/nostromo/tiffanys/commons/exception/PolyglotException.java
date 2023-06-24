package ch.nostromo.tiffanys.commons.exception;

/**
 * Polyglot io / parsing exception
 */
public class PolyglotException extends TiffanysException {

    public PolyglotException(String message) {
        super(message);
    }

    public PolyglotException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
