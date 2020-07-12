package ch.nostromo.tiffanys.commons.pieces;

import java.util.List;

import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.move.Move;

public class Bishop extends AbstractPiece {

    @Override
    public void addPseudoLegalMoves(Board board, List<Move> moves, int boardIdx, GameColor colorToMove) {
        addDiagonalMoves(board, moves, boardIdx, colorToMove);
    }

}
