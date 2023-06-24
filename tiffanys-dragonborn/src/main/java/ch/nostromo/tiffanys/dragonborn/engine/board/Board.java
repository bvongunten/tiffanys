package ch.nostromo.tiffanys.dragonborn.engine.board;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.Side;
import ch.nostromo.tiffanys.commons.board.Square;
import ch.nostromo.tiffanys.dragonborn.engine.bitboards.AttackBitboards;
import ch.nostromo.tiffanys.dragonborn.engine.bitboards.BaseBitboards;
import ch.nostromo.tiffanys.dragonborn.engine.bitboards.MagicBitboards;
import ch.nostromo.tiffanys.dragonborn.engine.move.MoveUtils;
import ch.nostromo.tiffanys.dragonborn.engine.transposition.ZobristUtils;
import lombok.Getter;


/**
 * BOARD including current setup, make and unmake of a move
 */
public final class Board {


    /**
     * pieces[coloredPiece] = bitboard of all squares occupied by a piece.
     */
    @Getter
    final long[] pieces = new long[12];

    /**
     * occByColor[WHITE] = all white pieces; occByColor[BLACK] = all black pieces.
     */
    @Getter
    final long[] occByColor = new long[2];

    /**
     * All occupied squares (white | black). Used for sliding piece blocker detection.
     */
    @Getter
    long occupancy;

    /**
     * mailbox[sq] = colored piece index (0..11), or -1 if the square is empty.
     */
    @Getter
    final int[] mailbox = new int[64];

    /**
     * Current side to move. Piece.WHITE (0) or Piece.BLACK (1).
     */
    @Getter
    int sideToMove;

    /**
     * Castling rights as a 4-bit mask:
     * bit 0 (value 1) = white king-side
     * bit 1 (value 2) = white queen-side
     * bit 2 (value 4) = black king-side
     * bit 3 (value 8) = black queen-side
     */
    @Getter
    int castlingRights;

    public static final int CASTLE_WHITE_SHORT = 1;
    public static final int CASTLE_WHITE_LONG = 2;
    public static final int CASTLE_BLACK_SHORT = 4;
    public static final int CASTLE_BLACK_LONG = 8;

    /**
     * Square index of the en-passant target, or -1 if no EP is possible.
     */
    @Getter
    int enPassantSquare;

    /**
     * Number of half-moves since last capture or pawn move (for 50-move rule).
     */
    @Getter
    int halfmoveClock;

    /**
     * Full move counter (incremented after Black's move).
     */
    @Getter
    int fullmoveNumber;

    /**
     * Zobrist hash of the current position (maintained incrementally).
     */
    @Getter
    long hash;

    /**
     * PACKED UNDO — state saved/restored in makeMove/unmakeMove
     * <p>
     * Layout of the packed long returned by makeMove():
     * bits  0- 4 : capturedPiece + 1 (0 = no capture, 1..12 = piece index + 1)
     * bits  5- 8 : previous castling rights (4 bits)
     * bits  9-15 : previous en-passant square + 1 (0 = none, 1..64)
     * bits 16-22 : previous halfmove clock (7 bits, max 127)
     */
    private static final int U_CAP_SHIFT = 0;
    private static final long U_CAP_MASK = 0x1FL;
    private static final int U_CR_SHIFT = 5;
    private static final long U_CR_MASK = 0xFL;
    private static final int U_EP_SHIFT = 9;
    private static final long U_EP_MASK = 0x7FL;
    private static final int U_HM_SHIFT = 16;
    private static final long U_HM_MASK = 0x7FL;

    /**
     * Castling mask
     */
    private static final int[] CASTLING_MASK = new int[64];

    static {
        java.util.Arrays.fill(CASTLING_MASK, 0xF);
        CASTLING_MASK[0] = ~CASTLE_WHITE_LONG & 0xF;              // a1 — white queen-side rook
        CASTLING_MASK[7] = ~CASTLE_WHITE_SHORT & 0xF;              // h1 — white king-side rook
        CASTLING_MASK[4] = ~(CASTLE_WHITE_SHORT | CASTLE_WHITE_LONG) & 0xF;    // e1 — white king
        CASTLING_MASK[56] = ~CASTLE_BLACK_LONG & 0xF;              // a8 — black queen-side rook
        CASTLING_MASK[63] = ~CASTLE_BLACK_SHORT & 0xF;              // h8 — black king-side rook
        CASTLING_MASK[60] = ~(CASTLE_BLACK_SHORT | CASTLE_BLACK_LONG) & 0xF;    // e8 — black king
    }


