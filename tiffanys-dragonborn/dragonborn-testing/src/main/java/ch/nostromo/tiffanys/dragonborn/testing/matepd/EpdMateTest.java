package ch.nostromo.tiffanys.dragonborn.testing.matepd;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.ChessGameInfo;
import ch.nostromo.tiffanys.commons.epd.EpdFormat;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.dragonborn.commons.Engine;
import ch.nostromo.tiffanys.dragonborn.commons.EngineException;
import ch.nostromo.tiffanys.dragonborn.commons.EngineResult;
import ch.nostromo.tiffanys.dragonborn.commons.EngineSettings;
import ch.nostromo.tiffanys.dragonborn.engine.EngineFactory;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class EpdMateTest {


    private void solvePuzzle(String input) throws EngineException {
        try {
            EpdFormat epd = new EpdFormat(input);
            ChessGame game = new ChessGame(new ChessGameInfo(), epd.toFenFormat());

            EngineSettings engineSettings = new EngineSettings();
            engineSettings.setDepth(99);

            Engine engine = EngineFactory.createEngine(engineSettings);
            EngineResult result = engine.syncScoreMoves(game);

            String epdizedMove = epdIzeMove(result.getSelectedMove());
            List<String> bm = parseBmString(epd.getOpCommand("bm"));

            int expectedMate = Integer.valueOf(epd.getOpCommand("ce").substring(2));

            if (!bm.contains(epdizedMove) || result.getSelectedMove().getMoveAttributes().getMateIn() != expectedMate) {
                System.out.println("\nDifference found:");
                System.out.println("puzzle: " + input);
                System.out.println("found mate: " + result.getSelectedMove().getMoveAttributes().getMateIn());

                System.out.println("expected mate: " + expectedMate);
                System.out.println("selected move: " + epdizedMove);
                System.out.println("best move: " + bm);
            }
        } catch(Exception e) {

            System.out.println("\nCrashed on puzzle: " + input);
            e.printStackTrace();
        }

    }

    public void runMateTests(String file) throws EngineException, URISyntaxException, IOException {


        List<String> lines = Files.readAllLines(Paths.get(EpdMateTest.class.getClassLoader().getResource(file).toURI()), Charset.defaultCharset());

        System.out.println("\nRunning " + lines.size() + " mate tests from file " + file);


        int count = 0;
        for (String line : lines) {
            System.out.print(".");
            solvePuzzle(line);
            count++;
            if (count % 25 == 0) {
                System.out.println("Solved: " + count);
            }
        }



    }


    public static void main(String... args) throws EngineException, URISyntaxException, IOException {
        EpdMateTest testee = new EpdMateTest();

        testee.runMateTests("matein5.epd");
        testee.runMateTests("matein4.epd");
        testee.runMateTests("matein3.epd");
        testee.runMateTests("matein2.epd");
        testee.runMateTests("matein1.epd");

    }


    private static String epdIzeMove(Move move) {
        if (move.isCastling()) {
            return move.getCastling().getAnnotation();
        }

        if (move.isPromotion()) {
            return move.getFromCoord() + "-" + move.getToCoord() + move.getPromotion().getPieceCharCode();
        } else {
            return move.getFromCoord() + "-" + move.getToCoord();
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
