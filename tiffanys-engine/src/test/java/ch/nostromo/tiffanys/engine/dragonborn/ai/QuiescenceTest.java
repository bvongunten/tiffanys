package ch.nostromo.tiffanys.engine.dragonborn.ai;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.formats.FenFormat;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.engine.commons.*;
import ch.nostromo.tiffanys.engine.EngineFactory;
import org.junit.Test;

import java.util.List;

import static ch.nostromo.tiffanys.commons.board.Square.E5;
import static ch.nostromo.tiffanys.commons.board.Square.F4;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class QuiescenceTest {

    @Test
    public void testWithQuiescenceSearch() throws EngineException {
        ChessGame game = new ChessGame(new FenFormat("4k3/8/5p2/4p3/5Q2/8/8/4K3 w - - 0 1"));

        EngineSettings engineSettings = new EngineSettings();
        engineSettings.setThreads(1);
        engineSettings.setDepth(1);

        Engine engine = EngineFactory.createEngine(engineSettings);
        EngineResult result = engine.syncScoreMoves(game);

        assertFalse(bestMovesContains(result.getLegalMoves(), new Move(F4, E5)));

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