    /**
     * Empty board
     */
    public Board() {
        java.util.Arrays.fill(mailbox, -1);
        enPassantSquare = -1;
    }

    /**
     * Create board by given Tiffanys chess game
     */
    public Board(ChessGame game) {
        this();

        ch.nostromo.tiffanys.commons.board.Board commonsBoard = game.getCurrentBoard();
        Side side = game.getCurrentSide();

        // --- Pieces ---
        for (Square square : Square.values()) {
            if (!commonsBoard.isEmptySquare(square)) {
                int dragonPiece = PieceUtils.fromTiffanysPiece(commonsBoard.getPiece(square));
                int file = square.getBoardIdx() % 10 - 1;
                int rank = square.getBoardIdx() / 10 - 2;
                int sq = BaseBitboards.getSquareIndex(file, rank);
                pieces[dragonPiece] |= BaseBitboards.createSingleBitBoard(sq);
                mailbox[sq] = dragonPiece;
            }
        }

        // --- Side to move ---
        sideToMove = side == Side.WHITE ? PieceUtils.WHITE : PieceUtils.BLACK;

        // --- Castling rights ---
        if (commonsBoard.isCastlingWhiteShortAllowed()) {
            castlingRights |= CASTLE_WHITE_SHORT;
        }
        if (commonsBoard.isCastlingWhiteLongAllowed()) {
            castlingRights |= CASTLE_WHITE_LONG;
        }
        if (commonsBoard.isCastlingBlackShortAllowed()) {
            castlingRights |= CASTLE_BLACK_SHORT;
        }
        if (commonsBoard.isCastlingBlackLongAllowed()) {
            castlingRights |= CASTLE_BLACK_LONG;
        }

        // --- En passant ---
        Square epSquare = commonsBoard.getEnPassantField();
        if (epSquare != null) {
            int file = epSquare.getBoardIdx() % 10 - 1;
            int rank = epSquare.getBoardIdx() / 10 - 2;
            enPassantSquare = BaseBitboards.getSquareIndex(file, rank);
        }

        // --- Clocks ---
        halfmoveClock = game.getFiftyMoveDrawRuleCount();
        fullmoveNumber = game.getCurrentMoveNumber();
        if (fullmoveNumber == 0) {
            fullmoveNumber = 1;
        }

        // --- Derived state ---
        recomputeOccupancy();
        hash = ZobristUtils.createHashForBoard(this);

    }


    /**
     * Recompute occByColor[] and occupancy from the 12 piece bitboards.
     */
    public void recomputeOccupancy() {
        long w = 0;
        long bk = 0;
        for (int i = 0; i < 6; i++) w |= pieces[i];      // white pieces
        for (int i = 6; i < 12; i++) bk |= pieces[i];      // black pieces
        occByColor[PieceUtils.WHITE] = w;
        occByColor[PieceUtils.BLACK] = bk;
        occupancy = w | bk;
    }


    /**
     * Check whether the given square is attacked by any piece of the given side.
     */
    public boolean isSquareAttacked(int square, int bySide) {
        long occ = occupancy;

        int pawn = bySide == PieceUtils.WHITE ? PieceUtils.WHITE_PAWN : PieceUtils.BLACK_PAWN;
        int knight = bySide == PieceUtils.WHITE ? PieceUtils.WHITE_KNIGHT : PieceUtils.BLACK_KNIGHT;
        int king = bySide == PieceUtils.WHITE ? PieceUtils.WHITE_KING : PieceUtils.BLACK_KING;
        int bishop = bySide == PieceUtils.WHITE ? PieceUtils.WHITE_BISHOP : PieceUtils.BLACK_BISHOP;
        int rook = bySide == PieceUtils.WHITE ? PieceUtils.WHITE_ROOK : PieceUtils.BLACK_ROOK;
        int queen = bySide == PieceUtils.WHITE ? PieceUtils.WHITE_QUEEN : PieceUtils.BLACK_QUEEN;
        int opponentSide = bySide == PieceUtils.WHITE ? PieceUtils.BLACK : PieceUtils.WHITE;

        if ((AttackBitboards.getPawnAttacks(opponentSide, square) & pieces[pawn]) != 0) return true;
        if ((AttackBitboards.getKnightAttacks(square) & pieces[knight]) != 0) return true;
        if ((AttackBitboards.getKingAttacks(square) & pieces[king]) != 0) return true;

        long bishopQueens = pieces[bishop] | pieces[queen];
        if ((MagicBitboards.getBishopAttacks(square, occ) & bishopQueens) != 0) return true;

        long rookQueens = pieces[rook] | pieces[queen];
        return (MagicBitboards.getRookAttacks(square, occ) & rookQueens) != 0;
    }

