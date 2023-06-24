package ch.nostromo.tiffanys.dragonborn.engine.transposition;


import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.format.FenFormat;
import ch.nostromo.tiffanys.dragonborn.engine.board.Board;
import ch.nostromo.tiffanys.dragonborn.engine.move.MoveGenerator;
import ch.nostromo.tiffanys.dragonborn.engine.move.MoveList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ZobristUtilsTest {

    @Test
    void testDeterministicHash() {
        assertEquals(new Board(new ChessGame(new FenFormat("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"))).getHash(), new Board(new ChessGame(new FenFormat("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"))).getHash());
    }

    @Test
    void testDifferentPositions() {
        assertNotEquals(new Board(new ChessGame(new FenFormat("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"))).getHash(), new Board(new ChessGame(new FenFormat("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1"))).getHash());
    }

    @Test void testMakeAndUnMakeBoardHashes() {
        Board board = new Board(new ChessGame(new FenFormat("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1")));
        long original = board.getHash();
        MoveList ml = new MoveList(); MoveGenerator.generateLegalMoves(board, ml);
        for (int i = 0; i < ml.size(); i++) {
            long u = board.makeMove(ml.get(i));
            board.unmakeMove(ml.get(i), u);

            assertEquals(original, board.getHash());
        }
    }

    @Test
    void testSideToMove() {
        assertNotEquals(new Board(new ChessGame(new FenFormat("4k3/8/8/8/8/8/8/4K3 w - - 0 1"))).getHash(), new Board(new ChessGame(new FenFormat("4k3/8/8/8/8/8/8/4K3 b - - 0 1"))).getHash());
    }

    @Test
    void testEpField() {
        assertNotEquals(new Board(new ChessGame(new FenFormat("4k3/8/8/3Pp3/8/8/8/4K3 w - e6 0 1"))).getHash(), new Board(new ChessGame(new FenFormat("4k3/8/8/3Pp3/8/8/8/4K3 w - - 0 1"))).getHash());
    }
}
