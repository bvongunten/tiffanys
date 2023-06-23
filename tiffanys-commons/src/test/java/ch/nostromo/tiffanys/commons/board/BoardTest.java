package ch.nostromo.tiffanys.commons.board;

import ch.nostromo.tiffanys.commons.TestHelper;
import ch.nostromo.tiffanys.commons.enums.Castling;
import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.enums.Piece;
import ch.nostromo.tiffanys.commons.fen.FenFormat;
import ch.nostromo.tiffanys.commons.move.Move;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest extends TestHelper {

    private static final FenFormat INITIAL_FEN = new FenFormat("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");

    @Test
    public void testFenInitial() {
        checkFen(INITIAL_FEN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFenIllegal() {
        checkFen(new FenFormat("abcdefgh/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1"));
    }

    @Test
    public void testFenEnPassant() {
        checkFen(new FenFormat("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1"));
    }

    @Test
    public void testFenCastling() {
        checkFen(new FenFormat("4k3/8/8/8/8/8/4P3/4K3 w - - 5 39"));
    }

    @Test
    public void testCastlingLongInitialBoadWhite() {
        Board board = new Board(INITIAL_FEN);
        assertTrue(board.isCastlingAllowed(Castling.WHITE_LONG));
    }

    @Test
    public void testCastlingShortInitialBoadWhite() {
        Board board = new Board(INITIAL_FEN);
        assertTrue(board.isCastlingAllowed(Castling.WHITE_SHORT));
    }

    @Test
    public void testCastlingLongInitialBoardBlack() {
        Board board = new Board(INITIAL_FEN);
        assertTrue(board.isCastlingAllowed(Castling.BLACK_LONG));
    }

    @Test
    public void testCastlingShortInitialBoadBlack() {
        Board board = new Board(INITIAL_FEN);
        assertTrue(board.isCastlingAllowed(Castling.BLACK_SHORT));
    }

    @Test
    public void testClone() throws Exception {
        Board board = new Board(INITIAL_FEN);
        board.clone();
    }

    @Test
    public void testIsPieceOfColor() {
        Board board = new Board(INITIAL_FEN);

        assertEquals(board.getPieceColor(21), GameColor.WHITE);
        assertEquals(board.getPieceColor(98), GameColor.BLACK);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsPieceOfColorFail() {
        Board board = new Board(INITIAL_FEN);
        board.getPieceColor(54);
    }

    private void checkFen(FenFormat originalFen) {
        Board board = new Board(originalFen);

        FenFormat fenByBoard = board.getFenFormat();

        assertEquals("FenPos", originalFen.getPosition(), fenByBoard.getPosition());
        assertEquals("FenCastling", originalFen.getCastling(), fenByBoard.getCastling());
        assertEquals("FenEnPassant", originalFen.getEnPassant(), fenByBoard.getEnPassant());
    }

    @Test
    public void testApplyNormalMove() {
        FenFormat fen = new FenFormat("8/7k/8/8/3Q4/8/K7/8 w KQkq b6 0 1");
        Board board = new Board(fen);

        // move queen 1 field
        board.applyMove(new Move("D4", "D3"), GameColor.WHITE);

        assertEquals(board.getFields()[54].getPiece(), null);
        assertEquals(board.getFields()[54].getPieceColor(), null);

        assertEquals(board.getFields()[44].getPiece(), Piece.QUEEN);
        assertEquals(board.getFields()[44].getPieceColor(), GameColor.WHITE);

        assertTrue(board.castlingBlackLongAllowed);
        assertTrue(board.castlingBlackShortAllowed);
        assertTrue(board.castlingWhiteLongAllowed);
        assertTrue(board.castlingWhiteShortAllowed);
        assertEquals(board.enPassantField, Integer.MIN_VALUE);
    }

    @Test
    public void testApplyPawnOpening() {
        Board board = new Board(INITIAL_FEN);

        // move queen 1 field
        board.applyMove(new Move("E2", "E4"), GameColor.WHITE);

        assertEquals(board.getFields()[35].getPiece(), null);
        assertEquals(board.getFields()[35].getPieceColor(), null);

        assertEquals(board.getFields()[55].getPiece(), Piece.PAWN);
        assertEquals(board.getFields()[55].getPieceColor(), GameColor.WHITE);

        assertTrue(board.castlingBlackLongAllowed);
        assertTrue(board.castlingBlackShortAllowed);
        assertTrue(board.castlingWhiteLongAllowed);
        assertTrue(board.castlingWhiteShortAllowed);
        assertEquals(board.enPassantField, 45);
    }

    @Test
    public void testApplyEnPassantBlack() {
        FenFormat fen = new FenFormat("3k4/8/8/8/1pP5/8/8/3K4 b - c3 0 1");
        Board board = new Board(fen);

        board.applyMove(new Move("B4", "C3"), GameColor.BLACK);

        assertEquals(board.getFields()[52].getPiece(), null);
        assertEquals(board.getFields()[52].getPieceColor(), null);

        assertEquals(board.getFields()[43].getPiece(), Piece.PAWN);
        assertEquals(board.getFields()[43].getPieceColor(), GameColor.BLACK);

        assertEquals(board.getFields()[53].getPiece(), null);
        assertEquals(board.getFields()[53].getPieceColor(), null);

        assertFalse(board.castlingBlackLongAllowed);
        assertFalse(board.castlingBlackShortAllowed);
        assertFalse(board.castlingWhiteLongAllowed);
        assertFalse(board.castlingWhiteShortAllowed);

        assertEquals(board.enPassantField, Integer.MIN_VALUE);
    }

    @Test
    public void testApplyEnPassantWhite() {
        FenFormat fen = new FenFormat("3k4/8/8/1pP5/8/8/8/3K4 w KQkq b6 0 1");
        Board board = new Board(fen);

        board.applyMove(new Move("C5", "B6"), GameColor.WHITE);

        assertEquals(board.getFields()[63].getPiece(), null);
        assertEquals(board.getFields()[63].getPieceColor(), null);

        assertEquals(board.getFields()[72].getPiece(), Piece.PAWN);
        assertEquals(board.getFields()[72].getPieceColor(), GameColor.WHITE);

        assertEquals(board.getFields()[62].getPiece(), null);
        assertEquals(board.getFields()[62].getPieceColor(), null);

        assertTrue(board.castlingBlackLongAllowed);
        assertTrue(board.castlingBlackShortAllowed);
        assertTrue(board.castlingWhiteLongAllowed);
        assertTrue(board.castlingWhiteShortAllowed);

        assertEquals(board.enPassantField, Integer.MIN_VALUE);
    }

    @Test
    public void testApplyShortCastlingWhite() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        Board board = new Board(fen);

        board.applyMove(new Move(Castling.WHITE_SHORT), GameColor.WHITE);

        assertEquals(board.getFields()[25].getPiece(), null);
        assertEquals(board.getFields()[25].getPieceColor(), null);
        assertEquals(board.getFields()[28].getPiece(), null);
        assertEquals(board.getFields()[28].getPieceColor(), null);

        assertEquals(board.getFields()[27].getPiece(), Piece.KING);
        assertEquals(board.getFields()[27].getPieceColor(), GameColor.WHITE);
        assertEquals(board.getFields()[26].getPiece(), Piece.ROOK);
        assertEquals(board.getFields()[26].getPieceColor(), GameColor.WHITE);

        assertTrue(board.castlingBlackLongAllowed);
        assertTrue(board.castlingBlackShortAllowed);
        assertFalse(board.castlingWhiteLongAllowed);
        assertFalse(board.castlingWhiteShortAllowed);

        assertEquals(board.enPassantField, Integer.MIN_VALUE);
    }

    @Test
    public void testApplyShortCastlingBlack() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        Board board = new Board(fen);

        board.applyMove(new Move(Castling.BLACK_SHORT), GameColor.BLACK);

        assertEquals(board.getFields()[95].getPiece(), null);
        assertEquals(board.getFields()[95].getPieceColor(), null);
        assertEquals(board.getFields()[98].getPiece(), null);
        assertEquals(board.getFields()[98].getPieceColor(), null);

        assertEquals(board.getFields()[97].getPiece(), Piece.KING);
        assertEquals(board.getFields()[97].getPieceColor(), GameColor.BLACK);
        assertEquals(board.getFields()[96].getPiece(), Piece.ROOK);
        assertEquals(board.getFields()[96].getPieceColor(), GameColor.BLACK);

        assertFalse(board.castlingBlackLongAllowed);
        assertFalse(board.castlingBlackShortAllowed);
        assertTrue(board.castlingWhiteLongAllowed);
        assertTrue(board.castlingWhiteShortAllowed);

        assertEquals(board.enPassantField, Integer.MIN_VALUE);
    }

    @Test
    public void testApplyLongCastlingWhite() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        Board board = new Board(fen);

        board.applyMove(new Move(Castling.WHITE_LONG), GameColor.WHITE);

        assertEquals(board.getFields()[25].getPiece(), null);
        assertEquals(board.getFields()[25].getPieceColor(), null);
        assertEquals(board.getFields()[21].getPiece(), null);
        assertEquals(board.getFields()[21].getPieceColor(), null);

        assertEquals(board.getFields()[23].getPiece(), Piece.KING);
        assertEquals(board.getFields()[23].getPieceColor(), GameColor.WHITE);
        assertEquals(board.getFields()[24].getPiece(), Piece.ROOK);
        assertEquals(board.getFields()[24].getPieceColor(), GameColor.WHITE);

        assertTrue(board.castlingBlackLongAllowed);
        assertTrue(board.castlingBlackShortAllowed);
        assertFalse(board.castlingWhiteLongAllowed);
        assertFalse(board.castlingWhiteShortAllowed);

        assertEquals(board.enPassantField, Integer.MIN_VALUE);
    }

    @Test
    public void testApplyLongCastlingBlack() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        Board board = new Board(fen);

        board.applyMove(new Move(Castling.BLACK_LONG), GameColor.BLACK);

        assertEquals(board.getFields()[95].getPiece(), null);
        assertEquals(board.getFields()[95].getPieceColor(), null);
        assertEquals(board.getFields()[91].getPiece(), null);
        assertEquals(board.getFields()[91].getPieceColor(), null);

        assertEquals(board.getFields()[93].getPiece(), Piece.KING);
        assertEquals(board.getFields()[93].getPieceColor(), GameColor.BLACK);
        assertEquals(board.getFields()[94].getPiece(), Piece.ROOK);
        assertEquals(board.getFields()[94].getPieceColor(), GameColor.BLACK);

        assertFalse(board.castlingBlackLongAllowed);
        assertFalse(board.castlingBlackShortAllowed);
        assertTrue(board.castlingWhiteLongAllowed);
        assertTrue(board.castlingWhiteShortAllowed);

        assertEquals(board.enPassantField, Integer.MIN_VALUE);
    }

    @Test
    public void testApplyDestroyCastlingWhite() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");

        Board board = new Board(fen);
        board.applyMove(new Move("A1", "B1"), GameColor.WHITE);
        assertTrue(board.castlingBlackLongAllowed);
        assertTrue(board.castlingBlackShortAllowed);
        assertFalse(board.castlingWhiteLongAllowed);
        assertTrue(board.castlingWhiteShortAllowed);

        board = new Board(fen);
        board.applyMove(new Move("H1", "G1"), GameColor.WHITE);
        assertTrue(board.castlingBlackLongAllowed);
        assertTrue(board.castlingBlackShortAllowed);
        assertTrue(board.castlingWhiteLongAllowed);
        assertFalse(board.castlingWhiteShortAllowed);

        board = new Board(fen);
        board.applyMove(new Move("E1", "E2"), GameColor.WHITE);
        assertTrue(board.castlingBlackLongAllowed);
        assertTrue(board.castlingBlackShortAllowed);
        assertFalse(board.castlingWhiteLongAllowed);
        assertFalse(board.castlingWhiteShortAllowed);

    }

    @Test
    public void testApplyDestroyCastlingBlack() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R b KQkq - 0 1");

        Board board = new Board(fen);
        board.applyMove(new Move("A8", "B8"), GameColor.BLACK);
        assertFalse(board.castlingBlackLongAllowed);
        assertTrue(board.castlingBlackShortAllowed);
        assertTrue(board.castlingWhiteLongAllowed);
        assertTrue(board.castlingWhiteShortAllowed);

        board = new Board(fen);
        board.applyMove(new Move("H8", "G8"), GameColor.BLACK);
        assertTrue(board.castlingBlackLongAllowed);
        assertFalse(board.castlingBlackShortAllowed);
        assertTrue(board.castlingWhiteLongAllowed);
        assertTrue(board.castlingWhiteShortAllowed);

        board = new Board(fen);
        board.applyMove(new Move("E8", "E7"), GameColor.BLACK);
        assertFalse(board.castlingBlackLongAllowed);
        assertFalse(board.castlingBlackShortAllowed);
        assertTrue(board.castlingWhiteLongAllowed);
        assertTrue(board.castlingWhiteShortAllowed);

    }

    @Test
    public void testApplyPromotion() {
        FenFormat fen = new FenFormat("3k1q2/6P1/8/8/8/8/6p1/3K1Q2 w - - 0 1");
        Board board = new Board(fen);

        board.applyMove(new Move("G7", "G8", Piece.QUEEN), GameColor.WHITE);
        assertEquals(board.getFields()[87].getPiece(), null);
        assertEquals(board.getFields()[87].getPieceColor(), null);
        assertEquals(board.getFields()[97].getPiece(), Piece.QUEEN);
        assertEquals(board.getFields()[97].getPieceColor(), GameColor.WHITE);

    }

    @Test
    public void testHelperFunctions() {
        Board board = new Board(INITIAL_FEN);

        assertTrue(board.containsPiece(21));
        assertTrue(board.isVoid(20));
        assertTrue(board.isPieceAndColor(25, Piece.KING, GameColor.WHITE));

    }

    @Test
    public void testEnPassantField() {
        Board board = new Board(new FenFormat("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1"));

        assertEquals(45, board.getEnPassantField());

    }

}
