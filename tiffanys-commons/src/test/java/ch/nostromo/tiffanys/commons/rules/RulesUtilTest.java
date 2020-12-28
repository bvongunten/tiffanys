package ch.nostromo.tiffanys.commons.rules;

import ch.nostromo.tiffanys.commons.TestHelper;
import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.fen.FenFormat;
import ch.nostromo.tiffanys.commons.move.Move;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class RulesUtilTest extends TestHelper {

    @Test
    public void checkLegalMovesOppening() throws Exception {
        FenFormat fen = new FenFormat("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        Board board = new Board(fen);
        List<Move> moves = RulesUtil.getLegalMoves(board, GameColor.WHITE);

        this.checkAndDeleteMove(moves, new Move("A2", "A3"));
        this.checkAndDeleteMove(moves, new Move("A2", "A4"));
        this.checkAndDeleteMove(moves, new Move("B2", "B3"));
        this.checkAndDeleteMove(moves, new Move("B2", "B4"));
        this.checkAndDeleteMove(moves, new Move("C2", "C3"));
        this.checkAndDeleteMove(moves, new Move("C2", "C4"));
        this.checkAndDeleteMove(moves, new Move("D2", "D3"));
        this.checkAndDeleteMove(moves, new Move("D2", "D4"));
        this.checkAndDeleteMove(moves, new Move("E2", "E3"));
        this.checkAndDeleteMove(moves, new Move("E2", "E4"));
        this.checkAndDeleteMove(moves, new Move("F2", "F3"));
        this.checkAndDeleteMove(moves, new Move("F2", "F4"));
        this.checkAndDeleteMove(moves, new Move("G2", "G3"));
        this.checkAndDeleteMove(moves, new Move("G2", "G4"));
        this.checkAndDeleteMove(moves, new Move("H2", "H3"));
        this.checkAndDeleteMove(moves, new Move("H2", "H4"));

        this.checkAndDeleteMove(moves, new Move("B1", "A3"));
        this.checkAndDeleteMove(moves, new Move("B1", "C3"));
        this.checkAndDeleteMove(moves, new Move("G1", "H3"));
        this.checkAndDeleteMove(moves, new Move("G1", "F3"));

        this.checkRemainingMoves(moves);
    }

    @Test
    public void checkLegalMovesInCheck() throws Exception {
        FenFormat fen = new FenFormat("5b2/1p1b1p2/5kp1/P1n1p3/3pP2p/P1pP1P2/4q1r1/B3RK2 w - - 0 39");
        Board board = new Board(fen);
        List<Move> moves = RulesUtil.getLegalMoves(board, GameColor.WHITE);

        this.checkAndDeleteMove(moves, new Move("E1", "E2"));

        this.checkRemainingMoves(moves);
    }

    @Test
    public void testIsCheck() throws Exception {
        FenFormat fen = new FenFormat("5b2/1p1b1p2/5kp1/P1n1p3/3pP2p/P1pP1P2/4q1r1/B3RK2 w - - 0 39");
        Board board = new Board(fen);
        assertTrue(RulesUtil.isInCheck(board, GameColor.WHITE));

    }

    @Test
    public void testIsMate() throws Exception {
        FenFormat fen = new FenFormat("5b2/1p1b1p2/5kp1/P1n1p3/3pP2p/P1pP1P2/5qr1/B3RK2 w - - 0 39");
        Board board = new Board(fen);
        assertTrue(RulesUtil.isMate(board, GameColor.WHITE));

    }

    @Test
    public void testIsMateBecauseOfKing() throws Exception {
        FenFormat fen = new FenFormat("2K5/1pq5/p2k4/P7/8/8/8/8 w - - 0 66");
        Board board = new Board(fen);
        assertTrue(RulesUtil.isMate(board, GameColor.WHITE));

    }

    @Test
    public void testLeadsToCheck() throws Exception {
        FenFormat fen = new FenFormat("5b2/1p1b1p2/5kp1/P1n1p3/3pP2p/P1pP1P2/3q2r1/B3RK2 b - - 0 38");
        Board board = new Board(fen);

        Move move = new Move("D2", "E2");

        assertTrue(RulesUtil.leadsToCheck(move, board, GameColor.BLACK));
    }

    @Test
    public void testLeadsToMate() throws Exception {
        FenFormat fen = new FenFormat("5b2/1p1b1p2/5kp1/P1n1p3/3pP2p/P1pP1P2/3q2r1/B3RK2 b - - 0 38");
        Board board = new Board(fen);

        Move move = new Move("D2", "F2");

        assertTrue(RulesUtil.leadsToMate(move, board, GameColor.BLACK));
    }

    @Test
    public void testIsCheckIssue() throws CloneNotSupportedException {
        FenFormat fen = new FenFormat("8/3R4/6rp/1p1kn3/3b1P2/6P1/7K/3q4 b - - 0 1");
        Board board = new Board(fen);

        assertTrue(RulesUtil.isInCheck(board, GameColor.BLACK));

    }

    @Test
    public void testIsCheckIssue2() throws CloneNotSupportedException {
        FenFormat fen = new FenFormat("8/6pp/p1k5/1p1pb3/4r3/1P3nPb/PPN2PK1/3R4 w - - 0 33");
        Board board = new Board(fen);

        assertTrue(RulesUtil.isInCheck(board, GameColor.WHITE));

    }

}
