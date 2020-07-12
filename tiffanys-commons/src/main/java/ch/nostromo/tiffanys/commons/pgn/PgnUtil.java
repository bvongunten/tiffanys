package ch.nostromo.tiffanys.commons.pgn;

import java.util.List;
import java.util.StringTokenizer;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.ChessGameInfo;
import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.enums.GameState;
import ch.nostromo.tiffanys.commons.move.Move;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PgnUtil {

    public static ChessGame pgn2Game(PgnFormat pgn) {
        ChessGameInfo gi = new ChessGameInfo(pgn.getWhitePlayer(), pgn.getBlackPlayer(), pgn.getSite(), pgn.getDate(), pgn.getRound());

        ChessGame result = new ChessGame(gi);

        StringTokenizer pgnLineTokenizer = new StringTokenizer(pgn.getPgnMoves(), "\n");

        while (pgnLineTokenizer.hasMoreTokens()) {
            String line = pgnLineTokenizer.nextToken();

            StringTokenizer st = new StringTokenizer(line, " ");
            while (st.hasMoreTokens()) {

                String move = st.nextToken().replace(" ", "");

                if (move.startsWith("*")) {
                    return result;
                } else if (move.startsWith("1-0") || move.startsWith("0-1") || move.startsWith("1/2-1/2")) {
                    result.setFinishedGameState(GameState.getGameStateByValue(move));
                    return result;
                }

                int point = move.indexOf(".");
                if (point >= 0) {
                    move = move.substring(point + 1);
                }

                // Break ?
                if (move.length() == 0) {
                    continue;
                }

                Move moveInput = SanUtil.san2Move(move, result.getCurrentBoard(), result.getCurrentColorToMove());
                result.applyMove(moveInput);

            }
        }

        throw new IllegalArgumentException("Unexpected end of pgn:" + pgn);
    }

    public static PgnFormat game2pgn(ChessGame game) {

        GameColor colorToMove = game.getStartingColor();
        List<Board> boardsList = game.getHistoryBoards();
        List<Move> movesList = game.getHistoryMoves();

        int moveCounter = 1;
        String currentLine = "";
        StringBuilder pgnMoves = new StringBuilder();

        for (int i = 0; i < movesList.size(); i++) {

            String addOn = "";
            if (colorToMove == GameColor.WHITE) {
                addOn += moveCounter + ".";
                moveCounter++;
            }
            currentLine += addOn;

            addOn = "";
            addOn += SanUtil.move2San(movesList.get(i), boardsList.get(i), colorToMove);
            addOn += " ";

            if (currentLine.length() + addOn.length() > 78) {
                pgnMoves.append(currentLine + "\n");
                currentLine = addOn;
            } else {
                currentLine += addOn;
            }

            if (colorToMove == GameColor.WHITE) {
                colorToMove = GameColor.BLACK;
            } else {
                colorToMove = GameColor.WHITE;
            }
        }

        pgnMoves.append(currentLine);

        // add currentResult
        GameState gameState = game.getCurrentGameState();
        pgnMoves.append(gameState.getValue());

        ChessGameInfo gameInfo = game.getGameInfo();
        PgnFormat pgn = new PgnFormat(gameInfo.getSite(), gameInfo.getDate(), gameInfo.getRound(), gameInfo.getWhitePlayer(), gameInfo.getBlackPlayer(), gameState.getValue(), pgnMoves.toString());

        return pgn;
    }
}
