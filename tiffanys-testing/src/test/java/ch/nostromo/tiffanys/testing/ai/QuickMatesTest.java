package ch.nostromo.tiffanys.testing.ai;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.formats.FenFormat;
import ch.nostromo.tiffanys.engine.commons.Engine;
import ch.nostromo.tiffanys.engine.commons.EngineException;
import ch.nostromo.tiffanys.engine.commons.EngineResult;
import ch.nostromo.tiffanys.engine.commons.EngineSettings;
import ch.nostromo.tiffanys.engine.EngineFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class QuickMatesTest {

    static final int maxDepth = 99;

    private void runMateTest(String fenStr, int expectedMate) throws EngineException {
        FenFormat fen = new FenFormat(fenStr);
        ChessGame game = new ChessGame(fen);

        EngineSettings engineSettings = new EngineSettings();
        engineSettings.setDepth(maxDepth);

        Engine engine = EngineFactory.createEngine(engineSettings);
        EngineResult result = engine.syncScoreMoves(game);

        assertEquals(expectedMate, result.getSelectedMove().getMoveAttributes().getMateIn());

    }

    @Test
    public void checkMateIn1A() throws EngineException {
        runMateTest("5k2/R7/1R6/8/8/8/3K4/8 w - - 0 1", 1);
    }

    @Test
    public void checkMateIn2A() throws EngineException {
        runMateTest("5r1k/5p1p/1p4p1/8/2pqPn1n/3p1PN1/6r1/4R2K b - - 0 37", 2);
    }

    @Test
    public void checkMateIn2B() throws EngineException {
        runMateTest("r6k/pp1b2p1/3Np2p/8/3p1PRQ/2nB4/q1P4P/2K5 w - - 0 1", 2);
    }

    @Test
    public void checkMateIn3A() throws EngineException {
        runMateTest("1r3r1k/5Bpp/8/8/P2qQ3/5R2/1b4PP/5K2 w - - 0 0", 3);
    }

}
