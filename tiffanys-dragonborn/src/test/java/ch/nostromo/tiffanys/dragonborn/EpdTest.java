package ch.nostromo.tiffanys.dragonborn;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.format.EpdFormat;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.commons.engine.Engine;
import ch.nostromo.tiffanys.commons.engine.EngineResult;
import ch.nostromo.tiffanys.commons.engine.settings.EngineSettings;
import ch.nostromo.tiffanys.commons.engine.settings.EngineSettingsDepth;
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

class EpdTest {

    private static final Logger LOG = LoggerFactory.getLogger(EpdTest.class);

    private static final int MAX_TESTS = 100;

    @Test
    @Tag("epd")
    void testDragonbornProblems() throws IOException, URISyntaxException {
        assertTrue(runMateTests("dragon.epd"));
    }

    @Test
    @Tag("epd")
    void testMateIn1() throws IOException, URISyntaxException {
        assertTrue(runMateTests("matein1.epd"));
    }

    @Test
    @Tag("epd")
    void testMateIn2() throws IOException, URISyntaxException {
        assertTrue(runMateTests("matein2.epd"));
    }

    @Test
    @Tag("epd")
    void testMateIn3() throws IOException, URISyntaxException {
        assertTrue(runMateTests("matein3.epd"));
    }

    @Test
    @Tag("epd")
    void testMateIn4() throws IOException, URISyntaxException {
        assertTrue(runMateTests("matein4.epd"));
    }

    @Test
    @Tag("epd")
    void testMateIn5() throws IOException, URISyntaxException {
        assertTrue(runMateTests("matein5.epd"));
    }

    @Test
    @Tag("epd")
    void testMateIn6() throws IOException, URISyntaxException {
        assertTrue(runMateTests("matein6.epd"));
    }


    private boolean solvePuzzle(String epdLine, int counter) {
        try {
            long startMs = System.currentTimeMillis();

            EpdFormat epd = new EpdFormat(epdLine);
            ChessGame game = new ChessGame(epd.getFen());

            EngineSettings engineSettings = new EngineSettingsDepth(99);

            Engine engine = new DragonbornEngine(engineSettings);
            EngineResult result = engine.startSearch(game);

            String epdizedMove = epdIzeMove(result.getSelectedMove());
            List<String> bm = parseBmString(epd.getOpCommand("bm"));

            int expectedMate = Integer.parseInt(epd.getOpCommand("ce").substring(2));

            boolean success = bm.contains(epdizedMove) && result.getSelectedMove().getMoveAttributes().getMateIn() == expectedMate;

            long time = System.currentTimeMillis() - startMs;

            String logMessage = "Count: " + counter + ", time: " + time + ", expected mate: " + expectedMate + ", found mate: " + result.getSelectedMove().getMoveAttributes().getMateIn() +", selected move: " + epdizedMove + ", best move: " + bm + ", puzzle: " + epd + ", attributes: " + result.getSelectedMove().getMoveAttributes();

            if (success) {
                LOG.info(logMessage);
                return true;
            } else {
                LOG.warn("** WRONG RESULT ** {}", logMessage);
                return false;
            }


        } catch(Exception e) {
            LOG.error("Exception on puzzle: {}", e.getMessage() , e);
            return false;
        }

    }

    boolean runMateTests(String file) throws URISyntaxException, IOException {

        List<String> lines = Files.readAllLines(Paths.get(Objects.requireNonNull(EpdTest.class.getClassLoader().getResource(file)).toURI()), Charset.defaultCharset());

        LOG.info("Running mate tests from file: {}" , file);

        int count = 0;
        int success = 0;
        int failed = 0;
        long startMs = System.currentTimeMillis();

        for (String line : lines) {

            if (solvePuzzle(line, count)) {
                success ++;
            } else {
                failed ++;
            }

            count++;

            if (count == MAX_TESTS) {
                break;
            }
        }

        if (count == 0) {
            count = 1;
        }

        long totalMs = System.currentTimeMillis() - startMs;
        String result = "Finished mate tests from file: " + file + ", puzzles solved: " + success +", puzzles failed: " + failed + ", puzzles total: " + count + ". Total time: " + totalMs + ", average: " + (totalMs/count);

        LOG.info(result);

        return failed == 0;

    }


    private static String epdIzeMove(Move move) {
        if (move.isCastling()) {
            return move.getCastling().getAnnotation();
        }

        if (move.isPromotion()) {
            return move.getFrom().getLowerCaseName() + "-" + move.getTo().getLowerCaseName() + move.getPromotion().getCharCode().toUpperCase();
        } else {
            return move.getFrom().getLowerCaseName() + "-" + move.getTo().getLowerCaseName();
        }
    }

    private static List<String> parseBmString(String bm) {
        StringTokenizer st = new StringTokenizer(bm, " ");
        List<String> result = new ArrayList<>();

        while(st.hasMoreTokens())
        {
            result.add(parseBm(st.nextToken()));
        }
        return result;

    }

    private static String parseBm(String bm) {
        if (bm.equals("O-O-O") || bm.equals("O-O")) {
            return bm;
        }

        return parseFrom(bm) + "-" + parseTo(bm);
    }


    private static String parseFrom(String bm) {
        String from = "";
        if (bm.contains("x")) {
            from = bm.substring(0, bm.indexOf("x"));
        } else {
            from = bm.substring(0, bm.indexOf("-"));
        }

        if (Character.isUpperCase(from.charAt(0))) {
            from = from.substring(1);
        }

        return from;
    }

    private static String parseTo(String bm) {
        String to = "";
        if (bm.contains("x")) {
            to = bm.substring(bm.indexOf("x") + 1);
        } else {
            to = bm.substring(bm.indexOf("-") + 1);
        }

        if (to.endsWith("+")) {
            to = to.substring(0, to.length() - 1);
        }

        return to;
    }



}
