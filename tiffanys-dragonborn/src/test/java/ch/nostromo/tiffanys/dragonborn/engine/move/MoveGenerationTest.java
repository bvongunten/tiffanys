package ch.nostromo.tiffanys.dragonborn.engine.move;


import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.format.FenFormat;
import ch.nostromo.tiffanys.dragonborn.BaseTest;
import ch.nostromo.tiffanys.dragonborn.engine.board.Board;
import ch.nostromo.tiffanys.dragonborn.engine.board.PieceUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveGenerationTest extends BaseTest {

    // Perft to check (a lot) of correct move generation by numbers

    @Test
    void testStartingPosition() {
        assertPerft("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", false,
                20L, 400L, 8902L, 197281L);
    }

    @Test
    void testKiwiPete() {
        assertPerft("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1", false,
                48L, 2039L, 97862L);
    }


    @Test
    void testPosition3() {
        assertPerft("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1", false,
                14L, 191L, 2812L, 43238L);
    }


    @Test
    void testPosition4() {
        assertPerft("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1", false,
                6L, 264L, 9467L);
    }


    @Test
    void testStartingPosition20Moves() {
        assertEquals(20, generateLegalMoves("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1").size());
    }

    @Test
    void testPawnDoublePush() {
        MoveList ml = generateLegalMoves("4k3/8/8/8/8/8/P7/4K3 w - - 0 1");
        assertTrue(containsUci(ml,  "a2a4"));
    }

    @Test
    void testPawnPromotion() {
        MoveList ml = generateLegalMoves("4k3/P7/8/8/8/8/8/4K3 w - - 0 1");
        assertTrue(containsUci(ml,  "a7a8q"));
        assertTrue(containsUci(ml,  "a7a8n"));
    }

    @Test
    void testEnPassant() {
        assertTrue(containsUci(generateLegalMoves("4k3/8/8/8/3Pp3/8/8/4K3 b - d3 0 1"), "e4d3"));
    }

    @Test
    void testCastleBothSides() {
        MoveList ml = generateLegalMoves("4k3/8/8/8/8/8/8/R3K2R w KQ - 0 1");
        assertTrue(containsUci(ml,  "e1g1"));
        assertTrue(containsUci(ml,  "e1c1"));
    }

    @Test
    void testCannotCastleThroughCheck() {
        assertFalse(containsUci(generateLegalMoves("4kr2/8/8/8/8/8/8/R3K2R w KQ - 0 1"), "e1g1"));
    }

    @Test
    void testCannotCastleInCheck() {
        MoveList ml = generateLegalMoves("4r3/8/8/8/8/8/8/R3K2R w KQ - 0 1");
        assertFalse(containsUci(ml,  "e1g1"));
        assertFalse(containsUci(ml,  "e1c1"));
    }

    @Test
    void testCheckmate() {
        assertEquals(0, generateLegalMoves("rnb1kbnr/pppp1ppp/8/4p3/6Pq/5P2/PPPPP2P/RNBQKBNR w KQkq - 1 3").size());
    }

    @Test
    void testStalemate() {
        assertEquals(0, generateLegalMoves("7k/5Q2/6K1/8/8/8/8/8 b - - 0 1").size());
    }

    @Test
    void testMustRespondToCheck() {
        Board b = new Board(new ChessGame(new FenFormat("4q3/8/8/8/8/8/8/4K3 w - - 0 1")));
        MoveList ml = new MoveList();
        MoveGenerator.generateLegalMoves(b, ml);

        for (int i = 0; i < ml.size(); i++) {
            long u = b.makeMove(ml.get(i));
            assertFalse(b.isSquareAttacked(b.kingSquare(PieceUtils.WHITE), PieceUtils.BLACK));
            b.unmakeMove(ml.get(i), u);
        }
    }

    /**
     * Check whether the list contains a move with the given UCI string (e.g. "e2e4").
     */
    private static boolean containsUci(MoveList moveList, String uci) {
        for (int i = 0; i < moveList.size; i++) {
            if (toUci(moveList.get(i)).equals(uci)) return true;
        }
        return false;
    }
}
