package ch.nostromo.tiffanys.testing.bugs;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.formats.FenFormat;
import ch.nostromo.tiffanys.engine.commons.Engine;
import ch.nostromo.tiffanys.engine.commons.EngineException;
import ch.nostromo.tiffanys.engine.commons.EngineResult;
import ch.nostromo.tiffanys.engine.commons.EngineSettings;
import ch.nostromo.tiffanys.engine.EngineFactory;
import org.junit.Test;


public class BugTests {


    @Test
    public void scoreMate() throws EngineException {

        FenFormat fen = new FenFormat("r4k2/ppq2p2/1n1R4/2p3Q1/2PnP3/1P1P3R/P5BP/6K1 b - - 0 31");
        ChessGame game = new ChessGame(fen);

        EngineSettings engineSettings = new EngineSettings();
        engineSettings.setDepth(6);

        Engine engine = EngineFactory.createEngine(engineSettings);
        EngineResult result = engine.syncScoreMoves(game);
    }

}
