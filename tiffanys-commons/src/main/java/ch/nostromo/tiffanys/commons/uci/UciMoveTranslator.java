package ch.nostromo.tiffanys.commons.uci;

import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.board.BoardUtil;
import ch.nostromo.tiffanys.commons.enums.Castling;
import ch.nostromo.tiffanys.commons.enums.Piece;
import ch.nostromo.tiffanys.commons.move.Move;

public class UciMoveTranslator {


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

    public static Move uciStringToMove(String move, Board board) {
        String froms = move.substring(0, 2);
        String tos = move.substring(2, 4);

        Move toMove;
        if (move.length() > 4) {
            Piece pieceCode = Piece.getPieceByCharCode(move.substring(4));
            toMove = new Move(froms, tos, pieceCode);
        } else {
            toMove = new Move(froms, tos);
        }

        Castling castling = isUciCastling(board, toMove);
        if (castling != null) {
            toMove = new Move(castling);
        }

        return toMove;
    }

    public static Castling isUciCastling(Board board, Move move) {

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
