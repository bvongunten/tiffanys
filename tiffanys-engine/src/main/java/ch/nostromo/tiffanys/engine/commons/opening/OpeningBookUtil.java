package ch.nostromo.tiffanys.engine.commons.opening;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.move.Move;

public class OpeningBookUtil {

    public static String getMovesAsString(ChessGame game) {
        StringBuilder result = new StringBuilder();

        for (Move move : game.getMoveHistory()) {
            if (move.getCastling() != null) {
                result.append(move.getCastling());
            } else {
                result.append(move.getFrom().getLowerCaseName());
                result.append(";");
                result.append(move.getTo().getLowerCaseName());
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
