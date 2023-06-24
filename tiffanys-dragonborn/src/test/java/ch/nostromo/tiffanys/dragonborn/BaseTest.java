package ch.nostromo.tiffanys.dragonborn;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.format.FenFormat;
import ch.nostromo.tiffanys.dragonborn.engine.bitboards.BaseBitboards;
import ch.nostromo.tiffanys.dragonborn.engine.board.Board;
import ch.nostromo.tiffanys.dragonborn.engine.board.PieceUtils;
import ch.nostromo.tiffanys.dragonborn.engine.move.MoveGenerator;
import ch.nostromo.tiffanys.dragonborn.engine.move.MoveList;
import ch.nostromo.tiffanys.dragonborn.engine.move.MoveUtils;

import static org.junit.jupiter.api.Assertions.*;

public abstract class BaseTest {

    protected static void assertBitSet(long bb, int sq) {
        assertTrue(isSet(bb, sq));
    }

    protected static void assertBitNotSet(long bb, int sq) {
        assertFalse(isSet(bb, sq));
    }

    protected static void assertBitCount(long bb, int expected) {
        assertEquals(expected, Long.bitCount(bb), "Unexpected bit count");
    }
    protected static boolean isSet(long bb, int sq) {
        return (bb & (1L << sq)) != 0;
    }


    protected static MoveList generateLegalMoves(String fen) {
        Board b = new Board(new ChessGame(new FenFormat(fen)));
        MoveList ml = new MoveList();
        MoveGenerator.generateLegalMoves(b, ml);
        return ml;
    }

    /**
     * Assert if boards are equal
     */
    protected static void assertBoardsEqual(Board a, Board b) {
        assertArrayEquals(a.getPieces(), b.getPieces(), "pieces differ");
        assertArrayEquals(a.getOccByColor(), b.getOccByColor(), "occByColor differ");
        assertArrayEquals(a.getMailbox(), b.getMailbox(), "mailbox differs");

        assertEquals(a.getOccupancy(), b.getOccupancy());
        assertEquals(a.getSideToMove(), b.getSideToMove());
        assertEquals(a.getCastlingRights(), b.getCastlingRights());
        assertEquals(a.getEnPassantSquare(), b.getEnPassantSquare());
        assertEquals(a.getHalfmoveClock(), b.getHalfmoveClock());
        assertEquals(a.getFullmoveNumber(), b.getFullmoveNumber());
        assertEquals(a.getHash(), b.getHash());
    }


    // ---------- PERFT TESTS ---------------
    // https://www.chessprogramming.org/Perft_Results


    private static final int MAX_DEPTH = 32;

    private static final ThreadLocal<MoveList[]> POOL = ThreadLocal.withInitial(() -> {
        MoveList[] pool = new MoveList[MAX_DEPTH];
        for (int i = 0; i < MAX_DEPTH; i++) {
            pool[i] = new MoveList(256);
        }
        return pool;
    });


    public static void assertPerft(String fen, boolean validateBaord, long...expectedMoveCount) {
        Board board = new Board(new ChessGame(new FenFormat(fen)));

        for (int i = 1; i <= expectedMoveCount.length; i++) {
            assertEquals(expectedMoveCount[i-1], perft(board, i, validateBaord));
        }


    }


    /**
     * Count the number of leaf positions reachable from the given position
     * at the given depth.
     */
    public static long perft(Board board, int depth, boolean validateBaord) {
        if (depth == 0) return 1L;
        return perftInternal(board, depth, POOL.get(), validateBaord);
    }

    /**
     * Recursive Tests
     */
    private static long perftInternal(Board board, int depth, MoveList[] pool, boolean validateBaord) {
        MoveList moves = pool[depth];



        moves.clear();
        MoveGenerator.generateLegalMoves(board, moves);

        if (depth == 1) return moves.getSize();

        // Recurse for each legal move
        long nodes = 0;
        for (int i = 0; i < moves.getSize(); i++) {
            int move = moves.getMoves()[i];

            // Create copy for later unmake tests
            Board validateCopy = null;
            if (validateBaord) {
                validateCopy = board.copy();
            }

            long undo = board.makeMove(move);
            nodes += perftInternal(board, depth - 1, pool, validateBaord);

            board.unmakeMove(move, undo);

            // Validate boards after unmake to earlier copy
            if (validateBaord) {
                assertBoardsEqual(board, validateCopy);
            }

        }
        return nodes;
    }


    /**
     * Convert a move to UCI "long algebraic" notation (helper only)
     */
    public static String toUci(int move) {
        StringBuilder sb = new StringBuilder(5);
        sb.append(BaseBitboards.getTiffanysSquareBySquareIndex(MoveUtils.getFromSquare(move)));
        sb.append(BaseBitboards.getTiffanysSquareBySquareIndex(MoveUtils.getToSquare(move)));
        if (MoveUtils.isPromotion(move)) {
            switch (MoveUtils.getPromotionType(move)) {
                case PieceUtils.KNIGHT: sb.append('n'); break;
                case PieceUtils.BISHOP: sb.append('b'); break;
                case PieceUtils.ROOK:   sb.append('r'); break;
                case PieceUtils.QUEEN:  sb.append('q'); break;
                default:
                    throw new IllegalStateException("Unexpected promotion type: " + MoveUtils.getPromotionType(move));
            }
        }
        return sb.toString().toLowerCase();
    }


    public static int findMoveInMoveList(MoveList moveList, String uci) {
        for (int i = 0; i < moveList.size(); i++) {
            if (toUci(moveList.get(i)).equals(uci)) {
                return moveList.get(i);
            }
        }
        throw new AssertionError("move not found: " + uci);
    }

}
