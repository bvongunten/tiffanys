package ch.nostromo.tiffanys.dragonborn.engine.board.movegen;

import org.junit.Test;

import ch.nostromo.tiffanys.commons.fen.FenFormat;
import ch.nostromo.tiffanys.dragonborn.engine.TestHelper;

public class PawnLegalMovesTest extends TestHelper {

    @Test
    public void testMoveGenStartLinesWhite() {
        FenFormat fen = new FenFormat("3k4/3p4/4p3/8/8/4P3/3P4/3K4 w - - 0 7");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenStartLinesBlack() {
        FenFormat fen = new FenFormat("3k4/3p4/4p3/8/8/4P3/3P4/3K4 b - - 0 7");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenStartLinesBlockedWhite() {
        FenFormat fen = new FenFormat("3k4/3p4/3P4/6p1/6P1/3p4/3P4/3K4 w - - 0 7");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenStartLinesBlockedBlack() {
        FenFormat fen = new FenFormat("3k4/3p4/3P4/6p1/6P1/3p4/3P4/3K4 b - - 0 7");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenInFieldWhite() {
        FenFormat fen = new FenFormat("3k4/3p4/4p3/8/8/4P3/3P4/3K4 w - - 0 7");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenInFieldBlack() {
        FenFormat fen = new FenFormat("3k4/3p4/4p3/8/8/4P3/3P4/3K4 b - - 0 7");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenInFieldBlockedWhite() {
        FenFormat fen = new FenFormat("3k4/3p4/3P4/6p1/6P1/3p4/3P4/3K4 w - - 0 7");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenInFieldBlockedBlack() {
        FenFormat fen = new FenFormat("3k4/3p4/3P4/6p1/6P1/3p4/3P4/3K4 b - - 0 7");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenHitMovesWhite() {
        FenFormat fen = new FenFormat("3k4/3p4/2PpP3/8/8/2pPp3/3P4/3K4 w - - 0 7");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenHitMovesBlack() {
        FenFormat fen = new FenFormat("3k4/3p4/2PpP3/8/8/2pPp3/3P4/3K4 b - - 0 7");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testPromotionsWhite() {
        FenFormat fen = new FenFormat("3k1q2/6P1/8/8/8/8/6p1/3K1Q2 w - - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testPromotionsBlack() {
        FenFormat fen = new FenFormat("3k1q2/6P1/8/8/8/8/6p1/3K1Q2 b - - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testEnPassantWhite() {
        FenFormat fen = new FenFormat("3k4/8/8/PpP5/8/8/8/3K4 w - b6 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testEnPassantBlack() {
        FenFormat fen = new FenFormat("3k4/8/8/8/1pPp4/8/8/3K4 b - c3 0 1");
        compareBaseAndEngineMoves(fen);
    }
}
