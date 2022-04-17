package ch.nostromo.tiffanys.dragonborn.engine.ai.eval;

public class TiffanysAiTools {

    public static int[] COMMON_BOARD_VALUES = null;

    public static int[] PAWN_WHITE_VALUES = null;

    public static int[] PAWN_BLACK_VALUES = null;

    public static int[] KNIGHT_WHITE_VALUES = null;

    public static int[] KNIGHT_BLACK_VALUES = null;

    public static int[] BISHOP_WHITE_VALUES = null;

    public static int[] BISHOP_BLACK_VALUES = null;

    public static int[] ROOK_WHITE_VALUES = null;

    public static int[] ROOK_BLACK_VALUES = null;

    public static int[] QUEEN_WHITE_VALUES = null;

    public static int[] QUEEN_BLACK_VALUES = null;

    public static int[] KING_WHITE_VALUES = null;

    public static int[] KING_BLACK_VALUES = null;

    static {

        initPawnBoards();
        initKnightBoards();
        initBishopBoards();
        initRookBoards();
        initKingBoards();

    }

    public static void initPawnBoards() {

        PAWN_WHITE_VALUES = new int[64];
        PAWN_BLACK_VALUES = new int[64];

        PAWN_WHITE_VALUES[0] = 0;
        PAWN_WHITE_VALUES[1] = 0;
        PAWN_WHITE_VALUES[2] = 0;
        PAWN_WHITE_VALUES[3] = 5;
        PAWN_WHITE_VALUES[4] = 5;
        PAWN_WHITE_VALUES[5] = 0;
        PAWN_WHITE_VALUES[6] = 0;
        PAWN_WHITE_VALUES[7] = 0;

        PAWN_WHITE_VALUES[8] = 0;
        PAWN_WHITE_VALUES[9] = 0;
        PAWN_WHITE_VALUES[10] = 0;
        PAWN_WHITE_VALUES[11] = 5;
        PAWN_WHITE_VALUES[12] = 5;
        PAWN_WHITE_VALUES[13] = 0;
        PAWN_WHITE_VALUES[14] = 0;
        PAWN_WHITE_VALUES[15] = 0;

        PAWN_WHITE_VALUES[16] = 0;
        PAWN_WHITE_VALUES[17] = 0;
        PAWN_WHITE_VALUES[18] = 0;
        PAWN_WHITE_VALUES[19] = 15;
        PAWN_WHITE_VALUES[20] = 15;
        PAWN_WHITE_VALUES[21] = 0;
        PAWN_WHITE_VALUES[22] = 0;
        PAWN_WHITE_VALUES[23] = 0;

        PAWN_WHITE_VALUES[24] = 0;
        PAWN_WHITE_VALUES[25] = 0;
        PAWN_WHITE_VALUES[26] = 0;
        PAWN_WHITE_VALUES[27] = 25;
        PAWN_WHITE_VALUES[28] = 25;
        PAWN_WHITE_VALUES[29] = 0;
        PAWN_WHITE_VALUES[30] = 0;
        PAWN_WHITE_VALUES[31] = 0;

        PAWN_WHITE_VALUES[32] = 0;
        PAWN_WHITE_VALUES[33] = 0;
        PAWN_WHITE_VALUES[34] = 0;
        PAWN_WHITE_VALUES[35] = 15;
        PAWN_WHITE_VALUES[36] = 15;
        PAWN_WHITE_VALUES[37] = 0;
        PAWN_WHITE_VALUES[38] = 0;
        PAWN_WHITE_VALUES[39] = 0;

        PAWN_WHITE_VALUES[40] = 0;
        PAWN_WHITE_VALUES[41] = 0;
        PAWN_WHITE_VALUES[42] = 0;
        PAWN_WHITE_VALUES[43] = 5;
        PAWN_WHITE_VALUES[44] = 5;
        PAWN_WHITE_VALUES[45] = 0;
        PAWN_WHITE_VALUES[46] = 0;
        PAWN_WHITE_VALUES[47] = 0;

        PAWN_WHITE_VALUES[48] = 0;
        PAWN_WHITE_VALUES[49] = 0;
        PAWN_WHITE_VALUES[50] = 0;
        PAWN_WHITE_VALUES[51] = 5;
        PAWN_WHITE_VALUES[52] = 5;
        PAWN_WHITE_VALUES[53] = 0;
        PAWN_WHITE_VALUES[54] = 0;
        PAWN_WHITE_VALUES[55] = 0;

        PAWN_WHITE_VALUES[56] = 0;
        PAWN_WHITE_VALUES[57] = 0;
        PAWN_WHITE_VALUES[58] = 0;
        PAWN_WHITE_VALUES[59] = 5;
        PAWN_WHITE_VALUES[60] = 5;
        PAWN_WHITE_VALUES[61] = 0;
        PAWN_WHITE_VALUES[62] = 0;
        PAWN_WHITE_VALUES[63] = 0;

        int counter = 63;
        for (int i = 0; i < PAWN_WHITE_VALUES.length; i++) {
            PAWN_BLACK_VALUES[counter] = PAWN_WHITE_VALUES[i];
            counter--;
        }

    }

