package ch.nostromo.tiffanys.commons.move.movegenerator;

import ch.nostromo.tiffanys.commons.Side;
import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.move.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * Queen move generator
 */
public class QueenMoveGenerator extends AbstractMoveGenerator {

    @Override
    public List<Move> getPseudoLegalMoves(Board board, int startPos, Side sideToMove) {
        List<Move> result = new ArrayList<>();

        result.addAll(getOrthogonalMoves(board, startPos, sideToMove));
        result.addAll(getDiagonalMoves(board, startPos, sideToMove));

        return result;
    }

}
