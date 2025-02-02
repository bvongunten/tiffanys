package ch.nostromo.tiffanys.commons.board;

import ch.nostromo.tiffanys.commons.ChessGameException;
import ch.nostromo.tiffanys.commons.Side;
import ch.nostromo.tiffanys.commons.BaseTest;
import ch.nostromo.tiffanys.commons.formats.FenFormat;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.commons.move.Castling;
import org.junit.Test;

import java.util.List;

import static ch.nostromo.tiffanys.commons.board.Square.*;
import static org.junit.Assert.*;

public class BoardTest extends BaseTest {


    @Test
    public void checkLegalMovesOppening() throws Exception {
        FenFormat fen = new FenFormat("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        Board board = new Board(fen);
        List<Move> moves = board.getLegalMoves(Side.WHITE);

        this.assertAndRemoveExpectedMove(moves, new Move(A2, A3));
        this.assertAndRemoveExpectedMove(moves, new Move(A2, A4));
        this.assertAndRemoveExpectedMove(moves, new Move(B2, B3));
        this.assertAndRemoveExpectedMove(moves, new Move(B2, B4));
        this.assertAndRemoveExpectedMove(moves, new Move(C2, C3));
        this.assertAndRemoveExpectedMove(moves, new Move(C2, C4));
        this.assertAndRemoveExpectedMove(moves, new Move(D2, D3));
        this.assertAndRemoveExpectedMove(moves, new Move(D2, D4));
        this.assertAndRemoveExpectedMove(moves, new Move(E2, E3));
        this.assertAndRemoveExpectedMove(moves, new Move(E2, E4));
        this.assertAndRemoveExpectedMove(moves, new Move(F2, F3));
        this.assertAndRemoveExpectedMove(moves, new Move(F2, F4));
        this.assertAndRemoveExpectedMove(moves, new Move(G2, G3));
        this.assertAndRemoveExpectedMove(moves, new Move(G2, G4));
        this.assertAndRemoveExpectedMove(moves, new Move(H2, H3));
        this.assertAndRemoveExpectedMove(moves, new Move(H2, H4));

        this.assertAndRemoveExpectedMove(moves, new Move(B1, A3));
        this.assertAndRemoveExpectedMove(moves, new Move(B1, C3));
        this.assertAndRemoveExpectedMove(moves, new Move(G1, H3));
        this.assertAndRemoveExpectedMove(moves, new Move(G1, F3));

        this.assertEmptyMoveList(moves);
    }

    @Test
    public void checkLegalMovesInCheck() throws Exception {
        FenFormat fen = new FenFormat("5b2/1p1b1p2/5kp1/P1n1p3/3pP2p/P1pP1P2/4q1r1/B3RK2 w - - 0 39");
        Board board = new Board(fen);
        List<Move> moves = board.getLegalMoves(Side.WHITE);

        this.assertAndRemoveExpectedMove(moves, new Move(E1, E2));

        this.assertEmptyMoveList(moves);
    }

    @Test
    public void testIsCheck() throws Exception {
        FenFormat fen = new FenFormat("5b2/1p1b1p2/5kp1/P1n1p3/3pP2p/P1pP1P2/4q1r1/B3RK2 w - - 0 39");
        Board board = new Board(fen);
        assertTrue(board.isInCheck(Side.WHITE));

    }

    @Test
    public void testIsMate() throws Exception {
        FenFormat fen = new FenFormat("5b2/1p1b1p2/5kp1/P1n1p3/3pP2p/P1pP1P2/5qr1/B3RK2 w - - 0 39");
        Board board = new Board(fen);
        assertTrue(board.isMate(Side.WHITE));

    }

    @Test
    public void testIsMateBecauseOfKing() throws Exception {
        FenFormat fen = new FenFormat("2K5/1pq5/p2k4/P7/8/8/8/8 w - - 0 66");
        Board board = new Board(fen);
        assertTrue(board.isMate(Side.WHITE));

    }

    @Test
    public void testLeadsToCheck() throws Exception {
        FenFormat fen = new FenFormat("5b2/1p1b1p2/5kp1/P1n1p3/3pP2p/P1pP1P2/3q2r1/B3RK2 b - - 0 38");
        Board board = new Board(fen);

        Move move = new Move(D2, E2);

        assertTrue(board.leadsToCheck(move, Side.BLACK));
    }

    @Test
    public void testLeadsToMate() throws Exception {
        FenFormat fen = new FenFormat("5b2/1p1b1p2/5kp1/P1n1p3/3pP2p/P1pP1P2/3q2r1/B3RK2 b - - 0 38");
        Board board = new Board(fen);

        Move move = new Move(D2, F2);

        assertTrue(board.leadsToMate(move, Side.BLACK));
    }

    @Test
    public void testIsCheckIssue() throws CloneNotSupportedException {
        FenFormat fen = new FenFormat("8/3R4/6rp/1p1kn3/3b1P2/6P1/7K/3q4 b - - 0 1");
        Board board = new Board(fen);

        assertTrue(board.isInCheck(Side.BLACK));

    }

    @Test
    public void testIsCheckIssue2() throws CloneNotSupportedException {
        FenFormat fen = new FenFormat("8/6pp/p1k5/1p1pb3/4r3/1P3nPb/PPN2PK1/3R4 w - - 0 33");
        Board board = new Board(fen);

        assertTrue(board.isInCheck(Side.WHITE));

    }

    @Test
    public void testFenInitial() {
        checkFen(FenFormat.INITIAL_FEN);
    }

    @Test(expected = ChessGameException.class)
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


    private void checkFen(FenFormat originalFen) {
        Board board = new Board(originalFen);

        assertEquals("FenPos", originalFen.getPosition(), board.getFenPosition());
        assertEquals("FenCastling", originalFen.getCastling(), board.getFenCastling());
        assertEquals("FenEnPassant", originalFen.getEnPassant(), board.getFenEnPassant());
    }


    @Test
    public void testCastlingLongInitialBoadWhite() {
        Board board = new Board();
        assertTrue(board.isCastlingAllowed(Castling.WHITE_LONG));
    }

    @Test
    public void testCastlingShortInitialBoadWhite() {
        Board board = new Board();
        assertTrue(board.isCastlingAllowed(Castling.WHITE_SHORT));
    }

    @Test
    public void testCastlingLongInitialBoardBlack() {
        Board board = new Board();
        assertTrue(board.isCastlingAllowed(Castling.BLACK_LONG));
    }

    @Test
    public void testCastlingShortInitialBoadBlack() {
        Board board = new Board();
        assertTrue(board.isCastlingAllowed(Castling.BLACK_SHORT));
    }

    @Test
    public void testClone() throws Exception {
        Board board = new Board();
        board.clone();
    }

    @Test
    public void testIsPieceOfColor() {
        Board board = new Board(FenFormat.INITIAL_FEN);

        assertEquals(board.getPieceSide(Square.A1), Side.WHITE);
        assertEquals(board.getPieceSide(Square.H8), Side.BLACK);
    }

    @Test(expected = ChessGameException.class)
    public void testIsPieceOfColorFail() {
        Board board = new Board();
        board.getPieceSide(Square.D4);
    }

    @Test
    public void testApplyNormalMove() {
        FenFormat fen = new FenFormat("8/7k/8/8/3Q4/8/K7/8 w KQkq b6 0 1");
        Board board = new Board(fen);

        // move queen 1 field
        board.applyMove(new Move(D4, D3), Side.WHITE);

        assertNull(board.getBoardSquares()[54].getPiece());

        assertEquals(board.getBoardSquares()[44].getPiece(), Piece.WHITE_QUEEN);
        assertEquals(board.getBoardSquares()[44].getPiece().getSide(), Side.WHITE);

        assertTrue(board.castlingBlackLongAllowed);
        assertTrue(board.castlingBlackShortAllowed);
        assertTrue(board.castlingWhiteLongAllowed);
        assertTrue(board.castlingWhiteShortAllowed);
        assertNull(board.enPassantField);
    }

    @Test
    public void testApplyPawnOpening() {
        Board board = new Board();

        // move queen 1 field
        board.applyMove(new Move(E2, E4), Side.WHITE);

        assertNull(board.getBoardSquares()[35].getPiece());

        assertEquals(board.getBoardSquares()[55].getPiece(), Piece.WHITE_PAWN);
        assertEquals(board.getBoardSquares()[55].getPiece().getSide(), Side.WHITE);

        assertTrue(board.castlingBlackLongAllowed);
        assertTrue(board.castlingBlackShortAllowed);
        assertTrue(board.castlingWhiteLongAllowed);
        assertTrue(board.castlingWhiteShortAllowed);
        assertEquals(board.enPassantField, Square.E3);
    }

    @Test
    public void testApplyEnPassantBlack() {
        FenFormat fen = new FenFormat("3k4/8/8/8/1pP5/8/8/3K4 b - c3 0 1");
        Board board = new Board(fen);

        board.applyMove(new Move(B4, C3), Side.BLACK);

        assertNull(board.getBoardSquares()[52].getPiece());

        assertEquals(board.getBoardSquares()[43].getPiece(), Piece.BLACK_PAWN);
        assertEquals(board.getBoardSquares()[43].getPiece().getSide(), Side.BLACK);

        assertNull(board.getBoardSquares()[53].getPiece());

        assertFalse(board.castlingBlackLongAllowed);
        assertFalse(board.castlingBlackShortAllowed);
        assertFalse(board.castlingWhiteLongAllowed);
        assertFalse(board.castlingWhiteShortAllowed);

        assertNull(board.enPassantField);
    }

    @Test
    public void testApplyEnPassantWhite() {
        FenFormat fen = new FenFormat("3k4/8/8/1pP5/8/8/8/3K4 w KQkq b6 0 1");
        Board board = new Board(fen);

        board.applyMove(new Move(C5, B6), Side.WHITE);

        assertNull(board.getBoardSquares()[63].getPiece());

        assertEquals(board.getBoardSquares()[72].getPiece(), Piece.WHITE_PAWN);
        assertEquals(board.getBoardSquares()[72].getPiece().getSide(), Side.WHITE);

        assertNull(board.getBoardSquares()[62].getPiece());

        assertTrue(board.castlingBlackLongAllowed);
        assertTrue(board.castlingBlackShortAllowed);
        assertTrue(board.castlingWhiteLongAllowed);
        assertTrue(board.castlingWhiteShortAllowed);

        assertNull(board.enPassantField);
    }

    @Test
    public void testApplyShortCastlingWhite() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        Board board = new Board(fen);

        board.applyMove(new Move(Castling.WHITE_SHORT), Side.WHITE);

        assertNull(board.getBoardSquares()[25].getPiece());
        assertNull(board.getBoardSquares()[28].getPiece());

        assertEquals(board.getBoardSquares()[27].getPiece(), Piece.WHITE_KING);
        assertEquals(board.getBoardSquares()[27].getPiece().getSide(), Side.WHITE);
        assertEquals(board.getBoardSquares()[26].getPiece(), Piece.WHITE_ROOK);
        assertEquals(board.getBoardSquares()[26].getPiece().getSide(), Side.WHITE);

        assertTrue(board.castlingBlackLongAllowed);
        assertTrue(board.castlingBlackShortAllowed);
        assertFalse(board.castlingWhiteLongAllowed);
        assertFalse(board.castlingWhiteShortAllowed);

        assertNull(board.enPassantField);
    }

