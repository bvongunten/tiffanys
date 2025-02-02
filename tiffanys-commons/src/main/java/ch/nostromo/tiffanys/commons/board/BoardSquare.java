package ch.nostromo.tiffanys.commons.board;

import ch.nostromo.tiffanys.commons.ChessGameException;
import lombok.Data;

/**
 * Board square
 */
@Data
public class BoardSquare implements Cloneable {

    // Piece if present, may be null
    Piece piece;

    // Clone square
    @Override
    public BoardSquare clone() {
        try {
            return (BoardSquare) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new ChessGameException("Unable to clone BoardSquare");
        }
    }
}
