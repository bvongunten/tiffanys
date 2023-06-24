package ch.nostromo.tiffanys.commons.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Board square
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardSquare {

    // Piece if present, may be null
    Piece piece;

    public BoardSquare copy() {
        return new BoardSquare(piece);
    }

}
