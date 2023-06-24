package ch.nostromo.tiffanys.commons.move.movegenerator;

import ch.nostromo.tiffanys.commons.BaseTest;
import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.board.Piece;
import ch.nostromo.tiffanys.commons.format.FenFormat;
import ch.nostromo.tiffanys.commons.move.Move;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static ch.nostromo.tiffanys.commons.board.Square.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PawnMoveGeneratorTest extends BaseTest {

    @Test
    void testMoveGenStartLines() {
        FenFormat fen = new FenFormat("3k4/3p4/4p3/8/8/4P3/3P4/3K4 b - - 0 7");
        Board board = new Board(fen);

        List<Move> generatedWhiteMoves = Piece.WHITE_PAWN.getPseudoLegalMoves(board, 34);

        List<Move> expectedWhiteMoves = new ArrayList<>();
        expectedWhiteMoves.add(new Move(D2, D3));
        expectedWhiteMoves.add(new Move(D2, D4));
        assertGeneratedMoves(generatedWhiteMoves, expectedWhiteMoves);

        List<Move> generatedBlackMoves = Piece.BLACK_PAWN.getPseudoLegalMoves(board, 84);

        List<Move> expectedBlackMoves = new ArrayList<>();
        expectedBlackMoves.add(new Move(D7, D6));
        expectedBlackMoves.add(new Move(D7, D5));
        assertGeneratedMoves(generatedBlackMoves, expectedBlackMoves);
    }

    @Test
    void testOneFieldOpening() {
        FenFormat fen = new FenFormat(" 3k4/3p4/8/3r4/3R4/8/3P4/3K4 b - - 0 7");
        Board board = new Board(fen);

        List<Move> generatedWhiteMoves = Piece.WHITE_PAWN.getPseudoLegalMoves(board, 34);

        List<Move> expectedWhiteMoves = new ArrayList<>();
        expectedWhiteMoves.add(new Move(D2, D3));
        assertGeneratedMoves(generatedWhiteMoves, expectedWhiteMoves);


        List<Move> generatedBlackMoves = Piece.BLACK_PAWN.getPseudoLegalMoves(board, 84);

        List<Move> expectedBlackMoves = new ArrayList<>();
        expectedBlackMoves.add(new Move(D7, D6));
        assertGeneratedMoves(generatedBlackMoves, expectedBlackMoves);
    }


    @Test
    void testMoveGenStartLinesBlocked() {
        FenFormat fen = new FenFormat("3k4/3p4/3P4/6p1/6P1/3p4/3P4/3K4 b - - 0 7");
        Board board = new Board(fen);

        assertTrue(Piece.WHITE_PAWN.getPseudoLegalMoves(board, 34).isEmpty());
        assertTrue(Piece.BLACK_PAWN.getPseudoLegalMoves(board, 84).isEmpty());
    }

    @Test
    void testMoveGenInField() {
        FenFormat fen = new FenFormat("3k4/3p4/4p3/8/8/4P3/3P4/3K4 b - - 0 7");
        Board board = new Board(fen);

        List<Move> generatedWhiteMoves = Piece.WHITE_PAWN.getPseudoLegalMoves(board, 45);

        List<Move> expectedWhiteMoves = new ArrayList<>();
        expectedWhiteMoves.add(new Move(E3, E4));
        assertGeneratedMoves(generatedWhiteMoves, expectedWhiteMoves);

        List<Move> generatedBlackMoves = Piece.BLACK_PAWN.getPseudoLegalMoves(board, 75);

        List<Move> expectedBlackMoves = new ArrayList<>();
        expectedBlackMoves.add(new Move(E6, E5));
        assertGeneratedMoves(generatedBlackMoves, expectedBlackMoves);
    }

    @Test
    void testMoveGenInFieldBlocked() {
        FenFormat fen = new FenFormat("3k4/3p4/3P4/6p1/6P1/3p4/3P4/3K4 b - - 0 7");
        Board board = new Board(fen);

        assertTrue(Piece.WHITE_PAWN.getPseudoLegalMoves(board, 57).isEmpty());
        assertTrue(Piece.BLACK_PAWN.getPseudoLegalMoves(board, 67).isEmpty());
    }

    @Test
    void testMoveGenHitMoves() {
        FenFormat fen = new FenFormat("3k4/3p4/2PpP3/8/8/2pPp3/3P4/3K4 b - - 0 7");
        Board board = new Board(fen);

        List<Move> generatedWhiteMoves = Piece.WHITE_PAWN.getPseudoLegalMoves(board, 34);
        List<Move> expectedWhiteMoves = new ArrayList<>();
        expectedWhiteMoves.add(new Move(D2, C3));
        expectedWhiteMoves.add(new Move(D2, E3));
        assertGeneratedMoves(generatedWhiteMoves, expectedWhiteMoves);

        List<Move> generatedBlackMoves = Piece.BLACK_PAWN.getPseudoLegalMoves(board, 84);
        List<Move> expectedBlackMoves = new ArrayList<>();
        expectedBlackMoves.add(new Move(D7, C6));
        expectedBlackMoves.add(new Move(D7, E6));
        assertGeneratedMoves(generatedBlackMoves, expectedBlackMoves);
    }

    @Test
    void testPromotions() {
        FenFormat fen = new FenFormat("3k1q2/6P1/8/8/8/8/6p1/3K1Q2 w - - 0 1");
        Board board = new Board(fen);

        List<Move> generatedWhiteMoves = Piece.WHITE_PAWN.getPseudoLegalMoves(board, 87);
        List<Move> expectedWhiteMoves = new ArrayList<>();
        expectedWhiteMoves.add(new Move(G7, G8, Piece.WHITE_BISHOP));
        expectedWhiteMoves.add(new Move(G7, G8, Piece.WHITE_KNIGHT));
        expectedWhiteMoves.add(new Move(G7, G8, Piece.WHITE_ROOK));
        expectedWhiteMoves.add(new Move(G7, G8, Piece.WHITE_QUEEN));
        expectedWhiteMoves.add(new Move(G7, F8, Piece.WHITE_BISHOP));
        expectedWhiteMoves.add(new Move(G7, F8, Piece.WHITE_KNIGHT));
        expectedWhiteMoves.add(new Move(G7, F8, Piece.WHITE_ROOK));
        expectedWhiteMoves.add(new Move(G7, F8, Piece.WHITE_QUEEN));
        assertGeneratedMoves(generatedWhiteMoves, expectedWhiteMoves);

        List<Move> generatedBlackMoves = Piece.BLACK_PAWN.getPseudoLegalMoves(board, 37);
        List<Move> expectedBlackMoves = new ArrayList<>();
        expectedBlackMoves.add(new Move(G2, G1, Piece.BLACK_BISHOP));
        expectedBlackMoves.add(new Move(G2, G1, Piece.BLACK_KNIGHT));
        expectedBlackMoves.add(new Move(G2, G1, Piece.BLACK_ROOK));
        expectedBlackMoves.add(new Move(G2, G1, Piece.BLACK_QUEEN));
        expectedBlackMoves.add(new Move(G2, F1, Piece.BLACK_BISHOP));
        expectedBlackMoves.add(new Move(G2, F1, Piece.BLACK_KNIGHT));
        expectedBlackMoves.add(new Move(G2, F1, Piece.BLACK_ROOK));
        expectedBlackMoves.add(new Move(G2, F1, Piece.BLACK_QUEEN));
        assertGeneratedMoves(generatedBlackMoves, expectedBlackMoves);
    }

    @Test
    void testEnPassantWhite() {
        FenFormat fen = new FenFormat("3k4/8/8/1pP5/8/8/8/3K4 w - b6 0 1");
        Board board = new Board(fen);

        List<Move> generatedWhiteMoves = Piece.WHITE_PAWN.getPseudoLegalMoves(board, 63);
        List<Move> expectedWhiteMoves = new ArrayList<>();
        expectedWhiteMoves.add(new Move(C5, C6));
        expectedWhiteMoves.add(new Move(C5, B6));
        assertGeneratedMoves(generatedWhiteMoves, expectedWhiteMoves);
    }

    @Test
    void testEnPassantBlack() {
        FenFormat fen = new FenFormat("3k4/8/8/8/1pP5/8/8/3K4 b - c3 0 1");
        Board board = new Board(fen);

        List<Move> generatedBlackMoves = Piece.BLACK_PAWN.getPseudoLegalMoves(board, 52);
        List<Move> expectedBlackMoves = new ArrayList<>();

        expectedBlackMoves.add(new Move(B4, B3));
        expectedBlackMoves.add(new Move(B4, C3));
        assertGeneratedMoves(generatedBlackMoves, expectedBlackMoves);
    }

}
