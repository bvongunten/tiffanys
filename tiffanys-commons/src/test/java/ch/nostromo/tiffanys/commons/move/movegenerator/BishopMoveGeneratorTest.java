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

class BishopMoveGeneratorTest extends BaseTest {

    @Test
    void testMoveGenEmptyBoard() {
        FenFormat fen = new FenFormat("8/7k/8/8/3B4/8/K7/8 w - - 0 1");
        Board board = new Board(fen);

        List<Move> generatedMoves = Piece.WHITE_BISHOP.getPseudoLegalMoves(board, 54);

        List<Move> expectedMoves = new ArrayList<>();
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
        FenFormat fen = new FenFormat("8/7k/1q3n2/8/3B4/8/Kb3r2/8 w - - 0 1");
        Board board = new Board(fen);

        List<Move> generatedMoves = Piece.WHITE_BISHOP.getPseudoLegalMoves(board, 54);

        List<Move> expectedMoves = new ArrayList<>();
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
        FenFormat fen = new FenFormat("8/7k/1Q3N2/8/3B4/8/KB3R2/8 b - - 0 1");
        Board board = new Board(fen);

        List<Move> generatedMoves = Piece.WHITE_BISHOP.getPseudoLegalMoves(board, 54);

        List<Move> expectedMoves = new ArrayList<>();
        expectedMoves.add(new Move(D4, C5));
        expectedMoves.add(new Move(D4, C3));
        expectedMoves.add(new Move(D4, E3));
        expectedMoves.add(new Move(D4, E5));

        assertGeneratedMoves(generatedMoves, expectedMoves);

    }

}
