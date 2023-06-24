package ch.nostromo.tiffanys.dragonborn.engine.bitboards;

import ch.nostromo.tiffanys.dragonborn.BaseTest;
import ch.nostromo.tiffanys.dragonborn.engine.board.PieceUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AttackBitboardsTest extends BaseTest {


    @Test
    void testKnightCenterHas8Moves() {
        int e4 = BaseBitboards.getSquareIndex(4, 3);
        long attacks = AttackBitboards.getKnightAttacks(e4);

        assertBitCount(attacks, 8);
    }

    @Test
    void testKnightCornerHas2Moves() {
        int a1 = BaseBitboards.getSquareIndex(0, 0);
        long attacks = AttackBitboards.getKnightAttacks(a1);

        assertBitCount(attacks, 2);
    }

    @Test
    void testKingCenterHas8Moves() {
        int d4 = BaseBitboards.getSquareIndex(3, 3);
        long attacks = AttackBitboards.getKingAttacks(d4);

        assertBitCount(attacks, 8);
    }

    @Test
    void testKingCornerHas3Moves() {
        int a1 = BaseBitboards.getSquareIndex(0, 0);
        long attacks = AttackBitboards.getKingAttacks(a1);

        assertBitCount(attacks, 3);
    }

    @Test
    void testKingEdgeHas5Moves() {
        int a4 = BaseBitboards.getSquareIndex(0, 3);
        long attacks = AttackBitboards.getKingAttacks(a4);

        assertBitCount(attacks, 5);
    }

    @Test
    void testWhitePawnAttacksCorrectly() {
        int e4 = BaseBitboards.getSquareIndex(4, 3);
        long attacks = AttackBitboards.getPawnAttacks(PieceUtils.WHITE, e4);

        int d5 = BaseBitboards.getSquareIndex(3, 4);
        int f5 = BaseBitboards.getSquareIndex(5, 4);

        assertBitCount(attacks, 2);
        assertBitSet(attacks, d5);
        assertBitSet(attacks, f5);
    }

    @Test
    void testBlackPawnAttacksCorrectly() {
        int e4 = BaseBitboards.getSquareIndex(4, 3);
        long attacks = AttackBitboards.getPawnAttacks(PieceUtils.BLACK, e4);

        int d3 = BaseBitboards.getSquareIndex(3, 2);
        int f3 = BaseBitboards.getSquareIndex(5, 2);

        assertBitCount(attacks, 2);
        assertBitSet(attacks, d3);
        assertBitSet(attacks, f3);
    }

    @Test
    void testPawnEdgeDoesNotWrap() {
        int a4 = BaseBitboards.getSquareIndex(0, 3);

        long white = AttackBitboards.getPawnAttacks(PieceUtils.WHITE, a4);
        long black = AttackBitboards.getPawnAttacks(PieceUtils.BLACK, a4);

        assertBitCount(white, 1);
        assertBitCount(black, 1);
    }

    @Test
    void testQueenEqualsRookPlusBishop() {
        int d4 = BaseBitboards.getSquareIndex(3, 3);
        long occupancy = 0L;

        long bishop = AttackBitboards.getBishopAttacks(d4, occupancy);
        long rook = AttackBitboards.getRookAttacks(d4, occupancy);
        long queen = AttackBitboards.getQueenAttacks(d4, occupancy);

        assertEquals(bishop | rook, queen);
    }

    @Test
    void testRookBlockedByPiece() {
        int d4 = BaseBitboards.getSquareIndex(3, 3);
        int d6 = BaseBitboards.getSquareIndex(3, 5);
        int d7 = BaseBitboards.getSquareIndex(3, 6);

        long occupancy = 1L << d6;
        long attacks = AttackBitboards.getRookAttacks(d4, occupancy);

        assertBitSet(attacks, d6);
        assertBitNotSet(attacks, d7);
    }

    @Test
    void testBishopBlockedByPiece() {
        int d4 = BaseBitboards.getSquareIndex(3, 3);
        int f6 = BaseBitboards.getSquareIndex(5, 5);
        int g7 = BaseBitboards.getSquareIndex(6, 6);

        long occupancy = 1L << f6;
        long attacks = AttackBitboards.getBishopAttacks(d4, occupancy);

        assertBitSet(attacks, f6);
        assertBitNotSet(attacks, g7);
    }



}
