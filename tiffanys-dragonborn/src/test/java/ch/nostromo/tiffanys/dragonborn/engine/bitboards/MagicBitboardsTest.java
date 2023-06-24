package ch.nostromo.tiffanys.dragonborn.engine.bitboards;

import ch.nostromo.tiffanys.dragonborn.BaseTest;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MagicBitboardsTest extends BaseTest {

    @Test
    void testRookMatchesClassicalRandomized() {
        Random rnd = new Random(42);

        for (int i = 0; i < 10_000; i++) {
            int sq = rnd.nextInt(64);
            long occupancy = rnd.nextLong();

            long expected = classicalRook(sq, occupancy);
            long actual = MagicBitboards.getRookAttacks(sq, occupancy);

            assertEquals(expected, actual);
        }
    }

    @Test
    void testBishopMatchesClassicalRandomized() {
        Random rnd = new Random(1337);

        for (int i = 0; i < 10_000; i++) {
            int sq = rnd.nextInt(64);
            long occupancy = rnd.nextLong();

            long expected = classicalBishop(sq, occupancy);
            long actual = MagicBitboards.getBishopAttacks(sq, occupancy);

            assertEquals(expected, actual);
        }
    }

    @Test
    void testRookStopsAtBlocker() {
        int d4 = BaseBitboards.getSquareIndex(3, 3);
        int d6 = BaseBitboards.getSquareIndex(3, 5);
        int d7 = BaseBitboards.getSquareIndex(3, 6);

        long occupancy = BaseBitboards.createSingleBitBoard(d6);

        long attacks = MagicBitboards.getRookAttacks(d4, occupancy);

        assertBitSet(attacks, d6);     // blocker included
        assertBitNotSet(attacks, d7);  // beyond blocked
    }

    @Test
    void testBishopStopsAtBlocker() {
        int d4 = BaseBitboards.getSquareIndex(3, 3);
        int f6 = BaseBitboards.getSquareIndex(5, 5);
        int g7 = BaseBitboards.getSquareIndex(6, 6);

        long occupancy = BaseBitboards.createSingleBitBoard(f6);

        long attacks = MagicBitboards.getBishopAttacks(d4, occupancy);

        assertBitSet(attacks, f6);
        assertBitNotSet(attacks, g7);
    }

    @Test
    void testRookEmptyBoardFullRays() {
        int d4 = BaseBitboards.getSquareIndex(3, 3);

        long attacks = MagicBitboards.getRookAttacks(d4, 0L);
        long expected = classicalRook(d4, 0L);

        assertEquals(expected, attacks);
    }

    @Test
    void testBishopEmptyBoardFullDiagonals() {
        int d4 = BaseBitboards.getSquareIndex(3, 3);

        long attacks = MagicBitboards.getBishopAttacks(d4, 0L);
        long expected = classicalBishop(d4, 0L);

        assertEquals(expected, attacks);
    }

    @Test
    void testQueenEqualsRookPlusBishop() {
        Random rnd = new Random(7);

        for (int i = 0; i < 5000; i++) {
            int sq = rnd.nextInt(64);
            long occ = rnd.nextLong();

            long rook = MagicBitboards.getRookAttacks(sq, occ);
            long bishop = MagicBitboards.getBishopAttacks(sq, occ);
            long queen = MagicBitboards.getQueenAttacks(sq, occ);

            assertEquals(rook | bishop, queen);
        }
    }



    private static long classicalRook(int square, long blockers) {
        long attack = 0L;
        int file = BaseBitboards.getFileBySquareIndex(square);
        int rank = BaseBitboards.getRankBySquareIndex(square);

        for (int rr = rank + 1; rr < 8; rr++) {
            int s = BaseBitboards.getSquareIndex(file, rr);
            attack |= BaseBitboards.createSingleBitBoard(s);
            if (isSet(blockers, s)) break;
        }
        for (int rr = rank - 1; rr >= 0; rr--) {
            int s = BaseBitboards.getSquareIndex(file, rr);
            attack |= BaseBitboards.createSingleBitBoard(s);
            if (isSet(blockers, s)) break;
        }
        for (int ff = file + 1; ff < 8; ff++) {
            int s = BaseBitboards.getSquareIndex(ff, rank);
            attack |= BaseBitboards.createSingleBitBoard(s);
            if (isSet(blockers, s)) break;
        }
        for (int ff = file - 1; ff >= 0; ff--) {
            int s = BaseBitboards.getSquareIndex(ff, rank);
            attack |= BaseBitboards.createSingleBitBoard(s);
            if (isSet(blockers, s)) break;
        }
        return attack;
    }

    private static long classicalBishop(int square, long blockers) {
        long attack = 0L;
        int file = BaseBitboards.getFileBySquareIndex(square);
        int rank = BaseBitboards.getRankBySquareIndex(square);

        for (int ff=file+1, rr=rank+1; ff<8 && rr<8; ff++, rr++) {
            int s = BaseBitboards.getSquareIndex(ff, rr);
            attack |= BaseBitboards.createSingleBitBoard(s);
            if (isSet(blockers, s)) break;
        }
        for (int ff=file-1, rr=rank+1; ff>=0 && rr<8; ff--, rr++) {
            int s = BaseBitboards.getSquareIndex(ff, rr);
            attack |= BaseBitboards.createSingleBitBoard(s);
            if (isSet(blockers, s)) break;
        }
        for (int ff=file+1, rr=rank-1; ff<8 && rr>=0; ff++, rr--) {
            int s = BaseBitboards.getSquareIndex(ff, rr);
            attack |= BaseBitboards.createSingleBitBoard(s);
            if (isSet(blockers, s)) break;
        }
        for (int ff=file-1, rr=rank-1; ff>=0 && rr>=0; ff--, rr--) {
            int s = BaseBitboards.getSquareIndex(ff, rr);
            attack |= BaseBitboards.createSingleBitBoard(s);
            if (isSet(blockers, s)) break;
        }
        return attack;
    }

}
