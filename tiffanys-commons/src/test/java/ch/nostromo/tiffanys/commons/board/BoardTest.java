package ch.nostromo.tiffanys.commons.board;

import ch.nostromo.tiffanys.commons.BaseTest;
import ch.nostromo.tiffanys.commons.Side;
import ch.nostromo.tiffanys.commons.format.FenFormat;
import ch.nostromo.tiffanys.commons.move.Castling;
import ch.nostromo.tiffanys.commons.move.Move;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static ch.nostromo.tiffanys.commons.board.Square.*;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest extends BaseTest {


    @Test
    void checkLegalMovesOppening() {
        FenFormat fen = new FenFormat("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        Board board = new Board(fen);
        List<Move> generatedMoves = board.getLegalMoves(Side.WHITE);

        List<Move> expectedMoves = new ArrayList<>();
        expectedMoves.add(new Move(A2, A3));
        expectedMoves.add(new Move(A2, A4));
        expectedMoves.add(new Move(B2, B3));
        expectedMoves.add(new Move(B2, B4));
        expectedMoves.add(new Move(C2, C3));
        expectedMoves.add(new Move(C2, C4));
        expectedMoves.add(new Move(D2, D3));
        expectedMoves.add(new Move(D2, D4));
        expectedMoves.add(new Move(E2, E3));
        expectedMoves.add(new Move(E2, E4));
        expectedMoves.add(new Move(F2, F3));
        expectedMoves.add(new Move(F2, F4));
        expectedMoves.add(new Move(G2, G3));
        expectedMoves.add(new Move(G2, G4));
        expectedMoves.add(new Move(H2, H3));
        expectedMoves.add(new Move(H2, H4));

        expectedMoves.add(new Move(B1, A3));
        expectedMoves.add(new Move(B1, C3));
        expectedMoves.add(new Move(G1, H3));
        expectedMoves.add(new Move(G1, F3));

        assertGeneratedMoves(generatedMoves, expectedMoves);
    }

    @Test
    void checkLegalMovesInCheck() {
        FenFormat fen = new FenFormat("5b2/1p1b1p2/5kp1/P1n1p3/3pP2p/P1pP1P2/4q1r1/B3RK2 w - - 0 39");
        Board board = new Board(fen);
        List<Move> generatedMoves = board.getLegalMoves(Side.WHITE);

        List<Move> expectedMoves = new ArrayList<>();
        expectedMoves.add(new Move(E1, E2));

        assertGeneratedMoves(generatedMoves, expectedMoves);
    }

    @Test
    void testIsCheck() {
        FenFormat fen = new FenFormat("5b2/1p1b1p2/5kp1/P1n1p3/3pP2p/P1pP1P2/4q1r1/B3RK2 w - - 0 39");
        Board board = new Board(fen);
        assertTrue(board.isInCheck(Side.WHITE));

    }

    @Test
    void testIsMate() {
        FenFormat fen = new FenFormat("5b2/1p1b1p2/5kp1/P1n1p3/3pP2p/P1pP1P2/5qr1/B3RK2 w - - 0 39");
        Board board = new Board(fen);
        assertTrue(board.isMate(Side.WHITE));

    }

    @Test
    void testIsMateBecauseOfKing() {
        FenFormat fen = new FenFormat("2K5/1pq5/p2k4/P7/8/8/8/8 w - - 0 66");
        Board board = new Board(fen);
        assertTrue(board.isMate(Side.WHITE));

    }

    @Test
    void testLeadsToCheck() {
        FenFormat fen = new FenFormat("5b2/1p1b1p2/5kp1/P1n1p3/3pP2p/P1pP1P2/3q2r1/B3RK2 b - - 0 38");
        Board board = new Board(fen);

        Move move = new Move(D2, E2);

        assertTrue(board.leadsToCheck(move, Side.BLACK));
    }

    @Test
    void testLeadsToMate()  {
        FenFormat fen = new FenFormat("5b2/1p1b1p2/5kp1/P1n1p3/3pP2p/P1pP1P2/3q2r1/B3RK2 b - - 0 38");
        Board board = new Board(fen);

        Move move = new Move(D2, F2);

        assertTrue(board.leadsToMate(move, Side.BLACK));
    }

    @Test
    void testIsCheckIssue() {
        FenFormat fen = new FenFormat("8/3R4/6rp/1p1kn3/3b1P2/6P1/7K/3q4 b - - 0 1");
        Board board = new Board(fen);

        assertTrue(board.isInCheck(Side.BLACK));

    }

    @Test
    void testIsCheckIssue2()  {
        FenFormat fen = new FenFormat("8/6pp/p1k5/1p1pb3/4r3/1P3nPb/PPN2PK1/3R4 w - - 0 33");
        Board board = new Board(fen);

        assertTrue(board.isInCheck(Side.WHITE));

    }

    @Test
    void testFenInitial() {
        checkFen(FenFormat.INITIAL_FEN);
    }

    @Test
    void testFenIllegal() {
        FenFormat fen = new FenFormat("abcdefgh/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1");
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            checkFen(fen);
        });

        assertEquals("Unknown piece char code: a", thrown.getMessage());
    }

    @Test
    void testFenEnPassant() {
        checkFen(new FenFormat("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1"));
    }

    @Test
    void testFenCastling() {
        checkFen(new FenFormat("4k3/8/8/8/8/8/4P3/4K3 w - - 5 39"));
    }


    private void checkFen(FenFormat originalFen) {
        Board board = new Board(originalFen);

        assertEquals(originalFen.getPosition(), board.getFenPosition());
        assertEquals(originalFen.getCastling(), board.getFenCastling());
        assertEquals(originalFen.getEnPassant(), board.getFenEnPassant());
    }


    @Test
    void testCastlingLongInitialBoadWhite() {
        Board board = new Board();
        assertTrue(board.isCastlingAllowed(Castling.WHITE_LONG));
    }

    @Test
    void testCastlingShortInitialBoadWhite() {
        Board board = new Board();
        assertTrue(board.isCastlingAllowed(Castling.WHITE_SHORT));
    }

    @Test
    void testCastlingLongInitialBoardBlack() {
        Board board = new Board();
        assertTrue(board.isCastlingAllowed(Castling.BLACK_LONG));
    }

    @Test
    void testCastlingShortInitialBoadBlack() {
        Board board = new Board();
        assertTrue(board.isCastlingAllowed(Castling.BLACK_SHORT));
    }

    @Test
    void testClone() {
        Board board = new Board();
        Board copy = board.copy();

        assertEquals(board, copy);
    }

    @Test
    void testIsPieceOfColor() {
        Board board = new Board(FenFormat.INITIAL_FEN);

        assertEquals(Side.WHITE, board.getPieceSide(Square.A1));
        assertEquals(Side.BLACK, board.getPieceSide(Square.H8));
    }

    @Test
    void testIsPieceOfColorFail() {
        Board board = new Board();
        assertThrows(IllegalArgumentException.class, () -> {
            board.getPieceSide(Square.D4);
        });
    }

    @Test
    void testApplyNormalMove() {
        FenFormat fen = new FenFormat("8/7k/8/8/3Q4/8/K7/8 w KQkq b6 0 1");
        Board board = new Board(fen);

        // move queen 1 field
        board.applyMove(new Move(D4, D3), Side.WHITE);

        assertNull(board.getBoardSquares()[54].getPiece());

        assertEquals(Piece.WHITE_QUEEN, board.getBoardSquares()[44].getPiece());
        assertEquals(Side.WHITE, board.getBoardSquares()[44].getPiece().getSide());

        assertTrue(board.castlingBlackLongAllowed);
        assertTrue(board.castlingBlackShortAllowed);
        assertTrue(board.castlingWhiteLongAllowed);
        assertTrue(board.castlingWhiteShortAllowed);
        assertNull(board.enPassantField);
    }

    @Test
    void testApplyPawnOpening() {
        Board board = new Board();

        // move queen 1 field
        board.applyMove(new Move(E2, E4), Side.WHITE);

        assertNull(board.getBoardSquares()[35].getPiece());

        assertEquals(Piece.WHITE_PAWN, board.getBoardSquares()[55].getPiece());
        assertEquals(Side.WHITE, board.getBoardSquares()[55].getPiece().getSide());

        assertTrue(board.castlingBlackLongAllowed);
        assertTrue(board.castlingBlackShortAllowed);
        assertTrue(board.castlingWhiteLongAllowed);
        assertTrue(board.castlingWhiteShortAllowed);
        assertEquals(Square.E3, board.enPassantField);
    }

    @Test
    void testApplyEnPassantBlack() {
        FenFormat fen = new FenFormat("3k4/8/8/8/1pP5/8/8/3K4 b - c3 0 1");
        Board board = new Board(fen);

        board.applyMove(new Move(B4, C3), Side.BLACK);

        assertNull(board.getBoardSquares()[52].getPiece());

        assertEquals(Piece.BLACK_PAWN, board.getBoardSquares()[43].getPiece());
        assertEquals(Side.BLACK, board.getBoardSquares()[43].getPiece().getSide());

        assertNull(board.getBoardSquares()[53].getPiece());

        assertFalse(board.castlingBlackLongAllowed);
        assertFalse(board.castlingBlackShortAllowed);
        assertFalse(board.castlingWhiteLongAllowed);
        assertFalse(board.castlingWhiteShortAllowed);

        assertNull(board.enPassantField);
    }

    @Test
    void testApplyEnPassantWhite() {
        FenFormat fen = new FenFormat("3k4/8/8/1pP5/8/8/8/3K4 w KQkq b6 0 1");
        Board board = new Board(fen);

        board.applyMove(new Move(C5, B6), Side.WHITE);

        assertNull(board.getBoardSquares()[63].getPiece());

        assertEquals(Piece.WHITE_PAWN, board.getBoardSquares()[72].getPiece());
        assertEquals(Side.WHITE, board.getBoardSquares()[72].getPiece().getSide());

        assertNull(board.getBoardSquares()[62].getPiece());

        assertTrue(board.castlingBlackLongAllowed);
        assertTrue(board.castlingBlackShortAllowed);
        assertTrue(board.castlingWhiteLongAllowed);
        assertTrue(board.castlingWhiteShortAllowed);

        assertNull(board.enPassantField);
    }

    @Test
    void testApplyShortCastlingWhite() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        Board board = new Board(fen);

        board.applyMove(new Move(Castling.WHITE_SHORT), Side.WHITE);

        assertNull(board.getBoardSquares()[25].getPiece());
        assertNull(board.getBoardSquares()[28].getPiece());

        assertEquals(Piece.WHITE_KING, board.getBoardSquares()[27].getPiece());
        assertEquals(Side.WHITE, board.getBoardSquares()[27].getPiece().getSide());
        assertEquals(Piece.WHITE_ROOK, board.getBoardSquares()[26].getPiece());
        assertEquals(Side.WHITE, board.getBoardSquares()[26].getPiece().getSide());

        assertTrue(board.castlingBlackLongAllowed);
        assertTrue(board.castlingBlackShortAllowed);
        assertFalse(board.castlingWhiteLongAllowed);
        assertFalse(board.castlingWhiteShortAllowed);

        assertNull(board.enPassantField);
    }

    @Test
    void testApplyShortCastlingBlack() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        Board board = new Board(fen);

        board.applyMove(new Move(Castling.BLACK_SHORT), Side.BLACK);

        assertNull(board.getBoardSquares()[95].getPiece());
        assertNull(board.getBoardSquares()[98].getPiece());

        assertEquals(Piece.BLACK_KING, board.getBoardSquares()[97].getPiece());
        assertEquals(Piece.BLACK_ROOK, board.getBoardSquares()[96].getPiece());

        assertFalse(board.castlingBlackLongAllowed);
        assertFalse(board.castlingBlackShortAllowed);
        assertTrue(board.castlingWhiteLongAllowed);
        assertTrue(board.castlingWhiteShortAllowed);

        assertNull(board.enPassantField);
    }

    @Test
    void testApplyLongCastlingWhite() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        Board board = new Board(fen);

        board.applyMove(new Move(Castling.WHITE_LONG), Side.WHITE);

        assertNull(board.getBoardSquares()[25].getPiece());
        assertNull(board.getBoardSquares()[21].getPiece());

        assertEquals(Piece.WHITE_KING, board.getBoardSquares()[23].getPiece());
        assertEquals(Side.WHITE, board.getBoardSquares()[23].getPiece().getSide());
        assertEquals(Piece.WHITE_ROOK, board.getBoardSquares()[24].getPiece());
        assertEquals(Side.WHITE, board.getBoardSquares()[24].getPiece().getSide());

        assertTrue(board.castlingBlackLongAllowed);
        assertTrue(board.castlingBlackShortAllowed);
        assertFalse(board.castlingWhiteLongAllowed);
        assertFalse(board.castlingWhiteShortAllowed);

        assertNull(board.enPassantField);
    }

    @Test
    void testApplyLongCastlingBlack() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        Board board = new Board(fen);

        board.applyMove(new Move(Castling.BLACK_LONG), Side.BLACK);

        assertNull(board.getBoardSquares()[95].getPiece());
        assertNull(board.getBoardSquares()[91].getPiece());

        assertEquals(Piece.BLACK_KING, board.getBoardSquares()[93].getPiece());
        assertEquals(Piece.BLACK_ROOK, board.getBoardSquares()[94].getPiece());

        assertFalse(board.castlingBlackLongAllowed);
        assertFalse(board.castlingBlackShortAllowed);
        assertTrue(board.castlingWhiteLongAllowed);
        assertTrue(board.castlingWhiteShortAllowed);

        assertNull(board.enPassantField);
    }

    @Test
    void testApplyDestroyCastlingWhite() {
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
    void testApplyDestroyCastlingBlack() {
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
    void testApplyPromotion() {
        FenFormat fen = new FenFormat("3k1q2/6P1/8/8/8/8/6p1/3K1Q2 w - - 0 1");
        Board board = new Board(fen);

        board.applyMove(new Move(G7, G8, Piece.WHITE_QUEEN), Side.WHITE);
        assertNull(board.getBoardSquares()[87].getPiece());
        assertEquals(Piece.WHITE_QUEEN, board.getBoardSquares()[97].getPiece());
        assertEquals(Side.WHITE, board.getBoardSquares()[97].getPiece().getSide());

    }

    @Test
    void testHelperFunctions() {
        Board board = new Board();

        assertFalse(board.isEmptySquare(Square.A1));
        assertTrue(board.isBorder(20));
        assertTrue(board.containsPieceOfSide(25, PieceType.KING, Side.WHITE));

    }

    @Test
    void testEnPassantField() {
        Board board = new Board(new FenFormat("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1"));

        assertEquals(Square.E3, board.getEnPassantField());
    }

    @Test
    void testGetSquareInformation() {
        Board board = new Board();

        assertEquals(Piece.WHITE_PAWN, board.getPiece(Square.A2));
        assertEquals(Piece.BLACK_PAWN, board.getPiece(Square.A7));

        assertEquals(Side.WHITE, board.getPieceSide(Square.A2));
        assertEquals(Side.BLACK, board.getPieceSide(Square.A7));

        assertEquals(PieceType.PAWN, board.getPieceType(Square.A2));
        assertEquals(PieceType.PAWN, board.getPieceType(Square.A7));
    }

    @Test
    void testGetInvalidSquareInformation() {
        Board board = new Board();
        assertThrows(IllegalArgumentException.class, () -> {
            board.getPiece(Square.A5);
        });
    }

    @Test
    void testDump() {

        // @formatter:off
        String baseBoard = new StringBuilder().
                append("8 BR BN BB BQ BK BB BN BR \n").
                append("7 BP BP BP BP BP BP BP BP \n").
                append("6 [] [] [] [] [] [] [] [] \n").
                append("5 [] [] [] [] [] [] [] [] \n").
                append("4 [] [] [] [] [] [] [] [] \n").
                append("3 [] [] [] [] [] [] [] [] \n").
                append("2 WP WP WP WP WP WP WP WP \n").
                append("1 WR WN WB WQ WK WB WN WR \n").
                append("  A  B  C  D  E  F  G  H  ").toString();
        //@formatter:on

        assertEquals(baseBoard, new Board().toString());
    }
}
