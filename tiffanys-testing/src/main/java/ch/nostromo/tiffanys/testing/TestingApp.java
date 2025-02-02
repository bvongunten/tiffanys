package ch.nostromo.tiffanys.testing;

import ch.nostromo.tiffanys.commons.utils.Constants;
import ch.nostromo.tiffanys.commons.utils.LogUtils;
import ch.nostromo.tiffanys.engine.commons.EngineException;
import ch.nostromo.tiffanys.testing.matepd.EpdMateTest;
import ch.nostromo.tiffanys.testing.matepd.WacTest;
import ch.nostromo.tiffanys.testing.performance.PseudoMoveGen;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestingApp {
    private static Logger logger = Logger.getLogger(TestingApp.class.getName());

    private static final int MATE_TESTS = -1;

    private static final int MAX_TIME_WAC = 1000;

    public static final String LOG_FILE = Constants.APPLICATION + "-Testing-" + Constants.VERSION +  ".log";

    public static void main(String... args) throws EngineException, URISyntaxException, IOException {
        int limitedMateTests = MATE_TESTS;

        if (args.length > 0) {
            limitedMateTests = Integer.valueOf(args[0]);
        }

        // Fire up logging
        LogUtils.initializeConsoleAndFileLogging(Level.INFO, Level.INFO, LOG_FILE);



        List<String> results = new ArrayList<>();


        // Run mate epd tests
        EpdMateTest mateTests = new EpdMateTest();
        results.add(mateTests.runMateTests("dragon.epd", limitedMateTests));
        // results.add(mateTests.runMateTests("matein6.epd", limitedMateTests));
        // results.add(mateTests.runMateTests("matein5.epd", limitedMateTests));
        // results.add(mateTests.runMateTests("matein4.epd", limitedMateTests));
        results.add(mateTests.runMateTests("matein3.epd", limitedMateTests));
        results.add(mateTests.runMateTests("matein2.epd", limitedMateTests));
        results.add(mateTests.runMateTests("matein1.epd", limitedMateTests));

        // Run wac tests
        WacTest wacTest = new WacTest();
        // results.add(wacTest.wacTests("wac.epd", limitedMateTests, MAX_TIME_WAC));

        // Pseude Move Gen
        PseudoMoveGen pseudoMoveGen = new PseudoMoveGen();
        results.addAll(pseudoMoveGen.runTests());




        logger.info("Test results: ");

        for (String result : results) {
            logger.info(result);
        }

    }


}