    /**
     * Find the square of the king for the given color.
     */
    public int kingSquare(int color) {
        return BaseBitboards.getLsb(pieces[PieceUtils.createColoredPiece(color, PieceUtils.KING)]);
    }

    /**
     * Check whether the given color's king is currently in check.
     */
    public boolean inCheck(int color) {
        return isSquareAttacked(kingSquare(color), color ^ 1);
    }

    /**
     * Square of the pawn captured via en-passant, given the capturing pawn's destination.
     */
    private int epCapturedSquare(int toSquare, int capturingSide) {
        return capturingSide == PieceUtils.WHITE ? toSquare - 8 : toSquare + 8;
    }

    /**
     * Returns [rookFrom, rookTo] for a castling move.
     */
    private int[] castlingRookSquares(int flag, int side) {
        boolean kingSide = flag == MoveUtils.FLAG_CASTLE_KING;
        boolean white = side == PieceUtils.WHITE;
        if (kingSide) {
            return white ? new int[]{7, 5} : new int[]{63, 61};
        } else {
            return white ? new int[]{0, 3} : new int[]{56, 59};
        }
    }

    /**
     * XOR the EP file into the hash if an EP square is set.
     */
    private long xorEpIfSet(long h) {
        if (enPassantSquare >= 0) {
            h ^= ZobristUtils.getEnPassant(BaseBitboards.getFileBySquareIndex(enPassantSquare));
        }
        return h;
    }

    /**
     * Occupancy toggle shared by make and unmake:
     */
    private void applyOccupancy(int flag, int capturedPiece, long fromBit, long moveBits) {
        boolean capNonEp = capturedPiece != -1 && flag != MoveUtils.FLAG_EN_PASSANT;
        occupancy ^= capNonEp ? fromBit : moveBits;
    }

    /**
     * Toggle a rook between its castling from/to squares. Used by both make and unmake
     */
    private long toggleCastlingRook(int flag, int side, boolean makeDirection, long h) {
        int[] rs = castlingRookSquares(flag, side);
        int rookFrom = rs[0];
        int rookTo = rs[1];
        int rook = PieceUtils.createColoredPiece(side, PieceUtils.ROOK);
        long rookBits = BaseBitboards.createSingleBitBoard(rookFrom) | BaseBitboards.createSingleBitBoard(rookTo);
        pieces[rook] ^= rookBits;
        occByColor[side] ^= rookBits;
        occupancy ^= rookBits;
        if (makeDirection) {
            mailbox[rookFrom] = -1;
            mailbox[rookTo] = rook;
        } else {
            mailbox[rookFrom] = rook;
            mailbox[rookTo] = -1;
        }
        h ^= ZobristUtils.getPieceSquare(rook, rookFrom);
        h ^= ZobristUtils.getPieceSquare(rook, rookTo);
        return h;
    }

    /**
     * Remove the captured piece (if any) from boards + mailbox + hash.
     */
    private long removeCaptured(int flag, int capturedPiece, int to, int us, int them, long toBit, long h) {
        if (flag == MoveUtils.FLAG_EN_PASSANT) {
            int capSq = epCapturedSquare(to, us);
            long capBit = BaseBitboards.createSingleBitBoard(capSq);
            pieces[capturedPiece] ^= capBit;
            occByColor[them] ^= capBit;
            occupancy ^= capBit;
            mailbox[capSq] = -1;
            return h ^ ZobristUtils.getPieceSquare(capturedPiece, capSq);
        }
        if (capturedPiece != -1) {
            pieces[capturedPiece] ^= toBit;
            occByColor[them] ^= toBit;
            // occupancy is handled by applyOccupancy()
            return h ^ ZobristUtils.getPieceSquare(capturedPiece, to);
        }
        return h;
    }

