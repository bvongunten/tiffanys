package ch.nostromo.tiffanys.dragonborn.engine.ai;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import ch.nostromo.tiffanys.commons.move.Move;
import org.junit.Ignore;
import org.junit.Test;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.ChessGameInfo;
import ch.nostromo.tiffanys.commons.fen.FenFormat;
import ch.nostromo.tiffanys.dragonborn.commons.EngineException;
import ch.nostromo.tiffanys.dragonborn.commons.EngineResult;
import ch.nostromo.tiffanys.dragonborn.commons.EngineSettings;
import ch.nostromo.tiffanys.dragonborn.engine.DragonbornEngine;
import ch.nostromo.tiffanys.dragonborn.engine.TestHelper;


public class MateTest extends TestHelper {

    @Test
    public void checkMateIn1A() throws EngineException {

        FenFormat fen = new FenFormat("5k2/R7/1R6/8/8/8/3K4/8 w - - 0 1");
        ChessGame game = new ChessGame(new ChessGameInfo(), fen);

        EngineSettings engineSettings = new EngineSettings();
        engineSettings.setDepth(1);

        DragonbornEngine engine = new DragonbornEngine(engineSettings);
        EngineResult result = engine.syncScoreMoves(game);

        printMoves(result.getLegalMoves());
        logger.info("Positions evaluated: " + result.getPositionsEvaluated());
        logger.info("Time spent: " + result.getTotalTimeInMs());

        logger.info("Best move score: " + result.getSelectdMove().getMoveAttributes().getScore());

        assertTrue(9999.0 == result.getSelectdMove().getMoveAttributes().getScore());

    }

    @Test
    public void checkMateIn2A() throws  EngineException {

        FenFormat fen = new FenFormat("5r1k/5p1p/1p4p1/8/2pqPn1n/3p1PN1/6r1/4R2K b - - 0 37");
        ChessGame game = new ChessGame(new ChessGameInfo(), fen);

        EngineSettings engineSettings = new EngineSettings();
        engineSettings.setDepth(3);

        DragonbornEngine engine = new DragonbornEngine(engineSettings);
        EngineResult result = engine.syncScoreMoves(game);

        printMoves(result.getLegalMoves());
        logger.info("Positions evaluated: " + result.getPositionsEvaluated());
        logger.info("Time spent: " + result.getTotalTimeInMs());

        logger.info("Best move score: " + result.getLegalMoves().get(0).getMoveAttributes().getScore());

        assertTrue(9997.0 == result.getSelectdMove().getMoveAttributes().getScore());

    }

    @Test
    public void checkMateIn2B() throws EngineException {

        FenFormat fen = new FenFormat("r6k/pp1b2p1/3Np2p/8/3p1PRQ/2nB4/q1P4P/2K5 w - - 0 1");
        ChessGame game = new ChessGame(new ChessGameInfo(), fen);

        EngineSettings engineSettings = new EngineSettings();
        engineSettings.setDepth(3);

        DragonbornEngine engine = new DragonbornEngine(engineSettings);
        EngineResult result = engine.syncScoreMoves(game);

        printMoves(result.getLegalMoves());
        logger.info("Positions evaluated: " + result.getPositionsEvaluated());
        logger.info("Time spent: " + result.getTotalTimeInMs());
        logger.info("Best move score: " + result.getLegalMoves().get(0).getMoveAttributes().getScore());

        assertTrue(9997.0 == result.getSelectdMove().getMoveAttributes().getScore());

    }

    @Test
    public void checkMateIn3A() throws EngineException {

        FenFormat fen = new FenFormat("1r3r1k/5Bpp/8/8/P2qQ3/5R2/1b4PP/5K2 w - - 0 0");
        ChessGame game = new ChessGame(new ChessGameInfo(), fen);

        EngineSettings engineSettings = new EngineSettings();
        engineSettings.setDepth(5);

        DragonbornEngine engine = new DragonbornEngine(engineSettings);
        EngineResult result = engine.syncScoreMoves(game);

        printMoves(result.getLegalMoves());
        logger.info("Positions evaluated: " + result.getPositionsEvaluated());
        logger.info("Time spent: " + result.getTotalTimeInMs());

        assertTrue(9995.0 == result.getSelectdMove().getMoveAttributes().getScore());

    }

