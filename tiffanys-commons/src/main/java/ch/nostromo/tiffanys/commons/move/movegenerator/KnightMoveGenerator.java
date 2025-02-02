package ch.nostromo.tiffanys.commons.move.movegenerator;

import ch.nostromo.tiffanys.commons.Side;
import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.move.Move;

import java.util.ArrayList;
import java.util.List;

public class KnightMoveGenerator extends AbstractMoveGenerator {


    @Override
    public List<Move> getPseudoLegalMoves(Board board, int startPos, Side sideToMove) {
        List<Move> result = new ArrayList<>();

        for (Direction direction : Direction.KNIGHT_DIRECTIONS) {
            checkAndAddPossibleFreeOrHitMove(board, result, startPos, startPos + direction.getValue(), sideToMove);
        }
        return result;
    }

}
