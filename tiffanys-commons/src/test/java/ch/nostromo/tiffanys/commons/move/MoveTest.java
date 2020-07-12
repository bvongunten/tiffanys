package ch.nostromo.tiffanys.commons.move;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ch.nostromo.tiffanys.commons.enums.Castling;
import ch.nostromo.tiffanys.commons.enums.Piece;
import ch.nostromo.tiffanys.commons.move.Move;

public class MoveTest {

    @Test
    public void testSimpleMoveByCoord() {
        Move move = new Move("a1", "b1");

        assertEquals(move.getFrom(), 21);
        assertEquals(move.getTo(), 22);
    }

    @Test
    public void testSimpleMoveByField() {
        Move move = new Move(21, 22);

        assertEquals(move.getFromCoord(), "a1");
        assertEquals(move.getToCoord(), "b1");
    }

    @Test
    public void testSimplePromotionByCoord() {
        Move move = new Move("a1", "b1", Piece.KING);

        assertEquals(move.getFrom(), 21);
        assertEquals(move.getTo(), 22);
        assertEquals(move.getPromotion(), Piece.KING);
        assertTrue(move.isPromotion());

    }

    @Test
    public void testSimplePromotionByField() {
        Move move = new Move(21, 22, Piece.KING);

        assertEquals(move.getFromCoord(), "a1");
        assertEquals(move.getToCoord(), "b1");
        assertEquals(move.getPromotion(), Piece.KING);

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
        Move move = new Move(21, 22);
        assertEquals("Move [a1-b1 (21-22)]", move.toString());

        Move moveCastling = new Move(Castling.WHITE_LONG);
        assertEquals("Move [WHITE_LONG]", moveCastling.toString());

        Move movePromo = new Move(21, 22, Piece.KING);
        assertEquals("Move [a1-b1 (21-22) + Promotion=KING]", movePromo.toString());

    }

    @Test
    public void testEquals() {
        Move castling1 = new Move(Castling.BLACK_LONG);
        Move castling1b = new Move(Castling.BLACK_LONG);
        Move castling2 = new Move(Castling.WHITE_LONG);
        assertEquals(castling1, castling1b);
        assertNotEquals(castling1, castling2);

        Move moveNormal1 = new Move("a1", "b1");
        Move moveNormal2 = new Move(21, 22);
        assertEquals(moveNormal1, moveNormal2);

        Move moveEp1 = new Move("a1", "b1");
        Move moveEp2 = new Move(21, 22);
        assertEquals(moveEp1, moveEp2);

        Move movePromotion1 = new Move("a1", "b1", Piece.KING);
        Move movePromotion2 = new Move(21, 22, Piece.KING);
        assertEquals(movePromotion1, movePromotion2);

    }

}
