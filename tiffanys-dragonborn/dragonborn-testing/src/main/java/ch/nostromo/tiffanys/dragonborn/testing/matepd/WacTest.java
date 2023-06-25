package ch.nostromo.tiffanys.dragonborn.testing.matepd;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.ChessGameInfo;
import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.epd.EpdFormat;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.commons.pgn.SanUtil;
import ch.nostromo.tiffanys.dragonborn.commons.Engine;
import ch.nostromo.tiffanys.dragonborn.commons.EngineException;
import ch.nostromo.tiffanys.dragonborn.commons.EngineResult;
import ch.nostromo.tiffanys.dragonborn.commons.EngineSettings;
import ch.nostromo.tiffanys.dragonborn.engine.EngineFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class WacTest {


    private boolean solvePuzzle(String input) throws EngineException {
        try {
            EpdFormat epd = new EpdFormat(input);
            ChessGame game = new ChessGame(new ChessGameInfo(), epd.toFenFormat());

            EngineSettings engineSettings = new EngineSettings();
            engineSettings.setMode(EngineSettings.EngineMode.TIME_FOR_MOVE);
            engineSettings.setTime(3000);

            Engine engine = EngineFactory.createEngine(engineSettings);
            EngineResult result = engine.syncScoreMoves(game);


            String san = SanUtil.move2San(result.getSelectedMove(), game.getCurrentBoard(), game.getColorToMove());
            List<String> bm = parseBmString(epd.getOpCommand("bm"), game.getCurrentBoard(), game.getColorToMove());



            if (!bm.contains(san)) {
                System.out.println("\nDifference found:");
                System.out.println("puzzle: " + input);
                System.out.println("selected move: " + san);
                System.out.println("best move: " + bm);
                return false;
            } else {
                return true;
            }
        } catch(Exception e) {

            System.out.println("\nCrashed on puzzle: " + input);
            e.printStackTrace();
            return false;
        }

    }

    public void runMateTests(String file) throws EngineException, URISyntaxException, IOException {


        List<String> lines = Files.readAllLines(Paths.get(WacTest.class.getClassLoader().getResource(file).toURI()), Charset.defaultCharset());

        System.out.println("\nRunning " + lines.size() + " tests from file " + file);


        int count = 0;
        int solved = 0;
        int failed = 0;
        for (String line : lines) {
            System.out.print(".");
            if (solvePuzzle(line)) {
                solved ++;
            } else {
                failed ++;
            }



            count++;
            if (count % 25 == 0) {
                System.out.println("Run: " + count + ", solved=" + solved + ", failed="+ failed);
            }
        }



    }


    public static void main(String... args) throws EngineException, URISyntaxException, IOException {
        WacTest testee = new WacTest();

        testee.runMateTests("wac.epd");

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

    private static List<String> parseBmString(String bm, Board board, GameColor colorToMove) {
        StringTokenizer st = new StringTokenizer(bm, " ");
        List<String> result = new ArrayList<>();

        while(st.hasMoreTokens())
        {
            result.add(st.nextToken());
        }
        return result;

    }

}
