package ch.nostromo.tiffanys.commons.move.movegenerator;

import ch.nostromo.tiffanys.commons.Side;
import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.board.Square;
import ch.nostromo.tiffanys.commons.move.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * Base move generator
 */
public abstract class AbstractMoveGenerator {

    public abstract List<Move> getPseudoLegalMoves(Board board, int boardIdx, Side sideToMove);


    /**
     * Get al pseudo legal diagonal moves on a given board and starting position
     */
    protected List<Move> getDiagonalMoves(Board board, int startPos, Side sideToMove) {
        List<Move> result = new ArrayList<>();
        for (Direction direction : Direction.DIAGONAL_DIRECTIONS) {
            checkAndAddPossibleMovesInDirection(board, result, startPos, sideToMove, direction);
        }
        return result;
    }

    /**
     * Get al pseudo legal orthogonal moves on a given board and starting position
     */
    protected List<Move> getOrthogonalMoves(Board board, int startPos, Side sideToMove) {
        List<Move> result = new ArrayList<>();

        for (Direction direction : Direction.ORTHOGONAL_DIRECTIONS) {
            checkAndAddPossibleMovesInDirection(board, result, startPos, sideToMove, direction);
        }

        return result;
    }

    /**
     * Check if to field is of opponent side or free, then adds a move if one is true.
     */
    protected void checkAndAddPossibleFreeOrHitMove(Board board, List<Move> moveList, int fromField, int toField, Side sideToMove) {
        if (board.isPseudoLegalMoveToField(toField, sideToMove)) {
            moveList.add(new Move(Square.byBoardIdx(fromField), Square.byBoardIdx(toField)));
        }
    }


    /**
     * Does add all legal moves to the move list, going into a given direction
     */
    private void checkAndAddPossibleMovesInDirection(Board board, List<Move> moveList, int startPosition, Side sideToMove, Direction direction) {
        int counter = 1;
        boolean exit = false;
        while (!exit) {
            int toField = startPosition + (counter * direction.getValue());
            if (board.isBorder(toField)) {
                exit = true;
            } else if (board.isEmptySquare(Square.byBoardIdx(toField))) {
                moveList.add(new Move(Square.byBoardIdx(startPosition), Square.byBoardIdx(toField)));
                counter++;
            } else if (board.containsPieceOfSide(Square.byBoardIdx(toField), sideToMove.invert())) {
                moveList.add(new Move(Square.byBoardIdx(startPosition), Square.byBoardIdx(toField)));
                exit = true;
            } else if (board.containsPieceOfSide(Square.byBoardIdx(toField), sideToMove)) {
                exit = true;
            }
        }
    }

}
