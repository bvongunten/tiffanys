package ch.nostromo.tiffanys.commons.move;

import ch.nostromo.tiffanys.commons.enums.Castling;

public class MoveTranslator {


    public static String moveToString(Move move) {

        String result;
        if (Castling.WHITE_LONG.equals(move.getCastling()) || Castling.BLACK_LONG.equals(move.getCastling())) {
            result = "O-O-O";
        } else if (Castling.WHITE_SHORT.equals(move.getCastling()) || Castling.BLACK_SHORT.equals(move.getCastling())) {
            result = "O-O";
        } else {
            result = move.getFromCoord();
            result += "-";
            result += move.getToCoord();
            if (move.isPromotion()) {
                result += move.getPromotion().getPieceCharCode();
            }
        }

        return result;
    }

}
