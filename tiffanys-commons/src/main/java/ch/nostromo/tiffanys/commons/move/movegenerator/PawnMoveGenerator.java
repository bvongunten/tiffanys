package ch.nostromo.tiffanys.commons.move.movegenerator;

import ch.nostromo.tiffanys.commons.Side;
import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.board.Piece;
import ch.nostromo.tiffanys.commons.board.Square;
import ch.nostromo.tiffanys.commons.move.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * Pawn move generator
 */
public class PawnMoveGenerator extends AbstractMoveGenerator {

    private void createMoves(List<Move> moves, int from, int to) {
        if (to > 90) {
            moves.add(new Move(Square.byBoardIdx(from), Square.byBoardIdx(to), Piece.WHITE_KNIGHT));
            moves.add(new Move(Square.byBoardIdx(from), Square.byBoardIdx(to), Piece.WHITE_QUEEN));
            moves.add(new Move(Square.byBoardIdx(from), Square.byBoardIdx(to), Piece.WHITE_ROOK));
            moves.add(new Move(Square.byBoardIdx(from), Square.byBoardIdx(to), Piece.WHITE_BISHOP));
        } else if (to < 29) {
            moves.add(new Move(Square.byBoardIdx(from), Square.byBoardIdx(to), Piece.BLACK_KNIGHT));
            moves.add(new Move(Square.byBoardIdx(from), Square.byBoardIdx(to), Piece.BLACK_QUEEN));
            moves.add(new Move(Square.byBoardIdx(from), Square.byBoardIdx(to), Piece.BLACK_ROOK));
            moves.add(new Move(Square.byBoardIdx(from), Square.byBoardIdx(to), Piece.BLACK_BISHOP));

        } else {
            moves.add(new Move(Square.byBoardIdx(from), Square.byBoardIdx(to)));
        }
    }


    @Override
    public List<Move> getPseudoLegalMoves(Board board, int startPos, Side sideToMove) {
        List<Move> result = new ArrayList<>();

        int toField = 0;

        // One step forward (including possible "pawn promotion")
        toField = startPos + (10 * sideToMove.getCalculationModifier());
        if (board.isEmptySquare(Square.byBoardIdx(toField))) {
            createMoves(result, startPos, toField);
        }

        // Two steps forward
        int toField1 = startPos + (10 * sideToMove.getCalculationModifier());
        toField = startPos + (20 * sideToMove.getCalculationModifier());
        if (sideToMove == Side.WHITE) {
            if (startPos < 39 && board.isEmptySquare(Square.byBoardIdx(toField1)) && board.isEmptySquare(Square.byBoardIdx(toField))) {
                result.add(new Move(Square.byBoardIdx(startPos), Square.byBoardIdx(toField)));
            }
        } else {
            if (startPos > 80 && board.isEmptySquare(Square.byBoardIdx(toField1)) && board.isEmptySquare(Square.byBoardIdx(toField))) {
                result.add(new Move(Square.byBoardIdx(startPos), Square.byBoardIdx(toField)));
            }
        }

        // hit Moves ...
        toField = startPos + (11 * sideToMove.getCalculationModifier());
        if (!board.isBorder(toField) && board.containsPieceOfSide(Square.byBoardIdx(toField), sideToMove.invert())) {
            createMoves(result, startPos, toField);
        }

        // also for enpassant
        if (board.getEnPassantField() != null) {
            if (toField == board.getEnPassantField().getBoardIdx()) {
                result.add(new Move(Square.byBoardIdx(startPos), Square.byBoardIdx(toField)));
            }
        }

        // hit Moves to the other side
        toField = startPos + (9 * sideToMove.getCalculationModifier());
        if (!board.isBorder(toField) && board.containsPieceOfSide(Square.byBoardIdx(toField), sideToMove.invert())) {
            createMoves(result, startPos, toField);
        }

        // also for enpassant to the other side
        if (board.getEnPassantField() != null) {
            if (toField == board.getEnPassantField().getBoardIdx()) {
                result.add(new Move(Square.byBoardIdx(startPos), Square.byBoardIdx(toField)));
            }
        }
        return result;
    }

}
