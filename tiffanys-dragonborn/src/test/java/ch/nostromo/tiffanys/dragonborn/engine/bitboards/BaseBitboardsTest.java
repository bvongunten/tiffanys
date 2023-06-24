package ch.nostromo.tiffanys.dragonborn.engine.bitboards;

import ch.nostromo.tiffanys.commons.board.Square;
import org.junit.jupiter.api.Test;

import static ch.nostromo.tiffanys.commons.board.Square.A1;
import static ch.nostromo.tiffanys.commons.board.Square.H8;
import static org.junit.jupiter.api.Assertions.*;

class BaseBitboardsTest {


     @Test
     void testFileAMask() {
         long expected = 0x0101010101010101L;
         assertEquals(BaseBitboards.FILE_A, expected);
     }

     @Test
     void testFileShiftConsistency() {
         assertEquals(BaseBitboards.FILE_B, BaseBitboards.FILE_A << 1);
         assertEquals(BaseBitboards.FILE_H, BaseBitboards.FILE_A << 7);
     }

     @Test
     void testFileCombinedMasks() {
         assertEquals(BaseBitboards.FILE_AB, BaseBitboards.FILE_A | BaseBitboards.FILE_B);
         assertEquals(BaseBitboards.FILE_GH, BaseBitboards.FILE_G | BaseBitboards.FILE_H);
     }

     @Test
     void testNotFileMasks() {
         assertEquals(BaseBitboards.NOT_FILE_A, ~BaseBitboards.FILE_A);
         assertEquals(BaseBitboards.NOT_FILE_H, ~BaseBitboards.FILE_H);
         assertEquals(BaseBitboards.NOT_FILE_AB, ~BaseBitboards.FILE_AB);
         assertEquals(BaseBitboards.NOT_FILE_GH, ~BaseBitboards.FILE_GH);
     }

     @Test
     void testRankMasks() {
         assertEquals(0xFFL, BaseBitboards.RANK_1);
         assertEquals(BaseBitboards.RANK_2, BaseBitboards.RANK_1 << 8);
         assertEquals(BaseBitboards.RANK_8, BaseBitboards.RANK_1 << 56);
     }

     @Test
     void testSquareIndexMapping() {
         assertEquals(0, BaseBitboards.getSquareIndex(0, 0)); // a1
         assertEquals(7, BaseBitboards.getSquareIndex(7, 0)); // h1
         assertEquals(56, BaseBitboards.getSquareIndex(0, 7)); // a8
         assertEquals(63, BaseBitboards.getSquareIndex(7, 7)); // h8
     }

     @Test
     void testFileAndRankExtraction() {
         int sq = BaseBitboards.getSquareIndex(3, 5); // d6
         assertEquals(3, BaseBitboards.getFileBySquareIndex(sq));
         assertEquals(5, BaseBitboards.getRankBySquareIndex(sq));
     }

     @Test
     void testCreateSingleBitBoardSingle() {
         assertEquals(1L, BaseBitboards.createSingleBitBoard(0));
         assertEquals(1L << 63, BaseBitboards.createSingleBitBoard(63));
     }

     @Test
     void testGetLsb() {
         long bb = 0b101000L; // bits at 3 and 5
         assertEquals(3, BaseBitboards.getLsb(bb));
     }

     @Test
     void testPopGetLsb() {
         long bb = 0b101000L; // bits at 3 and 5
         long result = BaseBitboards.popLsb(bb);
         assertEquals(0b100000L, result); // bit 3 removed
     }

     @Test
     void testGetPopCount() {
         long bb = 0b101011L;
         assertEquals(4, BaseBitboards.getPopCount(bb));
     }

     @Test
     void testCreateSingleBitBoardIteration() {
         long bb = 0b10110L; // bits: 1, 2, 4
         int count = 0;

         while (bb != 0) {
             int sq = BaseBitboards.getLsb(bb);
             assertNotEquals(0, (bb & (1L << sq)));
             bb = BaseBitboards.popLsb(bb);
             count++;
         }

         assertEquals(3, count);
     }

    @Test
    void testGetSquareByIndex() {
        assertEquals(A1, BaseBitboards.getTiffanysSquareBySquareIndex(0));
        assertEquals(H8, BaseBitboards.getTiffanysSquareBySquareIndex(63));
    }

    @Test
    void testGetSquareIndexByTiffanysSquare() {
        assertEquals(0, BaseBitboards.getSquareIndexByTiffanysSquare(A1));
        assertEquals(63, BaseBitboards.getSquareIndexByTiffanysSquare(H8));
    }

    @Test
     void testRoundTripSquareConversion() {
         for (int sq = 0; sq < 64; sq++) {
             Square square = BaseBitboards.getTiffanysSquareBySquareIndex(sq);
             int sqIdx = BaseBitboards.getSquareIndexByTiffanysSquare(square);
             assertEquals(sq, sqIdx);
         }
     }
}
