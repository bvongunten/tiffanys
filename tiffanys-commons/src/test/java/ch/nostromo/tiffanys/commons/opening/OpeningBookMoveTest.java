package ch.nostromo.tiffanys.commons.opening;

import ch.nostromo.tiffanys.commons.board.Piece;
import ch.nostromo.tiffanys.commons.board.Square;
import ch.nostromo.tiffanys.commons.format.FenFormat;
import ch.nostromo.tiffanys.commons.move.Castling;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OpeningBookMoveTest {

    @Test
    void castlingTest() {
        OpeningBookMove testee;
        FenFormat fen = new FenFormat("r3k2r/2P4p/8/8/8/8/P1p5/R3K2R w KQkq - 0 1");

        testee = new OpeningBookMove((short) 10, 100, 0, 4, 0, 7, 0);
        assertEquals(Castling.WHITE_SHORT, testee.getMove(fen).getCastling());

        testee = new OpeningBookMove((short) 10, 100, 0, 4, 0, 0, 0);
        assertEquals(Castling.WHITE_LONG, testee.getMove(fen).getCastling());

        testee = new OpeningBookMove((short) 10, 100, 7, 4, 7, 7, 0);
        assertEquals(Castling.BLACK_SHORT, testee.getMove(fen).getCastling());

        testee = new OpeningBookMove((short) 10, 100, 7, 4, 7, 0, 0);
        assertEquals(Castling.BLACK_LONG, testee.getMove(fen).getCastling());
    }
    @Test
    void moveAndPromotionWhiteTest() {
        OpeningBookMove testee;
        FenFormat fen = new FenFormat("r3k2r/2P4p/8/8/8/8/P1p5/R3K2R w KQkq - 0 1");

        testee = new OpeningBookMove((short) 10, 100, 6, 2, 7, 2, 1);
        assertEquals(Square.C7, testee.getMove(fen).getFrom());
        assertEquals(Square.C8, testee.getMove(fen).getTo());
        assertEquals(Piece.WHITE_KNIGHT, testee.getMove(fen).getPromotion());

        testee = new OpeningBookMove((short) 10, 100, 6, 2, 7, 2, 2);
        assertEquals(Piece.WHITE_BISHOP, testee.getMove(fen).getPromotion());

        testee = new OpeningBookMove((short) 10, 100, 6, 2, 7, 2, 3);
        assertEquals(Piece.WHITE_ROOK, testee.getMove(fen).getPromotion());

        testee = new OpeningBookMove((short) 10, 100, 6, 2, 7, 2, 4);
        assertEquals(Piece.WHITE_QUEEN, testee.getMove(fen).getPromotion());
    }

    @Test
    void moveAndPromotionBlackTest() {
        OpeningBookMove testee;
        FenFormat fen = new FenFormat("r3k2r/2P4p/8/8/8/8/P1p5/R3K2R w KQkq - 0 1");

        testee = new OpeningBookMove((short) 10, 100, 1, 2, 0, 2, 1);
        assertEquals(Square.C2, testee.getMove(fen).getFrom());
        assertEquals(Square.C1, testee.getMove(fen).getTo());
        assertEquals(Piece.BLACK_KNIGHT, testee.getMove(fen).getPromotion());

        testee = new OpeningBookMove((short) 10, 100, 1, 2, 0, 2, 2);
        assertEquals(Piece.BLACK_BISHOP, testee.getMove(fen).getPromotion());

        testee = new OpeningBookMove((short) 10, 100, 1, 2, 0, 2, 3);
        assertEquals(Piece.BLACK_ROOK, testee.getMove(fen).getPromotion());

        testee = new OpeningBookMove((short) 10, 100, 1, 2, 0, 2, 4);
        assertEquals(Piece.BLACK_QUEEN, testee.getMove(fen).getPromotion());

    }




}
