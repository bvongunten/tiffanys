package ch.nostromo.tiffanys.dragonborn.engine.ai;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.ChessGameInfo;
import ch.nostromo.tiffanys.commons.fen.FenFormat;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.dragonborn.commons.EngineException;
import ch.nostromo.tiffanys.dragonborn.commons.EngineResult;
import ch.nostromo.tiffanys.dragonborn.commons.EngineSettings;
import ch.nostromo.tiffanys.dragonborn.engine.DragonbornEngine;
import ch.nostromo.tiffanys.dragonborn.engine.TestHelper;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class QuiescenceTest extends TestHelper {

    @Test
    public void testWithQuiescenceSearch() throws EngineException {
        FenFormat fen = new FenFormat("4k3/8/5p2/4p3/5Q2/8/8/4K3 w - - 0 1");
        ChessGame game = new ChessGame(new ChessGameInfo(), fen);

        EngineSettings engineSettings = new EngineSettings();
        engineSettings.setThreads(1);
        engineSettings.setDepth(1);

        DragonbornEngine engine = new DragonbornEngine(engineSettings);
        EngineResult result = engine.syncScoreMoves(game);

        printMoves(result.getLegalMoves());
        assertFalse(bestMovesContains(result.getLegalMoves(), new Move("f4", "e5")));

    }
}
