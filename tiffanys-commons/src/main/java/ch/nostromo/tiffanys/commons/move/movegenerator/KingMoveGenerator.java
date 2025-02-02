package ch.nostromo.tiffanys.commons.move.movegenerator;

import ch.nostromo.tiffanys.commons.Side;
import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.board.PieceType;
import ch.nostromo.tiffanys.commons.board.Square;
import ch.nostromo.tiffanys.commons.move.Castling;
import ch.nostromo.tiffanys.commons.move.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * King move generator. Utility methods to check if a king is attacked on a board
 */
public class KingMoveGenerator extends AbstractMoveGenerator {


    @Override
    public List<Move> getPseudoLegalMoves(Board board, int startPos, Side sideToMove) {
        List<Move> result = new ArrayList<>();

        for (Direction direction : Direction.ORTHOGONAL_DIRECTIONS) {
            checkAndAddPossibleFreeOrHitMove(board, result, startPos, startPos + direction.getValue(), sideToMove);
        }
        for (Direction direction : Direction.DIAGONAL_DIRECTIONS) {
            checkAndAddPossibleFreeOrHitMove(board, result, startPos, startPos + direction.getValue(), sideToMove);
        }

        if (sideToMove == Side.WHITE && board.isCastlingAllowed(Castling.WHITE_LONG)) {
            checkAndAddPossibleCasting(result, board, Castling.WHITE_LONG);
        }

        if (sideToMove == Side.BLACK && board.isCastlingAllowed(Castling.BLACK_LONG)) {
            checkAndAddPossibleCasting(result, board, Castling.BLACK_LONG);
        }

        if (sideToMove == Side.WHITE && board.isCastlingAllowed(Castling.WHITE_SHORT)) {
            checkAndAddPossibleCasting(result, board, Castling.WHITE_SHORT);
        }

        if (sideToMove == Side.BLACK && board.isCastlingAllowed(Castling.BLACK_SHORT)) {
            checkAndAddPossibleCasting(result, board, Castling.BLACK_SHORT);
        }
        return result;
    }

    /**
     * Add castling moves if legal
     */
    private void checkAndAddPossibleCasting(List<Move> moves, Board board, Castling castling) {
        // check empty
        for (Square square : castling.getMustBeEmpty()) {
            if (!board.isEmptySquare(square)) {
                return;
            }
        }
        // check against check ;-)
        for (Square square : castling.getMustNotBeCheck()) {
            if (isKingAttacked(board, square.getBoardIdx(), castling.getSide())) {
                return;
            }
        }

        moves.add(new Move(castling));
    }


    /**
     * Returns true if king on given position is attacked by any opponent piece
     */
    public static boolean isKingAttacked(Board board, int boardIdx, Side sideToMove) {

        for (Direction direction : Direction.KNIGHT_DIRECTIONS) {
            if (board.containsPieceOfSide(boardIdx + direction.getValue(), PieceType.KNIGHT, sideToMove.invert())) {
                return true;
            }
        }

        for (Direction direction : Direction.ORTHOGONAL_DIRECTIONS) {
            if (isOrthogonalOpponent(board, boardIdx, sideToMove, direction)) {
                return true;
            }
        }
        for (Direction direction : Direction.DIAGONAL_DIRECTIONS) {
            if (isDiagonalOpponent(board, boardIdx, sideToMove, direction)) {
                return true;
            }
        }

        if (board.containsPieceOfSide(boardIdx + (11 * sideToMove.getCalculationModifier()), PieceType.PAWN, sideToMove.invert())) {
            return true;
        }

        return board.containsPieceOfSide(boardIdx + (9 * sideToMove.getCalculationModifier()), PieceType.PAWN, sideToMove.invert());
    }


    /**
     * Returns true a piece on a given direction is of diagonal moving opponent
     */
    private static boolean isDiagonalOpponent(Board board, int boardIdx, Side sideToMove, Direction direction) {

        int dCount = checkDirectionToNextOpponentPiece(board, boardIdx, sideToMove, direction);
        int toField = boardIdx + ((dCount + 1) * direction.getValue());
        if (dCount == 0 && board.containsPieceOfSide(toField, PieceType.KING, sideToMove.invert())) {
            return true;
        } else return dCount >= 0 && (board.containsPieceOfSide(toField, PieceType.BISHOP, sideToMove.invert()) || board.containsPieceOfSide(toField, PieceType.QUEEN, sideToMove.invert()));
    }


    /**
     * Returns true a piece on a given direction is of orthogonal moving opponent
     */
    private static boolean isOrthogonalOpponent(Board board, int boardIdx, Side sideToMove, Direction direction) {

        int dCount = checkDirectionToNextOpponentPiece(board, boardIdx, sideToMove, direction);
        int toField = boardIdx + ((dCount + 1) * direction.getValue());
        if (dCount == 0 && board.containsPieceOfSide(toField, PieceType.KING, sideToMove.invert())) {
            return true;
        } else return dCount >= 0 && (board.containsPieceOfSide(toField, PieceType.ROOK, sideToMove.invert()) || board.containsPieceOfSide(toField, PieceType.QUEEN, sideToMove.invert()));
    }


    /**
     * Returns the distance to the next opponent piece in given direction. Returns Integer.MIN_VALUE if board end or own piece is reached
     */
    private static int checkDirectionToNextOpponentPiece(Board board, int startPosition, Side sideToMove, Direction direction) {
        int counter = 0;
        while (true) {
            int toField = startPosition + ((counter + 1) * direction.getValue());
            if (board.isBorder(toField)) {
                return Integer.MIN_VALUE;
            } else if (board.isEmptySquare(Square.byBoardIdx(toField))) {
                counter++;
            } else if (board.containsPieceOfSide(Square.byBoardIdx(toField), sideToMove.invert())) {
                return counter;
            } else if (board.containsPieceOfSide(Square.byBoardIdx(toField), sideToMove)) {
                return Integer.MIN_VALUE;
            }
        }
    }

}
