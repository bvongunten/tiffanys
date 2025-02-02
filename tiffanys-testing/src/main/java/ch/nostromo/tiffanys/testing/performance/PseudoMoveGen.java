package ch.nostromo.tiffanys.testing.performance;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.formats.FenFormat;
import ch.nostromo.tiffanys.engine.EngineFactory;
import ch.nostromo.tiffanys.engine.commons.Engine;
import ch.nostromo.tiffanys.engine.commons.EngineException;
import ch.nostromo.tiffanys.engine.commons.EngineSettings;
import ch.nostromo.tiffanys.testing.matepd.EpdMateTest;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PseudoMoveGen {
    private static Logger logger = Logger.getLogger(EpdMateTest.class.getName());

    static final int runCount = 5;

    static final int iterationsPerRun = 10000000;

    public PseudoMoveGen() {
        super();
    }

    private void measureGeneration(String fenStr) {
        FenFormat fen = new FenFormat(fenStr);
        ChessGame game = new ChessGame(fen);

        EngineSettings engineSettings = new EngineSettings();

        Engine engine = EngineFactory.createEngine(engineSettings);
        long resultMs = engine.testPseudoMoveGeneratorPerformance(game, iterationsPerRun);
    }

    private String runMoveGen(String category, String fen) throws EngineException {

        long startMs = System.currentTimeMillis();
        for (int i = 0; i < runCount; i++) {
            measureGeneration(fen);
        }
        long timeMS = System.currentTimeMillis() - startMs;

        String msg = "Pseudo move generation for category: " + category + ", time: " + timeMS + ", runs: " + runCount + ", iterations: " + iterationsPerRun;
        logger.info(msg);
        return msg;
    }

    public List<String> runTests() throws EngineException {

        logger.info("Running pseudo move generator.");

        List<String> results = new ArrayList<>();

        results.add(runMoveGen("MoveGen 0", "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"));
        results.add(runMoveGen("MoveGen 1", "5k2/R7/1R6/8/8/8/3K4/8 w - - 0 1"));
        results.add(runMoveGen("MoveGen 2", "5r1k/5p1p/1p4p1/8/2pqPn1n/3p1PN1/6r1/4R2K b - - 0 37"));
        results.add(runMoveGen("MoveGen 2", "r6k/pp1b2p1/3Np2p/8/3p1PRQ/2nB4/q1P4P/2K5 w - - 0 1"));
        results.add(runMoveGen("MoveGen 3", "1r3r1k/5Bpp/8/8/P2qQ3/5R2/1b4PP/5K2 w - - 0 0"));
        results.add(runMoveGen("MoveGen 4", "8/pp3Qp1/7k/8/3P3r/4N3/PPP1K1Bq/5R2 w - - 0 1"));
        results.add(runMoveGen("MoveGen 5", "2q1nk1r/4Rp2/1ppp1P2/6Pp/3p1B2/3P3P/PPP1Q3/6K1 w - - 0 1"));

        return results;
    }


    public static void main(String... args) throws EngineException {
        PseudoMoveGen tests = new PseudoMoveGen();
        tests.runTests();

    }

}
