package ch.nostromo.tiffanys.testing.matepd;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.formats.EpdFormat;
import ch.nostromo.tiffanys.commons.move.Move;
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

public class EpdMateTest {

    private static Logger logger = Logger.getLogger(EpdMateTest.class.getName());

    private boolean solvePuzzle(String epdLine, int counter) {
        try {
            long startMs = System.currentTimeMillis();

            EpdFormat epd = new EpdFormat(epdLine);
            ChessGame game = new ChessGame(epd.getFen());

            EngineSettings engineSettings = new EngineSettings();
            engineSettings.setDepth(99);

            Engine engine = EngineFactory.createEngine(engineSettings);
            EngineResult result = engine.syncScoreMoves(game);

            String epdizedMove = epdIzeMove(result.getSelectedMove());
            List<String> bm = parseBmString(epd.getOpCommand("bm"));

            int expectedMate = Integer.valueOf(epd.getOpCommand("ce").substring(2));

            boolean success = true;

            if (!bm.contains(epdizedMove) || result.getSelectedMove().getMoveAttributes().getMateIn() != expectedMate) {
                success = false;
            }

            long time = System.currentTimeMillis() - startMs;

            String logMessage = "Count: " + counter + ", time: " + time + ", expected mate: " + expectedMate + ", found mate: " + result.getSelectedMove().getMoveAttributes().getMateIn() +", selected move: " + epdizedMove + ", best move: " + bm + ", puzzle: " + epd + ", attributes: " + result.getSelectedMove().getMoveAttributes();

            if (success) {
                logger.info(logMessage);
                return true;
            } else {
                logger.warning(logMessage);
                return false;
            }


        } catch(Exception e) {
            logger.log(Level.SEVERE, "Exception on puzzle: " + epdLine, e);
            return false;
        }

    }

    public String runMateTests(String file, int maxTests) throws EngineException, URISyntaxException, IOException {


        List<String> lines = Files.readAllLines(Paths.get(EpdMateTest.class.getClassLoader().getResource(file).toURI()), Charset.defaultCharset());

        logger.info("Running mate tests from file: " + file);

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

            if (maxTests > 0 && count == maxTests) {
                logger.warning("Max tests counter reached: " + maxTests);
                break;
            }

        }

        long totalMs = System.currentTimeMillis() - startMs;
        String result = "Finished mate tests from file: " + file + ", puzzles solved: " + success +", puzzles failed: " + failed + ", puzzles total: " + count + ". Total time: " + totalMs + ", average: " + (totalMs/count);

        logger.info(result);

        return result;

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
