package ch.nostromo.tiffanys.dragonborn.engine.board;

import ch.nostromo.tiffanys.commons.board.Piece;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * PIECE CONSTANTS AND ENCODING
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PieceUtils {

    // ---- Colors ----
    /** White side. Also used as array index (0). */
    public static final int WHITE = 0;
    /** Black side. Also used as array index (1). */
    public static final int BLACK = 1;

    // ---- Piece types ----

    public static final int PAWN   = 0;
    public static final int KNIGHT = 1;
    public static final int BISHOP = 2;
    public static final int ROOK   = 3;
    public static final int QUEEN  = 4;
    public static final int KING   = 5;

    public static final int NONE   = 6;

    // ---- Colored piece constants (for readability in code) ----

    public static final int WHITE_PAWN = 0;
    public static final int WHITE_KNIGHT = 1;
    public static final int WHITE_BISHOP = 2;
    public static final int WHITE_ROOK = 3;
    public static final int WHITE_QUEEN = 4;
    public static final int WHITE_KING = 5;
    public static final int BLACK_PAWN = 6;
    public static final int BLACK_KNIGHT = 7;
    public static final int BLACK_BISHOP = 8;
    public static final int BLACK_ROOK = 9;
    public static final int BLACK_QUEEN = 10;
    public static final int BLACK_KING = 11;

    // ---- Fast lookup tables (avoid division/modulo in hot paths) ----

    /** Maps colored piece (0..11) → piece type (0..5). */
    private static final int[] TYPE_OF  = { 0, 1, 2, 3, 4, 5,   0, 1, 2, 3, 4, 5 };

    /** Maps colored piece (0..11) → color (0 or 1). */
    private static final int[] COLOR_OF = { 0, 0, 0, 0, 0, 0,   1, 1, 1, 1, 1, 1 };

    // ---- Methods ----

    /**
     * Combine a color and piece type into a colored piece index.
     */
    public static int createColoredPiece(int color, int type) {
        return color * 6 + type;
    }

    /** Extract the color from a colored piece. Uses a lookup table for speed. */
    public static int getColorOfPiece(int piece) {
        return COLOR_OF[piece];
    }

    /** Extract the piece type from a colored piece. Uses a lookup table for speed. */
    public static int getTypeOfPiece(int piece) {
        return TYPE_OF[piece];
    }

    /**
     * Return a piece int by it's Tiffanys enum value
     */
    public static int fromTiffanysPiece(Piece piece) {

        return switch (piece) {
            case WHITE_KING   -> WHITE_KING;
            case WHITE_QUEEN  -> WHITE_QUEEN;
            case WHITE_ROOK   -> WHITE_ROOK;
            case WHITE_BISHOP -> WHITE_BISHOP;
            case WHITE_KNIGHT -> WHITE_KNIGHT;
            case WHITE_PAWN   -> WHITE_PAWN;
            case BLACK_KING   -> BLACK_KING;
            case BLACK_QUEEN  -> BLACK_QUEEN;
            case BLACK_ROOK   -> BLACK_ROOK;
            case BLACK_BISHOP -> BLACK_BISHOP;
            case BLACK_KNIGHT -> BLACK_KNIGHT;
            case BLACK_PAWN   -> BLACK_PAWN;
        };

    }
}