    @Test
    public void testApplyShortCastlingBlack() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        Board board = new Board(fen);

        board.applyMove(new Move(Castling.BLACK_SHORT), Side.BLACK);

        assertNull(board.getBoardSquares()[95].getPiece());
        assertNull(board.getBoardSquares()[98].getPiece());

        assertEquals(board.getBoardSquares()[97].getPiece(), Piece.BLACK_KING);
        assertEquals(board.getBoardSquares()[96].getPiece(), Piece.BLACK_ROOK);

        assertFalse(board.castlingBlackLongAllowed);
        assertFalse(board.castlingBlackShortAllowed);
        assertTrue(board.castlingWhiteLongAllowed);
        assertTrue(board.castlingWhiteShortAllowed);

        assertNull(board.enPassantField);
    }

    @Test
    public void testApplyLongCastlingWhite() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        Board board = new Board(fen);

        board.applyMove(new Move(Castling.WHITE_LONG), Side.WHITE);

        assertNull(board.getBoardSquares()[25].getPiece());
        assertNull(board.getBoardSquares()[21].getPiece());

        assertEquals(board.getBoardSquares()[23].getPiece(), Piece.WHITE_KING);
        assertEquals(board.getBoardSquares()[23].getPiece().getSide(), Side.WHITE);
        assertEquals(board.getBoardSquares()[24].getPiece(), Piece.WHITE_ROOK);
        assertEquals(board.getBoardSquares()[24].getPiece().getSide(), Side.WHITE);

        assertTrue(board.castlingBlackLongAllowed);
        assertTrue(board.castlingBlackShortAllowed);
        assertFalse(board.castlingWhiteLongAllowed);
        assertFalse(board.castlingWhiteShortAllowed);

        assertNull(board.enPassantField);
    }

    @Test
    public void testApplyLongCastlingBlack() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        Board board = new Board(fen);

        board.applyMove(new Move(Castling.BLACK_LONG), Side.BLACK);

        assertNull(board.getBoardSquares()[95].getPiece());
        assertNull(board.getBoardSquares()[91].getPiece());

        assertEquals(board.getBoardSquares()[93].getPiece(), Piece.BLACK_KING);
        assertEquals(board.getBoardSquares()[94].getPiece(), Piece.BLACK_ROOK);

        assertFalse(board.castlingBlackLongAllowed);
        assertFalse(board.castlingBlackShortAllowed);
        assertTrue(board.castlingWhiteLongAllowed);
        assertTrue(board.castlingWhiteShortAllowed);

        assertNull(board.enPassantField);
    }

    @Test
    public void testApplyDestroyCastlingWhite() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");

        Board board = new Board(fen);
        board.applyMove(new Move(A1, B1), Side.WHITE);
        assertTrue(board.castlingBlackLongAllowed);
        assertTrue(board.castlingBlackShortAllowed);
        assertFalse(board.castlingWhiteLongAllowed);
        assertTrue(board.castlingWhiteShortAllowed);

        board = new Board(fen);
        board.applyMove(new Move(H1, G1), Side.WHITE);
        assertTrue(board.castlingBlackLongAllowed);
        assertTrue(board.castlingBlackShortAllowed);
        assertTrue(board.castlingWhiteLongAllowed);
        assertFalse(board.castlingWhiteShortAllowed);

        board = new Board(fen);
        board.applyMove(new Move(E1, E2), Side.WHITE);
        assertTrue(board.castlingBlackLongAllowed);
        assertTrue(board.castlingBlackShortAllowed);
        assertFalse(board.castlingWhiteLongAllowed);
        assertFalse(board.castlingWhiteShortAllowed);

    }

    @Test
    public void testApplyDestroyCastlingBlack() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R b KQkq - 0 1");

        Board board = new Board(fen);
        board.applyMove(new Move(A8, B8), Side.BLACK);
        assertFalse(board.castlingBlackLongAllowed);
        assertTrue(board.castlingBlackShortAllowed);
        assertTrue(board.castlingWhiteLongAllowed);
        assertTrue(board.castlingWhiteShortAllowed);

        board = new Board(fen);
        board.applyMove(new Move(H8, G8), Side.BLACK);
        assertTrue(board.castlingBlackLongAllowed);
        assertFalse(board.castlingBlackShortAllowed);
        assertTrue(board.castlingWhiteLongAllowed);
        assertTrue(board.castlingWhiteShortAllowed);

        board = new Board(fen);
        board.applyMove(new Move(E8, E7), Side.BLACK);
        assertFalse(board.castlingBlackLongAllowed);
        assertFalse(board.castlingBlackShortAllowed);
        assertTrue(board.castlingWhiteLongAllowed);
        assertTrue(board.castlingWhiteShortAllowed);

    }

    @Test
    public void testApplyPromotion() {
        FenFormat fen = new FenFormat("3k1q2/6P1/8/8/8/8/6p1/3K1Q2 w - - 0 1");
        Board board = new Board(fen);

        board.applyMove(new Move(G7, G8, Piece.WHITE_QUEEN), Side.WHITE);
        assertNull(board.getBoardSquares()[87].getPiece());
        assertEquals(board.getBoardSquares()[97].getPiece(), Piece.WHITE_QUEEN);
        assertEquals(board.getBoardSquares()[97].getPiece().getSide(), Side.WHITE);

    }

    @Test
    public void testHelperFunctions() {
        Board board = new Board();

        assertFalse(board.isEmptySquare(Square.A1));
        assertTrue(board.isBorder(20));
        assertTrue(board.containsPieceOfSide(25, PieceType.KING, Side.WHITE));

    }

    @Test
    public void testEnPassantField() {
        Board board = new Board(new FenFormat("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1"));

        assertEquals(Square.E3, board.getEnPassantField());
    }

    @Test
    public void testGetSquareInformation() {
        Board board = new Board();

        assertEquals(Piece.WHITE_PAWN, board.getPiece(Square.A2));
        assertEquals(Piece.BLACK_PAWN, board.getPiece(Square.A7));

        assertEquals(Side.WHITE, board.getPieceSide(Square.A2));
        assertEquals(Side.BLACK, board.getPieceSide(Square.A7));

        assertEquals(PieceType.PAWN, board.getPieceType(Square.A2));
        assertEquals(PieceType.PAWN, board.getPieceType(Square.A7));
    }

    @Test(expected = ChessGameException.class)
    public void testGetInvalidSquareInformation() {
        Board board = new Board();

        board.getPiece(Square.A5);
    }

    @Test
    public void testDump() {

        // @formatter:off
        String baseBoard = "8 BR BN BB BQ BK BB BN BR \n" +
                           "7 BP BP BP BP BP BP BP BP \n" +
                           "6 [] [] [] [] [] [] [] [] \n" +
                           "5 [] [] [] [] [] [] [] [] \n" +
                           "4 [] [] [] [] [] [] [] [] \n" +
                           "3 [] [] [] [] [] [] [] [] \n" +
                           "2 WP WP WP WP WP WP WP WP \n" +
                           "1 WR WN WB WQ WK WB WN WR \n" +
                           "  A  B  C  D  E  F  G  H  ";
        //@formatter:on

        assertEquals("Board Dump", baseBoard, new Board().toString());
    }
}
