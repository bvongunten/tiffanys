package ch.nostromo.tiffanys.uci.utils;

import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.enums.Coordinates;
import ch.nostromo.tiffanys.commons.enums.Castling;
import ch.nostromo.tiffanys.commons.enums.Piece;
import ch.nostromo.tiffanys.commons.move.Move;

public class UciMoveTranslator {


    public static String moveToUciString(Move move) {

        String result;
        if (move.getCastling() != null) {
            result = move.getCastling().getFromKing().nameLowerCase();
            result += move.getCastling().getToKing().nameLowerCase();
        } else {
            result = move.getFrom().nameLowerCase();
            result += move.getTo().nameLowerCase();
            if (move.isPromotion()) {
                result += move.getPromotion().getCharCode();
            }
        }

        return result;
    }

    public static Move uciStringToMove(String move, Board board) {
        String froms = move.substring(0, 2);
        String tos = move.substring(2, 4);

        Coordinates from = Coordinates.byName(froms);
        Coordinates to = Coordinates.byName(tos);



        Move toMove;
        if (move.length() > 4) {
            Piece pieceCode = Piece.getPieceByCharCode(move.substring(4));
            toMove = new Move(from, to, pieceCode);
        } else {
            toMove = new Move(from, to);
        }

        Castling castling = isUciCastling(board, toMove);
        if (castling != null) {
            toMove = new Move(castling);
        }

        return toMove;
    }

    public static Castling isUciCastling(Board board, Move move) {

        if (board.isCastlingAllowed(Castling.WHITE_LONG) && move.getFrom().getIdx() == Castling.WHITE_LONG.getFromKing().getIdx() && move.getTo().getIdx() == Castling.WHITE_LONG.getToKing().getIdx()) {
            return Castling.WHITE_LONG;
        }

        if (board.isCastlingAllowed(Castling.WHITE_SHORT) && move.getFrom().getIdx() == Castling.WHITE_SHORT.getFromKing().getIdx() && move.getTo().getIdx() == Castling.WHITE_SHORT.getToKing().getIdx()) {
            return Castling.WHITE_SHORT;
        }

        if (board.isCastlingAllowed(Castling.BLACK_LONG) && move.getFrom().getIdx() == Castling.BLACK_LONG.getFromKing().getIdx() && move.getTo().getIdx() == Castling.BLACK_LONG.getToKing().getIdx()) {
            return Castling.BLACK_LONG;
        }

        if (board.isCastlingAllowed(Castling.BLACK_SHORT) && move.getFrom().getIdx() == Castling.BLACK_SHORT.getFromKing().getIdx() && move.getTo().getIdx() == Castling.BLACK_SHORT.getToKing().getIdx()) {
            return Castling.BLACK_SHORT;
        }

        return null;

    }

}
