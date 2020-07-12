package ch.nostromo.tiffanys.commons.rules;

import java.util.ArrayList;
import java.util.List;

import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.enums.Piece;
import ch.nostromo.tiffanys.commons.fen.FenFormat;
import ch.nostromo.tiffanys.commons.fields.Field;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.commons.pieces.King;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RulesUtil {

    public static List<Move> getLegalMoves(Board board, GameColor colorToMove) {
        List<Move> moves = new ArrayList<Move>();

        Field[] fields = board.getFields();
        List<Move> pseudoLegalMoves = new ArrayList<Move>();

        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getPiece() != null && fields[i].getPieceColor() == colorToMove) {
                fields[i].getPiece().addPseudoLegalMoves(board, pseudoLegalMoves, i, colorToMove);
            }
        }

        for (Move move : pseudoLegalMoves) {
            Board boardClone = board.clone();
            boardClone.applyMove(move, colorToMove);
            if (!isInCheck(boardClone, colorToMove)) {
                moves.add(move);
            }
        }

        return moves;
    }

    public static boolean leadsToCheck(Move move, Board board, GameColor colorToMove) {
        Board boardClone = board.clone();
        boardClone.applyMove(move, colorToMove);

        return isInCheck(boardClone, colorToMove.invert());
    }

    public static boolean leadsToMate(Move move, Board board, GameColor colorToMove) {
        Board boardClone = board.clone();
        boardClone.applyMove(move, colorToMove);

        return isMate(boardClone, colorToMove.invert());
    }

    public static boolean isInCheck(Board board, GameColor colorToMove) {

        Field[] playedFields = board.getFields();
        boolean isCheck = false;
        for (int x = 0; x < playedFields.length; x++) {
            if (playedFields[x].getPiece() == Piece.KING && playedFields[x].getPieceColor() == colorToMove) {
                isCheck = (King.isKingAttacked(board, x, colorToMove));
            }
        }

        return isCheck;
    }

    public static boolean isMate(Board board, GameColor colorToMove) {
        List<Move> legalMoves = getLegalMoves(board, colorToMove);
        boolean isInCheck = isInCheck(board, colorToMove);
        return isInCheck && legalMoves.size() == 0;
    }

    public static boolean isStaleMate(Board board, GameColor colorToMove) {
        List<Move> legalMoves = getLegalMoves(board, colorToMove);
        boolean isInCheck = isInCheck(board, colorToMove);
        return !isInCheck && legalMoves.size() == 0;
    }

    public static boolean isRemisByThree(List<Board> boardHistory) {
        if (boardHistory.size() >= 6) {
            FenFormat currentFen = boardHistory.get(boardHistory.size() - 1).getFenFormat();
            int count = 0;
            for (Board board : boardHistory) {
                if (board.getFenFormat().equalsPosition(currentFen)) {
                    count++;
                }
            }

            return count >= 3;

        } else {
            return false;
        }

    }

    public static boolean isRemisByFifty(List<Board> boardHistory, List<Move> moveHistory) {
        if (boardHistory.size() >= 50) {
            int currentPieceCount = boardHistory.get(boardHistory.size() - 1).getPieceCount();

            // Piece change ?
            for (int i = boardHistory.size() - 1; i > boardHistory.size() - 50; i--) {
                if (currentPieceCount != boardHistory.get(i).getPieceCount()) {
                    return false;
                }
            }

            // Check for pawn moves
            for (int i = moveHistory.size() - 1; i > moveHistory.size() - 50; i--) {
                Move move = moveHistory.get(i);
                Board board = boardHistory.get(i);
                Field[] fields = board.getFields();

                if (fields[move.getTo()].getPiece() == Piece.PAWN) {
                    return false;
                }

            }

            return true;
        } else {
            return false;
        }

    }
}
