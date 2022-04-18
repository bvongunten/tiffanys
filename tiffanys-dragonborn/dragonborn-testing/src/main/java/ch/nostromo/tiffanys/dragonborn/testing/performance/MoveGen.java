package ch.nostromo.tiffanys.dragonborn.testing.performance;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.ChessGameInfo;
import ch.nostromo.tiffanys.commons.fen.FenFormat;
import ch.nostromo.tiffanys.dragonborn.commons.Engine;
import ch.nostromo.tiffanys.dragonborn.commons.EngineException;
import ch.nostromo.tiffanys.dragonborn.commons.EngineSettings;
import ch.nostromo.tiffanys.dragonborn.engine.EngineFactory;
import ch.nostromo.tiffanys.dragonborn.testing.BaseTest;
import ch.nostromo.tiffanys.dragonborn.testing.TestMeasurement;

public class MoveGen extends BaseTest {

    static final int runCount = 3;

    static final int iterationsPerRun = 10000000;

    public MoveGen() {
        super();
    }

    private void measureGeneration(String fenStr) throws EngineException {
        FenFormat fen = new FenFormat(fenStr);
        ChessGame game = new ChessGame(new ChessGameInfo(), fen);

        EngineSettings engineSettings = new EngineSettings();

        Engine engine = EngineFactory.createEngine(engineSettings);
        engine.testGeneratePseudMoves(game, iterationsPerRun);
    }

    private void runMoveGen(String category, String fen) throws EngineException {

        TestMeasurement measurement = createTestMeasurment(category, fen);

        for (int i = 0; i < runCount; i++) {
            startMeasurement(measurement);
            measureGeneration(fen);
            stopMeasurment(measurement);
        }

    }

    public void runTests() throws EngineException {

        runMoveGen("MoveGen 0", "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        runMoveGen("MoveGen 1", "5k2/R7/1R6/8/8/8/3K4/8 w - - 0 1");
        runMoveGen("MoveGen 2", "5r1k/5p1p/1p4p1/8/2pqPn1n/3p1PN1/6r1/4R2K b - - 0 37");
        runMoveGen("MoveGen 2", "r6k/pp1b2p1/3Np2p/8/3p1PRQ/2nB4/q1P4P/2K5 w - - 0 1");
        runMoveGen("MoveGen 3", "1r3r1k/5Bpp/8/8/P2qQ3/5R2/1b4PP/5K2 w - - 0 0");
        runMoveGen("MoveGen 4", "8/pp3Qp1/7k/8/3P3r/4N3/PPP1K1Bq/5R2 w - - 0 1");
        runMoveGen("MoveGen 5", "2q1nk1r/4Rp2/1ppp1P2/6Pp/3p1B2/3P3P/PPP1Q3/6K1 w - - 0 1");
    }



    public static void main(String...args) throws EngineException {
            MoveGen tests = new MoveGen();
            tests.runTests();
            System.out.println(tests.dumpResults());

    }

}
