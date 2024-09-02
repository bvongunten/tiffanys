package ch.nostromo.tiffanys.commons.board;

import ch.nostromo.tiffanys.commons.TestHelper;
import ch.nostromo.tiffanys.commons.enums.Coordinates;
import ch.nostromo.tiffanys.commons.enums.Castling;
import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.enums.Piece;
import ch.nostromo.tiffanys.commons.fen.FenFormat;
import ch.nostromo.tiffanys.commons.move.Move;
import org.junit.Test;

import static ch.nostromo.tiffanys.commons.enums.Coordinates.A1;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.A8;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.B1;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.B4;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.B6;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.B8;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.C3;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.C5;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.D3;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.D4;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.E1;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.E2;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.E4;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.E7;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.E8;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.G1;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.G7;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.G8;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.H1;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.H8;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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


        assertEquals("FenPos", originalFen.getPosition(), board.getFenPosition());
        assertEquals("FenCastling", originalFen.getCastling(), board.getFenCastling());
        assertEquals("FenEnPassant", originalFen.getEnPassant(), board.getFenEnpassant());
    }

    @Test
    public void testApplyNormalMove() {
        FenFormat fen = new FenFormat("8/7k/8/8/3Q4/8/K7/8 w KQkq b6 0 1");
        Board board = new Board(fen);

        // move queen 1 field
        board.applyMove(new Move(D4, D3), GameColor.WHITE);

        assertEquals(board.getBoardFields()[54].getPiece(), null);
        assertEquals(board.getBoardFields()[54].getPieceColor(), null);

        assertEquals(board.getBoardFields()[44].getPiece(), Piece.QUEEN);
        assertEquals(board.getBoardFields()[44].getPieceColor(), GameColor.WHITE);

        assertTrue(board.castlingBlackLongAllowed);
        assertTrue(board.castlingBlackShortAllowed);
        assertTrue(board.castlingWhiteLongAllowed);
        assertTrue(board.castlingWhiteShortAllowed);
        assertEquals(board.enPassantField, null);
    }

    @Test
    public void testApplyPawnOpening() {
        Board board = new Board(INITIAL_FEN);

        // move queen 1 field
        board.applyMove(new Move(E2, E4), GameColor.WHITE);

        assertEquals(board.getBoardFields()[35].getPiece(), null);
        assertEquals(board.getBoardFields()[35].getPieceColor(), null);

        assertEquals(board.getBoardFields()[55].getPiece(), Piece.PAWN);
        assertEquals(board.getBoardFields()[55].getPieceColor(), GameColor.WHITE);

        assertTrue(board.castlingBlackLongAllowed);
        assertTrue(board.castlingBlackShortAllowed);
        assertTrue(board.castlingWhiteLongAllowed);
        assertTrue(board.castlingWhiteShortAllowed);
        assertEquals(board.enPassantField, Coordinates.E3);
    }

    @Test
    public void testApplyEnPassantBlack() {
        FenFormat fen = new FenFormat("3k4/8/8/8/1pP5/8/8/3K4 b - c3 0 1");
        Board board = new Board(fen);

        board.applyMove(new Move(B4, C3), GameColor.BLACK);

        assertEquals(board.getBoardFields()[52].getPiece(), null);
        assertEquals(board.getBoardFields()[52].getPieceColor(), null);

        assertEquals(board.getBoardFields()[43].getPiece(), Piece.PAWN);
        assertEquals(board.getBoardFields()[43].getPieceColor(), GameColor.BLACK);

        assertEquals(board.getBoardFields()[53].getPiece(), null);
        assertEquals(board.getBoardFields()[53].getPieceColor(), null);

        assertFalse(board.castlingBlackLongAllowed);
        assertFalse(board.castlingBlackShortAllowed);
        assertFalse(board.castlingWhiteLongAllowed);
        assertFalse(board.castlingWhiteShortAllowed);

        assertEquals(board.enPassantField, null);
    }

    @Test
    public void testApplyEnPassantWhite() {
        FenFormat fen = new FenFormat("3k4/8/8/1pP5/8/8/8/3K4 w KQkq b6 0 1");
        Board board = new Board(fen);

        board.applyMove(new Move(C5, B6), GameColor.WHITE);

        assertEquals(board.getBoardFields()[63].getPiece(), null);
        assertEquals(board.getBoardFields()[63].getPieceColor(), null);

        assertEquals(board.getBoardFields()[72].getPiece(), Piece.PAWN);
        assertEquals(board.getBoardFields()[72].getPieceColor(), GameColor.WHITE);

        assertEquals(board.getBoardFields()[62].getPiece(), null);
        assertEquals(board.getBoardFields()[62].getPieceColor(), null);

        assertTrue(board.castlingBlackLongAllowed);
        assertTrue(board.castlingBlackShortAllowed);
        assertTrue(board.castlingWhiteLongAllowed);
        assertTrue(board.castlingWhiteShortAllowed);

        assertEquals(board.enPassantField, null);
    }

    @Test
    public void testApplyShortCastlingWhite() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        Board board = new Board(fen);

        board.applyMove(new Move(Castling.WHITE_SHORT), GameColor.WHITE);

        assertEquals(board.getBoardFields()[25].getPiece(), null);
        assertEquals(board.getBoardFields()[25].getPieceColor(), null);
        assertEquals(board.getBoardFields()[28].getPiece(), null);
        assertEquals(board.getBoardFields()[28].getPieceColor(), null);

        assertEquals(board.getBoardFields()[27].getPiece(), Piece.KING);
        assertEquals(board.getBoardFields()[27].getPieceColor(), GameColor.WHITE);
        assertEquals(board.getBoardFields()[26].getPiece(), Piece.ROOK);
        assertEquals(board.getBoardFields()[26].getPieceColor(), GameColor.WHITE);

        assertTrue(board.castlingBlackLongAllowed);
        assertTrue(board.castlingBlackShortAllowed);
        assertFalse(board.castlingWhiteLongAllowed);
        assertFalse(board.castlingWhiteShortAllowed);

        assertEquals(board.enPassantField, null);
    }

    @Test
    public void testApplyShortCastlingBlack() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        Board board = new Board(fen);

        board.applyMove(new Move(Castling.BLACK_SHORT), GameColor.BLACK);

        assertEquals(board.getBoardFields()[95].getPiece(), null);
        assertEquals(board.getBoardFields()[95].getPieceColor(), null);
        assertEquals(board.getBoardFields()[98].getPiece(), null);
        assertEquals(board.getBoardFields()[98].getPieceColor(), null);

        assertEquals(board.getBoardFields()[97].getPiece(), Piece.KING);
        assertEquals(board.getBoardFields()[97].getPieceColor(), GameColor.BLACK);
        assertEquals(board.getBoardFields()[96].getPiece(), Piece.ROOK);
        assertEquals(board.getBoardFields()[96].getPieceColor(), GameColor.BLACK);

        assertFalse(board.castlingBlackLongAllowed);
        assertFalse(board.castlingBlackShortAllowed);
        assertTrue(board.castlingWhiteLongAllowed);
        assertTrue(board.castlingWhiteShortAllowed);

        assertEquals(board.enPassantField, null);
    }

    @Test
    public void testApplyLongCastlingWhite() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        Board board = new Board(fen);

        board.applyMove(new Move(Castling.WHITE_LONG), GameColor.WHITE);

        assertEquals(board.getBoardFields()[25].getPiece(), null);
        assertEquals(board.getBoardFields()[25].getPieceColor(), null);
        assertEquals(board.getBoardFields()[21].getPiece(), null);
        assertEquals(board.getBoardFields()[21].getPieceColor(), null);

        assertEquals(board.getBoardFields()[23].getPiece(), Piece.KING);
        assertEquals(board.getBoardFields()[23].getPieceColor(), GameColor.WHITE);
        assertEquals(board.getBoardFields()[24].getPiece(), Piece.ROOK);
        assertEquals(board.getBoardFields()[24].getPieceColor(), GameColor.WHITE);

        assertTrue(board.castlingBlackLongAllowed);
        assertTrue(board.castlingBlackShortAllowed);
        assertFalse(board.castlingWhiteLongAllowed);
        assertFalse(board.castlingWhiteShortAllowed);

        assertEquals(board.enPassantField, null);
    }

    @Test
    public void testApplyLongCastlingBlack() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        Board board = new Board(fen);

        board.applyMove(new Move(Castling.BLACK_LONG), GameColor.BLACK);

        assertEquals(board.getBoardFields()[95].getPiece(), null);
        assertEquals(board.getBoardFields()[95].getPieceColor(), null);
        assertEquals(board.getBoardFields()[91].getPiece(), null);
        assertEquals(board.getBoardFields()[91].getPieceColor(), null);

        assertEquals(board.getBoardFields()[93].getPiece(), Piece.KING);
        assertEquals(board.getBoardFields()[93].getPieceColor(), GameColor.BLACK);
        assertEquals(board.getBoardFields()[94].getPiece(), Piece.ROOK);
        assertEquals(board.getBoardFields()[94].getPieceColor(), GameColor.BLACK);

        assertFalse(board.castlingBlackLongAllowed);
        assertFalse(board.castlingBlackShortAllowed);
        assertTrue(board.castlingWhiteLongAllowed);
        assertTrue(board.castlingWhiteShortAllowed);

        assertEquals(board.enPassantField, null);
    }

    @Test
    public void testApplyDestroyCastlingWhite() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");

        Board board = new Board(fen);
        board.applyMove(new Move(A1,B1), GameColor.WHITE);
        assertTrue(board.castlingBlackLongAllowed);
        assertTrue(board.castlingBlackShortAllowed);
        assertFalse(board.castlingWhiteLongAllowed);
        assertTrue(board.castlingWhiteShortAllowed);

        board = new Board(fen);
        board.applyMove(new Move(H1, G1), GameColor.WHITE);
        assertTrue(board.castlingBlackLongAllowed);
        assertTrue(board.castlingBlackShortAllowed);
        assertTrue(board.castlingWhiteLongAllowed);
        assertFalse(board.castlingWhiteShortAllowed);

        board = new Board(fen);
        board.applyMove(new Move(E1, E2), GameColor.WHITE);
        assertTrue(board.castlingBlackLongAllowed);
        assertTrue(board.castlingBlackShortAllowed);
        assertFalse(board.castlingWhiteLongAllowed);
        assertFalse(board.castlingWhiteShortAllowed);

    }

    @Test
    public void testApplyDestroyCastlingBlack() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R b KQkq - 0 1");

        Board board = new Board(fen);
        board.applyMove(new Move(A8, B8), GameColor.BLACK);
        assertFalse(board.castlingBlackLongAllowed);
        assertTrue(board.castlingBlackShortAllowed);
        assertTrue(board.castlingWhiteLongAllowed);
        assertTrue(board.castlingWhiteShortAllowed);

        board = new Board(fen);
        board.applyMove(new Move(H8, G8), GameColor.BLACK);
        assertTrue(board.castlingBlackLongAllowed);
        assertFalse(board.castlingBlackShortAllowed);
        assertTrue(board.castlingWhiteLongAllowed);
        assertTrue(board.castlingWhiteShortAllowed);

        board = new Board(fen);
        board.applyMove(new Move(E8, E7), GameColor.BLACK);
        assertFalse(board.castlingBlackLongAllowed);
        assertFalse(board.castlingBlackShortAllowed);
        assertTrue(board.castlingWhiteLongAllowed);
        assertTrue(board.castlingWhiteShortAllowed);

    }

    @Test
    public void testApplyPromotion() {
        FenFormat fen = new FenFormat("3k1q2/6P1/8/8/8/8/6p1/3K1Q2 w - - 0 1");
        Board board = new Board(fen);

        board.applyMove(new Move(G7, G8, Piece.QUEEN), GameColor.WHITE);
        assertEquals(board.getBoardFields()[87].getPiece(), null);
        assertEquals(board.getBoardFields()[87].getPieceColor(), null);
        assertEquals(board.getBoardFields()[97].getPiece(), Piece.QUEEN);
        assertEquals(board.getBoardFields()[97].getPieceColor(), GameColor.WHITE);

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

        assertEquals(Coordinates.E3, board.getEnPassantField());

    }

}
