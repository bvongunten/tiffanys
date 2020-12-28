package ch.nostromo.tiffanys.commons.pieces;

import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.move.Move;

import java.util.List;

public class Rook extends AbstractPiece {

    @Override
    public void addPseudoLegalMoves(Board board, List<Move> moves, int startPos, GameColor colorToMove) {
        addHorizontalMoves(board, moves, startPos, colorToMove);
    }

}
