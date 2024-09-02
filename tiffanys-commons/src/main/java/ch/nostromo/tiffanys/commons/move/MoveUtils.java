package ch.nostromo.tiffanys.commons.move;

import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.pgn.SanUtil;


public class MoveUtils {

    public static String generateSanPrincipalVariation(Move move, Board initialBoard, GameColor gameColor) {
        StringBuilder result = new StringBuilder();

        Board board = initialBoard.clone();

        // Selected Move
        result.append(SanUtil.move2San(move, board, gameColor));

        board.applyMove(move, gameColor);

        for (Move pvMove : move.moveAttributes.getPrincipalVariations()) {
            gameColor = gameColor.invert();

            result.append(" ");
            result.append(SanUtil.move2San(pvMove, board, gameColor));

            board.applyMove(pvMove, gameColor);
        }

        return result.toString();
    }



}