    public static void initKnightBoards() {

        KNIGHT_WHITE_VALUES = new int[64];
        KNIGHT_BLACK_VALUES = new int[64];

        KNIGHT_WHITE_VALUES[0] = -50;
        KNIGHT_WHITE_VALUES[1] = -40;
        KNIGHT_WHITE_VALUES[2] = -30;
        KNIGHT_WHITE_VALUES[3] = -25;
        KNIGHT_WHITE_VALUES[4] = -25;
        KNIGHT_WHITE_VALUES[5] = -30;
        KNIGHT_WHITE_VALUES[6] = -40;
        KNIGHT_WHITE_VALUES[7] = -50;

        KNIGHT_WHITE_VALUES[8] = -35;
        KNIGHT_WHITE_VALUES[9] = -25;
        KNIGHT_WHITE_VALUES[10] = -15;
        KNIGHT_WHITE_VALUES[11] = -10;
        KNIGHT_WHITE_VALUES[12] = -10;
        KNIGHT_WHITE_VALUES[13] = -15;
        KNIGHT_WHITE_VALUES[14] = -25;
        KNIGHT_WHITE_VALUES[15] = -35;

        KNIGHT_WHITE_VALUES[16] = -20;
        KNIGHT_WHITE_VALUES[17] = -10;
        KNIGHT_WHITE_VALUES[18] = 0;
        KNIGHT_WHITE_VALUES[19] = 5;
        KNIGHT_WHITE_VALUES[20] = 5;
        KNIGHT_WHITE_VALUES[21] = 0;
        KNIGHT_WHITE_VALUES[22] = -10;
        KNIGHT_WHITE_VALUES[23] = -20;

        KNIGHT_WHITE_VALUES[24] = -10;
        KNIGHT_WHITE_VALUES[25] = 0;
        KNIGHT_WHITE_VALUES[26] = 10;
        KNIGHT_WHITE_VALUES[27] = 15;
        KNIGHT_WHITE_VALUES[28] = 15;
        KNIGHT_WHITE_VALUES[29] = 10;
        KNIGHT_WHITE_VALUES[30] = 0;
        KNIGHT_WHITE_VALUES[31] = -10;

        KNIGHT_WHITE_VALUES[32] = -5;
        KNIGHT_WHITE_VALUES[33] = 5;
        KNIGHT_WHITE_VALUES[34] = 15;
        KNIGHT_WHITE_VALUES[35] = 20;
        KNIGHT_WHITE_VALUES[36] = 20;
        KNIGHT_WHITE_VALUES[37] = 15;
        KNIGHT_WHITE_VALUES[38] = 5;
        KNIGHT_WHITE_VALUES[39] = -5;

        KNIGHT_WHITE_VALUES[40] = -5;
        KNIGHT_WHITE_VALUES[41] = 5;
        KNIGHT_WHITE_VALUES[42] = 15;
        KNIGHT_WHITE_VALUES[43] = 20;
        KNIGHT_WHITE_VALUES[44] = 20;
        KNIGHT_WHITE_VALUES[45] = 15;
        KNIGHT_WHITE_VALUES[46] = 5;
        KNIGHT_WHITE_VALUES[47] = -5;

        KNIGHT_WHITE_VALUES[48] = -20;
        KNIGHT_WHITE_VALUES[49] = -10;
        KNIGHT_WHITE_VALUES[50] = 0;
        KNIGHT_WHITE_VALUES[51] = 5;
        KNIGHT_WHITE_VALUES[52] = 5;
        KNIGHT_WHITE_VALUES[53] = 0;
        KNIGHT_WHITE_VALUES[54] = -10;
        KNIGHT_WHITE_VALUES[55] = -20;

        KNIGHT_WHITE_VALUES[56] = -135;
        KNIGHT_WHITE_VALUES[57] = -25;
        KNIGHT_WHITE_VALUES[58] = -15;
        KNIGHT_WHITE_VALUES[59] = -10;
        KNIGHT_WHITE_VALUES[60] = -10;
        KNIGHT_WHITE_VALUES[61] = -15;
        KNIGHT_WHITE_VALUES[62] = -25;
        KNIGHT_WHITE_VALUES[63] = -135;

        int counter = 63;
        for (int i = 0; i < KNIGHT_WHITE_VALUES.length; i++) {
            KNIGHT_BLACK_VALUES[counter] = KNIGHT_WHITE_VALUES[i];
            counter--;
        }

    }

