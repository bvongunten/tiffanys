package ch.nostromo.tiffanys.commons.move;

import ch.nostromo.tiffanys.commons.board.Piece;
import org.junit.Test;

import static ch.nostromo.tiffanys.commons.board.Square.A1;
import static ch.nostromo.tiffanys.commons.board.Square.B1;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class MoveTest {

    @Test
    public void testSimpleMoveByCoord() {
        Move move = new Move(A1, B1);

        assertEquals(move.getFrom().getBoardIdx(), 21);
        assertEquals(move.getTo().getBoardIdx(), 22);
    }

    @Test
    public void testSimplePromotionByCoord() {
        Move move = new Move(A1, B1, Piece.WHITE_KING);

        assertEquals(move.getFrom().getBoardIdx(), 21);
        assertEquals(move.getTo().getBoardIdx(), 22);
        assertEquals(move.getPromotion(), Piece.WHITE_KING);
        assertTrue(move.isPromotion());

    }

    @Test
    public void testSimpleCastling() {
        Move move = new Move(Castling.WHITE_LONG);

        assertEquals(move.getCastling(), Castling.WHITE_LONG);
        assertTrue(move.isCastling());
    }

    @Test
    public void testToStringCall() {
        Move move = new Move(A1, B1);
        assertEquals("Move [a1-b1]", move.toString());

        Move moveCastling = new Move(Castling.WHITE_LONG);
        assertEquals("Move [O-O-O]", moveCastling.toString());

        Move movePromo = new Move(A1, B1, Piece.WHITE_KING);
        assertEquals("Move [a1-b1K]", movePromo.toString());

    }

    @Test
    public void testCastlingEquals() {
        Move castling1 = new Move(Castling.BLACK_LONG);
        Move castling1b = new Move(Castling.BLACK_LONG);
        Move castling2 = new Move(Castling.WHITE_LONG);
        assertEquals(castling1, castling1b);
        assertNotEquals(castling1, castling2);
    }

}
