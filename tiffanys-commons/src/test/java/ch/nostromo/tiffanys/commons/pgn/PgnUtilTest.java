package ch.nostromo.tiffanys.commons.pgn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.TestHelper;

public class PgnUtilTest extends TestHelper {

    @Test
    public void testPgn() {
        String pgnInput = "";
        pgnInput += "[Site \"?\"]\n";
        pgnInput += "[Date \"2007.08.14\"]\n";
        pgnInput += "[Round \"-\"]\n";
        pgnInput += "[White \"Tiffanys Two\"]\n";
        pgnInput += "[Black \"Tiffanys Three\"]\n";
        pgnInput += "[Result \"*\"]\n";
        pgnInput += "\n";
        pgnInput += "1.e4 d5 2.e5 Nd7 3.Nf3 c5 4.e6 fxe6 5.Nc3 d4 6.Bb5 dxc3 7.dxc3 Qb6 8.Qd3 a6 9.\n";
        pgnInput += "Ba4 Qd6 10.Qe4 Qd5 11.Qxd5 exd5 12.Bf4 b5 13.Bb3 c4 14.Rd1 Ngf6 15.Bg5 cxb3 16.\n";
        pgnInput += "cxb3 e6 17.Bxf6 Nxf6 18.Ke2 Bd6 19.c4 Bd7 20.cxd5 exd5 21.Rhe1 O-O-O 22.Rc1+ \n";
        pgnInput += "Kb7 23.Kd3 Rhe8 24.Nd4 Ng4 25.Re2 Rxe2 26.Kxe2 Re8+ 27.Kf1 Nxh2+ 28.Kg1 Re4 29.\n";
        pgnInput += "Rd1 Be5 30.Nc2 Kc6 31.g3 Nf3+ 32.Kg2 Bh3+ 33.Kxh3 Ng5+ 34.Kg2 Bxb2 35.Ne3 d4 36.\n";
        pgnInput += "Nf5 g6 37.Nh4 Kd5 38.Nf3 Ne6 39.a4 Nc5 40.Rd2 Bc3 41.Rc2 Nxb3 42.axb5 axb5 43.\n";
        pgnInput += "Ra2 h6 44.Kf1 g5 45.Kg2 g4 46.Nh2 d3 47.Ra3 Kc4 48.Ra7 Nc5 49.Rc7 Kd5 50.Nf1 \n";
        pgnInput += "Bd4 51.Nh2 d2 52.Rh7 Nd3 53.Nxg4 Rxg4 54.f3 Rg6 55.Kh2 Ne5 56.f4 d1=Q 57.Rd7+ \n";
        pgnInput += "Nxd7 58.g4 Qg1+ 59.Kh3 b4 60.f5 Rxg4 61.f6 Kc5 62.f7 *\n";

        PgnFormat pgnFormatIn = new PgnFormat(pgnInput);
        ChessGame game = PgnUtil.pgn2Game(pgnFormatIn);
        PgnFormat pgnFormatOut = PgnUtil.game2pgn(game);

        comparePgnFormat(pgnFormatIn, pgnFormatOut);

    }

    @Test
    public void testLichessPgn()  {
        String pgnInput = "[Event \"Rated Blitz game\"]\n" +
                "[Site \"https://lichess.org/mxVSED7u\"]\n" +
                "[Date \"2020.07.13\"]\n" +
                "[White \"bvongunten\"]\n" +
                "[Black \"Ashavakhi\"]\n" +
                "[Result \"0-1\"]\n" +
                "[UTCDate \"2020.07.13\"]\n" +
                "[UTCTime \"22:10:07\"]\n" +
                "[WhiteElo \"1285\"]\n" +
                "[BlackElo \"1324\"]\n" +
                "[WhiteRatingDiff \"-22\"]\n" +
                "[BlackRatingDiff \"+6\"]\n" +
                "[Variant \"Standard\"]\n" +
                "[TimeControl \"300+0\"]\n" +
                "[ECO \"C44\"]\n" +
                "[Termination \"Normal\"]\n" +
                "\n" +
                "1.e4 e5 0-1\n";

        PgnFormat pgnFormatIn = new PgnFormat(pgnInput);
        ChessGame game = PgnUtil.pgn2Game(pgnFormatIn);
        PgnFormat pgnFormatOut = PgnUtil.game2pgn(game);

        comparePgnFormat(pgnFormatIn, pgnFormatOut);

    }

