package ch.nostromo.tiffanys.commons.move.movegenerator;

import ch.nostromo.tiffanys.commons.BaseTest;
import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.board.Piece;
import ch.nostromo.tiffanys.commons.format.FenFormat;
import ch.nostromo.tiffanys.commons.move.Castling;
import ch.nostromo.tiffanys.commons.move.Move;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static ch.nostromo.tiffanys.commons.board.Square.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KingMoveGeneratorTest extends BaseTest {

    @Test
    void testMoveGenEmptyBoard() {
        FenFormat fen = new FenFormat("8/7k/8/8/3K4/8/8/8 w - - 0 1");
        Board board = new Board(fen);

        List<Move> generatedMoves = Piece.WHITE_KING.getPseudoLegalMoves(board, 54);

        List<Move> expectedMoves = new ArrayList<>();
        expectedMoves.add(new Move(D4, C3));
        expectedMoves.add(new Move(D4, D3));
        expectedMoves.add(new Move(D4, E3));
        expectedMoves.add(new Move(D4, C4));
        expectedMoves.add(new Move(D4, E4));
        expectedMoves.add(new Move(D4, C5));
        expectedMoves.add(new Move(D4, D5));
        expectedMoves.add(new Move(D4, E5));

        assertGeneratedMoves(generatedMoves, expectedMoves);
    }

    @Test
    void testMoveGenOpponentPieces() {
        FenFormat fen = new FenFormat("8/7k/8/2qqq3/2qKq3/2qqq3/8/8 w - - 0 1");
        Board board = new Board(fen);

        List<Move> generatedMoves = Piece.WHITE_KING.getPseudoLegalMoves(board, 54);

        List<Move> expectedMoves = new ArrayList<>();
        expectedMoves.add(new Move(D4, C3));
        expectedMoves.add(new Move(D4, D3));
        expectedMoves.add(new Move(D4, E3));
        expectedMoves.add(new Move(D4, C4));
        expectedMoves.add(new Move(D4, E4));
        expectedMoves.add(new Move(D4, C5));
        expectedMoves.add(new Move(D4, D5));
        expectedMoves.add(new Move(D4, E5));

        assertGeneratedMoves(generatedMoves, expectedMoves);
    }

    @Test
    void testMoveGenOwnPieces() {
        FenFormat fen = new FenFormat("8/7k/8/2QQQ3/2QKQ3/2QQQ3/8/8 b - - 0 1");
        Board board = new Board(fen);

        assertTrue(Piece.WHITE_KING.getPseudoLegalMoves(board, 54).isEmpty());
    }

    @Test
    void testMoveGenCastlingAllAllowed() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        Board board = new Board(fen);

        List<Move> generatedWhiteMoves = Piece.WHITE_KING.getPseudoLegalMoves(board, 25);

        List<Move> expectedWhiteMoves = new ArrayList<>();
        expectedWhiteMoves.add(new Move(Castling.WHITE_LONG));
        expectedWhiteMoves.add(new Move(Castling.WHITE_SHORT));
        expectedWhiteMoves.add(new Move(E1, D1));
        expectedWhiteMoves.add(new Move(E1, D2));
        expectedWhiteMoves.add(new Move(E1, E2));
        expectedWhiteMoves.add(new Move(E1, F2));
        expectedWhiteMoves.add(new Move(E1, F1));

        assertGeneratedMoves(generatedWhiteMoves, expectedWhiteMoves);

        List<Move> generatedBlackMoves = Piece.BLACK_KING.getPseudoLegalMoves(board, 95);

        List<Move> expectedBlackMoves = new ArrayList<>();
        expectedBlackMoves.add(new Move(Castling.BLACK_LONG));
        expectedBlackMoves.add(new Move(Castling.BLACK_SHORT));
        expectedBlackMoves.add(new Move(E8, D8));
        expectedBlackMoves.add(new Move(E8, D7));
        expectedBlackMoves.add(new Move(E8, E7));
        expectedBlackMoves.add(new Move(E8, F7));
        expectedBlackMoves.add(new Move(E8, F8));

        assertGeneratedMoves(generatedBlackMoves, expectedBlackMoves);

    }

    @ParameterizedTest
    @MethodSource("boardProvider")
    void testCastlingNotAllowed(Board board) {
        List<Move> generatedWhiteMoves = Piece.WHITE_KING.getPseudoLegalMoves(board, 25);

        List<Move> expectedWhiteMoves = new ArrayList<>();
        expectedWhiteMoves.add(new Move(E1, D1));
        expectedWhiteMoves.add(new Move(E1, D2));
        expectedWhiteMoves.add(new Move(E1, E2));
        expectedWhiteMoves.add(new Move(E1, F2));
        expectedWhiteMoves.add(new Move(E1, F1));

        assertGeneratedMoves(generatedWhiteMoves, expectedWhiteMoves);

        List<Move> generatedBlackMoves = Piece.BLACK_KING.getPseudoLegalMoves(board, 95);

        List<Move> expectedBlackMoves = new ArrayList<>();
        expectedBlackMoves.add(new Move(E8, D8));
        expectedBlackMoves.add(new Move(E8, D7));
        expectedBlackMoves.add(new Move(E8, E7));
        expectedBlackMoves.add(new Move(E8, F7));
        expectedBlackMoves.add(new Move(E8, F8));

        assertGeneratedMoves(generatedBlackMoves, expectedBlackMoves);
    }

    static Stream<Board> boardProvider() {
        return Stream.of(
                // None allowed
                new Board(new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R w - - 0 1")),
                // Diagonal chess
                new Board(new FenFormat("r3k2r/8/1B1B4/8/8/1b1b4/8/R3K2R w KQkq - 0 1")),
                // Horizontal chess
                new Board(new FenFormat("r3k2r/8/2R2R2/8/8/2r2r2/8/R3K2R w KQkq - 0 1")),
                // Pawn chess
                new Board(new FenFormat("r3k2r/2P3P1/8/8/8/8/2p3p1/R3K2R w KQkq - 0 1")),
                // Knight chess
                new Board(new FenFormat("r3k2r/8/4N3/8/8/4n3/8/R3K2R w KQkq - 0 1"))

        );
    }



}
