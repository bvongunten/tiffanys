package ch.nostromo.tiffanys.lichess.exception;

public class LichessException extends RuntimeException {

    public LichessException(String msg) {
        super(msg);
    }

    public LichessException(String msg, Throwable t) {
        super(msg, t);
    }

}
