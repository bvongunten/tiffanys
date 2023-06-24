package ch.nostromo.tiffanys.dragonborn;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.format.EpdFormat;
import ch.nostromo.tiffanys.commons.move.MoveUtils;
import ch.nostromo.tiffanys.commons.engine.Engine;
import ch.nostromo.tiffanys.commons.engine.EngineResult;
import ch.nostromo.tiffanys.commons.engine.settings.EngineSettings;
import ch.nostromo.tiffanys.commons.engine.settings.EngineSettingsTimePerMove;
import ch.nostromo.tiffanys.dragonborn.engine.DragonbornEngine;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

import static org.junit.jupiter.api.Assertions.assertTrue;

class WacTest {
    private static final Logger LOG = LoggerFactory.getLogger(WacTest.class);

    private static final int MAX_COUNT = Integer.MAX_VALUE;
    private static final int MAX_TIME = 10000;

    @Test
    @Tag("wac")
    void wacTests() throws URISyntaxException, IOException {
        assertTrue(wacTests("wac.epd") > 80);
    }

    private boolean solvePuzzle(String wacLine, int counter) {
        try {
            long startMs = System.currentTimeMillis();

            EpdFormat epd = new EpdFormat(wacLine);
            ChessGame game = new ChessGame(epd.getFen());

            EngineSettings engineSettings = new EngineSettingsTimePerMove(MAX_TIME);

            Engine engine = new DragonbornEngine(engineSettings);
            EngineResult result = engine.startSearch(game);


            String san = MoveUtils.move2San(result.getSelectedMove(), game.getCurrentBoard(), game.getCurrentSide());
            List<String> bm = parseBmString(epd.getOpCommand("bm"));

            boolean success = bm.contains(san);

            long time = System.currentTimeMillis() - startMs;

            String logMessage = "Count: " + counter + ", time: " + time + ", selected move: " + san + ", best move: " + bm + ", puzzle: " + wacLine + ", move Attributes: " + result.getSelectedMove().getMoveAttributes();


            if (success) {
                LOG.info(logMessage);
                return true;
            } else {
                LOG.warn("** WRONG RESULT ** {}", logMessage);
                return false;
            }


        } catch (Exception e) {
            LOG.error("Exception on puzzle: {}", wacLine, e);
            return false;
        }

    }

    public double wacTests(String file) throws URISyntaxException, IOException {

        List<String> lines = Files.readAllLines(Paths.get(Objects.requireNonNull(WacTest.class.getClassLoader().getResource(file)).toURI()), Charset.defaultCharset());

        LOG.info("Running WAC tests from file: {}", file);

        int count = 0;
        int solved = 0;
        int failed = 0;
        long startMs = System.currentTimeMillis();

        for (String line : lines) {
            if (solvePuzzle(line, count)) {
                solved++;
            } else {
                failed++;
            }
            count++;

            if (count == MAX_COUNT) {
                break;
            }

        }

        if (count == 0) {
            count = 1;
        }

        int successRate = (solved * 100) / count;


        long totalMs = System.currentTimeMillis() - startMs;
        String result = "Finished WAC tests from file: " + file + ", puzzles solved: " + solved + ", puzzles failed: " + failed + ", success rate %: " + successRate + ", puzzles total: " + count + ". Total time: " + totalMs + ", average: " + (totalMs / count);

        LOG.info(result);


        return successRate;

    }


    private static List<String> parseBmString(String bm) {
        StringTokenizer st = new StringTokenizer(bm, " ");
        List<String> result = new ArrayList<>();

        while (st.hasMoreTokens()) {
            result.add(st.nextToken());
        }
        return result;

    }


}
