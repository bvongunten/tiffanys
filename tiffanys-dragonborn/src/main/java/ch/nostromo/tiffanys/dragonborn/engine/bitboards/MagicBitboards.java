package ch.nostromo.tiffanys.dragonborn.engine.bitboards;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static ch.nostromo.tiffanys.dragonborn.engine.bitboards.BaseBitboards.*;

/**
 * MAGIC BITBOARDS — Fast Sliding Piece Attack Lookup
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MagicBitboards {


    private static final int[][] ORTHOGONAL_DIRS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
    private static final int[][] DIAGONAL_DIRS = {{1, 1}, {-1, 1}, {1, -1}, {-1, -1}};

    private static final long[] ROOK_MASKS = new long[64];
    private static final long[] BISHOP_MASKS = new long[64];
    private static final long[] ROOK_MAGICS = new long[64];
    private static final long[] BISHOP_MAGICS = new long[64];

    private static final int[] ROOK_SHIFTS = new int[64];
    private static final int[] BISHOP_SHIFTS = new int[64];
    private static final int[] ROOK_OFFSETS = new int[64];
    private static final int[] BISHOP_OFFSETS = new int[64];

    private static final long[] ROOK_ATTACKS;
    private static final long[] BISHOP_ATTACKS;

    private static final long[] ROOK_MAGIC_NUMBERS = {
            0x8a80104000800020L, 0x140002000100040L, 0x2801880a0017001L, 0x100081001000420L,
            0x200020010080420L, 0x3001c0002010008L, 0x8480008002000100L, 0x2080088004402900L,
            0x800098204000L, 0x2024401000200040L, 0x100802000801000L, 0x120800800801000L,
            0x208808088000400L, 0x2802200800400L, 0x2200800100020080L, 0x801000060821100L,
            0x80044006422000L, 0x100808020004000L, 0x12108a0010204200L, 0x140848010000802L,
            0x481828014002800L, 0x8094004002004100L, 0x4010040010010802L, 0x20008806104L,
            0x100400080208000L, 0x2040002120081000L, 0x21200680100081L, 0x20100080080080L,
            0x2000a00200410L, 0x20080800400L, 0x80088400100102L, 0x80004600042881L,
            0x4040008040800020L, 0x440003000200801L, 0x4200011004500L, 0x188020010100100L,
            0x14800401802800L, 0x2080040080800200L, 0x124080204001001L, 0x200046502000484L,
            0x480400080088020L, 0x1000422010034000L, 0x30200100110040L, 0x100021010009L,
            0x2002080100110004L, 0x202008004008002L, 0x20020004010100L, 0x2048440040820001L,
            0x101002200408200L, 0x40802000401080L, 0x4008142004410100L, 0x2060820c0120200L,
            0x1001004080100L, 0x20c020080040080L, 0x2935610830022400L, 0x44440041009200L,
            0x280001040802101L, 0x2100190040002085L, 0x80c0084100102001L, 0x4024081001000421L,
            0x20030a0244872L, 0x12001008414402L, 0x2006104900a0804L, 0x1004081002402L
    };

    private static final long[] BISHOP_MAGIC_NUMBERS = {
            0x40040844404084L, 0x2004208a004208L, 0x10190041080202L, 0x108060845042010L,
            0x581104180800210L, 0x2112080446200010L, 0x1080820820060210L, 0x3c0808410220200L,
            0x4050404440404L, 0x21001420088L, 0x24d0080801082102L, 0x1020a0a020400L,
            0x40308200402L, 0x4011002100800L, 0x401484104104005L, 0x801010402020200L,
            0x400210c3880100L, 0x404022024108200L, 0x810018200204102L, 0x4002801a02003L,
            0x85040820080400L, 0x810102c808880400L, 0xe900410884800L, 0x8002020480840102L,
            0x220200865090201L, 0x2010100a02021202L, 0x152048408022401L, 0x20080002081110L,
            0x4001001021004000L, 0x800040400a011002L, 0xe4004081011002L, 0x1c004001012080L,
            0x8004200962a00220L, 0x8422100208500202L, 0x2000402200300c08L, 0x8646020080080080L,
            0x80020a0200100808L, 0x2010004880111000L, 0x623000a080011400L, 0x42008c0340209202L,
            0x209188240001000L, 0x400408a884001800L, 0x110400a6080400L, 0x1840060a44020800L,
            0x90080104000041L, 0x201011000808101L, 0x1a2208080504f080L, 0x8012020600211212L,
            0x500861011240000L, 0x180806108200800L, 0x4000020e01040044L, 0x300000261044000aL,
            0x802241102020002L, 0x20906061210001L, 0x5a84841004010310L, 0x4010801011c04L,
            0xa010109502200L, 0x4a02012000L, 0x500201010098b028L, 0x8040002811040900L,
            0x28000010020204L, 0x6000020202d0240L, 0x8918844842082200L, 0x4010011029020020L
    };

    static {
        // Phase 1: compute masks, shifts, and total table sizes
        int rookTotal = 0;
        int bishopTotal = 0;
        for (int sq = 0; sq < 64; sq++) {
            ROOK_MASKS[sq] = computeOrthogonalMask(sq);
            BISHOP_MASKS[sq] = computeDiagonalMask(sq);
            ROOK_MAGICS[sq] = ROOK_MAGIC_NUMBERS[sq];
            BISHOP_MAGICS[sq] = BISHOP_MAGIC_NUMBERS[sq];

            int rookBits = Long.bitCount(ROOK_MASKS[sq]);
            int bishopBits = Long.bitCount(BISHOP_MASKS[sq]);
            ROOK_SHIFTS[sq] = 64 - rookBits;
            BISHOP_SHIFTS[sq] = 64 - bishopBits;
            ROOK_OFFSETS[sq] = rookTotal;
            BISHOP_OFFSETS[sq] = bishopTotal;
            rookTotal += 1 << rookBits;
            bishopTotal += 1 << bishopBits;
        }

        // Phase 2: allocate flat tables and fill them
        ROOK_ATTACKS = new long[rookTotal];
        BISHOP_ATTACKS = new long[bishopTotal];
        for (int sq = 0; sq < 64; sq++) {
            fillTable(sq, ROOK_MASKS[sq], ROOK_MAGICS[sq], ROOK_SHIFTS[sq],
                    ROOK_OFFSETS[sq], ROOK_ATTACKS, true);
            fillTable(sq, BISHOP_MASKS[sq], BISHOP_MAGICS[sq], BISHOP_SHIFTS[sq],
                    BISHOP_OFFSETS[sq], BISHOP_ATTACKS, false);
        }
    }

    /**
     * Fill one square's attack table by enumerating all subsets of the mask.
     */
    private static void fillTable(int square, long mask, long magic, int shift, int offset, long[] table, boolean isRook) {
        long subset = 0L;
        do {
            long attacks = isRook ? orthogonalAttacks(square, subset)
                    : diagonalAttacks(square, subset);
            int index = (int) ((subset * magic) >>> shift);
            table[offset + index] = attacks;
            subset = (subset - mask) & mask;
        } while (subset != 0);
    }

    private static long computeOrthogonalMask(int square) {
        return buildMask(square, ORTHOGONAL_DIRS);
    }

    private static long computeDiagonalMask(int square) {
        return buildMask(square, DIAGONAL_DIRS);
    }

    private static long orthogonalAttacks(int square, long blockers) {
        return buildAttacks(square, blockers, ORTHOGONAL_DIRS);
    }

    private static long diagonalAttacks(int square, long blockers) {
        return buildAttacks(square, blockers, DIAGONAL_DIRS);
    }

    private static long buildMask(int square, int[][] dirs) {
        int file = getFileBySquareIndex(square);
        int rank = getRankBySquareIndex(square);
        long mask = 0L;

        for (int[] d : dirs) {
            int dx = d[0];
            int dy = d[1];
            int ff = file + dx;
            int rr = rank + dy;
            while ((dx == 0 || (ff >= 1 && ff <= 6))
                    && (dy == 0 || (rr >= 1 && rr <= 6))) {
                mask |= createSingleBitBoard(getSquareIndex(ff, rr));
                ff += dx;
                rr += dy;
            }
        }
        return mask;
    }

    private static long buildAttacks(int square, long blockers, int[][] dirs) {
        int file = getFileBySquareIndex(square);
        int rank = getRankBySquareIndex(square);
        long attack = 0L;

        for (int[] d : dirs) {
            int ff = file + d[0];
            int rr = rank + d[1];
            while (ff >= 0 && ff < 8 && rr >= 0 && rr < 8) {
                long bit = createSingleBitBoard(getSquareIndex(ff, rr));
                attack |= bit;
                if ((blockers & bit) != 0) break;
                ff += d[0];
                rr += d[1];
            }
        }
        return attack;
    }


    /**
     * Rook attacks from `sq` given the current board occupancy.
     */
    public static long getRookAttacks(int sq, long occupancy) {
        long blockers = occupancy & ROOK_MASKS[sq];
        int index = (int) ((blockers * ROOK_MAGICS[sq]) >>> ROOK_SHIFTS[sq]);
        return ROOK_ATTACKS[ROOK_OFFSETS[sq] + index];
    }

    /**
     * Bishop attacks from `sq` given the current board occupancy.
     */
    public static long getBishopAttacks(int sq, long occupancy) {
        long blockers = occupancy & BISHOP_MASKS[sq];
        int index = (int) ((blockers * BISHOP_MAGICS[sq]) >>> BISHOP_SHIFTS[sq]);
        return BISHOP_ATTACKS[BISHOP_OFFSETS[sq] + index];
    }

    /**
     * Queen attacks = bishop attacks + rook attacks.
     */
    public static long getQueenAttacks(int sq, long occupancy) {
        return getRookAttacks(sq, occupancy) | getBishopAttacks(sq, occupancy);
    }
}
