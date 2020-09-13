package ch.nostromo.tiffanys.lichess.tools;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.ChessGameInfo;
import ch.nostromo.tiffanys.commons.fen.FenFormat;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.commons.uci.UciMoveTranslator;
import ch.nostromo.tiffanys.lichess.dtos.board.BoardGameFull;
import ch.nostromo.tiffanys.lichess.dtos.commons.GameState;
import ch.nostromo.tiffanys.lichess.streams.BoardGameStateListener;
import ch.nostromo.tiffanys.lichess.streams.BoardGameStateStream;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Data
public class LichessBoardGameStateHelper implements BoardGameStateListener {
    protected Logger LOG = Logger.getLogger(getClass().getName());

    BoardGameStateStream stream;

    List<Move> movesKnownByLichess = new ArrayList<>();
    FenFormat initialFen;

    ChessGame lichessGame;
    GameState lastReadGameState;

    boolean initialized;

    List<LichessBoardGameStateHelperEventListener> eventListeners = new ArrayList<>();

    public LichessBoardGameStateHelper(FenFormat initialFen, BoardGameStateStream stream) {
        this.initialFen = initialFen;
        this.stream = stream;

        lichessGame = new ChessGame(new ChessGameInfo(), initialFen);
    }

    public void start() {
        this.stream.getListeners().add(this);
        this.stream.startStream();
    }

    @Override
    public void onBoardGameStateFull(BoardGameFull gameFull) {
        initialized = true;

        onBoardGameState(gameFull.getState());
    }

    @Override
    public void onBoardGameState(GameState gameState) {
        LOG.info("Received game state from lichess:  " + gameState);

        lastReadGameState = gameState;

        movesKnownByLichess.clear();

        if (!gameState.getMoves().isEmpty()) {

            String[] uciMoves = gameState.getMoves().split(" ");

            lichessGame.resetToInitialFen();

            if (uciMoves.length > 0) {
                for (int i = 0; i < uciMoves.length; i++) {
                    Move move = UciMoveTranslator.uciStringToMove(uciMoves[i], lichessGame.getCurrentBoard());
                    movesKnownByLichess.add(move);
                    lichessGame.applyMove(move);
                }
            }
        }
        eventListeners.forEach(LichessBoardGameStateHelperEventListener::onChange);
    }

    public Move getMove(ChessGame game) {
        if (game.getHistoryMoves().size() < lichessGame.getHistoryMoves().size()) {
            return lichessGame.getHistoryMoves().get(lichessGame.getHistoryMoves().size() - 1);
        }

        return null;
    }


}
