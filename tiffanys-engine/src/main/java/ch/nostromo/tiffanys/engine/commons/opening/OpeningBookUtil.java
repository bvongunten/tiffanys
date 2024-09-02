package ch.nostromo.tiffanys.engine.commons.opening;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.board.BoardUtil;
import ch.nostromo.tiffanys.commons.move.Move;

public class OpeningBookUtil {

    public static String getMovesAsString(ChessGame game) {
        StringBuilder result = new StringBuilder();

        for (Move move : game.getHistoryMoves()) {
            if (move.getCastling() != null) {
                result.append(move.getCastling());
            } else {
                result.append(move.getFrom().nameLowerCase());
                result.append(";");
                result.append(move.getTo().nameLowerCase());
                if (move.getPromotion() != null) {
                    result.append(";");
                    result.append(move.getPromotion());
                }
            }
            result.append("|");
        }
        return result.toString();
    }

}
