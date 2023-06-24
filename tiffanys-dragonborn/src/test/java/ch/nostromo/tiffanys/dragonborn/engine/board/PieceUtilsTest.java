package ch.nostromo.tiffanys.dragonborn.engine.board;

import ch.nostromo.tiffanys.commons.board.Piece;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PieceUtilsTest {

    @Test
    void testCreateColoredPiece() {
        assertEquals(PieceUtils.WHITE_PAWN, PieceUtils.createColoredPiece(PieceUtils.WHITE, PieceUtils.PAWN));
        assertEquals(PieceUtils.BLACK_KING, PieceUtils.createColoredPiece(PieceUtils.BLACK, PieceUtils.KING));
    }

    @Test
    void testGetColorOfPiece() {
        assertEquals(PieceUtils.WHITE, PieceUtils.getColorOfPiece(PieceUtils.WHITE_QUEEN));
        assertEquals(PieceUtils.BLACK, PieceUtils.getColorOfPiece(PieceUtils.BLACK_KNIGHT));
    }

    @Test
    void testGetTypeOfPiece() {
        assertEquals(PieceUtils.PAWN, PieceUtils.getTypeOfPiece(PieceUtils.WHITE_PAWN));
        assertEquals(PieceUtils.KING, PieceUtils.getTypeOfPiece(PieceUtils.BLACK_KING));
    }

    @Test
    void testFromTiffanysPieceMapping() {
        assertEquals(PieceUtils.WHITE_KING, PieceUtils.fromTiffanysPiece(Piece.WHITE_KING));
        assertEquals(PieceUtils.WHITE_QUEEN, PieceUtils.fromTiffanysPiece(Piece.WHITE_QUEEN));
        assertEquals(PieceUtils.WHITE_ROOK, PieceUtils.fromTiffanysPiece(Piece.WHITE_ROOK));
        assertEquals(PieceUtils.WHITE_BISHOP, PieceUtils.fromTiffanysPiece(Piece.WHITE_BISHOP));
        assertEquals(PieceUtils.WHITE_KNIGHT, PieceUtils.fromTiffanysPiece(Piece.WHITE_KNIGHT));
        assertEquals(PieceUtils.WHITE_PAWN, PieceUtils.fromTiffanysPiece(Piece.WHITE_PAWN));

        assertEquals(PieceUtils.BLACK_KING, PieceUtils.fromTiffanysPiece(Piece.BLACK_KING));
        assertEquals(PieceUtils.BLACK_QUEEN, PieceUtils.fromTiffanysPiece(Piece.BLACK_QUEEN));
        assertEquals(PieceUtils.BLACK_ROOK, PieceUtils.fromTiffanysPiece(Piece.BLACK_ROOK));
        assertEquals(PieceUtils.BLACK_BISHOP, PieceUtils.fromTiffanysPiece(Piece.BLACK_BISHOP));
        assertEquals(PieceUtils.BLACK_KNIGHT, PieceUtils.fromTiffanysPiece(Piece.BLACK_KNIGHT));
        assertEquals(PieceUtils.BLACK_PAWN, PieceUtils.fromTiffanysPiece(Piece.BLACK_PAWN));
    }

    @Test
    void testRoundTripColorAndType() {
        for (int piece = 0; piece < 12; piece++) {
            int color = PieceUtils.getColorOfPiece(piece);
            int type  = PieceUtils.getTypeOfPiece(piece);

            int reconstructed = PieceUtils.createColoredPiece(color, type);

            assertEquals(piece, reconstructed, "Roundtrip failed for piece " + piece);
        }
    }
}
