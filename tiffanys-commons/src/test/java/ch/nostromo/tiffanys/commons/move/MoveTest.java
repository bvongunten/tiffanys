package ch.nostromo.tiffanys.commons.move;

import ch.nostromo.tiffanys.commons.board.Piece;
import org.junit.jupiter.api.Test;

import static ch.nostromo.tiffanys.commons.board.Square.A1;
import static ch.nostromo.tiffanys.commons.board.Square.B1;
import static org.junit.jupiter.api.Assertions.*;


class MoveTest {

    @Test
    void testSimpleMoveByCoord() {
        Move move = new Move(A1, B1);

        assertEquals(21, move.getFrom().getBoardIdx());
        assertEquals(22, move.getTo().getBoardIdx());
    }

    @Test
    void testSimplePromotionByCoord() {
        Move move = new Move(A1, B1, Piece.WHITE_KING);

        assertEquals(21, move.getFrom().getBoardIdx());
        assertEquals(22, move.getTo().getBoardIdx());
        assertEquals(Piece.WHITE_KING, move.getPromotion());
        assertTrue(move.isPromotion());

    }

    @Test
    void testSimpleCastling() {
        Move move = new Move(Castling.WHITE_LONG);

        assertEquals(Castling.WHITE_LONG, move.getCastling());
        assertTrue(move.isCastling());
    }

    @Test
    void testToStringCall() {
        Move move = new Move(A1, B1);
        assertEquals("Move [a1-b1]", move.toString());

        Move moveCastling = new Move(Castling.WHITE_LONG);
        assertEquals("Move [O-O-O]", moveCastling.toString());

        Move movePromo = new Move(A1, B1, Piece.WHITE_KING);
        assertEquals("Move [a1-b1K]", movePromo.toString());

    }

    @Test
    void testCastlingEquals() {
        Move castling1 = new Move(Castling.BLACK_LONG);
        Move castling1b = new Move(Castling.BLACK_LONG);
        Move castling2 = new Move(Castling.WHITE_LONG);
        assertEquals(castling1, castling1b);
        assertNotEquals(castling1, castling2);
    }

}
