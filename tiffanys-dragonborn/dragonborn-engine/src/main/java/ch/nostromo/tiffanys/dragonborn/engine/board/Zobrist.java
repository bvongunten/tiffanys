package ch.nostromo.tiffanys.dragonborn.engine.board;

import java.util.Random;

public class Zobrist {

    /**
     * The random numbers for the key computation.
     */
    public static long[][][] ZOBRIST_FACTORS;

    /**
     * The factor for black moves.
     */
    public static long ZOBRIST_SIDE_TO_MOVE;

    public static final int ZOBRIST_KING = 0;
    public static final int ZOBRIST_QUEEN = 1;
    public static final int ZOBRIST_ROOK = 2;
    public static final int ZOBRIST_KNIGHT = 3;
    public static final int ZOBRIST_BISHOP = 4;
    public static final int ZOBRIST_PAWN = 5;

    public static final int ZOBRIST_WHITE = 0;
    public static final int ZOBRIST_BLACK = 1;


    static {

        // Create a new multidimensional array.
        ZOBRIST_FACTORS = new long[2][6][64];

        Random rand = new Random(1L);

        for (int i = 0; i < 64; i++) {
            for (int j = 0; j < 6; j++) {
                ZOBRIST_FACTORS[0][j][i] = Math.abs(rand.nextLong());
                ZOBRIST_FACTORS[1][j][i] = Math.abs(rand.nextLong());
            }
        }

        ZOBRIST_SIDE_TO_MOVE = Math.abs(rand.nextLong());
    }

}
