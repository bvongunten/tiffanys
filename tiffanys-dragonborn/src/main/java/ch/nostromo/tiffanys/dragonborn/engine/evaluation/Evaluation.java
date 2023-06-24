package ch.nostromo.tiffanys.dragonborn.engine.evaluation;

import ch.nostromo.tiffanys.dragonborn.engine.board.PieceUtils;
import ch.nostromo.tiffanys.dragonborn.engine.board.Board;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * EVALUATION — Static Position Assessment
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Evaluation {

    // Material values (centi pawns) for mid and end game
    private static final int[] MATERIAL_MG = { 100, 320, 330, 500, 900, 20000 };
    private static final int[] MATERIAL_EG = { 120, 320, 330, 550, 900, 20000 };

    // Piece weights
    private static final int PHASE_KNIGHT = 1;
    private static final int PHASE_BISHOP = 1;
    private static final int PHASE_ROOK   = 2;
    private static final int PHASE_QUEEN  = 4;
    private static final int PHASE_TOTAL = 24;

    // Bishop pair bonus
    private static final int BISHOP_PAIR_MG = 15;
    private static final int BISHOP_PAIR_EG = 25;

    // Passed pawn bonus
    private static final int[] PASSED_PAWN_MG = { 0, 0, 5, 10, 20, 40, 70, 0 };
    private static final int[] PASSED_PAWN_EG = { 0, 10, 15, 30, 55, 90, 150, 0 };
    private static final long[] WHITE_PASSED_MASK = new long[64];
    private static final long[] BLACK_PASSED_MASK = new long[64];

    static {
        for (int sq = 0; sq < 64; sq++) {
            int file = sq & 7;
            int rank = sq >>> 3;

            // Build a 3-file mask (own file + neighbours, clipped at edges)
            long fileMask = fileBits(file);
            if (file > 0) fileMask |= fileBits(file - 1);
            if (file < 7) fileMask |= fileBits(file + 1);

            // For white: need all ranks strictly above this pawn's rank
            long aheadWhite = 0L;
            for (int r = rank + 1; r < 8; r++) aheadWhite |= 0xFFL << (r * 8);
            WHITE_PASSED_MASK[sq] = fileMask & aheadWhite;

            // For black: need all ranks strictly below
            long aheadBlack = 0L;
            for (int r = rank - 1; r >= 0; r--) aheadBlack |= 0xFFL << (r * 8);
            BLACK_PASSED_MASK[sq] = fileMask & aheadBlack;
        }
    }

    private static long fileBits(int file) {
        return 0x0101010101010101L << file;
    }

    // *************** Midgame position values *******************
    private static final int[] PAWN_MG = {
            0,  0,  0,  0,  0,  0,  0,  0,
            5, 10, 10,-20,-20, 10, 10,  5,
            5, -5,-10,  0,  0,-10, -5,  5,
            0,  0,  0, 20, 20,  0,  0,  0,
            5,  5, 10, 25, 25, 10,  5,  5,
            10, 10, 20, 30, 30, 20, 10, 10,
            50, 50, 50, 50, 50, 50, 50, 50,
            0,  0,  0,  0,  0,  0,  0,  0
    };

    private static final int[] KNIGHT_MG = {
            -50,-40,-30,-30,-30,-30,-40,-50,
            -40,-20,  0,  5,  5,  0,-20,-40,
            -30,  5, 10, 15, 15, 10,  5,-30,
            -30,  0, 15, 20, 20, 15,  0,-30,
            -30,  5, 15, 20, 20, 15,  5,-30,
            -30,  0, 10, 15, 15, 10,  0,-30,
            -40,-20,  0,  0,  0,  0,-20,-40,
            -50,-40,-30,-30,-30,-30,-40,-50
    };

    private static final int[] BISHOP_MG = {
            -20,-10,-10,-10,-10,-10,-10,-20,
            -10,  5,  0,  0,  0,  0,  5,-10,
            -10, 10, 10, 10, 10, 10, 10,-10,
            -10,  0, 10, 10, 10, 10,  0,-10,
            -10,  5,  5, 10, 10,  5,  5,-10,
            -10,  0,  5, 10, 10,  5,  0,-10,
            -10,  0,  0,  0,  0,  0,  0,-10,
            -20,-10,-10,-10,-10,-10,-10,-20
    };

    private static final int[] ROOK_MG = {
            0,  0,  5, 10, 10,  5,  0,  0,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            5, 10, 10, 10, 10, 10, 10,  5,
            0,  0,  0,  0,  0,  0,  0,  0
    };

    private static final int[] QUEEN_MG = {
            -20,-10,-10, -5, -5,-10,-10,-20,
            -10,  0,  5,  0,  0,  0,  0,-10,
            -10,  5,  5,  5,  5,  5,  0,-10,
            0,  0,  5,  5,  5,  5,  0, -5,
            -5,  0,  5,  5,  5,  5,  0, -5,
            -10,  0,  5,  5,  5,  5,  0,-10,
            -10,  0,  0,  0,  0,  0,  0,-10,
            -20,-10,-10, -5, -5,-10,-10,-20
    };

    private static final int[] KING_MG = {
            20, 30, 10,  0,  0, 10, 30, 20,
            20, 20,  0,  0,  0,  0, 20, 20,
            -10,-20,-20,-20,-20,-20,-20,-10,
            -20,-30,-30,-40,-40,-30,-30,-20,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30
    };

    // *************** Endgame position values *******************

    private static final int[] PAWN_EG = {
            0,  0,  0,  0,  0,  0,  0,  0,
            0,  0,  0,  0,  0,  0,  0,  0,
            10, 10, 10, 10, 10, 10, 10, 10,
            20, 20, 20, 20, 20, 20, 20, 20,
            40, 40, 40, 40, 40, 40, 40, 40,
            60, 60, 60, 60, 60, 60, 60, 60,
            90, 90, 90, 90, 90, 90, 90, 90,
            0,  0,  0,  0,  0,  0,  0,  0
    };

    private static final int[] KNIGHT_EG = {
            -40,-30,-20,-20,-20,-20,-30,-40,
            -30,-15,  0,  0,  0,  0,-15,-30,
            -20,  0, 10, 10, 10, 10,  0,-20,
            -20,  0, 10, 15, 15, 10,  0,-20,
            -20,  0, 10, 15, 15, 10,  0,-20,
            -20,  0, 10, 10, 10, 10,  0,-20,
            -30,-15,  0,  0,  0,  0,-15,-30,
            -40,-30,-20,-20,-20,-20,-30,-40
    };

    private static final int[] BISHOP_EG = {
            -15,-10,-10,-10,-10,-10,-10,-15,
            -10,  0,  0,  0,  0,  0,  0,-10,
            -10,  0,  5,  5,  5,  5,  0,-10,
            -10,  0,  5, 10, 10,  5,  0,-10,
            -10,  0,  5, 10, 10,  5,  0,-10,
            -10,  0,  5,  5,  5,  5,  0,-10,
            -10,  0,  0,  0,  0,  0,  0,-10,
            -15,-10,-10,-10,-10,-10,-10,-15
    };

    private static final int[] ROOK_EG = {
            5,  5,  5,  5,  5,  5,  5,  5,
            0,  0,  0,  0,  0,  0,  0,  0,
            0,  0,  0,  0,  0,  0,  0,  0,
            0,  0,  0,  0,  0,  0,  0,  0,
            0,  0,  0,  0,  0,  0,  0,  0,
            0,  0,  0,  0,  0,  0,  0,  0,
            10, 10, 10, 10, 10, 10, 10, 10,
            5,  5,  5,  5,  5,  5,  5,  5
    };

    private static final int[] QUEEN_EG = {
            -10, -5, -5,  0,  0, -5, -5,-10,
            -5,  0,  5,  5,  5,  5,  0, -5,
            -5,  5,  5,  5,  5,  5,  5, -5,
            0,  5,  5,  5,  5,  5,  5,  0,
            0,  5,  5,  5,  5,  5,  5,  0,
            -5,  5,  5,  5,  5,  5,  5, -5,
            -5,  0,  5,  5,  5,  5,  0, -5,
            -10, -5, -5,  0,  0, -5, -5,-10
    };

    /** Endgame king PST: centralise! The king is a fighting piece in the endgame. */
    private static final int[] KING_EG = {
            -50,-30,-20,-10,-10,-20,-30,-50,
            -30,-10,  0, 10, 10,  0,-10,-30,
            -20,  0, 20, 30, 30, 20,  0,-20,
            -10, 10, 30, 40, 40, 30, 10,-10,
            -10, 10, 30, 40, 40, 30, 10,-10,
            -20,  0, 20, 30, 30, 20,  0,-20,
            -30,-10,  0, 10, 10,  0,-10,-30,
            -50,-30,-20,-10,-10,-20,-30,-50
    };

    private static final int[][] PST_MG_WHITE = { PAWN_MG, KNIGHT_MG, BISHOP_MG, ROOK_MG, QUEEN_MG, KING_MG };
    private static final int[][] PST_EG_WHITE = { PAWN_EG, KNIGHT_EG, BISHOP_EG, ROOK_EG, QUEEN_EG, KING_EG };

    private static final int[][] PST_MG_BLACK = new int[6][64];
    private static final int[][] PST_EG_BLACK = new int[6][64];

    static {
        // Mirror White's tables vertically for Black: sq ^ 56 flips the rank.
        for (int type = 0; type < 6; type++) {
            for (int sq = 0; sq < 64; sq++) {
                PST_MG_BLACK[type][sq] = PST_MG_WHITE[type][sq ^ 56];
                PST_EG_BLACK[type][sq] = PST_EG_WHITE[type][sq ^ 56];
            }
        }
    }


    /**
     * Compute the static evaluation of the given position.
     */
    public static int evaluate(Board board) {
        int mgWhite = 0;
        int mgBlack = 0;

        int egWhite = 0;
        int egBlack = 0;

        int phase = 0;

        for (int type = 0; type < 6; type++) {
            int matMg = MATERIAL_MG[type];
            int matEg = MATERIAL_EG[type];
            int phaseWeight = phaseWeight(type);

            // White pieces of this type
            long wp = board.getPieces()[type];
            while (wp != 0) {
                int sq = Long.numberOfTrailingZeros(wp);
                wp &= wp - 1;
                mgWhite += matMg + PST_MG_WHITE[type][sq];
                egWhite += matEg + PST_EG_WHITE[type][sq];
                phase += phaseWeight;
            }

            // Black pieces of this type (pieces[6..11])
            long bp = board.getPieces()[type + 6];
            while (bp != 0) {
                int sq = Long.numberOfTrailingZeros(bp);
                bp &= bp - 1;
                mgBlack += matMg + PST_MG_BLACK[type][sq];
                egBlack += matEg + PST_EG_BLACK[type][sq];
                phase += phaseWeight;
            }
        }

        // Clamp: if somehow more pieces are on the board than a normal start
        // (theoretical via promotions) never scale above 1.0.
        if (phase > PHASE_TOTAL) phase = PHASE_TOTAL;

        int mgScore = mgWhite - mgBlack;
        int egScore = egWhite - egBlack;

        // --- Bishop pair bonus ---
        if (Long.bitCount(board.getPieces()[2]) >= 2) {       // white bishops
            mgScore += BISHOP_PAIR_MG;
            egScore += BISHOP_PAIR_EG;
        }
        if (Long.bitCount(board.getPieces()[2 + 6]) >= 2) {   // black bishops
            mgScore -= BISHOP_PAIR_MG;
            egScore -= BISHOP_PAIR_EG;
        }

        // --- Passed pawn bonus ---
        long whitePawns = board.getPieces()[0];
        long blackPawns = board.getPieces()[6];

        long wp = whitePawns;
        while (wp != 0) {
            int sq = Long.numberOfTrailingZeros(wp);
            wp &= wp - 1;
            if ((WHITE_PASSED_MASK[sq] & blackPawns) == 0L) {
                int rank = sq >>> 3;
                mgScore += PASSED_PAWN_MG[rank];
                egScore += PASSED_PAWN_EG[rank];
            }
        }

        long bp = blackPawns;
        while (bp != 0) {
            int sq = Long.numberOfTrailingZeros(bp);
            bp &= bp - 1;
            if ((BLACK_PASSED_MASK[sq] & whitePawns) == 0L) {
                // From Black's perspective: rank 7 from bottom == rank 2 for black
                int rankFromBlack = 7 - (sq >>> 3);
                mgScore -= PASSED_PAWN_MG[rankFromBlack];
                egScore -= PASSED_PAWN_EG[rankFromBlack];
            }
        }

        // score
        int score = (mgScore * phase + egScore * (PHASE_TOTAL - phase)) / PHASE_TOTAL;

        return board.getSideToMove() == PieceUtils.WHITE ? score : -score;
    }

    /** Per-piece contribution to the game phase. Pawns and kings contribute 0. */
    private static int phaseWeight(int type) {
        return switch (type) {
            case 1 -> PHASE_KNIGHT;
            case 2 -> PHASE_BISHOP;
            case 3 -> PHASE_ROOK;
            case 4 -> PHASE_QUEEN;
            default -> 0;  // PAWN, KING
        };
    }
}