    public static void initBishopBoards() {

        BISHOP_WHITE_VALUES = new int[64];
        BISHOP_BLACK_VALUES = new int[64];

        BISHOP_WHITE_VALUES[0] = -20;
        BISHOP_WHITE_VALUES[1] = -15;
        BISHOP_WHITE_VALUES[2] = -15;
        BISHOP_WHITE_VALUES[3] = -13;
        BISHOP_WHITE_VALUES[4] = -13;
        BISHOP_WHITE_VALUES[5] = -15;
        BISHOP_WHITE_VALUES[6] = -15;
        BISHOP_WHITE_VALUES[7] = -20;

        BISHOP_WHITE_VALUES[8] = -5;
        BISHOP_WHITE_VALUES[9] = 0;
        BISHOP_WHITE_VALUES[10] = -5;
        BISHOP_WHITE_VALUES[11] = 0;
        BISHOP_WHITE_VALUES[12] = 0;
        BISHOP_WHITE_VALUES[13] = -5;
        BISHOP_WHITE_VALUES[14] = 0;
        BISHOP_WHITE_VALUES[15] = -5;

        BISHOP_WHITE_VALUES[16] = -6;
        BISHOP_WHITE_VALUES[17] = -2;
        BISHOP_WHITE_VALUES[18] = 4;
        BISHOP_WHITE_VALUES[19] = 2;
        BISHOP_WHITE_VALUES[20] = 2;
        BISHOP_WHITE_VALUES[21] = 4;
        BISHOP_WHITE_VALUES[22] = -2;
        BISHOP_WHITE_VALUES[23] = -6;

        BISHOP_WHITE_VALUES[24] = -4;
        BISHOP_WHITE_VALUES[25] = 0;
        BISHOP_WHITE_VALUES[26] = 2;
        BISHOP_WHITE_VALUES[27] = 10;
        BISHOP_WHITE_VALUES[28] = 10;
        BISHOP_WHITE_VALUES[29] = 2;
        BISHOP_WHITE_VALUES[30] = 0;
        BISHOP_WHITE_VALUES[31] = -4;

        BISHOP_WHITE_VALUES[32] = -4;
        BISHOP_WHITE_VALUES[33] = 0;
        BISHOP_WHITE_VALUES[34] = 2;
        BISHOP_WHITE_VALUES[35] = 10;
        BISHOP_WHITE_VALUES[36] = 10;
        BISHOP_WHITE_VALUES[37] = 2;
        BISHOP_WHITE_VALUES[38] = 0;
        BISHOP_WHITE_VALUES[39] = -4;

        BISHOP_WHITE_VALUES[40] = -6;
        BISHOP_WHITE_VALUES[41] = -2;
        BISHOP_WHITE_VALUES[42] = 4;
        BISHOP_WHITE_VALUES[43] = 2;
        BISHOP_WHITE_VALUES[44] = 2;
        BISHOP_WHITE_VALUES[45] = 4;
        BISHOP_WHITE_VALUES[46] = -2;
        BISHOP_WHITE_VALUES[47] = -6;

        BISHOP_WHITE_VALUES[48] = -5;
        BISHOP_WHITE_VALUES[49] = 0;
        BISHOP_WHITE_VALUES[50] = -2;
        BISHOP_WHITE_VALUES[51] = 0;
        BISHOP_WHITE_VALUES[52] = 0;
        BISHOP_WHITE_VALUES[53] = -2;
        BISHOP_WHITE_VALUES[54] = 0;
        BISHOP_WHITE_VALUES[55] = -5;

        BISHOP_WHITE_VALUES[56] = -8;
        BISHOP_WHITE_VALUES[57] = -8;
        BISHOP_WHITE_VALUES[58] = -6;
        BISHOP_WHITE_VALUES[59] = -4;
        BISHOP_WHITE_VALUES[60] = -4;
        BISHOP_WHITE_VALUES[61] = -6;
        BISHOP_WHITE_VALUES[62] = -8;
        BISHOP_WHITE_VALUES[63] = -8;

        int counter = 63;
        for (int i = 0; i < BISHOP_WHITE_VALUES.length; i++) {
            BISHOP_BLACK_VALUES[counter] = BISHOP_WHITE_VALUES[i];
            counter--;
        }

    }

