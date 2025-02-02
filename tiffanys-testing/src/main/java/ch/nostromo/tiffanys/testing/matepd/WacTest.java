package ch.nostromo.tiffanys.testing.matepd;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.formats.EpdFormat;
import ch.nostromo.tiffanys.commons.move.MoveUtils;
import ch.nostromo.tiffanys.engine.commons.Engine;
import ch.nostromo.tiffanys.engine.commons.EngineException;
import ch.nostromo.tiffanys.engine.commons.EngineResult;
import ch.nostromo.tiffanys.engine.commons.EngineSettings;
import ch.nostromo.tiffanys.engine.EngineFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WacTest {
    private static Logger logger = Logger.getLogger(WacTest.class.getName());


    private boolean solvePuzzle(String wacLine, int maxTime, int counter) throws EngineException {
        try {
            long startMs = System.currentTimeMillis();

            EpdFormat epd = new EpdFormat(wacLine);
            ChessGame game = new ChessGame(epd.getFen());

            EngineSettings engineSettings = new EngineSettings();
            engineSettings.setMode(EngineSettings.EngineMode.TIME_FOR_MOVE);
            engineSettings.setTime(maxTime);

            Engine engine = EngineFactory.createEngine(engineSettings);
            EngineResult result = engine.syncScoreMoves(game);


            String san = MoveUtils.move2San(result.getSelectedMove(), game.getCurrentBoard(), game.getCurrentSide());
            List<String> bm = parseBmString(epd.getOpCommand("bm"));

            boolean success = true;
            if (!bm.contains(san)) {
                success = false;
            }

            long time = System.currentTimeMillis() - startMs;

            String logMessage = "Count: " + counter + ", time: " + time + ", selected move: " + san + ", best move: " + bm + ", puzzle: " + wacLine +", move Attributes: "  + result.getSelectedMove().getMoveAttributes();

            if (success) {
                logger.info(logMessage);
                return true;
            } else {
                logger.warning(logMessage);
                return false;
            }


        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception on puzzle: " + wacLine, e);
            return false;
        }

    }

    public String wacTests(String file, int maxTests, int maxTime) throws EngineException, URISyntaxException, IOException {


        List<String> lines = Files.readAllLines(Paths.get(WacTest.class.getClassLoader().getResource(file).toURI()), Charset.defaultCharset());

        logger.info("Running WAC tests from file: " + file);


        int count = 0;
        int solved = 0;
        int failed = 0;
        long startMs = System.currentTimeMillis();

        for (String line : lines) {
            if (solvePuzzle(line, maxTime, count)) {
                solved++;
            } else {
                failed++;
            }
            count++;

            if (count == maxTests) {
                break;
            }
        }

        long totalMs = System.currentTimeMillis() - startMs;
        String result = "Finished WAC tests from file: " + file + ", puzzles solved: " + solved +", puzzles failed: " + failed + ", puzzles total: " + count + ". Total time: " + totalMs + ", average: " + (totalMs/count);


        logger.info(result);

        return result;
    }



    private static List<String> parseBmString(String bm) {
        StringTokenizer st = new StringTokenizer(bm, " ");
        List<String> result = new ArrayList<>();

        while (st.hasMoreTokens()) {
            result.add(st.nextToken());
        }
        return result;

    }


    public static void main(String... args) throws EngineException, URISyntaxException, IOException {
        WacTest testee = new WacTest();

        testee.wacTests("wac.epd", 3,1000);

    }

}
