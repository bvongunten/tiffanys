package ch.nostromo.tiffanys.commons.move;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.Side;
import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.board.Piece;
import ch.nostromo.tiffanys.commons.formats.FenFormat;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static ch.nostromo.tiffanys.commons.board.Square.*;
import static org.junit.Assert.assertEquals;

public class MoveUtilsTest {

    @Test
    public void testMove2SanLongCastling() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        Board board = new Board(fen);

        Move move = new Move(Castling.WHITE_LONG);

        assertEquals("O-O-O", MoveUtils.move2San(move, board, Side.WHITE));
    }

    @Test
    public void testMove2SanShortCastling() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        Board board = new Board(fen);

        Move move = new Move(Castling.WHITE_SHORT);

        assertEquals("O-O", MoveUtils.move2San(move, board, Side.WHITE));
    }

    @Test
    public void testMove2SanPawnOpening() {
        FenFormat fen = new FenFormat("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        Board board = new Board(fen);

        Move move = new Move(E2, E4);

        assertEquals("e4", MoveUtils.move2San(move, board, Side.WHITE));
    }

    @Test
    public void testMove2SanPawnSameRow() {
        FenFormat fen = new FenFormat("rnbqkbnr/ppp1pppp/8/3p4/2P1P3/8/PPP2PPP/RNBQKBNR w KQkq - 0 1");
        Board board = new Board(fen);

        Move move = new Move(E4, D5);

        assertEquals("exd5", MoveUtils.move2San(move, board, Side.WHITE));
    }

    @Test
    public void testMove2SanKnightOpening() {
        FenFormat fen = new FenFormat("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        Board board = new Board(fen);

        Move move = new Move(B1, C3);

        assertEquals("Nc3", MoveUtils.move2San(move, board, Side.WHITE));
    }

    @Test
    public void testMove2SanFullCoordsNeeded() {
        FenFormat fen = new FenFormat("7k/8/3Q4/8/8/Q2b4/2QQ4/K7 w - - 0 1");
        Board board = new Board(fen);

        Move move = new Move(D2, D3);

        assertEquals("Qd2xd3", MoveUtils.move2San(move, board, Side.WHITE));
    }

    @Test
    public void testMove2SanRowNeeded() {
        FenFormat fen = new FenFormat("7k/8/3Q4/8/8/3b4/3Q4/K7 w - - 0 1");
        Board board = new Board(fen);

        Move move = new Move(D2, D3);

        assertEquals("Q2xd3", MoveUtils.move2San(move, board, Side.WHITE));
    }

    @Test
    public void testMove2SanColNeeded() {
        FenFormat fen = new FenFormat("7k/8/3Q4/8/8/Q2b4/8/K7 w - - 0 1");
        Board board = new Board(fen);

        Move move = new Move(A3, D3);

        assertEquals("Qaxd3", MoveUtils.move2San(move, board, Side.WHITE));
    }

    @Test
    public void testMove2SanPromotion() {
        FenFormat fen = new FenFormat("7k/8/3Q4/8/8/Q2b4/8/K7 w - - 0 1");
        Board board = new Board(fen);

        Move move = new Move(A3, D3);

        assertEquals("Qaxd3", MoveUtils.move2San(move, board, Side.WHITE));
    }

    @Test
    public void testMove2SanPromotionAndCheck() {
        FenFormat fen = new FenFormat("7k/3P4/8/8/8/8/8/K7 w - - 0 1");
        Board board = new Board(fen);

        Move move = new Move(D7, D8, Piece.WHITE_QUEEN);

        assertEquals("d8=Q+", MoveUtils.move2San(move, board, Side.WHITE));
    }

    @Test
    public void testMove2SanPromotionAndMate() {
        FenFormat fen = new FenFormat("rnb5/pp3Ppk/5nNp/b7/2B5/8/PP3PPP/R1Bq1RK1 w - - 0 17");
        Board board = new Board(fen);

        Move move = new Move(F7, F8, Piece.WHITE_KNIGHT);

        assertEquals("f8=N#", MoveUtils.move2San(move, board, Side.WHITE));
    }

    @Test
    public void testSan2MoveLongCaslting() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        Board board = new Board(fen);

        Move result = MoveUtils.san2Move("O-O-O", board, Side.WHITE);
        Move expectedMove = new Move(Castling.WHITE_LONG);

        assertEquals(expectedMove, result);
    }

    @Test
    public void testSan2MoveShortCaslting() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        Board board = new Board(fen);

        Move result = MoveUtils.san2Move("O-O", board, Side.WHITE);
        Move expectedMove = new Move(Castling.WHITE_SHORT);

        assertEquals(expectedMove, result);
    }

    @Test
    public void testSan2MovePromotionAndMate() {
        FenFormat fen = new FenFormat("rnb5/pp3Ppk/5nNp/b7/2B5/8/PP3PPP/R1Bq1RK1 w - - 0 17");
        Board board = new Board(fen);

        Move result = MoveUtils.san2Move("f8=N#", board, Side.WHITE);
        Move expectedMove = new Move(F7, F8, Piece.WHITE_KNIGHT);

        assertEquals(expectedMove, result);
    }

    @Test
    public void testSan2MovePromotionAndCheck() {
        FenFormat fen = new FenFormat("7k/3P4/8/8/8/8/8/K7 w - - 0 1");
        Board board = new Board(fen);

        Move result = MoveUtils.san2Move("d8=Q+", board, Side.WHITE);
        Move expectedMove = new Move(D7, D8, Piece.WHITE_QUEEN);

        assertEquals(expectedMove, result);
    }

    @Test
    public void testSan2MovePawnOpening() {
        FenFormat fen = new FenFormat("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        Board board = new Board(fen);

        Move result = MoveUtils.san2Move("e4", board, Side.WHITE);
        Move expectedMove = new Move(E2, E4);

        assertEquals(expectedMove, result);
    }

    @Test
    public void testSan2MovePawnSameRow() {
        FenFormat fen = new FenFormat("rnbqkbnr/ppp1pppp/8/3p4/2P1P3/8/PPP2PPP/RNBQKBNR w KQkq - 0 1");
        Board board = new Board(fen);

        Move result = MoveUtils.san2Move("e4xd5", board, Side.WHITE);
        Move expectedMove = new Move(E4, D5);

        assertEquals(expectedMove, result);
    }

    @Test
    public void testSan2MoveFullCoordsNeeded() {
        FenFormat fen = new FenFormat("7k/8/3Q4/8/8/Q2b4/2QQ4/K7 w - - 0 1");
        Board board = new Board(fen);

        Move result = MoveUtils.san2Move("Qd2xd3", board, Side.WHITE);
        Move expectedMove = new Move(D2, D3);

        assertEquals(expectedMove, result);
    }


    @Test
    public void testPvGeneration() {
        Move testee = new Move(E2, E4);

        Side colorToMove = Side.WHITE;
        double score = 3.5;

        int mateIn = 1;
        int nodes = 200;
        int cutOffs = 50000;
        int plannedDepth = 5;
        int maxDepth = 10;
        long timeMs = 5123;

        List<Move> pv = new ArrayList<>();
        pv.add(new Move(F7, F5));
        pv.add(new Move(B1, C3));
        pv.add(new Move(G7, G5));
        pv.add(new Move(D1, H5));

        MoveAttributes moveAttributes = new MoveAttributes(colorToMove, score, mateIn, nodes, cutOffs, plannedDepth, maxDepth, timeMs, pv);
        testee.setMoveAttributes(moveAttributes);

        ChessGame game = new ChessGame();

        String result = MoveUtils.generateSanPrincipalVariation(testee, game.getCurrentBoard(), game.getCurrentSide());
        assertEquals("e4 f5 Nc3 g5 Qh5#", result);

    }
}
