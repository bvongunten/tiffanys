package ch.nostromo.tiffanys.dragonborn.engine.search;


import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.format.FenFormat;
import ch.nostromo.tiffanys.dragonborn.BaseTest;
import ch.nostromo.tiffanys.dragonborn.engine.board.Board;
import ch.nostromo.tiffanys.dragonborn.engine.move.MoveGenerator;
import ch.nostromo.tiffanys.dragonborn.engine.move.MoveList;
import ch.nostromo.tiffanys.dragonborn.engine.transposition.TranspositionTable;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SearchTest extends BaseTest {

    @Test
    void testMateIn1() {
        SearchResult r = doSearch("6k1/5ppp/8/8/8/8/5PPP/R5K1 w - - 0 1", 4);
        assertEquals("a1a8", toUci(r.bestMove));
        assertEquals(29999, r.score);
    }

    @Test
    void testMateIn2() {
        SearchResult r = doSearch("1B6/2R2PN1/8/7P/2p1pk2/2Q1pN1P/8/1B5K w - - 0 1", 4);
        assertEquals("g7f5", toUci(r.bestMove));
        assertEquals(29997, r.score);
    }

    @Test
    void testMateIn3() {
        SearchResult r = doSearch("8/8/8/8/1p1N4/1Bk1K3/3N4/b7 w - - 0 1", 6);
        assertEquals("d4e6", toUci(r.bestMove));
        assertEquals(29995, r.score);
    }

    @Test
    @Tag("deep")
    void testMateIn4() {
        SearchResult r = doSearch("5r1k/p1p1q1pp/1p1p4/8/2PPn3/B1P1P3/P1Q1P2p/1R5K b - - 0 1", 8);
        assertEquals("e4f2", toUci(r.bestMove));
        assertEquals(29993, r.score);
    }

    @Test
    @Tag("deep")
    void testMateIn5() {
        SearchResult r = doSearch("6b1/4Kpk1/5r2/8/3B2P1/7R/8/8 w - - 0 1", 10);
        assertEquals("h3h6", toUci(r.bestMove));
        assertEquals(29991, r.score);
    }

    @Test
    @Tag("deep")
    void testMateIn6() {
        SearchResult r = doSearch("1K1k1BB1/8/4P3/2p1P3/2p4b/8/8/8 w - - 0 1", 12);
        assertEquals("f8d6", toUci(r.bestMove));
        assertEquals(29989, r.score);
    }

    @Test
    void testStaleMate() {
        assertEquals(0, doSearch("7k/5Q2/6K1/8/8/8/8/8 b - - 0 1", 3).score);
    }

    @Test
    void testTimeControl() {
        long startNs = System.nanoTime();
        SearchResult r = new Search(new TranspositionTable(16)).searchTime(new Board(new ChessGame(new FenFormat("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"))), 200);
        assertTrue((System.nanoTime() - startNs) / 1_000_000L < 500);
        assertNotEquals(0, r.bestMove);
    }

    @Test
    void testPvLength() {
        assertEquals(6, doSearch("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 6).pvLength);
    }

    @Test
    void testPvLegal() {
        SearchResult r = doSearch("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 5);
        Board b = new Board(new ChessGame(new FenFormat("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")));
        for (int i = 0; i < r.pvLength; i++) {
            MoveList ml = new MoveList();
            MoveGenerator.generateLegalMoves(b, ml);
            assertTrue(ml.contains(r.pv[i]));
            b.makeMove(r.pv[i]);
        }
    }

    private static SearchResult doSearch(String fen, int depth) {
        ChessGame game = new ChessGame(new FenFormat(fen));

        Search ps = new Search( new TranspositionTable(16), Runtime.getRuntime().availableProcessors());
        SearchResult result = ps.searchDepth(new Board(game), depth);
        ps.shutdown();

        return result;
    }

}
