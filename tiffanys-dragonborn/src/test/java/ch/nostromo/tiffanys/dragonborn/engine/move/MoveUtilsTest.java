package ch.nostromo.tiffanys.dragonborn.engine.move;


import ch.nostromo.tiffanys.commons.board.Piece;
import ch.nostromo.tiffanys.commons.move.Castling;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.dragonborn.BaseTest;
import ch.nostromo.tiffanys.dragonborn.engine.board.PieceUtils;
import org.junit.jupiter.api.Test;

import static ch.nostromo.tiffanys.commons.board.Square.E2;
import static ch.nostromo.tiffanys.commons.board.Square.E4;
import static org.junit.jupiter.api.Assertions.*;

class MoveUtilsTest extends BaseTest {

    @Test
    void TestQuietMoveNotCapture() {
        assertFalse(MoveUtils.isCapture(findMoveInMoveList(generateLegalMoves("4k3/8/8/8/8/8/4P3/4K3 w - - 0 1"), "e2e4")));
    }

    @Test
    void testCaptureHasCorrectType() {
        int move = findMoveInMoveList(generateLegalMoves("4k3/8/8/4n3/3P4/8/8/4K3 w - - 0 1"), "d4e5");
        assertTrue(MoveUtils.isCapture(move));
        assertEquals(PieceUtils.KNIGHT, MoveUtils.getCaptureType(move));
    }

    @Test
    void testEnPassantIsPawnCapture() {
        int move = findMoveInMoveList(generateLegalMoves("4k3/8/8/3Pp3/8/8/8/4K3 w - e6 0 1"), "d5e6");
        assertTrue(MoveUtils.isEnPassant(move));
        assertTrue(MoveUtils.isCapture(move));
        assertEquals(PieceUtils.PAWN, MoveUtils.getCaptureType(move));
    }

    @Test
    void testCastlingNotCapture() {
        assertFalse(MoveUtils.isCapture(findMoveInMoveList(generateLegalMoves("4k3/8/8/8/8/8/8/R3K2R w KQ - 0 1"), "e1g1")));
    }

    @Test
    void testToTiffanysMoveBase() {
        Move move = MoveUtils.toTiffanysMove(findMoveInMoveList(generateLegalMoves("4k3/8/8/8/8/8/4P3/4K3 w - - 0 1"), "e2e4"));

        assertEquals(E2, move.getFrom());
        assertEquals(E4, move.getTo());
    }

    @Test
    void testToTiffanysMoveCastlingBlack() {
        assertTrue(MoveUtils.isCastle(findMoveInMoveList(generateLegalMoves("r3k2r/8/8/8/8/8/8/R3K2R b kq - 0 1"), "e8g8")));

        Move blackShortCastle = MoveUtils.toTiffanysMove(findMoveInMoveList(generateLegalMoves("r3k2r/8/8/8/8/8/8/R3K2R b kq - 0 1"), "e8g8"));
        assertTrue(blackShortCastle.isCastling());
        assertEquals(Castling.BLACK_SHORT, blackShortCastle.getCastling());

        Move blackLongCastle = MoveUtils.toTiffanysMove(findMoveInMoveList(generateLegalMoves("r3k2r/8/8/8/8/8/8/R3K2R b kq - 0 1"), "e8c8"));
        assertTrue(blackLongCastle.isCastling());
        assertEquals(Castling.BLACK_LONG, blackLongCastle.getCastling());

    }

    @Test
    void testToTiffanysMoveCastlingWhite() {
        assertTrue(MoveUtils.isCastle(findMoveInMoveList(generateLegalMoves("r3k2r/8/8/8/8/8/8/R3K2R w KQ - 0 1"), "e1g1")));

        Move whiteShortCastle = MoveUtils.toTiffanysMove(findMoveInMoveList(generateLegalMoves("r3k2r/8/8/8/8/8/8/R3K2R w KQ - 0 1"), "e1g1"));
        assertTrue(whiteShortCastle.isCastling());
        assertEquals(Castling.WHITE_SHORT, whiteShortCastle.getCastling());

        Move whiteLongCastle = MoveUtils.toTiffanysMove(findMoveInMoveList(generateLegalMoves("r3k2r/8/8/8/8/8/8/R3K2R w KQ - 0 1"), "e1c1"));
        assertTrue(whiteLongCastle.isCastling());
        assertEquals(Castling.WHITE_LONG, whiteLongCastle.getCastling());
    }

    @Test
    void testToTiffanysMovePromotionWhite() {
        Move queenPromotion = MoveUtils.toTiffanysMove(findMoveInMoveList(generateLegalMoves("r3k2r/1P6/8/8/8/8/8/R3K2R w KQ - 0 1"), "b7a8q"));
        assertTrue(queenPromotion.isPromotion());
        assertEquals(Piece.WHITE_QUEEN, queenPromotion.getPromotion());

        Move knightPromotion = MoveUtils.toTiffanysMove(findMoveInMoveList(generateLegalMoves("r3k2r/1P6/8/8/8/8/8/R3K2R w KQ - 0 1"), "b7a8n"));
        assertTrue(knightPromotion.isPromotion());
        assertEquals(Piece.WHITE_KNIGHT, knightPromotion.getPromotion());

        Move rookPromotion = MoveUtils.toTiffanysMove(findMoveInMoveList(generateLegalMoves("r3k2r/1P6/8/8/8/8/8/R3K2R w KQ - 0 1"), "b7a8r"));
        assertTrue(rookPromotion.isPromotion());
        assertEquals(Piece.WHITE_ROOK, rookPromotion.getPromotion());

        Move bishopPromotion = MoveUtils.toTiffanysMove(findMoveInMoveList(generateLegalMoves("r3k2r/1P6/8/8/8/8/8/R3K2R w KQ - 0 1"), "b7a8b"));
        assertTrue(bishopPromotion.isPromotion());
        assertEquals(Piece.WHITE_BISHOP, bishopPromotion.getPromotion());
    }

    @Test
    void testToTiffanysMovePromotionBlack() {
        Move queenPromotion = MoveUtils.toTiffanysMove(findMoveInMoveList(generateLegalMoves("r3k2r/1P6/8/8/8/8/1p6/R3K2R b KQ - 0 1"), "b2b1q"));
        assertTrue(queenPromotion.isPromotion());
        assertEquals(Piece.BLACK_QUEEN, queenPromotion.getPromotion());

        Move knightPromotion = MoveUtils.toTiffanysMove(findMoveInMoveList(generateLegalMoves("r3k2r/1P6/8/8/8/8/1p6/R3K2R b KQ - 0 1"), "b2b1n"));
        assertTrue(knightPromotion.isPromotion());
        assertEquals(Piece.BLACK_KNIGHT, knightPromotion.getPromotion());

        Move rookPromotion = MoveUtils.toTiffanysMove(findMoveInMoveList(generateLegalMoves("r3k2r/1P6/8/8/8/8/1p6/R3K2R b KQ - 0 1"), "b2b1r"));
        assertTrue(rookPromotion.isPromotion());
        assertEquals(Piece.BLACK_ROOK, rookPromotion.getPromotion());

        Move bishopPromotion = MoveUtils.toTiffanysMove(findMoveInMoveList(generateLegalMoves("r3k2r/1P6/8/8/8/8/1p6/R3K2R b KQ - 0 1"), "b2b1b"));
        assertTrue(bishopPromotion.isPromotion());
        assertEquals(Piece.BLACK_BISHOP, bishopPromotion.getPromotion());
    }


}