    @Test
    public void testPgnRemis() {
        String pgnInput = "";
        pgnInput += "[Site \"?\"]\n";
        pgnInput += "[Date \"2007.08.14\"]\n";
        pgnInput += "[Round \"-\"]\n";
        pgnInput += "[White \"Tiffanys Two\"]\n";
        pgnInput += "[Black \"Tiffanys Three\"]\n";
        pgnInput += "[Result \"1/2-1/2\"]\n";
        pgnInput += "\n";
        pgnInput += "1.e4 d5 1/2-1/2";

        PgnFormat pgnFormatIn = new PgnFormat(pgnInput);
        ChessGame game = PgnUtil.pgn2Game(pgnFormatIn);
        PgnFormat pgnFormatOut = PgnUtil.game2pgn(game);

        comparePgnFormat(pgnFormatIn, pgnFormatOut);
    }

    @Test
    public void testPgnWinWhite()  {
        String pgnInput = "";
        pgnInput += "[Site \"?\"]\n";
        pgnInput += "[Date \"2007.08.14\"]\n";
        pgnInput += "[Round \"-\"]\n";
        pgnInput += "[White \"Tiffanys Two\"]\n";
        pgnInput += "[Black \"Tiffanys Three\"]\n";
        pgnInput += "[Result \"1-0\"]\n";
        pgnInput += "\n";
        pgnInput += "1.e4 d5 1-0";

        PgnFormat pgnFormatIn = new PgnFormat(pgnInput);
        ChessGame game = PgnUtil.pgn2Game(pgnFormatIn);
        PgnFormat pgnFormatOut = PgnUtil.game2pgn(game);

        comparePgnFormat(pgnFormatIn, pgnFormatOut);
    }

    @Test
    public void testPgnWinBlack() {
        String pgnInput = "";
        pgnInput += "[Site \"?\"]\n";
        pgnInput += "[Date \"2007.08.14\"]\n";
        pgnInput += "[Round \"-\"]\n";
        pgnInput += "[White \"Tiffanys Two\"]\n";
        pgnInput += "[Black \"Tiffanys Three\"]\n";
        pgnInput += "[Result \"0-1\"]\n";
        pgnInput += "\n";
        pgnInput += "1.e4 d5 0-1";

        PgnFormat pgnFormatIn = new PgnFormat(pgnInput);
        ChessGame game = PgnUtil.pgn2Game(pgnFormatIn);
        PgnFormat pgnFormatOut = PgnUtil.game2pgn(game);

        comparePgnFormat(pgnFormatIn, pgnFormatOut);
    }

    public void comparePgnFormat(PgnFormat pgnFormat1, PgnFormat pgnFormat2) {

        assertEquals(pgnFormat1.getBlackPlayer(), pgnFormat2.getBlackPlayer());
        assertEquals(pgnFormat1.getDate(), pgnFormat2.getDate());
        assertEquals(pgnFormat1.getResult(), pgnFormat2.getResult());
        assertEquals(pgnFormat1.getRound(), pgnFormat2.getRound());
        assertEquals(pgnFormat1.getWhitePlayer(), pgnFormat2.getWhitePlayer());
        assertEquals(pgnFormat1.getSite(), pgnFormat2.getSite());

        String lineInput = pgnFormat1.getPgnMoves().replace("\n", "");
        String lineOutput = pgnFormat2.getPgnMoves().replace("\n", "");

        String[] tokensIn = lineInput.split(" ");
        String[] tokensOut = lineOutput.split(" ");

        if (!(tokensIn.length == tokensOut.length)) {
            fail("Unequal pgn lines:\n " + pgnFormat1.toString() + "\n" + pgnFormat2.toString());
        }
        for (int i = 0; i < tokensIn.length; i++) {

            assertEquals(tokensIn[i], tokensOut[i]);
            if (!tokensIn[i].equals(tokensOut[i])) {
                fail("Unequal pgn token: + " + tokensIn[i] + " != " + tokensOut[i] + " \n " + pgnFormat1.toString() + "\n" + pgnFormat2.toString());
            }
        }

    }
}
