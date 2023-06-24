package ch.nostromo.tiffanys.dragonborn.engine.transposition;

import ch.nostromo.tiffanys.dragonborn.engine.bitboards.BaseBitboards;
import ch.nostromo.tiffanys.dragonborn.engine.board.PieceUtils;
import ch.nostromo.tiffanys.dragonborn.engine.board.Board;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;
import java.util.Random;

/** Zobrist Utils */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ZobristUtils {

    /**
     * Random key for each (piece, square) combination.
     */
    private static final long[][] PIECE_SQUARE = new long[12][64];

    /**
     * Random key for each possible castling-rights configuration.
     */
    private static final long[] CASTLING = new long[16];

    /**
     * Random key for each en-passant file.
     */
    private static final long[] EN_PASSANT = new long[8];

    /**
     * Side to move
     */
    public static final long SIDE_TO_MOVE;

    public static long getEnPassant(int square) {
        return EN_PASSANT[square];
    }


    public static long getCastling(int castlingRights) {
        return CASTLING[castlingRights];
    }

    public static long getPieceSquare(int piece, int square) {
        return PIECE_SQUARE[piece][square];
    }


    static {
        Random rng = new SecureRandom();         //  Deterministic random seed on standard impl -> 0x1234567890ABCDEFL

        for (int piece = 0; piece < 12; piece++) {
            for (int sq = 0; sq < 64; sq++) {
                PIECE_SQUARE[piece][sq] = rng.nextLong();
            }
        }
        for (int i = 0; i < 16; i++) CASTLING[i] = rng.nextLong();
        for (int i = 0; i < 8; i++)  EN_PASSANT[i] = rng.nextLong();
        SIDE_TO_MOVE = rng.nextLong();
    }

    /**
     * Compute the Zobrist hash of the given board from scratch.
     */
    public static long createHashForBoard(Board board) {
        long result = 0L;

        // XOR in all pieces on the board
        for (int sq = 0; sq < 64; sq++) {
            int piece = board.getMailbox()[sq];
            if (piece >= 0) {
                result ^= PIECE_SQUARE[piece][sq];
            }
        }

        // XOR in castling rights
        result ^= CASTLING[board.getCastlingRights()];

        // XOR in en-passant file (if any)
        if (board.getEnPassantSquare() >= 0) {
            result ^= EN_PASSANT[BaseBitboards.getFileBySquareIndex(board.getEnPassantSquare())];
        }

        // XOR in side to move
        if (board.getSideToMove() == PieceUtils.BLACK) {
            result ^= SIDE_TO_MOVE;
        }

        return result;
    }
}
