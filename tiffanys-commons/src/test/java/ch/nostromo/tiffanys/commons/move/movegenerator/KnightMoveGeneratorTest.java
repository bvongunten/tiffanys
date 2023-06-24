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

class KnightMoveGeneratorTest extends BaseTest {

    @Test
    void testMoveGenEmptyBoard() {
        FenFormat fen = new FenFormat("8/7k/8/8/3N4/8/K7/8 w - - 0 1");
        Board board = new Board(fen);

        List<Move> generatedMoves = Piece.WHITE_KNIGHT.getPseudoLegalMoves(board, 54);

        List<Move> expectedMoves = new ArrayList<>();
        expectedMoves.add(new Move(D4, B3));
        expectedMoves.add(new Move(D4, C2));
        expectedMoves.add(new Move(D4, B5));
        expectedMoves.add(new Move(D4, C6));
        expectedMoves.add(new Move(D4, E6));
        expectedMoves.add(new Move(D4, F5));
        expectedMoves.add(new Move(D4, F3));
        expectedMoves.add(new Move(D4, E2));

        assertGeneratedMoves(generatedMoves, expectedMoves);
    }

    @Test
    void testMoveGenOpponentPieces() {
        FenFormat fen = new FenFormat("8/7k/2b1b3/1b3b2/3N4/1b3b2/K1b1b3/8 w - - 0 1");
        Board board = new Board(fen);

        List<Move> generatedMoves = Piece.WHITE_KNIGHT.getPseudoLegalMoves(board, 54);

        List<Move> expectedMoves = new ArrayList<>();
        expectedMoves.add(new Move(D4, B3));
        expectedMoves.add(new Move(D4, C2));
        expectedMoves.add(new Move(D4, B5));
        expectedMoves.add(new Move(D4, C6));
        expectedMoves.add(new Move(D4, E6));
        expectedMoves.add(new Move(D4, F5));
        expectedMoves.add(new Move(D4, F3));
        expectedMoves.add(new Move(D4, E2));

        assertGeneratedMoves(generatedMoves, expectedMoves);
    }

    @Test
    void testMoveGenOwnPieces() {
        FenFormat fen = new FenFormat("8/7k/2B1B3/1B3B2/3N4/1B3B2/K1B1B3/8 b - - 0 1");
        Board board = new Board(fen);

        assertTrue(Piece.WHITE_KNIGHT.getPseudoLegalMoves(board, 54).isEmpty());
    }

}
