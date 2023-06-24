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


    @Override
    public List<Move> getPseudoLegalMoves(Board board, int startPos, Side sideToMove) {
        List<Move> result = new ArrayList<>();

        addForwardMove(board, startPos, sideToMove, result);
        addDoubleForwardMove(board, startPos, sideToMove, result);
        addCaptureMove(board, startPos, sideToMove, result, 11);
        addCaptureMove(board, startPos, sideToMove, result, 9);

        return result;
    }

    private void addForwardMove(Board board, int startPos, Side sideToMove, List<Move> result) {
        int toField = startPos + (10 * sideToMove.getCalculationModifier());
        if (board.isEmptySquare(Square.byBoardIdx(toField))) {
            createMoves(result, startPos, toField);
        }
    }

    private void addDoubleForwardMove(Board board, int startPos, Side sideToMove, List<Move> result) {
        int toField1 = startPos + (10 * sideToMove.getCalculationModifier());
        int toField = startPos + (20 * sideToMove.getCalculationModifier());

        if (canMoveDoubleForward(board, startPos, sideToMove, toField1, toField)) {
            result.add(new Move(Square.byBoardIdx(startPos), Square.byBoardIdx(toField)));
        }
    }

    private boolean canMoveDoubleForward(Board board, int startPos, Side sideToMove, int toField1, int toField) {
        boolean onStartingRank = (sideToMove == Side.WHITE) ? startPos < 39 : startPos > 80;
        return onStartingRank
                && board.isEmptySquare(Square.byBoardIdx(toField1))
                && board.isEmptySquare(Square.byBoardIdx(toField));
    }

    private void addCaptureMove(Board board, int startPos, Side sideToMove, List<Move> result, int offset) {
        int toField = startPos + (offset * sideToMove.getCalculationModifier());

        // Regular capture
        if (!board.isBorder(toField) && board.containsPieceOfSide(Square.byBoardIdx(toField), sideToMove.invert())) {
            createMoves(result, startPos, toField);
        }

        // En passant
        if (board.getEnPassantField() != null && toField == board.getEnPassantField().getBoardIdx()) {
            result.add(new Move(Square.byBoardIdx(startPos), Square.byBoardIdx(toField)));
        }
    }

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


}
