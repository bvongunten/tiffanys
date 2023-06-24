package ch.nostromo.tiffanys.dragonborn.engine.move;

import ch.nostromo.tiffanys.dragonborn.engine.bitboards.AttackBitboards;
import ch.nostromo.tiffanys.dragonborn.engine.board.PieceUtils;
import ch.nostromo.tiffanys.dragonborn.engine.board.Board;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static ch.nostromo.tiffanys.dragonborn.engine.bitboards.BaseBitboards.*;

/**
 * Move Generator
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MoveGenerator {

    /**
     * Thread-local scratch list for pseudo-legal moves
     */
    private static final ThreadLocal<MoveList> SCRATCH = ThreadLocal.withInitial(() -> new MoveList(256));


    /**
     * Generate all pseudo-legal moves for the side to move.
     */
    public static void generatePseudoLegal(Board board, MoveList movelist) {
        generatePseudoLegalInternal(board, movelist, false);
    }

    /**
     * Generate all pseudo-legal capture moves for the side to move.
     */
    public static void generatePseudoLegalCaptures(Board board, MoveList movelist) {
        generatePseudoLegalInternal(board, movelist, true);
    }

    /**
     * Internal: Generate pseudo-legal moves with optional capture-only filter.
     * @param capturesOnly if true, only generate capture moves (skip quiet moves)
     */
    private static void generatePseudoLegalInternal(Board board, MoveList movelist, boolean capturesOnly) {

        int us = board.getSideToMove();
        long own = board.getOccByColor()[us];
        long enemy = board.getOccByColor()[us ^ 1];
        long occ = board.getOccupancy();
        long empty = ~occ;
        long targets = ~own;

        generatePawnMoves(board, us, enemy, empty, movelist, capturesOnly);

        if (!capturesOnly) {
            generateKnightMoves(board, us, enemy, targets, movelist, false);
            generateBishopMoves(board, us, occ, enemy, targets, movelist, false);
            generateRookMoves(board, us, occ, enemy, targets, movelist, false);
            generateQueenMoves(board, us, occ, enemy, targets, movelist, false);
            generateKingMoves(board, us, enemy, targets, movelist, false);
            generateCastling(board, us, movelist);
        } else {
            // Capture-only: only piece moves (no castling needed)
            generateKnightMoves(board, us, enemy, targets, movelist, true);
            generateBishopMoves(board, us, occ, enemy, targets, movelist, true);
            generateRookMoves(board, us, occ, enemy, targets, movelist, true);
            generateQueenMoves(board, us, occ, enemy, targets, movelist, true);
            generateKingMoves(board, us, enemy, targets, movelist, true);
        }
    }


    /**
     * Generate all fully legal moves for the side to move.
     */
    public static void generateLegalMoves(Board board, MoveList moveList) {
        generateLegalInternal(board, moveList, false);
    }

    /**
     * Generate all fully legal capture moves for the side to move.
     */
    public static void generateLegalCaptureMoves(Board board, MoveList moveList) {
        generateLegalInternal(board, moveList, true);
    }

    private static void generateLegalInternal(Board board, MoveList moveList, boolean capturesOnly) {

        // Use the thread-local scratch buffer for the pseudo-legal list
        MoveList pseudo = SCRATCH.get();

        pseudo.clear();

        if (capturesOnly) {
            generatePseudoLegalCaptures(board, pseudo);
        } else {
            generatePseudoLegal(board, pseudo);
        }

        int us = board.getSideToMove();
        int them = us ^ 1;

        for (int i = 0; i < pseudo.size; i++) {
            int move = pseudo.moves[i];

            // Make the move, check if our king is safe, unmake
            long undo = board.makeMove(move);
            boolean legal = !board.isSquareAttacked(board.kingSquare(us), them);
            board.unmakeMove(move, undo);

            if (legal) {
                moveList.add(move);
            }
        }
    }

    /**
     * Generate all pawn moves: single push, double push, captures, en passant,
     * and promotions (which can occur on both pushes and captures).
     */
    private static void generatePawnMoves(Board board, int us, long enemy, long empty, MoveList movelist, boolean capturesOnly) {
        long pawns = board.getPieces()[PieceUtils.createColoredPiece(us, PieceUtils.PAWN)];
        boolean white = us == PieceUtils.WHITE;
        int up = white ? 8 : -8;
        long promoRank = white ? RANK_8 : RANK_1;
        long startRank = white ? RANK_2 : RANK_7;

        if (!capturesOnly) {
            generatePawnPushes(white, pawns, empty, up, promoRank, movelist);
            generatePawnDoublePushes(white, pawns, empty, up, startRank, movelist);
        }

        generatePawnCaptures(board, white, pawns, enemy, promoRank, movelist);
        generateEnPassant(board, pawns, white, movelist);
    }

    /**
     * Generate single and promotion pushes.
     */
    private static void generatePawnPushes(boolean white, long pawns, long empty, int up, long promoRank, MoveList movelist) {
        long singlePushes = (white ? pawns << 8 : pawns >>> 8) & empty;
        long pushPromos = singlePushes & promoRank;
        long pushNonPromo = singlePushes & ~promoRank;

        long nonPromoMoves = pushNonPromo;
        while (nonPromoMoves != 0) {
            int to = getLsb(nonPromoMoves);
            nonPromoMoves = popLsb(nonPromoMoves);
            movelist.add(MoveUtils.makeMove(to - up, to, MoveUtils.FLAG_NORMAL, 0));
        }

        long promoMoves = pushPromos;
        while (promoMoves != 0) {
            int to = getLsb(promoMoves);
            promoMoves = popLsb(promoMoves);
            emitPromotions(to - up, to, -1, movelist);
        }
    }

    /**
     * Generate double pushes from starting rank.
     */
    private static void generatePawnDoublePushes(boolean white, long pawns, long empty, int up, long startRank, MoveList movelist) {
        long pawnsOnStart = pawns & startRank;
        long onePush = (white ? pawnsOnStart << 8 : pawnsOnStart >>> 8) & empty;
        long twoPush = (white ? onePush << 8 : onePush >>> 8) & empty;

        long doublePushMoves = twoPush;
        while (doublePushMoves != 0) {
            int to = getLsb(doublePushMoves);
            doublePushMoves = popLsb(doublePushMoves);
            movelist.add(MoveUtils.makeMove(to - 2 * up, to, MoveUtils.FLAG_DOUBLE_PUSH, 0));
        }
    }

    /**
     * Generate diagonal captures and capture promotions.
     */
    private static void generatePawnCaptures(Board board, boolean white, long pawns, long enemy, long promoRank, MoveList movelist) {
        long leftCaptures;
        long rightCaptures;
        if (white) {
            leftCaptures = (pawns << 7) & NOT_FILE_H & enemy;
            rightCaptures = (pawns << 9) & NOT_FILE_A & enemy;
        } else {
            leftCaptures = (pawns >>> 9) & NOT_FILE_H & enemy;
            rightCaptures = (pawns >>> 7) & NOT_FILE_A & enemy;
        }
        int leftShift = white ? 7 : -9;
        int rightShift = white ? 9 : -7;

        emitPawnCaptures(board, leftCaptures, leftShift, promoRank, movelist);
        emitPawnCaptures(board, rightCaptures, rightShift, promoRank, movelist);
    }

    private static void generateEnPassant(Board board, long pawns, boolean white, MoveList movelist) {
        int epSquare = board.getEnPassantSquare();
        if (epSquare < 0) {
            return;
        }

        long epBB = createSingleBitBoard(epSquare);
        long epAttackers = white
                ? ((epBB >>> 7) & NOT_FILE_A) | ((epBB >>> 9) & NOT_FILE_H)
                : ((epBB << 9) & NOT_FILE_A) | ((epBB << 7) & NOT_FILE_H);
        epAttackers &= pawns;

        while (epAttackers != 0) {
            int from = getLsb(epAttackers);
            epAttackers = popLsb(epAttackers);
            movelist.add(MoveUtils.makeCaptureMove(from, epSquare,
                    MoveUtils.FLAG_EN_PASSANT, 0, PieceUtils.PAWN));
        }
    }

    /**
     * Emit pawn capture moves, splitting into promotions and non-promotions.
     * Each capture reads the captured piece type from the mailbox
     */
    private static void emitPawnCaptures(Board board, long attacks, int shift, long promoRank, MoveList movelist) {
        long nonPromos = attacks & ~promoRank;
        long promos = attacks & promoRank;

        // Non-promotion captures
        long bb = nonPromos;
        while (bb != 0) {
            int to = getLsb(bb);
            bb = popLsb(bb);
            int capturedType = PieceUtils.getTypeOfPiece(board.getMailbox()[to]);
            movelist.add(MoveUtils.makeCaptureMove(to - shift, to, MoveUtils.FLAG_NORMAL, 0, capturedType));
        }
        // Promotion captures (4 moves each)
        bb = promos;
        while (bb != 0) {
            int to = getLsb(bb);
            bb = popLsb(bb);
            int capturedType = PieceUtils.getTypeOfPiece(board.getMailbox()[to]);
            emitPromotions(to - shift, to, capturedType, movelist);
        }
    }

    /**
     * Emit all 4 promotion moves (queen, rook, bishop, knight).
     */
    private static void emitPromotions(int from, int to, int capturedType, MoveList moveList) {
        if (capturedType < 0) {
            // Non-capture promotion (push to last rank)
            moveList.add(MoveUtils.makeMove(from, to, MoveUtils.FLAG_PROMOTION, PieceUtils.QUEEN));
            moveList.add(MoveUtils.makeMove(from, to, MoveUtils.FLAG_PROMOTION, PieceUtils.ROOK));
            moveList.add(MoveUtils.makeMove(from, to, MoveUtils.FLAG_PROMOTION, PieceUtils.BISHOP));
            moveList.add(MoveUtils.makeMove(from, to, MoveUtils.FLAG_PROMOTION, PieceUtils.KNIGHT));
        } else {
            // Capture-promotion
            moveList.add(MoveUtils.makeCaptureMove(from, to, MoveUtils.FLAG_PROMOTION, PieceUtils.QUEEN, capturedType));
            moveList.add(MoveUtils.makeCaptureMove(from, to, MoveUtils.FLAG_PROMOTION, PieceUtils.ROOK, capturedType));
            moveList.add(MoveUtils.makeCaptureMove(from, to, MoveUtils.FLAG_PROMOTION, PieceUtils.BISHOP, capturedType));
            moveList.add(MoveUtils.makeCaptureMove(from, to, MoveUtils.FLAG_PROMOTION, PieceUtils.KNIGHT, capturedType));
        }
    }

    private static void generateKnightMoves(Board board, int us, long enemy, long targets, MoveList moveList, boolean capturesOnly) {
        long knights = board.getPieces()[PieceUtils.createColoredPiece(us, PieceUtils.KNIGHT)];
        while (knights != 0) {
            int from = getLsb(knights);
            knights = popLsb(knights);
            long attacks = AttackBitboards.getKnightAttacks(from) & targets;
            if (capturesOnly) {
                emitCapturesOnly(board, from, attacks & enemy, moveList);
            } else {
                emitMovesWithCaptures(board, from, attacks, enemy, moveList);
            }
        }
    }

    private static void generateBishopMoves(Board board, int us, long occ, long enemy, long targets, MoveList moveList, boolean capturesOnly) {
        long bishops = board.getPieces()[PieceUtils.createColoredPiece(us, PieceUtils.BISHOP)];
        while (bishops != 0) {
            int from = getLsb(bishops);
            bishops = popLsb(bishops);
            long attacks = AttackBitboards.getBishopAttacks(from, occ) & targets;
            if (capturesOnly) {
                emitCapturesOnly(board, from, attacks & enemy, moveList);
            } else {
                emitMovesWithCaptures(board, from, attacks, enemy, moveList);
            }
        }
    }

    private static void generateRookMoves(Board board, int us, long occ, long enemy, long targets, MoveList moveList, boolean capturesOnly) {
        long rooks = board.getPieces()[PieceUtils.createColoredPiece(us, PieceUtils.ROOK)];
        while (rooks != 0) {
            int from = getLsb(rooks);
            rooks = popLsb(rooks);
            long attacks = AttackBitboards.getRookAttacks(from, occ) & targets;
            if (capturesOnly) {
                emitCapturesOnly(board, from, attacks & enemy, moveList);
            } else {
                emitMovesWithCaptures(board, from, attacks, enemy, moveList);
            }
        }
    }

    private static void generateQueenMoves(Board board, int us, long occ, long enemy, long targets, MoveList moveList, boolean capturesOnly) {
        long queens = board.getPieces()[PieceUtils.createColoredPiece(us, PieceUtils.QUEEN)];
        while (queens != 0) {
            int from = getLsb(queens);
            queens = popLsb(queens);
            long attacks = AttackBitboards.getQueenAttacks(from, occ) & targets;
            if (capturesOnly) {
                emitCapturesOnly(board, from, attacks & enemy, moveList);
            } else {
                emitMovesWithCaptures(board, from, attacks, enemy, moveList);
            }
        }
    }

    private static void generateKingMoves(Board board, int us, long enemy, long targets, MoveList movelist, boolean capturesOnly) {
        long king = board.getPieces()[PieceUtils.createColoredPiece(us, PieceUtils.KING)];

        int from = getLsb(king);
        long attacks = AttackBitboards.getKingAttacks(from) & targets;
        if (capturesOnly) {
            emitCapturesOnly(board, from, attacks & enemy, movelist);
        } else {
            emitMovesWithCaptures(board, from, attacks, enemy, movelist);
        }
    }

    /**
     * Generate castling moves.
     * 1. The king has the appropriate castling right
     * 2. The squares between king and rook are empty
     * 3. The king is not currently in check
     * 4. The king does not pass through or land on an attacked square
     */
    private static void generateCastling(Board board, int us, MoveList moveList) {
        int them = us ^ 1;
        int kingSq = board.kingSquare(us);

        // Can't castle while in check
        if (board.isSquareAttacked(kingSq, them)) {
            return;
        }

        if (us == PieceUtils.WHITE) {
            // King-side: king e1→g1, rook h1→f1. Squares f1,g1 must be empty and unattacked.
            if ((board.getCastlingRights() & Board.CASTLE_WHITE_SHORT) != 0
                    && ((board.getOccupancy() >>> 5) & 0x3L) == 0       // f1, g1 empty
                    && !board.isSquareAttacked(5, them)             // f1 not attacked
                    && !board.isSquareAttacked(6, them)) {          // g1 not attacked
                moveList.add(MoveUtils.makeMove(4, 6, MoveUtils.FLAG_CASTLE_KING, 0));
            }
            // Queen-side: king e1→c1, rook a1→d1. Squares b1,c1,d1 must be empty; c1,d1 unattacked.
            if ((board.getCastlingRights() & Board.CASTLE_WHITE_LONG) != 0
                    && ((board.getOccupancy() >>> 1) & 0x7L) == 0       // b1, c1, d1 empty
                    && !board.isSquareAttacked(3, them)             // d1 not attacked
                    && !board.isSquareAttacked(2, them)) {          // c1 not attacked
                moveList.add(MoveUtils.makeMove(4, 2, MoveUtils.FLAG_CASTLE_QUEEN, 0));
            }
        } else {
            // King-side: king e8→g8
            if ((board.getCastlingRights() & Board.CASTLE_BLACK_SHORT) != 0
                    && ((board.getOccupancy() >>> 61) & 0x3L) == 0
                    && !board.isSquareAttacked(61, them)
                    && !board.isSquareAttacked(62, them)) {
                moveList.add(MoveUtils.makeMove(60, 62, MoveUtils.FLAG_CASTLE_KING, 0));
            }
            // Queen-side: king e8→c8
            if ((board.getCastlingRights() & Board.CASTLE_BLACK_LONG) != 0
                    && ((board.getOccupancy() >>> 57) & 0x7L) == 0
                    && !board.isSquareAttacked(59, them)
                    && !board.isSquareAttacked(58, them)) {
                moveList.add(MoveUtils.makeMove(60, 58, MoveUtils.FLAG_CASTLE_QUEEN, 0));
            }
        }
    }

    /**
     * Emit all moves from a single source square to a set of target squares.
     * Includes both quiet and capture moves.
     */
    private static void emitMovesWithCaptures(Board board, int from, long targets, long enemy, MoveList movelist) {
        // Quiet moves (to empty squares)
        long quiets = targets & ~enemy;
        while (quiets != 0) {
            int to = getLsb(quiets);
            quiets = popLsb(quiets);
            movelist.add(MoveUtils.makeMove(from, to));
        }

        // Captures (to squares occupied by enemy pieces)
        long captures = targets & enemy;
        while (captures != 0) {
            int to = getLsb(captures);
            captures = popLsb(captures);
            int capturedType = PieceUtils.getTypeOfPiece(board.getMailbox()[to]);
            movelist.add(MoveUtils.makeCaptureMove(from, to, MoveUtils.FLAG_NORMAL, 0, capturedType));
        }
    }

    /**
     * Emit only capture moves from a source square to enemy-occupied targets.
     */
    private static void emitCapturesOnly(Board board, int from, long captures, MoveList movelist) {
        while (captures != 0) {
            int to = getLsb(captures);
            captures = popLsb(captures);
            int capturedType = PieceUtils.getTypeOfPiece(board.getMailbox()[to]);
            movelist.add(MoveUtils.makeCaptureMove(from, to, MoveUtils.FLAG_NORMAL, 0, capturedType));
        }
    }

    /**
     * Unload SCRATCH
     */
    public static void unload() {
        SCRATCH.remove();
    }

}
