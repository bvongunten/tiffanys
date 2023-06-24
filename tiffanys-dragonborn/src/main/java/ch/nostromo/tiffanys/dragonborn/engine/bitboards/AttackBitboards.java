package ch.nostromo.tiffanys.dragonborn.engine.bitboards;

import ch.nostromo.tiffanys.dragonborn.engine.board.PieceUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


/**
 * ATTACK TABLES — Precomputed Attack Bitboards for All Piece Types
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AttackBitboards {

    /**
     * KNIGHT[sq] = bitboard of squares a knight on `sq` can attack.
     */
    private static final long[] KNIGHT = new long[64];

    /**
     * KING[sq] = bitboard of squares a king on `sq` can attack (one step in any direction).
     */
    private static final long[] KING = new long[64];

    /**
     * PAWN_ATTACKS[color][sq] = bitboard of squares a pawn of `color` on `sq` attacks.
     */
    private static final long[][] PAWN_ATTACKS = new long[2][64];

    static {
        initKnightAttacks();
        initKingAttacks();
        initPawnAttacks();
    }

    /**
     * Initialize knight attack table.
     */
    private static void initKnightAttacks() {
        for (int square = 0; square < 64; square++) {
            long bitBoard = BaseBitboards.createSingleBitBoard(square);
            long attacks = 0L;
            attacks |= (bitBoard << 17) & BaseBitboards.NOT_FILE_A;   // 2 ranks up, 1 file right
            attacks |= (bitBoard << 15) & BaseBitboards.NOT_FILE_H;   // 2 ranks up, 1 file left
            attacks |= (bitBoard << 10) & BaseBitboards.NOT_FILE_AB;  // 1 rank up, 2 files right
            attacks |= (bitBoard << 6) & BaseBitboards.NOT_FILE_GH;  // 1 rank up, 2 files left
            attacks |= (bitBoard >>> 6) & BaseBitboards.NOT_FILE_AB; // 1 rank down, 2 files right
            attacks |= (bitBoard >>> 10) & BaseBitboards.NOT_FILE_GH; // 1 rank down, 2 files left
            attacks |= (bitBoard >>> 15) & BaseBitboards.NOT_FILE_A;  // 2 ranks down, 1 file right
            attacks |= (bitBoard >>> 17) & BaseBitboards.NOT_FILE_H;  // 2 ranks down, 1 file left
            KNIGHT[square] = attacks;
        }
    }

    /**
     * Initialize king attack table.
     */
    private static void initKingAttacks() {
        for (int square = 0; square < 64; square++) {
            long bitBoard = BaseBitboards.createSingleBitBoard(square);
            long attacks = 0L;
            attacks |= (bitBoard << 8);                // north
            attacks |= (bitBoard >>> 8);               // south
            attacks |= (bitBoard << 1) & BaseBitboards.NOT_FILE_A;   // east
            attacks |= (bitBoard >>> 1) & BaseBitboards.NOT_FILE_H;  // west
            attacks |= (bitBoard << 9) & BaseBitboards.NOT_FILE_A;   // north-east
            attacks |= (bitBoard << 7) & BaseBitboards.NOT_FILE_H;   // north-west
            attacks |= (bitBoard >>> 7) & BaseBitboards.NOT_FILE_A;  // south-east
            attacks |= (bitBoard >>> 9) & BaseBitboards.NOT_FILE_H;  // south-west
            KING[square] = attacks;
        }
    }

    /**
     * Initialize pawn attack tables for both colors.
     */
    private static void initPawnAttacks() {
        for (int sq = 0; sq < 64; sq++) {
            long bitBoard = BaseBitboards.createSingleBitBoard(sq);

            // White attacks: up-right (+9) and up-left (+7)
            PAWN_ATTACKS[PieceUtils.WHITE][sq] = ((bitBoard << 9) & BaseBitboards.NOT_FILE_A) | ((bitBoard << 7) & BaseBitboards.NOT_FILE_H);

            // Black attacks: down-right (-7) and down-left (-9)
            PAWN_ATTACKS[PieceUtils.BLACK][sq] = ((bitBoard >>> 7) & BaseBitboards.NOT_FILE_A) | ((bitBoard >>> 9) & BaseBitboards.NOT_FILE_H);
        }
    }

    /**
     * Get Knight attacks
     */
    public static long getKnightAttacks(int square) {
        return KNIGHT[square];
    }

    /** Get King attacks */
    public static long getKingAttacks(int square) {
        return KING[square];
    }

    /** Get Pawn attacks */
    public static long getPawnAttacks(int color, int square) {
        return PAWN_ATTACKS[color][square];
    }

    /**
     * Compute the attack bitboard for a bishop on the given square.
     */
    public static long getBishopAttacks(int sq, long occupancy) {
        return MagicBitboards.getBishopAttacks(sq, occupancy);
    }

    /**
     * Compute the attack bitboard for a rook on the given square.
     */
    public static long getRookAttacks(int sq, long occupancy) {
        return MagicBitboards.getRookAttacks(sq, occupancy);
    }

    /**
     * Compute the attack bitboard for a queen on the given square.
     * A queen moves like a bishop + rook combined.
     */
    public static long getQueenAttacks(int sq, long occupancy) {
        return MagicBitboards.getQueenAttacks(sq, occupancy);
    }
}
