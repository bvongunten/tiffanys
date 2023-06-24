package ch.nostromo.tiffanys.commons.exception;

/**
 * Game runtime exception
 */
public class TiffanysException extends RuntimeException {

    public TiffanysException(String message) {
        super(message);
    }

    public TiffanysException(String message, Throwable cause) {
        super(message, cause);
    }


}
