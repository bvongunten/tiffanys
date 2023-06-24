package ch.nostromo.tiffanys.commons.uci.utils;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.board.Piece;
import ch.nostromo.tiffanys.commons.format.FenFormat;
import ch.nostromo.tiffanys.commons.move.Castling;
import ch.nostromo.tiffanys.commons.move.Move;
import org.junit.jupiter.api.Test;

import static ch.nostromo.tiffanys.commons.board.Square.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UciMoveTranslatorTest {

    @Test
    void testMoveToUci() {
        assertEquals("e2e4", UciMoveTranslator.moveToUci(new Move(E2, E4)));
    }

    @Test
    void testMoveToUciCastling() {
        assertEquals("e1c1", UciMoveTranslator.moveToUci(new Move(Castling.WHITE_LONG)));
    }

    @Test
    void testMoveToUciPromotion() {
        assertEquals("e7e8b", UciMoveTranslator.moveToUci(new Move(E7, E8, Piece.WHITE_BISHOP)));
    }

    @Test
    void testUciToMove() {
        ChessGame chessGame = new ChessGame();
        assertEquals("Move [e2-e4]", UciMoveTranslator.uciToMove("e2e4", chessGame.getCurrentBoard()).toString());
    }

    @Test
    void testUciToMovePromotion() {
        ChessGame chessGame = new ChessGame(new FenFormat("k7/4P3/8/8/8/8/8/4K3 w - - 0 1"));
        assertEquals(Piece.WHITE_BISHOP, UciMoveTranslator.uciToMove("e7e8b", chessGame.getCurrentBoard()).getPromotion());
    }

    @Test
    void testUciToMoveCastling() {
        ChessGame chessGame = new ChessGame(new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1"));
        assertEquals(Castling.WHITE_LONG, UciMoveTranslator.uciToMove("e1c1", chessGame.getCurrentBoard()).getCastling());
        assertEquals(Castling.WHITE_SHORT, UciMoveTranslator.uciToMove("e1g1", chessGame.getCurrentBoard()).getCastling());
        assertEquals(Castling.BLACK_LONG, UciMoveTranslator.uciToMove("e8c8", chessGame.getCurrentBoard()).getCastling());
        assertEquals(Castling.BLACK_SHORT, UciMoveTranslator.uciToMove("e8g8", chessGame.getCurrentBoard()).getCastling());
    }

}