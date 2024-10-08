package ch.nostromo.tiffanys.commons.pgn;

import ch.nostromo.tiffanys.commons.TestHelper;
import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.enums.Castling;
import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.enums.Piece;
import ch.nostromo.tiffanys.commons.fen.FenFormat;
import ch.nostromo.tiffanys.commons.move.Move;
import org.junit.Test;

import static ch.nostromo.tiffanys.commons.enums.Coordinates.A3;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.B1;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.C3;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.D2;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.D3;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.D5;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.D7;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.D8;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.E2;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.E4;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.F7;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.F8;
import static org.junit.Assert.assertEquals;

public class SanUtilTest extends TestHelper {

    @Test
    public void testMove2SanLongCastling() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        Board board = new Board(fen);

        Move move = new Move(Castling.WHITE_LONG);

        assertEquals("O-O-O", SanUtil.move2San(move, board, GameColor.WHITE));
    }

    @Test
    public void testMove2SanShortCastling() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        Board board = new Board(fen);

        Move move = new Move(Castling.WHITE_SHORT);

        assertEquals("O-O", SanUtil.move2San(move, board, GameColor.WHITE));
    }

    @Test
    public void testMove2SanPawnOpening() {
        FenFormat fen = new FenFormat("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        Board board = new Board(fen);

        Move move = new Move(E2, E4);

        assertEquals("e4", SanUtil.move2San(move, board, GameColor.WHITE));
    }

    @Test
    public void testMove2SanPawnSameRow() {
        FenFormat fen = new FenFormat("rnbqkbnr/ppp1pppp/8/3p4/2P1P3/8/PPP2PPP/RNBQKBNR w KQkq - 0 1");
        Board board = new Board(fen);

        Move move = new Move(E4, D5);

        assertEquals("exd5", SanUtil.move2San(move, board, GameColor.WHITE));
    }

    @Test
    public void testMove2SanKnightOpening() {
        FenFormat fen = new FenFormat("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        Board board = new Board(fen);

        Move move = new Move(B1, C3);

        assertEquals("Nc3", SanUtil.move2San(move, board, GameColor.WHITE));
    }

    @Test
    public void testMove2SanFullCoordsNeeded() {
        FenFormat fen = new FenFormat("7k/8/3Q4/8/8/Q2b4/2QQ4/K7 w - - 0 1");
        Board board = new Board(fen);

        Move move = new Move(D2, D3);

        assertEquals("Qd2xd3", SanUtil.move2San(move, board, GameColor.WHITE));
    }

    @Test
    public void testMove2SanRowNeeded() {
        FenFormat fen = new FenFormat("7k/8/3Q4/8/8/3b4/3Q4/K7 w - - 0 1");
        Board board = new Board(fen);

        Move move = new Move(D2, D3);

        assertEquals("Q2xd3", SanUtil.move2San(move, board, GameColor.WHITE));
    }

    @Test
    public void testMove2SanColNeeded() {
        FenFormat fen = new FenFormat("7k/8/3Q4/8/8/Q2b4/8/K7 w - - 0 1");
        Board board = new Board(fen);

        Move move = new Move(A3, D3);

        assertEquals("Qaxd3", SanUtil.move2San(move, board, GameColor.WHITE));
    }

    @Test
    public void testMove2SanPromotion() {
        FenFormat fen = new FenFormat("7k/8/3Q4/8/8/Q2b4/8/K7 w - - 0 1");
        Board board = new Board(fen);

        Move move = new Move(A3, D3);

        assertEquals("Qaxd3", SanUtil.move2San(move, board, GameColor.WHITE));
    }

    @Test
    public void testMove2SanPromotionAndCheck() {
        FenFormat fen = new FenFormat("7k/3P4/8/8/8/8/8/K7 w - - 0 1");
        Board board = new Board(fen);

        Move move = new Move(D7, D8, Piece.QUEEN);

        assertEquals("d8=Q+", SanUtil.move2San(move, board, GameColor.WHITE));
    }

    @Test
    public void testMove2SanPromotionAndMate() {
        FenFormat fen = new FenFormat("rnb5/pp3Ppk/5nNp/b7/2B5/8/PP3PPP/R1Bq1RK1 w - - 0 17");
        Board board = new Board(fen);

        Move move = new Move(F7, F8, Piece.KNIGHT);

        assertEquals("f8=N#", SanUtil.move2San(move, board, GameColor.WHITE));
    }

    @Test
    public void testSan2MoveLongCaslting() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        Board board = new Board(fen);

        Move result = SanUtil.san2Move("O-O-O", board, GameColor.WHITE);
        Move expectedMove = new Move(Castling.WHITE_LONG);

        assertEquals(expectedMove, result);
    }

    @Test
    public void testSan2MoveShortCaslting() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        Board board = new Board(fen);

        Move result = SanUtil.san2Move("O-O", board, GameColor.WHITE);
        Move expectedMove = new Move(Castling.WHITE_SHORT);

        assertEquals(expectedMove, result);
    }

    @Test
    public void testSan2MovePromotionAndMate() {
        FenFormat fen = new FenFormat("rnb5/pp3Ppk/5nNp/b7/2B5/8/PP3PPP/R1Bq1RK1 w - - 0 17");
        Board board = new Board(fen);

        Move result = SanUtil.san2Move("f8=N#", board, GameColor.WHITE);
        Move expectedMove = new Move(F7, F8, Piece.KNIGHT);

        assertEquals(expectedMove, result);
    }

    @Test
    public void testSan2MovePromotionAndCheck() {
        FenFormat fen = new FenFormat("7k/3P4/8/8/8/8/8/K7 w - - 0 1");
        Board board = new Board(fen);

        Move result = SanUtil.san2Move("d8=Q+", board, GameColor.WHITE);
        Move expectedMove = new Move(D7, D8, Piece.QUEEN);

        assertEquals(expectedMove, result);
    }

    @Test
    public void testSan2MovePawnOpening() {
        FenFormat fen = new FenFormat("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        Board board = new Board(fen);

        Move result = SanUtil.san2Move("e4", board, GameColor.WHITE);
        Move expectedMove = new Move(E2, E4);

        assertEquals(expectedMove, result);
    }

    @Test
    public void testSan2MovePawnSameRow() {
        FenFormat fen = new FenFormat("rnbqkbnr/ppp1pppp/8/3p4/2P1P3/8/PPP2PPP/RNBQKBNR w KQkq - 0 1");
        Board board = new Board(fen);

        Move result = SanUtil.san2Move("e4xd5", board, GameColor.WHITE);
        Move expectedMove = new Move(E4, D5);

        assertEquals(expectedMove, result);
    }

    @Test
    public void testSan2MoveFullCoordsNeeded() {
        FenFormat fen = new FenFormat("7k/8/3Q4/8/8/Q2b4/2QQ4/K7 w - - 0 1");
        Board board = new Board(fen);

        Move result = SanUtil.san2Move("Qd2xd3", board, GameColor.WHITE);
        Move expectedMove = new Move(D2, D3);

        assertEquals(expectedMove, result);
    }

}
