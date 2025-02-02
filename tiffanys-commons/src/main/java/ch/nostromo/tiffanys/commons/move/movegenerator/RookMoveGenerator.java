package ch.nostromo.tiffanys.commons.move.movegenerator;

import ch.nostromo.tiffanys.commons.Side;
import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.move.Move;

import java.util.List;

/**
 * Rook move generator
 */
public class RookMoveGenerator extends AbstractMoveGenerator {

    @Override
    public List<Move> getPseudoLegalMoves(Board board, int startPos, Side sideToMove) {
        return getOrthogonalMoves(board, startPos, sideToMove);
    }
}