    public static void initRookBoards() {

        ROOK_WHITE_VALUES = new int[64];
        ROOK_BLACK_VALUES = new int[64];

        // ROOK_WHITE_VALUES[21] = -6;
        // ROOK_WHITE_VALUES[22] = -3;
        // ROOK_WHITE_VALUES[23] = 0;
        // ROOK_WHITE_VALUES[24] = 3;
        // ROOK_WHITE_VALUES[25] = 3;
        // ROOK_WHITE_VALUES[26] = 0;
        // ROOK_WHITE_VALUES[27] = -3;
        // ROOK_WHITE_VALUES[28] = -6;
        //
        // ROOK_WHITE_VALUES[31] = -6;
        // ROOK_WHITE_VALUES[32] = -3;
        // ROOK_WHITE_VALUES[33] = 0;
        // ROOK_WHITE_VALUES[34] = 3;
        // ROOK_WHITE_VALUES[35] = 3;
        // ROOK_WHITE_VALUES[36] = 0;
        // ROOK_WHITE_VALUES[37] = -3;
        // ROOK_WHITE_VALUES[38] = -6;
        //
        // ROOK_WHITE_VALUES[41] = -6;
        // ROOK_WHITE_VALUES[42] = -3;
        // ROOK_WHITE_VALUES[43] = 0;
        // ROOK_WHITE_VALUES[44] = 3;
        // ROOK_WHITE_VALUES[45] = 3;
        // ROOK_WHITE_VALUES[46] = 0;
        // ROOK_WHITE_VALUES[47] = -3;
        // ROOK_WHITE_VALUES[48] = -6;
        //
        // ROOK_WHITE_VALUES[51] = -6;
        // ROOK_WHITE_VALUES[52] = -3;
        // ROOK_WHITE_VALUES[53] = 0;
        // ROOK_WHITE_VALUES[54] = 3;
        // ROOK_WHITE_VALUES[55] = 3;
        // ROOK_WHITE_VALUES[56] = 0;
        // ROOK_WHITE_VALUES[57] = -3;
        // ROOK_WHITE_VALUES[58] = -6;
        //
        // ROOK_WHITE_VALUES[61] = -6;
        // ROOK_WHITE_VALUES[62] = -3;
        // ROOK_WHITE_VALUES[63] = 0;
        // ROOK_WHITE_VALUES[64] = 3;
        // ROOK_WHITE_VALUES[65] = 3;
        // ROOK_WHITE_VALUES[66] = 0;
        // ROOK_WHITE_VALUES[67] = -3;
        // ROOK_WHITE_VALUES[68] = -6;
        //
        // ROOK_WHITE_VALUES[71] = -6;
        // ROOK_WHITE_VALUES[72] = -3;
        // ROOK_WHITE_VALUES[73] = 0;
        // ROOK_WHITE_VALUES[74] = 3;
        // ROOK_WHITE_VALUES[75] = 3;
        // ROOK_WHITE_VALUES[76] = 0;
        // ROOK_WHITE_VALUES[77] = -3;
        // ROOK_WHITE_VALUES[78] = -6;
        //
        // ROOK_WHITE_VALUES[81] = -6;
        // ROOK_WHITE_VALUES[82] = -3;
        // ROOK_WHITE_VALUES[83] = 0;
        // ROOK_WHITE_VALUES[84] = 3;
        // ROOK_WHITE_VALUES[85] = 3;
        // ROOK_WHITE_VALUES[86] = 0;
        // ROOK_WHITE_VALUES[87] = -3;
        // ROOK_WHITE_VALUES[88] = -6;
        //
        // ROOK_WHITE_VALUES[91] = -6;
        // ROOK_WHITE_VALUES[92] = -3;
        // ROOK_WHITE_VALUES[93] = 0;
        // ROOK_WHITE_VALUES[94] = 3;
        // ROOK_WHITE_VALUES[95] = 3;
        // ROOK_WHITE_VALUES[96] = 0;
        // ROOK_WHITE_VALUES[97] = -3;
        // ROOK_WHITE_VALUES[98] = -6;

        int counter = 63;
        for (int i = 0; i < ROOK_WHITE_VALUES.length; i++) {
            ROOK_BLACK_VALUES[counter] = ROOK_WHITE_VALUES[i];
            counter--;
        }

    }

