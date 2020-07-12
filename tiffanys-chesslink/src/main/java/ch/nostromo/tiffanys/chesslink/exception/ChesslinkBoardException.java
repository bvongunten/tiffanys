package ch.nostromo.tiffanys.chesslink.exception;

public class ChesslinkBoardException extends RuntimeException {

    public ChesslinkBoardException(String msg) {
        super(msg);
    }

    public ChesslinkBoardException(String msg, Throwable t) {
        super(msg, t);
    }

}
