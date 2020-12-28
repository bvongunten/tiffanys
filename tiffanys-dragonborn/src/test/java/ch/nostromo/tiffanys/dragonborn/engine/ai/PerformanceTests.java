package ch.nostromo.tiffanys.dragonborn.engine.ai;

import org.junit.Test;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.ChessGameInfo;
import ch.nostromo.tiffanys.commons.fen.FenFormat;
import ch.nostromo.tiffanys.dragonborn.commons.EngineException;
import ch.nostromo.tiffanys.dragonborn.commons.EngineResult;
import ch.nostromo.tiffanys.dragonborn.commons.EngineSettings;
import ch.nostromo.tiffanys.dragonborn.engine.DragonbornEngine;
import ch.nostromo.tiffanys.dragonborn.engine.TestHelper;

public class PerformanceTests extends TestHelper {

    @Test
    public void checkMateThreatIn3A() throws EngineException {

        FenFormat fen = new FenFormat("r5k1/2p2ppp/b1P1pb2/p7/4P3/P2q1P2/2RQ1P1P/1NB3K1 w - - 2 25");
        ChessGame game = new ChessGame(new ChessGameInfo(), fen);

        EngineSettings engineSettings = new EngineSettings();
        engineSettings.setDepth(6);
        
        DragonbornEngine engine = new DragonbornEngine(engineSettings);
        EngineResult result = engine.syncScoreMoves(game);

        printMoves(result.getLegalMoves());
        logger.info("Positions evaluated: " + result.getPositionsEvaluated());
        logger.info("Time spent: " + result.getTotalTimeInMs());

        // assertTrue(9994.0 == result.getSelectdMove().getMoveAttributes().getScore());

    }
    @Test
    public void checkOpenPosition() throws EngineException {

        FenFormat fen = new FenFormat("r1bqkb1r/ppp2ppp/2n2n2/3pp3/4P3/2NP1N2/PPP2PPP/R1BQKB1R w KQkq - 2 5");
        ChessGame game = new ChessGame(new ChessGameInfo(), fen);

        EngineSettings engineSettings = new EngineSettings();
        engineSettings.setDepth(7);

        DragonbornEngine engine = new DragonbornEngine(engineSettings);
        EngineResult result = engine.syncScoreMoves(game);

        printMoves(result.getLegalMoves());
        logger.info("Positions evaluated: " + result.getPositionsEvaluated());
        logger.info("Time spent: " + result.getTotalTimeInMs());

        logger.info("Best move score: " + result.getSelectedMove().getMoveAttributes().getScore());

        // assertTrue(9999.0 == result.getSelectdMove().getMoveAttributes().getScore());

    }

}
