package ch.nostromo.tiffanys.commons;

import ch.nostromo.tiffanys.commons.board.Square;
import ch.nostromo.tiffanys.commons.formats.FenFormat;
import ch.nostromo.tiffanys.commons.formats.PgnFormat;
import ch.nostromo.tiffanys.commons.move.Move;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ChessGameTest extends BaseTest {

    @Test
    public void testGameInitDefault() {
        ChessGame game = new ChessGame();
        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", game.getFen().toString());
    }

    @Test
    public void testGameInitPgn() {
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
        ChessGame game = new ChessGame(pgnFormatIn);
        PgnFormat pgnFormatOut = game.getPgn();

        assertPgnFormat(pgnFormatIn, pgnFormatOut);

    }

    @Test
    public void testGameInitExtendedPgn() {
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
        ChessGame game = new ChessGame(pgnFormatIn);
        PgnFormat pgnFormatOut = game.getPgn();

        assertPgnFormat(pgnFormatIn, pgnFormatOut);

    }

    @Test
    public void testApplyMove() {
        // Play 3 moves
        ChessGame game = new ChessGame();

        ChessGameState moveState;

        // White opening
        moveState = game.applyMove(new Move(Square.E2, Square.E4));
        assertGameState(game, moveState, ChessGameState.GAME_OPEN, Side.BLACK, 1, 0, "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1");

        // Black opening
        moveState = game.applyMove(new Move(Square.E7, Square.E5));
        assertGameState(game, moveState, ChessGameState.GAME_OPEN, Side.WHITE, 1, 0, "rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w KQkq e6 0 1");

        // Some more moves inflicting move 50 counter
        moveState = game.applyMove(new Move(Square.G1, Square.F3));
        assertGameState(game, moveState, ChessGameState.GAME_OPEN, Side.BLACK, 2, 1, "rnbqkbnr/pppp1ppp/8/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2");

        moveState = game.applyMove(new Move(Square.G8, Square.E7));
        assertGameState(game, moveState, ChessGameState.GAME_OPEN, Side.WHITE, 2, 2, "rnbqkb1r/ppppnppp/8/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R w KQkq - 2 2");

        moveState = game.applyMove(new Move(Square.B1, Square.C3));
        assertGameState(game, moveState, ChessGameState.GAME_OPEN, Side.BLACK, 3, 3, "rnbqkb1r/ppppnppp/8/4p3/4P3/2N2N2/PPPP1PPP/R1BQKB1R b KQkq - 3 3");
    }


    @Test
    public void testTakeMoveBack() {
        ChessGame game = new ChessGame();

        game.applyMove(new Move(Square.G1, Square.F3));
        game.applyMove(new Move(Square.G8, Square.F6));

        game.takeBackMove();
        game.takeBackMove();

        assertGameState(game, ChessGameState.GAME_OPEN, ChessGameState.GAME_OPEN, Side.WHITE, 0, 0, FenFormat.INITIAL_FEN.toString());
    }

    @Test(expected = ChessGameException.class)
    public void testTakeMoveBackException() {
        ChessGame game = new ChessGame();

        game.applyMove(new Move(Square.G1, Square.F3));

        game.takeBackMove();

        // Trigger exception
        game.takeBackMove();
    }


    @Test(expected = ChessGameException.class)
    public void testApplyMoveIllegal() {
        ChessGame game = new ChessGame();
        game.applyMove(new Move(Square.A1, Square.A8));
    }

    @Test
    public void testPgnFormatRemis() {
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
        ChessGame game = new ChessGame(pgnFormatIn);
        PgnFormat pgnFormatOut = game.getPgn();

        assertPgnFormat(pgnFormatIn, pgnFormatOut);
    }

    @Test
    public void testPgnFormatWhiteWin() {
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
        ChessGame game = new ChessGame(pgnFormatIn);
        PgnFormat pgnFormatOut = game.getPgn();

        assertPgnFormat(pgnFormatIn, pgnFormatOut);
    }

    @Test
    public void testPgnFormatBlackWin() {
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
        ChessGame game = new ChessGame(pgnFormatIn);
        PgnFormat pgnFormatOut = game.getPgn();

        assertPgnFormat(pgnFormatIn, pgnFormatOut);
    }

    @Test
    public void testPgnFormatOpenGame() {
        String pgnInput = "";
        pgnInput += "[Site \"?\"]\n";
        pgnInput += "[Date \"2007.08.14\"]\n";
        pgnInput += "[Round \"-\"]\n";
        pgnInput += "[White \"Tiffanys Two\"]\n";
        pgnInput += "[Black \"Tiffanys Three\"]\n";
        pgnInput += "[Result \"*\"]\n";
        pgnInput += "\n";
        pgnInput += "1.e4 d5 *";

        PgnFormat pgnFormatIn = new PgnFormat(pgnInput);
        ChessGame game = new ChessGame(pgnFormatIn);
        PgnFormat pgnFormatOut = game.getPgn();

        assertPgnFormat(pgnFormatIn, pgnFormatOut);
    }


    @Test
    public void testGameStateMateByFen() {
        ChessGame game = new ChessGame(new FenFormat("5b2/1p1b1p2/5kp1/P1n1p3/3pP2p/P1pP1P2/5qr1/B3RK2 w - - 0 39"));
        assertEquals(ChessGameState.BLACK_WIN, game.getCurrentGameState());
    }

    @Test
    public void testGameStateByManualDecision() {
        ChessGame game = new ChessGame();
        game.setDecidedGameState(ChessGameState.REMIS_BY_FIFTY);
        assertEquals(ChessGameState.REMIS_BY_FIFTY, game.getCurrentGameState());
    }

    @Test
    public void testRulesThreeFoldRepetition() {
        ChessGame game = new ChessGame();

        game.applyMove(new Move(Square.G1, Square.F3));
        game.applyMove(new Move(Square.G8, Square.F6));
        game.applyMove(new Move(Square.F3, Square.G1));
        game.applyMove(new Move(Square.F6, Square.G8));

        game.applyMove(new Move(Square.G1, Square.F3));
        game.applyMove(new Move(Square.G8, Square.F6));
        game.applyMove(new Move(Square.F3, Square.G1));

        ChessGameState moveState = game.applyMove(new Move(Square.F6, Square.G8));

        assertGameState(game, moveState, ChessGameState.REMIS_BY_THREE, Side.WHITE, 4, 8, "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 8 4");
    }

    @Test
    public void testRulesThreeFoldRepetitionByPgn() {

        String pgnInput = "[Event \"?\"]\n" +
                "[White \"Neue Partie\"]\n" +
                "[Black \"?\"]\n" +
                "[Site \"?\"]\n" +
                "[Round \"?\"]\n" +
                "[Result \"1/2-1/2\"]\n" +
                "[Date \"????.??.??\"]\n" +
                "[ECO \"A00\"]\n" +
                "[PlyCount \"8\"]\n" +
                "[GameId \"2138396560389063\"]\n" +
                "[SourceVersionDate \"2025.01.26\"]\n" +
                "\n" +
                "1. Nf3 Nf6 2. Ng1 Ng8 3. Nf3 Nf6 4. Ng1 *";

        ChessGame game = new ChessGame(new PgnFormat(pgnInput));

        ChessGameState moveState = game.applyMove(new Move(Square.F6, Square.G8));
        assertGameState(game, moveState, ChessGameState.REMIS_BY_THREE, Side.WHITE, 4, 8, "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 8 4");

    }

    @Test
    public void testRulesFiftyMovesTrigger() {
        String fen = "8/8/8/4k3/8/8/P3K2R/8 w - - 49 121";

        ChessGame game = new ChessGame(new FenFormat(fen));
        ChessGameState moveState = game.applyMove(new Move(Square.H2, Square.H8));

        assertGameState(game, moveState, ChessGameState.REMIS_BY_FIFTY, Side.BLACK, 121, 50, "7R/8/8/4k3/8/8/P3K3/8 b - - 50 121");
    }

    @Test
    public void testRulesFiftyMovesLift() {
        String fen = "8/8/8/4k3/8/8/P3K2R/8 w - - 49 121";

        ChessGame game = new ChessGame(new FenFormat(fen));
        ChessGameState moveState = game.applyMove(new Move(Square.A2, Square.A3));

        assertGameState(game, moveState, ChessGameState.GAME_OPEN, Side.BLACK, 121, 0, "8/8/8/4k3/8/P7/4K2R/8 b - - 0 121");
    }

    @Test
    public void testRulesRemisByMaterial() {
        String fen = "8/8/8/4k3/8/8/4Kr2/8 w - - 0 1";

        ChessGame game = new ChessGame(new FenFormat(fen));
        ChessGameState moveState = game.applyMove(new Move(Square.E2, Square.F2));


        assertGameState(game, moveState, ChessGameState.REMIS_BY_MATERIAL, Side.BLACK, 1, 0, "8/8/8/4k3/8/8/5K2/8 b - - 0 1");
    }


    // ***************** HELPERS ********************

    private void assertGameState(ChessGame game, ChessGameState moveChessGameState, ChessGameState expectedChessGameState, Side expectedSideToMove, int expectedMoveNumber, int fiftyMoveRuleCount, String expectedFen) {
        assertEquals(expectedChessGameState, moveChessGameState);
        assertEquals(expectedChessGameState, game.getCurrentGameState());

        assertEquals(fiftyMoveRuleCount, game.getFiftyMoveDrawRuleCount());
        assertEquals(expectedSideToMove, game.getCurrentSide());
        assertEquals(expectedMoveNumber, game.getCurrentMoveNumber());
        assertEquals(expectedFen, game.getFen().toString());
    }


    public void assertPgnFormat(PgnFormat pgnFormat1, PgnFormat pgnFormat2) {

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