    public static void initKingBoards() {

        KING_WHITE_VALUES = new int[64];
        KING_BLACK_VALUES = new int[64];

        KING_WHITE_VALUES[0] = 10;
        KING_WHITE_VALUES[1] = 20;
        KING_WHITE_VALUES[2] = 10;
        KING_WHITE_VALUES[3] = 0;
        KING_WHITE_VALUES[4] = 0;
        KING_WHITE_VALUES[5] = 0;
        KING_WHITE_VALUES[6] = 20;
        KING_WHITE_VALUES[7] = 10;

        KING_WHITE_VALUES[8] = 10;
        KING_WHITE_VALUES[9] = 15;
        KING_WHITE_VALUES[10] = 0;
        KING_WHITE_VALUES[11] = 0;
        KING_WHITE_VALUES[12] = 0;
        KING_WHITE_VALUES[13] = 0;
        KING_WHITE_VALUES[14] = 15;
        KING_WHITE_VALUES[15] = 10;

        KING_WHITE_VALUES[16] = -10;
        KING_WHITE_VALUES[17] = -20;
        KING_WHITE_VALUES[18] = -20;
        KING_WHITE_VALUES[19] = -25;
        KING_WHITE_VALUES[20] = -25;
        KING_WHITE_VALUES[21] = -20;
        KING_WHITE_VALUES[22] = -20;
        KING_WHITE_VALUES[23] = -10;

        KING_WHITE_VALUES[24] = -15;
        KING_WHITE_VALUES[25] = -25;
        KING_WHITE_VALUES[26] = -40;
        KING_WHITE_VALUES[27] = -40;
        KING_WHITE_VALUES[28] = -40;
        KING_WHITE_VALUES[29] = -40;
        KING_WHITE_VALUES[30] = -25;
        KING_WHITE_VALUES[31] = -15;

        KING_WHITE_VALUES[32] = -30;
        KING_WHITE_VALUES[33] = -40;
        KING_WHITE_VALUES[34] = -40;
        KING_WHITE_VALUES[35] = -40;
        KING_WHITE_VALUES[36] = -40;
        KING_WHITE_VALUES[37] = -40;
        KING_WHITE_VALUES[38] = -40;
        KING_WHITE_VALUES[39] = -30;

        KING_WHITE_VALUES[40] = -50;
        KING_WHITE_VALUES[41] = -50;
        KING_WHITE_VALUES[42] = -50;
        KING_WHITE_VALUES[43] = -50;
        KING_WHITE_VALUES[44] = -50;
        KING_WHITE_VALUES[45] = -50;
        KING_WHITE_VALUES[46] = -50;
        KING_WHITE_VALUES[47] = -50;

        KING_WHITE_VALUES[48] = -50;
        KING_WHITE_VALUES[49] = -50;
        KING_WHITE_VALUES[50] = -50;
        KING_WHITE_VALUES[51] = -50;
        KING_WHITE_VALUES[52] = -50;
        KING_WHITE_VALUES[53] = -50;
        KING_WHITE_VALUES[54] = -50;
        KING_WHITE_VALUES[55] = -50;

        KING_WHITE_VALUES[56] = -50;
        KING_WHITE_VALUES[57] = -50;
        KING_WHITE_VALUES[58] = -50;
        KING_WHITE_VALUES[59] = -50;
        KING_WHITE_VALUES[60] = -50;
        KING_WHITE_VALUES[61] = -50;
        KING_WHITE_VALUES[62] = -50;
        KING_WHITE_VALUES[63] = -50;

        int counter = 63;
        for (int i = 0; i < KING_WHITE_VALUES.length; i++) {
            KING_BLACK_VALUES[counter] = KING_WHITE_VALUES[i];
            counter--;
        }

    }

}
