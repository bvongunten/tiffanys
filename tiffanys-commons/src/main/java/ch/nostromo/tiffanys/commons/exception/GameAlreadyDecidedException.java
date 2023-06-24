package ch.nostromo.tiffanys.commons.exception;

import ch.nostromo.tiffanys.commons.ChessGameState;
import lombok.Getter;

/**
 * Game runtime exception
 */
@Getter
public class GameAlreadyDecidedException extends TiffanysException {

   final ChessGameState chessGameState;

    public GameAlreadyDecidedException(String message, ChessGameState chessGameState) {
        super(message);
        this.chessGameState = chessGameState;
    }

}
