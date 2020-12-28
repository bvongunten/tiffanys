package ch.nostromo.tiffanys.commons.pieces;

import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.enums.Piece;
import ch.nostromo.tiffanys.commons.move.Move;

import java.util.List;

public class Pawn extends AbstractPiece {

    private void createMoves(List<Move> moves, int from, int to) {
        if (to > 90 || to < 29) {
            moves.add(new Move(from, to, Piece.KNIGHT));
            moves.add(new Move(from, to, Piece.QUEEN));
            moves.add(new Move(from, to, Piece.ROOK));
            moves.add(new Move(from, to, Piece.BISHOP));
        } else {
            moves.add(new Move(from, to));
        }
    }

    @Override
    public void addPseudoLegalMoves(Board board, List<Move> moves, int startPos, GameColor colorToMove) {

        int toField = 0;

        // One step forward (including possible "pawn promotion")
        toField = startPos + (10 * colorToMove.getCalculationModificator());
        if (!board.containsPiece(toField)) {
            createMoves(moves, startPos, toField);
        }

        // Two steps forward
        int toField1 = startPos + (10 * colorToMove.getCalculationModificator());
        toField = startPos + (20 * colorToMove.getCalculationModificator());
        if (colorToMove == GameColor.WHITE) {
            if (startPos < 39 && !board.containsPiece(toField1) && !board.containsPiece(toField)) {
                moves.add(new Move(startPos, toField));
            }
        } else {
            if (startPos > 80 && !board.containsPiece(toField1) && !board.containsPiece(toField)) {
                moves.add(new Move(startPos, toField));
            }
        }

        // hit Moves ...
        toField = startPos + (11 * colorToMove.getCalculationModificator());
        if (!board.isVoid(toField) && board.containsPiece(toField) && !board.getPieceColor(toField).equals(colorToMove)) {
            createMoves(moves, startPos, toField);
        }

        // also for enpassant
        if (toField == board.getEnPassantField()) {
            moves.add(new Move(startPos, toField));
        }

        // hit Moves to the other side
        toField = startPos + (9 * colorToMove.getCalculationModificator());
        if (!board.isVoid(toField) && board.containsPiece(toField) && !board.getPieceColor(toField).equals(colorToMove)) {
            createMoves(moves, startPos, toField);
        }
        // also for enpassant
        if (toField == board.getEnPassantField()) {
            moves.add(new Move(startPos, toField));
        }

    }
}
