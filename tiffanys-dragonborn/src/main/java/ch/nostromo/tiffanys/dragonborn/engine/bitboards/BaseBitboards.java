package ch.nostromo.tiffanys.dragonborn.engine.bitboards;

import ch.nostromo.tiffanys.commons.board.Square;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Bitboard constants & utilities
 * <p>
 * FILE AND RANK MASKS:
 * <p>
 * FILE_A = all squares on the a-file = bits 0, 8, 16, 24, 32, 40, 48, 56
 * RANK_1 = all squares on rank 1    = bits 0, 1, 2, 3, 4, 5, 6, 7
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BaseBitboards {

    /**
     * All squares on the a-file (leftmost column).
     */
    public static final long FILE_A = 0x0101010101010101L;
    public static final long FILE_B = FILE_A << 1;
    public static final long FILE_G = FILE_A << 6;
    /**
     * All squares on the h-file (rightmost column).
     */
    public static final long FILE_H = FILE_A << 7;
    /**
     * Files a and b combined — used to prevent knight wrap on the left side.
     */
    public static final long FILE_AB = FILE_A | FILE_B;
    /**
     * Files g and h combined — used to prevent knight wrap on the right side.
     */
    public static final long FILE_GH = FILE_G | FILE_H;

    /**
     * All squares on rank 1 (white's back rank).
     */
    public static final long RANK_1 = 0x00000000000000FFL;
    /**
     * Rank 2 — white pawns start here.
     */
    public static final long RANK_2 = RANK_1 << 8;
    /**
     * Rank 4 — target of white pawn double push.
     */
    public static final long RANK_4 = RANK_1 << 24;
    /**
     * Rank 5 — target of black pawn double push.
     */
    public static final long RANK_5 = RANK_1 << 32;
    /**
     * Rank 7 — black pawns start here.
     */
    public static final long RANK_7 = RANK_1 << 48;
    /**
     * Rank 8 — black's back rank; white pawn promotion rank.
     */
    public static final long RANK_8 = RANK_1 << 56;

    /**
     * All squares EXCEPT the a-file. Mask out a-file to prevent leftward wrap.
     */
    public static final long NOT_FILE_A = ~FILE_A;
    /**
     * All squares EXCEPT the h-file. Mask out h-file to prevent rightward wrap.
     */
    public static final long NOT_FILE_H = ~FILE_H;
    /**
     * All squares EXCEPT files a and b. Used for knight moves.
     */
    public static final long NOT_FILE_AB = ~FILE_AB;
    /**
     * All squares EXCEPT files g and h. Used for knight moves.
     */
    public static final long NOT_FILE_GH = ~FILE_GH;

    /**
     * Compute a square index from file and rank.
     */
    public static int getSquareIndex(int file, int rank) {
        return rank * 8 + file;
    }

    /**
     * Extract the file (0..7) from a square index.
     */
    public static int getFileBySquareIndex(int sq) {
        return sq & 7;  // equivalent to sq % 8
    }

    /**
     * Extract the rank (0..7) from a square index.
     */
    public static int getRankBySquareIndex(int sq) {
        return sq >>> 3; // equivalent to sq / 8
    }

    /**
     * Create a bitboard with exactly one bit set at the given square.
     */
    public static long createSingleBitBoard(int sq) {
        return 1L << sq;
    }

    /**
     * Find the index of the least significant set bit.
     */
    public static int getLsb(long bb) {
        return Long.numberOfTrailingZeros(bb);
    }

    /**
     * Clear the least significant set bit and return the result.
     */
    public static long popLsb(long bb) {
        return bb & (bb - 1);
    }

    /**
     * Count the number of set bits (= number of pieces on the bitboard).
     */
    public static int getPopCount(long bb) {
        return Long.bitCount(bb);
    }

    /**
     * Does return the square idx by a given Tiffanys Square
     */
    public static int getSquareIndexByTiffanysSquare(Square square) {
        // Quite lame ;)
        String name = square.name();

        int file = name.charAt(0) - 'A';  // 'A'→0 ... 'H'→7
        int rank = name.charAt(1) - '1';  // '1'→0 ... '8'→7

        return BaseBitboards.getSquareIndex(file, rank);
    }

    /**
     * Does return the Tiffanys Square by a given square IDX
     */
    public static Square getTiffanysSquareBySquareIndex(int sq) {
        // Lame again ;)
        int file = sq & 7;        // 0..7
        int rank = sq >>> 3;      // 0..7

        char fileChar = (char) ('A' + file); // uppercase for enum
        char rankChar = (char) ('1' + rank);

        return Square.valueOf("" + fileChar + rankChar);
    }

}
