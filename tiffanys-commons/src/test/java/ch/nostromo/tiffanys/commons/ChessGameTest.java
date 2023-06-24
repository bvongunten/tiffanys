package ch.nostromo.tiffanys.commons;

import ch.nostromo.tiffanys.commons.board.Square;
import ch.nostromo.tiffanys.commons.exception.GameAlreadyDecidedException;
import ch.nostromo.tiffanys.commons.exception.IllegalMoveException;
import ch.nostromo.tiffanys.commons.format.FenFormat;
import ch.nostromo.tiffanys.commons.format.PgnFormat;
import ch.nostromo.tiffanys.commons.format.pgn.PgnSplitter;
import ch.nostromo.tiffanys.commons.move.Move;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ChessGameTest extends BaseTest {

    @Test
    void testGameInitDefault() {
        ChessGame game = new ChessGame();
        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", game.createFen().toString());
    }



    @ParameterizedTest(name = "Game {index}: {0}")
    @MethodSource("providePgnGames")
    void testReconstruction(String testName, String pgn) {
        PgnFormat pgnFormat = new PgnFormat(pgn);
        ChessGame chessGame = new ChessGame(pgnFormat);
        assertEqualsIgnoringLineEndings(pgn, chessGame.createPgn().toString(), testName);
    }


    @Test
    void testGameInitPgn() {
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
        PgnFormat pgnFormatOut = game.createPgn();

        assertPgnFormat(pgnFormatIn, pgnFormatOut);
    }

    @Test
    void testGameInitExtendedPgn() {
        String pgnInput = new StringBuilder().
                append("[Event \"Rated Blitz game\"]\n").
                append("[Site \"https://lichess.org/mxVSED7u\"]\n").
                append("[Date \"2020.07.13\"]\n").
                append("[White \"bvongunten\"]\n").
                append("[Black \"Ashavakhi\"]\n").
                append("[Result \"0-1\"]\n").
                append("[UTCDate \"2020.07.13\"]\n").
                append("[UTCTime \"22:10:07\"]\n").
                append("[WhiteElo \"1285\"]\n").
                append("[BlackElo \"1324\"]\n").
                append("[WhiteRatingDiff \"-22\"]\n").
                append("[BlackRatingDiff \"+6\"]\n").
                append("[Variant \"Standard\"]\n").
                append("[TimeControl \"300+0\"]\n").
                append("[ECO \"C44\"]\n").
                append("[Termination \"Normal\"]\n").
                append("\n").
                append("1.e4 e5 0-1\n").
                toString();

        PgnFormat pgnFormatIn = new PgnFormat(pgnInput);
        ChessGame game = new ChessGame(pgnFormatIn);
        PgnFormat pgnFormatOut = game.createPgn();

        assertPgnFormat(pgnFormatIn, pgnFormatOut);
    }

    @Test
    void testGameStartingFromFen() {
        String pgnInput = new StringBuilder().
                append("[Event \"?\"]\n").
                append("[Site \"?\"]\n").
                append("[Date \"????.??.??\"]\n").
                append("[Round \"?\"]\n").
                append("[White \"Neue Partie\"]\n").
                append("[Black \"?\"]\n").
                append("[Result \"*\"]\n").
                append("[WhiteFideId \"-1\"]\n").
                append("[WhiteFideId \"-1\"]\n").
                append("[SetUp \"1\"]\n").
                append("[FEN \"8/5P2/8/8/8/2k5/5K2/8 w - - 0 0\"]\n").
                append("[PlyCount \"0\"]\n").
                append("[GameId \"2240952013834830\"]\n").
                append("\n").
                append("*").toString();

        PgnFormat pgnFormatIn = new PgnFormat(pgnInput);
        ChessGame game = new ChessGame(pgnFormatIn);

        FenFormat fen = game.createFen();
        assertEquals("8/5P2/8/8/8/2k5/5K2/8 w - - 0 1", fen.toString());

        PgnFormat pgnFormatOut = game.createPgn();
        assertPgnFormat(pgnFormatIn, pgnFormatOut);
    }

    @Test
    void testGameFinishedFromFenBlack() {
        String pgnInput = new StringBuilder().
                append("[Event \"?\"]\n").
                append("[Site \"?\"]\n").
                append("[Date \"????.??.??\"]\n").
                append("[Round \"?\"]\n").
                append("[White \"Allrounder\"]\n").
                append("[Black \"von Gunten, Bernhard\"]\n").
                append("[Result \"0-1\"]\n").
                append("[WhiteFideId \"-1\"]\n").
                append("[WhiteFideId \"-1\"]\n").
                append("[SetUp \"1\"]\n").
                append("[FEN \"8/8/4p3/p7/P6p/K1n4P/1pk5/8 b - - 0 57\"]\n").
                append("[PlyCount \"1\"]\n").
                append("[GameId \"2240957707238006\"]\n").
                append("\n").
                append("57... b1=N# 0-1").
                toString();

        PgnFormat pgnFormatIn = new PgnFormat(pgnInput);
        ChessGame game = new ChessGame(pgnFormatIn);

        FenFormat fen = game.createFen();
        assertEquals("8/8/4p3/p7/P6p/K1n4P/2k5/1n6 w - - 0 56", fen.toString());

        PgnFormat pgnFormatOut = game.createPgn();
        assertPgnFormat(pgnFormatIn, pgnFormatOut);
    }


    @Test
    void testApplyMove() {
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
    void testUnexpectedApplyMoveDecidedGame() {
        ChessGame game = new ChessGame();
        game.setGameEndIndicator(ChessGameState.BLACK_WIN_BY_RESIGNATION);

        Move move = new Move(Square.E7, Square.E4);

        GameAlreadyDecidedException expectedException = assertThrows(GameAlreadyDecidedException.class, () -> game.applyMove(move));

        assertEquals(ChessGameState.BLACK_WIN_BY_RESIGNATION, expectedException.getChessGameState());
    }


    @Test
    void testTakeMoveBack() {
        ChessGame game = new ChessGame();

        game.applyMove(new Move(Square.G1, Square.F3));
        game.applyMove(new Move(Square.G8, Square.F6));

        game.takeBackMove();
        game.takeBackMove();

        assertGameState(game, ChessGameState.GAME_OPEN, ChessGameState.GAME_OPEN, Side.WHITE, 0, 0, FenFormat.INITIAL_FEN.toString());
    }

    @Test
    void testTakeMoveBackException() {
        ChessGame game = new ChessGame();
        game.applyMove(new Move(Square.G1, Square.F3));
        game.takeBackMove();

        // Trigger exception
        assertThrows(IllegalStateException.class, game::takeBackMove);
    }


    @Test
    void testApplyMoveIllegal() {
        ChessGame game = new ChessGame();
        Move move = new Move(Square.A1, Square.A8);
        assertThrows(IllegalMoveException.class, () -> {
            game.applyMove(move);
        });
    }

    @ParameterizedTest
    @MethodSource("pgnInputs")
    void testPgnFormat(String pgnInput) {
        PgnFormat pgnFormatIn = new PgnFormat(pgnInput);
        ChessGame game = new ChessGame(pgnFormatIn);
        PgnFormat pgnFormatOut = game.createPgn();
        assertPgnFormat(pgnFormatIn, pgnFormatOut);
    }

    static Stream<String> pgnInputs() {
        return Stream.of(
                """
                        [Site "?"]
                        [Date "2007.08.14"]
                        [Round "-"]
                        [White "Tiffanys Two"]
                        [Black "Tiffanys Three"]
                        [Result "1/2-1/2"]
                        
                        1.e4 d5 1/2-1/2
                        """,
                """
                        [Site "?"]
                        [Date "2007.08.14"]
                        [Round "-"]
                        [White "Tiffanys Two"]
                        [Black "Tiffanys Three"]
                        [Result "1-0"]
                        
                        1.e4 d5 1-0
                        """,
                """
                        [Site "?"]
                        [Date "2007.08.14"]
                        [Round "-"]
                        [White "Tiffanys Two"]
                        [Black "Tiffanys Three"]
                        [Result "0-1"]
                        
                        1.e4 d5 0-1
                        """,
                """
                        [Site "?"]
                        [Date "2007.08.14"]
                        [Round "-"]
                        [White "Tiffanys Two"]
                        [Black "Tiffanys Three"]
                        [Result "*"]
                        
                        1.e4 d5 *
                        """
        );
    }

    @Test
    void testGameStateMateByFen() {
        ChessGame game = new ChessGame(new FenFormat("5b2/1p1b1p2/5kp1/P1n1p3/3pP2p/P1pP1P2/5qr1/B3RK2 w - - 0 39"));
        assertEquals(ChessGameState.BLACK_WIN, game.calculateCurrentGameState());
    }

    @Test
    void testGameStateByManualDecision() {
        ChessGame game = new ChessGame();
        game.setGameEndIndicator(ChessGameState.DRAW_BY_FIFTY);
        assertEquals(ChessGameState.DRAW_BY_FIFTY, game.calculateCurrentGameState());
    }

    @Test
    void testRulesThreeFoldRepetition() {
        ChessGame game = new ChessGame();

        game.applyMove(new Move(Square.G1, Square.F3));
        game.applyMove(new Move(Square.G8, Square.F6));
        game.applyMove(new Move(Square.F3, Square.G1));
        game.applyMove(new Move(Square.F6, Square.G8));

        game.applyMove(new Move(Square.G1, Square.F3));
        game.applyMove(new Move(Square.G8, Square.F6));
        game.applyMove(new Move(Square.F3, Square.G1));

        ChessGameState moveState = game.applyMove(new Move(Square.F6, Square.G8));

        assertGameState(game, moveState, ChessGameState.DRAW_BY_THREE, Side.WHITE, 4, 8, "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 8 4");
    }

    @Test
    void testRulesThreeFoldRepetitionByPgn() {

        String pgnInput = new StringBuilder().
                append("[Event \"?\"]\n").
                append("[White \"Neue Partie\"]\n").
                append("[Black \"?\"]\n").
                append("[Site \"?\"]\n").
                append("[Round \"?\"]\n").
                append("[Result \"1/2-1/2\"]\n").
                append("[Date \"????.??.??\"]\n").
                append("[ECO \"A00\"]\n").
                append("[PlyCount \"8\"]\n").
                append("[GameId \"2138396560389063\"]\n").
                append("[SourceVersionDate \"2025.01.26\"]\n").
                append("\n").
                append("1. Nf3 Nf6 2. Ng1 Ng8 3. Nf3 Nf6 4. Ng1 *").
                toString();

        ChessGame game = new ChessGame(new PgnFormat(pgnInput));

        ChessGameState moveState = game.applyMove(new Move(Square.F6, Square.G8));
        assertGameState(game, moveState, ChessGameState.DRAW_BY_THREE, Side.WHITE, 4, 8, "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 8 4");

    }

    @Test
    void testRulesFiftyMovesByPgn() {
        String pgnInput = new StringBuilder().
                append("[Event \"23rd European Teams Women\"]\n").
                append("[Site \"Catez SLO\"]\n").
                append("[Date \"2021.11.18\"]\n").
                append("[EventDate \"2021.11.12\"]\n").
                append("[Round \"6.5\"]\n").
                append("[Result \"1/2-1/2\"]\n").
                append("[White \"Hanna Marie Klek\"]\n").
                append("[Black \"Zsoka Gaal\"]\n").
                append("[ECO \"B12\"]\n").
                append("[WhiteElo \"2343\"]\n").
                append("[BlackElo \"2336\"]\n").
                append("[PlyCount \"222\"]\n").
                append("\n").
                append("1. e4 c6 2. d4 d5 3. e5 c5 4. dxc5 e6 5. Nf3 Bxc5 6. Bd3 Ne7 7. O-O Ng6 8.\n").
                append("Qe2 O-O 9. c3 Nd7 10. b4 Be7 11. Na3 a5 12. Nc2 Qc7 13. Bxg6 fxg6 14. Ncd4\n").
                append("Nb6 15. Bg5 Bxg5 16. Nxg5 Qe7 17. f4 h6 18. Nh3 axb4 19. Rf3 bxc3 20. Rxc3\n").
                append("Nc4 21. Rg3 Qf7 22. Rb1 b6 23. Nf3 Ba6 24. Qe1 Bb7 25. Nh4 Rxa2 26. Rxg6 d4\n").
                append("27. f5 exf5 28. Nf4 Be4 29. e6 Qe8 30. Ra1 Rxa1 31. Qxa1 Ne5 32. Qxd4 Nxg6\n").
                append("33. Nhxg6 Rf6 34. Ne5 Rxe6 35. Qc4 b5 36. Qxe6+ Qxe6 37. Nxe6 b4 38. Nc5\n").
                append("Bd5 39. Kf2 Kf8 40. g3 Ke7 41. Ke3 Kd6 42. Kd4 b3 43. Na4 g5 44. Nc3 Bg8\n").
                append("45. Nd3 Bh7 46. Nd1 Ke6 47. Nc5+ Kf6 48. Nxb3 f4 49. Nd2 Kf5 50. Nf2 h5 51.\n").
                append("Nc4 fxg3 52. hxg3 Bg8 53. Ne3+ Kf6 54. Ne4+ Kg6 55. Ke5 Bb3 56. Nf2 Ba4 57.\n").
                append("Nd5 h4 58. g4 h3 59. Nxh3 Bd1 60. Nf2 Bxg4 61. Nxg4 Kh5 62. Ndf6+ Kh4 63.\n").
                append("Ke4 Kg3 64. Ke3 Kg2 65. Ne4 Kh3 66. Kf3 Kh4 67. Ng3 Kh3 68. Nh5 Kh4 69.\n").
                append("Nhf6 Kh3 70. Nf2+ Kh4 71. N6g4 Kh5 72. Kg3 Kg6 73. Nd3 Kf5 74. Kf3 Ke6 75.\n").
                append("Ke4 Kd6 76. Nb4 Kc5 77. Nd5 Kd6 78. Kd4 Ke6 79. Nde3 Kd6 80. Nf5+ Kc6 81.\n").
                append("Kc4 Kb6 82. Nd4 Kc7 83. Kc5 Kd7 84. Nb5 Ke7 85. Nd6 Kf8 86. Nf5 Kf7 87. Kd6\n").
                append("Kg6 88. Ng3 Kf7 89. Kd7 Kf8 90. Nh5 Kf7 91. Nhf6 Kg7 92. Ke7 Kg6 93. Ke6\n").
                append("Kg7 94. Nh5+ Kf8 95. Ng3 Ke8 96. Nf5 Kd8 97. Kd6 Ke8 98. Ne7 Kd8 99. Nc6+\n").
                append("Kc8 100. Na5 Kd8 101. Nb7+ Ke8 102. Ke6 Kf8 103. Nd6 Kg7 104. Kf5 Kf8 105.\n").
                append("Kf6 Kg8 106. Nf5 Kf8 107. Ng7 Kg8 108. Ne6 Kh8 109. Kf7 Kh7 110. Nf8+ Kh8\n").
                append("*\n").toString();

        ChessGame game = new ChessGame(new PgnFormat(pgnInput));
        assertEquals(ChessGameState.GAME_OPEN, game.calculateCurrentGameState());
        assertEquals(99, game.createFen().getHalfMoveClock());


        ChessGameState moveState = game.applyMove(new Move(Square.G4, Square.F6));
        assertGameState(game, moveState, ChessGameState.DRAW_BY_FIFTY, Side.BLACK, 111, 100, "5N1k/5K2/5N2/6p1/8/8/8/8 b - - 100 111");

    }


    @Test
    void testRulesFiftyMovesTrigger() {
        String fen = "8/8/8/4k3/8/8/P3K2R/8 w - - 99 121";

        ChessGame game = new ChessGame(new FenFormat(fen));
        ChessGameState moveState = game.applyMove(new Move(Square.H2, Square.H8));

        assertGameState(game, moveState, ChessGameState.DRAW_BY_FIFTY, Side.BLACK, 121, 100, "7R/8/8/4k3/8/8/P3K3/8 b - - 100 121");
    }

    @Test
    void testRulesFiftyMovesLift() {
        String fen = "8/8/8/4k3/8/8/P3K2R/8 w - - 99 121";

        ChessGame game = new ChessGame(new FenFormat(fen));
        ChessGameState moveState = game.applyMove(new Move(Square.A2, Square.A3));

        assertGameState(game, moveState, ChessGameState.GAME_OPEN, Side.BLACK, 121, 0, "8/8/8/4k3/8/P7/4K2R/8 b - - 0 121");
    }

    @Test
    void testRulesDrawByMaterial() {
        String fen = "8/8/8/4k3/8/8/4Kr2/8 w - - 0 1";

        ChessGame game = new ChessGame(new FenFormat(fen));
        ChessGameState moveState = game.applyMove(new Move(Square.E2, Square.F2));


        assertGameState(game, moveState, ChessGameState.DRAW_BY_MATERIAL, Side.BLACK, 1, 0, "8/8/8/4k3/8/8/5K2/8 b - - 0 1");
    }


    /**
     * Perft -> https://www.chessprogramming.org/Perft
     */
    @Test
    @Tag("deep")
    void deepGenPosition1() {

        // Starting position
        String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

        ChessGame game = new ChessGame(new FenFormat(fen));

        assertEquals(20L, perft(game, 1));
        assertEquals(400L, perft(game, 2));
        assertEquals(8902L, perft(game, 3));
    }

    @Test
    @Tag("deep")
    void deepGenPosition2() {

        // Kiwipete
        String fen ="r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1";

        ChessGame game = new ChessGame(new FenFormat(fen));

        assertEquals(48L, perft(game, 1));
        assertEquals(2039L, perft(game, 2));
        assertEquals(97862L, perft(game, 3));
    }

    @Test
    @Tag("deep")
    void deepGenPosition3() {
        String fen ="8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1 ";

        ChessGame game = new ChessGame(new FenFormat(fen));

        assertEquals(14L, perft(game, 1));
        assertEquals(191L, perft(game, 2));
        assertEquals(2812L, perft(game, 3));
        assertEquals(43238L, perft(game, 4));
    }


    @Test
    @Tag("deep")
    void deepGenPosition4() {
        String fen ="r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1";

        ChessGame game = new ChessGame(new FenFormat(fen));

        assertEquals(6L, perft(game, 1));
        assertEquals(264L, perft(game, 2));
        assertEquals(9467L, perft(game, 3));
    }


    @Test
    @Tag("deep")
    void deepGenPosition5() {
        String fen ="rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8";

        ChessGame game = new ChessGame(new FenFormat(fen));

        assertEquals(44L, perft(game, 1));
        assertEquals(1486L, perft(game, 2));
        assertEquals(62379L, perft(game, 3));
    }





    // ***************** HELPERS ********************


    private static long perft(ChessGame game, int depth) {
        if (depth == 0) return 1L;
        return perftInternal(game, depth);
    }

    private static long perftInternal(ChessGame game, int depth) {
        if (depth == 1) return game.getLegalMoves().size();

        // Recurse for each legal move
        long nodes = 0;
        for (Move move : game.getLegalMoves()) {
            game.applyMove(move);
            nodes += perftInternal(game, depth - 1);
            game.takeBackMove();
        }

        return nodes;
    }

    private static Stream<org.junit.jupiter.params.provider.Arguments> providePgnGames() throws IOException {
        String pgnFile;
        try (var inputStream = ChessGameTest.class.getClassLoader().getResourceAsStream("GameCollection.pgn")) {
            Assertions.assertNotNull(inputStream);
            pgnFile = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
        List<String> pgns = PgnSplitter.splitPgnCollection(pgnFile);

        return Stream.iterate(0, i -> i < pgns.size(), i -> i + 1)
                .map(i -> org.junit.jupiter.params.provider.Arguments.of(
                        "PGN Game " + (i + 1),
                        pgns.get(i)
                ));
    }


    private void assertGameState(ChessGame game, ChessGameState moveChessGameState, ChessGameState
            expectedChessGameState, Side expectedSideToMove, int expectedMoveNumber, int fiftyMoveRuleCount, String
                                         expectedFen) {
        assertEquals(expectedChessGameState, moveChessGameState);
        assertEquals(expectedChessGameState, game.calculateCurrentGameState());

        assertEquals(fiftyMoveRuleCount, game.getFiftyMoveDrawRuleCount());
        assertEquals(expectedSideToMove, game.getCurrentSide());
        assertEquals(expectedMoveNumber, game.getCurrentMoveNumber());
        assertEquals(expectedFen, game.createFen().toString());
    }


    void assertPgnFormat(PgnFormat pgnFormat1, PgnFormat pgnFormat2) {

        assertEquals(pgnFormat1.getChessGameInformation().getEvent(), pgnFormat2.getChessGameInformation().getEvent());
        assertEquals(pgnFormat1.getChessGameInformation().getSite(), pgnFormat2.getChessGameInformation().getSite());
        assertEquals(pgnFormat1.getChessGameInformation().getDate(), pgnFormat2.getChessGameInformation().getDate());
        assertEquals(pgnFormat1.getChessGameInformation().getRound(), pgnFormat2.getChessGameInformation().getRound());
        assertEquals(pgnFormat1.getChessGameInformation().getWhite(), pgnFormat2.getChessGameInformation().getWhite());
        assertEquals(pgnFormat1.getChessGameInformation().getBlack(), pgnFormat2.getChessGameInformation().getBlack());
        assertEquals(pgnFormat1.getChessGameInformation().getResult(), pgnFormat2.getChessGameInformation().getResult());

        assertEquals(pgnFormat1.getChessGameInformation().getChessGameState(), pgnFormat2.getChessGameInformation().getChessGameState());


        String toString1 = pgnFormat1.toString();
        String toString2 = pgnFormat2.toString();
        assertEquals(toString1, toString2);
    }

}