    @Test
    public void checkMateIn4A() throws EngineException {

        FenFormat fen = new FenFormat("8/pp3Qp1/7k/8/3P3r/4N3/PPP1K1Bq/5R2 w - - 0 1");
        ChessGame game = new ChessGame(new ChessGameInfo(), fen);

        EngineSettings engineSettings = new EngineSettings();
        engineSettings.setMode(EngineSettings.EngineMode.TIME_FOR_MOVE);
        engineSettings.setTime(50000);
        engineSettings.setDepth(7);

        DragonbornEngine engine = new DragonbornEngine(engineSettings);
        EngineResult result = engine.syncScoreMoves(game);



        printMoves(result.getLegalMoves());
        logger.info("Positions evaluated: " + result.getPositionsEvaluated());
        logger.info("Time spent: " + result.getTotalTimeInMs());


        Move moveTomake = result.getSelectdMove();

        String pvs = "";
        for (Move pv : moveTomake.getMoveAttributes().getPrincipalVariations()) {
            pvs += Frontends.moveToUciString(pv) + " ";
        }
        System.out.println("PV: " + pvs);

        assertTrue(9993.0 == result.getSelectdMove().getMoveAttributes().getScore());

    }

    @Test
    public void checkMateIn5A() throws EngineException {

        FenFormat fen = new FenFormat("2q1nk1r/4Rp2/1ppp1P2/6Pp/3p1B2/3P3P/PPP1Q3/6K1 w - - 0 1");
        ChessGame game = new ChessGame(new ChessGameInfo(), fen);

        EngineSettings engineSettings = new EngineSettings();
        engineSettings.setMode(EngineSettings.EngineMode.TIME_FOR_MOVE);
        engineSettings.setTime(50000);
        engineSettings.setDepth(19);

        DragonbornEngine engine = new DragonbornEngine(engineSettings);
        EngineResult result = engine.syncScoreMoves(game);

        printMoves(result.getLegalMoves());
        logger.info("Positions evaluated: " + result.getPositionsEvaluated());
        logger.info("Time spent: " + result.getTotalTimeInMs());


        Move moveTomake = result.getSelectdMove();

        String pvs = "";
        for (Move pv : moveTomake.getMoveAttributes().getPrincipalVariations()) {
            pvs += Frontends.moveToUciString(pv) + " ";
        }
        System.out.println("PV: " + pvs);

        assertTrue(9991.0 == result.getSelectdMove().getMoveAttributes().getScore());

    }


    @Test
    public void fixNonF2F6() throws EngineException {
        // Rook must not move from f2 to f6

        // MAJOR BUG !!!!

        FenFormat fen = new FenFormat("8/8/2pknp2/1p4p1/pP1PP1p1/P2P4/1K2NRPr/8 w - - 8 43");
        ChessGame game = new ChessGame(new ChessGameInfo(), fen);

        EngineSettings engineSettings = new EngineSettings();
        engineSettings.setMode(EngineSettings.EngineMode.DEPTH);
        engineSettings.setTime(1090000);
        engineSettings.setDepth(10);

        DragonbornEngine engine = new DragonbornEngine(engineSettings);
        EngineResult result = engine.syncScoreMoves(game);

        printMoves(result.getLegalMoves());
        logger.info("Positions evaluated: " + result.getPositionsEvaluated());
        logger.info("Time spent: " + result.getTotalTimeInMs());


        Move moveTomake = result.getSelectdMove();

        assertTrue(moveTomake.getFrom() != 36);

    }

    @Test
    public void fixC3Move() throws EngineException {
        // Knight C3 must move

        FenFormat fen = new FenFormat("r2qkb1r/pp2pppp/2n1bn2/2p3B1/3p4/2NP1NP1/PPP1PPBP/R2QK2R w KQkq - 0 7");
        ChessGame game = new ChessGame(new ChessGameInfo(), fen);

        EngineSettings engineSettings = new EngineSettings();
        engineSettings.setMode(EngineSettings.EngineMode.TIME_FOR_MOVE);
        engineSettings.setTime(7000);
        engineSettings.setDepth(19);

        DragonbornEngine engine = new DragonbornEngine(engineSettings);
        EngineResult result = engine.syncScoreMoves(game);

        printMoves(result.getLegalMoves());
        logger.info("Positions evaluated: " + result.getPositionsEvaluated());
        logger.info("Time spent: " + result.getTotalTimeInMs());


        Move moveTomake = result.getSelectdMove();

        assertEquals(43, moveTomake.getFrom());

    }

}
