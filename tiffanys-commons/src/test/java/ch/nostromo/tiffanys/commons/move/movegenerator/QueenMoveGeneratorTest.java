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

class QueenMoveGeneratorTest extends BaseTest {

    @Test
    void testMoveGenEmptyBoard() {
        FenFormat fen = new FenFormat("8/7k/8/8/3Q4/8/K7/8 w - - 0 1");
        Board board = new Board(fen);

        List<Move> generatedMoves = Piece.WHITE_QUEEN.getPseudoLegalMoves(board, 54);

        List<Move> expectedMoves = new ArrayList<>();
        expectedMoves.add(new Move(D4, D3));
        expectedMoves.add(new Move(D4, D2));
        expectedMoves.add(new Move(D4, D1));
        expectedMoves.add(new Move(D4, D5));
        expectedMoves.add(new Move(D4, D6));
        expectedMoves.add(new Move(D4, D7));
        expectedMoves.add(new Move(D4, D8));
        expectedMoves.add(new Move(D4, C4));
        expectedMoves.add(new Move(D4, B4));
        expectedMoves.add(new Move(D4, A4));
        expectedMoves.add(new Move(D4, E4));
        expectedMoves.add(new Move(D4, F4));
        expectedMoves.add(new Move(D4, G4));
        expectedMoves.add(new Move(D4, H4));

        expectedMoves.add(new Move(D4, C3));
        expectedMoves.add(new Move(D4, B2));
        expectedMoves.add(new Move(D4, A1));
        expectedMoves.add(new Move(D4, C5));
        expectedMoves.add(new Move(D4, B6));
        expectedMoves.add(new Move(D4, A7));
        expectedMoves.add(new Move(D4, E3));
        expectedMoves.add(new Move(D4, F2));
        expectedMoves.add(new Move(D4, G1));
        expectedMoves.add(new Move(D4, E5));
        expectedMoves.add(new Move(D4, F6));
        expectedMoves.add(new Move(D4, G7));
        expectedMoves.add(new Move(D4, H8));

        assertGeneratedMoves(generatedMoves, expectedMoves);
    }

    @Test
    void testMoveGenOpponentPieces() {
        FenFormat fen = new FenFormat("7k/8/1n1n1q2/8/1b1Q1r2/8/1r1q1b2/K7 w - - 0 1");
        Board board = new Board(fen);

        List<Move> generatedMoves = Piece.WHITE_QUEEN.getPseudoLegalMoves(board, 54);

        List<Move> expectedMoves = new ArrayList<>();

        expectedMoves.add(new Move(D4, D3));
        expectedMoves.add(new Move(D4, D2));
        expectedMoves.add(new Move(D4, D5));
        expectedMoves.add(new Move(D4, D6));
        expectedMoves.add(new Move(D4, C4));
        expectedMoves.add(new Move(D4, B4));
        expectedMoves.add(new Move(D4, E4));
        expectedMoves.add(new Move(D4, F4));

        expectedMoves.add(new Move(D4, C5));
        expectedMoves.add(new Move(D4, B6));
        expectedMoves.add(new Move(D4, C3));
        expectedMoves.add(new Move(D4, B2));
        expectedMoves.add(new Move(D4, E3));
        expectedMoves.add(new Move(D4, F2));
        expectedMoves.add(new Move(D4, E5));
        expectedMoves.add(new Move(D4, F6));

        assertGeneratedMoves(generatedMoves, expectedMoves);
    }

    @Test
    void testMoveGenOwnPieces() {
        FenFormat fen = new FenFormat("8/7k/1Q1N1N2/8/1Q1Q1R2/8/KB1B1R2/8 b - - 0 1");
        Board board = new Board(fen);

        List<Move> generatedMoves = Piece.WHITE_QUEEN.getPseudoLegalMoves(board, 54);

        List<Move> expectedMoves = new ArrayList<>();
        expectedMoves.add(new Move(D4, C5));
        expectedMoves.add(new Move(D4, C3));
        expectedMoves.add(new Move(D4, E3));
        expectedMoves.add(new Move(D4, E5));

        expectedMoves.add(new Move(D4, D3));
        expectedMoves.add(new Move(D4, D5));
        expectedMoves.add(new Move(D4, C4));
        expectedMoves.add(new Move(D4, E4));

        assertGeneratedMoves(generatedMoves, expectedMoves);
    }

}
