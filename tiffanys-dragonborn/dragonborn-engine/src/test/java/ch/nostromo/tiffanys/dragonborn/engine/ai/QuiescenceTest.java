package ch.nostromo.tiffanys.dragonborn.engine.ai;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.ChessGameInfo;
import ch.nostromo.tiffanys.commons.fen.FenFormat;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.dragonborn.commons.*;
import ch.nostromo.tiffanys.dragonborn.engine.EngineFactory;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class QuiescenceTest {

    @Test
    public void testWithQuiescenceSearch() throws EngineException {
        FenFormat fen = new FenFormat("4k3/8/5p2/4p3/5Q2/8/8/4K3 w - - 0 1");
        ChessGame game = new ChessGame(new ChessGameInfo(), fen);

        EngineSettings engineSettings = new EngineSettings();
        engineSettings.setThreads(1);
        engineSettings.setDepth(1);

        Engine engine = EngineFactory.createEngine(engineSettings);
        EngineResult result = engine.syncScoreMoves(game);

        assertFalse(bestMovesContains(result.getLegalMoves(), new Move("f4", "e5")));

    }

    public boolean bestMovesContains(List<Move> moves, Move move) {
        if (moves.size() == 0) {
            fail("Moves list is empty");
        }

        int maxScore = (int) moves.get(0).getMoveAttributes().getScore();
        for (Move scoredMove : moves) {

            if (scoredMove.equals(move)) {
                return true;
            }

            if (scoredMove.getMoveAttributes().getScore() < maxScore) {
                return false;
            }
        }

        fail("Move not found!");
        return false;

    }
}
