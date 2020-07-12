package ch.nostromo.tiffanys.commons.pieces;

import java.util.List;

import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.enums.Direction;
import ch.nostromo.tiffanys.commons.move.Move;

public class Knight extends AbstractPiece {

    @Override
    public void addPseudoLegalMoves(Board board, List<Move> moves, int startPos, GameColor colorToMove) {

        for (Direction direction : Direction.getKnightDirections()) {
            checkAndAddPossibleFreeOrHitMove(board, moves, startPos, startPos + direction.getValue(), colorToMove);
        }
    }
}