    /**
     * Restore the captured piece (if any) onto boards + mailbox + hash.
     */
    private long restoreCaptured(int flag, int capturedPiece, int to, int us, int them, long toBit, long h) {
        if (flag == MoveUtils.FLAG_EN_PASSANT) {
            int capSq = epCapturedSquare(to, us);
            long capBit = BaseBitboards.createSingleBitBoard(capSq);
            pieces[capturedPiece] |= capBit;
            occByColor[them] |= capBit;
            occupancy |= capBit;
            mailbox[capSq] = capturedPiece;
            return h ^ ZobristUtils.getPieceSquare(capturedPiece, capSq);
        }

        if (capturedPiece != -1) {
            pieces[capturedPiece] |= toBit;
            occByColor[them] |= toBit;
            mailbox[to] = capturedPiece;
            return h ^ ZobristUtils.getPieceSquare(capturedPiece, to);
        }

        return h;
    }

    /**
     * Replace the just-moved pawn on 'to' with the promoted piece.
     */
    private long applyPromotion(int move, int pawnPiece, int us, int to, long toBit, long h) {
        int promoPiece = PieceUtils.createColoredPiece(us, MoveUtils.getPromotionType(move));
        pieces[pawnPiece] ^= toBit;
        pieces[promoPiece] |= toBit;
        mailbox[to] = promoPiece;
        h ^= ZobristUtils.getPieceSquare(pawnPiece, to);
        h ^= ZobristUtils.getPieceSquare(promoPiece, to);
        return h;
    }

    /**
     * Update castling rights after a move (king move strips both sides, from/to masks handle rook moves & captures).
     */
    private void updateCastlingRights(int pieceType, int us, int from, int to) {
        if (pieceType == PieceUtils.KING) {
            int loseMask = us == PieceUtils.WHITE ? ~(CASTLE_WHITE_SHORT | CASTLE_WHITE_LONG) : ~(CASTLE_BLACK_SHORT | CASTLE_BLACK_LONG);
            castlingRights &= loseMask;
        }

        castlingRights &= CASTLING_MASK[from];
        castlingRights &= CASTLING_MASK[to];
    }

    // ============================================================
    // Main API
    // ============================================================

    /**
     * Apply a pseudo-legal move to the board and return a packed undo long.
     */
    public long makeMove(int move) {
        final int from = MoveUtils.getFromSquare(move);
        final int to = MoveUtils.getToSquare(move);
        final int flag = MoveUtils.getFlag(move);
        final int piece = mailbox[from];
        final int pieceType = PieceUtils.getTypeOfPiece(piece);
        final int us = sideToMove;
        final int them = us ^ 1;

        // Captured piece (en-passant captures from a square behind 'to')
        final int capturedPiece = (flag == MoveUtils.FLAG_EN_PASSANT) ? mailbox[epCapturedSquare(to, us)] : mailbox[to];

        // Pack undo BEFORE modifying state
        long undo = ((capturedPiece + 1) & U_CAP_MASK) << U_CAP_SHIFT | (castlingRights & U_CR_MASK) << U_CR_SHIFT | ((enPassantSquare + 1) & U_EP_MASK) << U_EP_SHIFT | (halfmoveClock & U_HM_MASK) << U_HM_SHIFT;

        // Hash: remove old castling/EP, flip side
        long h = hash;
        h ^= ZobristUtils.getCastling(castlingRights);
        h = xorEpIfSet(h);
        h ^= ZobristUtils.SIDE_TO_MOVE;

        // Reset state
        enPassantSquare = -1;
        halfmoveClock = (pieceType == PieceUtils.PAWN || capturedPiece != -1) ? 0 : halfmoveClock + 1;

        final long fromBit = BaseBitboards.createSingleBitBoard(from);
        final long toBit = BaseBitboards.createSingleBitBoard(to);

        // Remove captured piece (if any)
        h = removeCaptured(flag, capturedPiece, to, us, them, toBit, h);

        // Move the piece
        long moveBits = fromBit | toBit;
        pieces[piece] ^= moveBits;
        occByColor[us] ^= moveBits;
        applyOccupancy(flag, capturedPiece, fromBit, moveBits);
        h ^= ZobristUtils.getPieceSquare(piece, from);
        h ^= ZobristUtils.getPieceSquare(piece, to);
        mailbox[from] = -1;
        mailbox[to] = piece;

        // Special-move handling
        if (flag == MoveUtils.FLAG_PROMOTION) {
            h = applyPromotion(move, piece, us, to, toBit, h);
        } else if (flag == MoveUtils.FLAG_DOUBLE_PUSH) {
            enPassantSquare = us == PieceUtils.WHITE ? from + 8 : from - 8;
        } else if (flag == MoveUtils.FLAG_CASTLE_KING || flag == MoveUtils.FLAG_CASTLE_QUEEN) {
            h = toggleCastlingRook(flag, us, true, h);
        }

        // Castling rights
        updateCastlingRights(pieceType, us, from, to);

        // Finalize hash
        h ^= ZobristUtils.getCastling(castlingRights);
        h = xorEpIfSet(h);
        hash = h;

        // Switch side
        if (us == PieceUtils.BLACK) fullmoveNumber++;
        sideToMove = them;

        return undo;
    }

