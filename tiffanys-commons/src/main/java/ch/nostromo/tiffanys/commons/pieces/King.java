package ch.nostromo.tiffanys.commons.pieces;

import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.enums.Coordinates;
import ch.nostromo.tiffanys.commons.enums.Castling;
import ch.nostromo.tiffanys.commons.enums.Direction;
import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.move.Move;

import java.util.List;

public class King extends AbstractPiece {

    @Override
    public void addPseudoLegalMoves(Board board, List<Move> moves, int startPos, GameColor colorToMove) {

        for (Direction direction : Direction.getHorizontalDirections()) {
            checkAndAddPossibleFreeOrHitMove(board, moves, startPos, startPos + direction.getValue(), colorToMove);
        }
        for (Direction direction : Direction.getDiagonalDirections()) {
            checkAndAddPossibleFreeOrHitMove(board, moves, startPos, startPos + direction.getValue(), colorToMove);
        }

        if (colorToMove == GameColor.WHITE && board.isCastlingAllowed(Castling.WHITE_LONG)) {
            checkAndAddPossibleCasting(moves, board, Castling.WHITE_LONG);
        }

        if (colorToMove == GameColor.BLACK && board.isCastlingAllowed(Castling.BLACK_LONG)) {
            checkAndAddPossibleCasting(moves, board, Castling.BLACK_LONG);
        }

        if (colorToMove == GameColor.WHITE && board.isCastlingAllowed(Castling.WHITE_SHORT)) {
            checkAndAddPossibleCasting(moves, board, Castling.WHITE_SHORT);
        }

        if (colorToMove == GameColor.BLACK && board.isCastlingAllowed(Castling.BLACK_SHORT)) {
            checkAndAddPossibleCasting(moves, board, Castling.BLACK_SHORT);
        }

    }

    private void checkAndAddPossibleCasting(List<Move> moves, Board board, Castling castling) {
        // check empty
        for (Coordinates coordinates : castling.getMustBeEmpty()) {
            if (board.containsPiece(coordinates.getIdx())) {
                return;
            }
        }
        // check against check ;-)
        for (Coordinates coordinates : castling.getMustNotBeCheck()) {
            if (isKingAttacked(board, coordinates.getIdx(), castling.getColorToMove())) {
                return;
            }
        }

        moves.add(new Move(castling));
    }

}
