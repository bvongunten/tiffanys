package ch.nostromo.tiffanys.dragonborn.engine.ai;

import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.board.BoardUtil;
import ch.nostromo.tiffanys.commons.enums.Castling;
import ch.nostromo.tiffanys.commons.enums.Piece;
import ch.nostromo.tiffanys.commons.move.Move;

public class Frontends {

    public static Move fxGuiToMove(Board board, int fromIdx, int toIdx) {

        if (board.isCastlingAllowed(Castling.WHITE_LONG) && fromIdx == Castling.WHITE_LONG.getFromKing() && toIdx == Castling.WHITE_LONG.getToKing()) {
            return new Move(Castling.WHITE_LONG);
        }

        if (board.isCastlingAllowed(Castling.WHITE_SHORT) && fromIdx == Castling.WHITE_SHORT.getFromKing() && toIdx == Castling.WHITE_SHORT.getToKing()) {
            return new Move(Castling.WHITE_SHORT);
        }

        if (board.isCastlingAllowed(Castling.BLACK_LONG) && fromIdx == Castling.BLACK_LONG.getFromKing() && toIdx == Castling.BLACK_LONG.getToKing()) {
            return new Move(Castling.BLACK_LONG);
        }

        if (board.isCastlingAllowed(Castling.BLACK_SHORT) && fromIdx == Castling.BLACK_SHORT.getFromKing() && toIdx == Castling.BLACK_SHORT.getToKing()) {
            return new Move(Castling.BLACK_SHORT);
        }

        return new Move(fromIdx, toIdx);
    }

    public static String moveToString(Move move) {

        String result;
        if (move.getCastling() != null) {
            result = move.getCastling().getAnnotation();
        } else {
            result = move.getFromCoord();
            result += move.getToCoord();
            if (move.isPromotion()) {
                result += move.getPromotion().getPieceCharCode();
            }
        }

        return result;
    }

    public static String moveToUciString(Move move) {

        String result;
        if (move.getCastling() != null) {
            result = BoardUtil.fieldToCoord(move.getCastling().getFromKing());
            result += BoardUtil.fieldToCoord(move.getCastling().getToKing());
        } else {
            result = move.getFromCoord();
            result += move.getToCoord();
            if (move.isPromotion()) {
                result += move.getPromotion().getPieceCharCode();
            }
        }

        return result;
    }

    public static Move stringToMove(String move, Board board) {
        String froms = move.substring(0, 2);
        String tos = move.substring(2, 4);

        Move toMove;
        if (move.length() > 4) {
            Piece pieceCode = Piece.getPieceByCharCode(move.substring(4));
            toMove = new Move(froms, tos, pieceCode);
        } else {
            toMove = new Move(froms, tos);
        }

        Castling castling = isXboardMoveCastling(board, toMove);
        if (castling != null) {
            toMove = new Move(castling);
        }

        return toMove;
    }

    public static Castling isXboardMoveCastling(Board board, Move move) {

        if (board.isCastlingAllowed(Castling.WHITE_LONG) && move.getFrom() == Castling.WHITE_LONG.getFromKing() && move.getTo() == Castling.WHITE_LONG.getToKing()) {
            return Castling.WHITE_LONG;
        }

        if (board.isCastlingAllowed(Castling.WHITE_SHORT) && move.getFrom() == Castling.WHITE_SHORT.getFromKing() && move.getTo() == Castling.WHITE_SHORT.getToKing()) {
            return Castling.WHITE_SHORT;
        }

        if (board.isCastlingAllowed(Castling.BLACK_LONG) && move.getFrom() == Castling.BLACK_LONG.getFromKing() && move.getTo() == Castling.BLACK_LONG.getToKing()) {
            return Castling.BLACK_LONG;
        }

        if (board.isCastlingAllowed(Castling.BLACK_SHORT) && move.getFrom() == Castling.BLACK_SHORT.getFromKing() && move.getTo() == Castling.BLACK_SHORT.getToKing()) {
            return Castling.BLACK_SHORT;
        }

        return null;

    }

}
