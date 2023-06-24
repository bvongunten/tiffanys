package ch.nostromo.tiffanys.dragonborn.engine.move;

import ch.nostromo.tiffanys.commons.board.Piece;
import ch.nostromo.tiffanys.commons.move.Castling;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.dragonborn.engine.bitboards.BaseBitboards;
import ch.nostromo.tiffanys.dragonborn.engine.board.PieceUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Move utils
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MoveUtils {


    /** Normal move — either a quiet move or a regular capture. */
    public static final int FLAG_NORMAL       = 0;
    /** Pawn double push — from rank 2 to rank 4 (white) or rank 7 to rank 5 (black). */
    public static final int FLAG_DOUBLE_PUSH  = 1;
    /** King-side castling (O-O). */
    public static final int FLAG_CASTLE_KING  = 2;
    /** Queen-side castling (O-O-O). */
    public static final int FLAG_CASTLE_QUEEN = 3;
    /** En passant capture — the captured pawn is NOT on the 'to' square. */
    public static final int FLAG_EN_PASSANT   = 4;
    /** Pawn promotion — the promotion piece type is in bits 12-14. */
    public static final int FLAG_PROMOTION    = 5;

    /** Bit mask for the capture flag (bit 18). */
    private static final int CAPTURE_BIT = 1 << 18;

    /**
     * Build a non-capture move with explicit flag and promotion type.
     */
    public static int makeMove(int from, int to, int flag, int promoType) {
        return from | (to << 6) | (promoType << 12) | (flag << 15);
    }

    /**
     * Build a simple quiet move (no special flags, no capture).
     */
    public static int makeMove(int from, int to) {
        return from | (to << 6);
    }

    /**
     * Build a capture move with the captured piece type baked in.
     */
    public static int makeCaptureMove(int from, int to, int flag, int promoType, int capturedType) {
        return from
             | (to << 6)
             | (promoType << 12)
             | (flag << 15)
             | CAPTURE_BIT
             | (capturedType << 19);
    }

    /** Source square (0..63). */
    public static int getFromSquare(int move) { return move & 0x3F; }

    /** Destination square (0..63). */
    public static int getToSquare(int move) { return (move >>> 6) & 0x3F; }

    /** Promotion piece type (0 if not a promotion; Piece.KNIGHT..QUEEN if it is). */
    public static int getPromotionType(int move) { return (move >>> 12) & 0x7; }

    /** Move flag (one of FLAG_* constants). */
    public static int getFlag(int move) { return (move >>> 15) & 0x7; }

    /** True if this move captures an opponent's piece (including en passant). */
    public static boolean isCapture(int move) { return (move & CAPTURE_BIT) != 0; }

    /**
     * The TYPE of the captured piece (Piece.PAWN..Piece.QUEEN).
     */
    public static int getCaptureType(int move) { return (move >>> 19) & 0x7; }

    /** True if this move is a pawn promotion. */
    public static boolean isPromotion(int move) { return getFlag(move) == FLAG_PROMOTION; }

    /** True if this move is castling (king-side or queen-side). */
    public static boolean isCastle(int move) {
        return isKingCastle(move) || isQueenCastle(move);
    }


    /** True if king castle */
    public static boolean isKingCastle(int move) {
        int f = getFlag(move);
        return f == FLAG_CASTLE_KING;
    }

    /** True if queen castle */
    public static boolean isQueenCastle(int move) {
        int f = getFlag(move);
        return f == FLAG_CASTLE_QUEEN;
    }

    /** True if this move is an en passant capture. */
    public static boolean isEnPassant(int move) { return getFlag(move) == FLAG_EN_PASSANT; }


    /**
     * Does create a Tiffanys move by given move
     */
    public static Move toTiffanysMove(int move) {

        Move result = new Move();

        result.setFrom(BaseBitboards.getTiffanysSquareBySquareIndex(getFromSquare(move)));
        result.setTo(BaseBitboards.getTiffanysSquareBySquareIndex(getToSquare(move)));

        if (isKingCastle(move)) {
            result.setCastling(getFromSquare(move) / 8 == 0 ? Castling.WHITE_SHORT : Castling.BLACK_SHORT);
            result.setFrom(null);
            result.setTo(null);
        } else if (isQueenCastle(move)) {
            result.setCastling(getFromSquare(move) / 8 == 0 ? Castling.WHITE_LONG : Castling.BLACK_LONG);
            result.setFrom(null);
            result.setTo(null);
        }

        if (isPromotion(move)) {
            int toRank = getToSquare(move) / 8;
            boolean isWhite = (toRank == 7);
            result.setPromotion(
                    switch (getPromotionType(move)) {
                        case PieceUtils.KNIGHT -> isWhite ? Piece.WHITE_KNIGHT : Piece.BLACK_KNIGHT;
                        case PieceUtils.BISHOP -> isWhite ? Piece.WHITE_BISHOP : Piece.BLACK_BISHOP;
                        case PieceUtils.ROOK -> isWhite ? Piece.WHITE_ROOK : Piece.BLACK_ROOK;
                        case PieceUtils.QUEEN -> isWhite ? Piece.WHITE_QUEEN : Piece.BLACK_QUEEN;
                        default -> throw new IllegalStateException("Unexpected value: " + getPromotionType(move));
                    }
            );
        }

        return result;
    }



}
