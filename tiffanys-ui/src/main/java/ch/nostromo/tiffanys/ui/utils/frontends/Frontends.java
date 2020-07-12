package ch.nostromo.tiffanys.ui.utils.frontends;

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

}
