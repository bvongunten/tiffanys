package ch.nostromo.tiffanys.commons.pieces;

import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.enums.Direction;
import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.enums.Piece;
import ch.nostromo.tiffanys.commons.move.Move;

import java.util.List;

public abstract class AbstractPiece {

    public abstract void addPseudoLegalMoves(Board board, List<Move> moves, int boardIdx, GameColor colorToMove);

    protected void addDiagonalMoves(Board board, List<Move> moves, int startPos, GameColor colorToMove) {

        for (Direction direction : Direction.getDiagonalDirections()) {
            checkDirection(board, moves, startPos, colorToMove, direction);
        }

    }

    protected void addHorizontalMoves(Board board, List<Move> moves, int startPos, GameColor colorToMove) {
        for (Direction direction : Direction.getHorizontalDirections()) {
            checkDirection(board, moves, startPos, colorToMove, direction);
        }
    }

    public static void checkAndAddPossibleFreeOrHitMove(Board board, List<Move> moveList, int fromField, int toField, GameColor colorToMove) {
        if (!board.isVoid(toField) && (!board.containsPiece(toField) || !board.getPieceColor(toField).equals(colorToMove))) {
            moveList.add(new Move(fromField, toField));
        }
    }

    public static boolean isKingAttacked(Board board, int boardIdx, GameColor colorToMove) {

        for (Direction direction : Direction.getKnightDirections()) {
            if (board.isPieceAndColor(boardIdx + direction.getValue(), Piece.KNIGHT, colorToMove.invert())) {
                return true;
            }
        }

        for (Direction direction : Direction.getHorizontalDirections()) {
            if (isHorizontalOpponent(board, boardIdx, colorToMove, direction)) {
                return true;
            }
        }
        for (Direction direction : Direction.getDiagonalDirections()) {
            if (isDiagonalOpponent(board, boardIdx, colorToMove, direction)) {
                return true;
            }
        }

        if (board.isPieceAndColor(boardIdx + (11 * colorToMove.getCalculationModificator()), Piece.PAWN, colorToMove.invert())) {
            return true;
        }
        if (board.isPieceAndColor(boardIdx + (9 * colorToMove.getCalculationModificator()), Piece.PAWN, colorToMove.invert())) {
            return true;
        }

        return false;
    }

    private static boolean isDiagonalOpponent(Board board, int boardIdx, GameColor colorToMove, Direction direction) {

        int dcount = checkDirectionToNextOpponentPiece(board, boardIdx, colorToMove, direction);
        int toField = boardIdx + ((dcount + 1) * direction.getValue());
        if (dcount == 0 && board.isPieceAndColor(toField, Piece.KING, colorToMove.invert())) {
            return true;
        } else if (dcount >= 0 && (board.isPieceAndColor(toField, Piece.BISHOP, colorToMove.invert()) || board.isPieceAndColor(toField, Piece.QUEEN, colorToMove.invert()))) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isHorizontalOpponent(Board board, int boardIdx, GameColor colorToMove, Direction direction) {

        int dcount = checkDirectionToNextOpponentPiece(board, boardIdx, colorToMove, direction);
        int toField = boardIdx + ((dcount + 1) * direction.getValue());
        if (dcount == 0 && board.isPieceAndColor(toField, Piece.KING, colorToMove.invert())) {
            return true;
        } else if (dcount >= 0 && (board.isPieceAndColor(toField, Piece.ROOK, colorToMove.invert()) || board.isPieceAndColor(toField, Piece.QUEEN, colorToMove.invert()))) {
            return true;
        } else {
            return false;
        }
    }

    public static int checkDirectionToNextOpponentPiece(Board board, int startPosition, GameColor colorToMove, Direction direction) {
        int counter = 0;
        while (true) {
            int toField = startPosition + ((counter + 1) * direction.getValue());
            if (board.isVoid(toField)) {
                return Integer.MIN_VALUE;
            } else if (!board.containsPiece(toField)) {
                counter++;
            } else if (board.containsPiece(toField) && board.getPieceColor(toField) != colorToMove) {
                return counter;
            } else if (board.containsPiece(toField) && board.getPieceColor(toField) == colorToMove) {
                return Integer.MIN_VALUE;
            }
        }
    }

    public static void checkDirection(Board board, List<Move> moveList, int startPosition, GameColor colorToMove, Direction direction) {
        int counter = 0;
        boolean exit = false;
        while (!exit) {
            int toField = startPosition + ((counter + 1) * direction.getValue());
            if (board.isVoid(toField)) {
                exit = true;
            } else if (!board.containsPiece(toField)) {
                moveList.add(new Move(startPosition, toField));
                counter++;
            } else if (board.containsPiece(toField) && board.getPieceColor(toField) != colorToMove) {
                moveList.add(new Move(startPosition, toField));
                counter++;
                exit = true;
            } else if (board.containsPiece(toField) && board.getPieceColor(toField) == colorToMove) {
                exit = true;
            }
        }
    }

}