    /**
     * Revert a move, restoring the board to its state before makeMove().
     */
    public void unmakeMove(int move, long undo) {
        final int from = MoveUtils.getFromSquare(move);
        final int to = MoveUtils.getToSquare(move);
        final int flag = MoveUtils.getFlag(move);
        final int them = sideToMove;
        final int us = them ^ 1;

        // Restore side + fullmove
        sideToMove = us;
        if (us == PieceUtils.BLACK) fullmoveNumber--;

        // Unpack undo
        final int capturedPiece = (int) ((undo >>> U_CAP_SHIFT) & U_CAP_MASK) - 1;
        final int prevCastling = (int) ((undo >>> U_CR_SHIFT) & U_CR_MASK);
        final int prevEpSquare = (int) ((undo >>> U_EP_SHIFT) & U_EP_MASK) - 1;
        halfmoveClock = (int) ((undo >>> U_HM_SHIFT) & U_HM_MASK);

        // Hash reversal: remove current EP/castling, flip side
        long h = hash;
        h = xorEpIfSet(h);
        h ^= ZobristUtils.getCastling(castlingRights);
        h ^= ZobristUtils.SIDE_TO_MOVE;

        castlingRights = prevCastling;
        enPassantSquare = prevEpSquare;

        int movedPiece = mailbox[to];
        final long fromBit = BaseBitboards.createSingleBitBoard(from);
        final long toBit = BaseBitboards.createSingleBitBoard(to);

        // Undo promotion first (piece on 'to' is the promoted piece → revert to pawn)
        if (flag == MoveUtils.FLAG_PROMOTION) {
            int pawn = PieceUtils.createColoredPiece(us, PieceUtils.PAWN);
            pieces[movedPiece] ^= toBit;
            pieces[pawn] |= toBit;
            h ^= ZobristUtils.getPieceSquare(movedPiece, to);
            h ^= ZobristUtils.getPieceSquare(pawn, to);
            movedPiece = pawn;
        }

        // Slide piece back from 'to' to 'from'
        long moveBits = fromBit | toBit;
        pieces[movedPiece] ^= moveBits;
        occByColor[us] ^= moveBits;
        applyOccupancy(flag, capturedPiece, fromBit, moveBits);
        h ^= ZobristUtils.getPieceSquare(movedPiece, to);
        h ^= ZobristUtils.getPieceSquare(movedPiece, from);
        mailbox[from] = movedPiece;
        mailbox[to] = -1;

        // Restore captured piece (if any)
        h = restoreCaptured(flag, capturedPiece, to, us, them, toBit, h);

        // Undo castling rook movement
        if (flag == MoveUtils.FLAG_CASTLE_KING || flag == MoveUtils.FLAG_CASTLE_QUEEN) {
            h = toggleCastlingRook(flag, us, false, h);
        }

        // Finalize hash
        h ^= ZobristUtils.getCastling(castlingRights);
        h = xorEpIfSet(h);
        hash = h;
    }

    /**
     * Create a deep copy of this board. The copy shares no mutable state with
     */
    public Board copy() {
        Board result = new Board();

        System.arraycopy(this.pieces, 0, result.pieces, 0, 12);
        System.arraycopy(this.occByColor, 0, result.occByColor, 0, 2);
        System.arraycopy(this.mailbox, 0, result.mailbox, 0, 64);

        result.occupancy = this.occupancy;
        result.sideToMove = this.sideToMove;
        result.castlingRights = this.castlingRights;
        result.enPassantSquare = this.enPassantSquare;
        result.halfmoveClock = this.halfmoveClock;
        result.fullmoveNumber = this.fullmoveNumber;
        result.hash = this.hash;

        return result;
    }
}
