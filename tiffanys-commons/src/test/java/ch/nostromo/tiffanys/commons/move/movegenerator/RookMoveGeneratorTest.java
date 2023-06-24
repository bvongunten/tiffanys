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

class RookMoveGeneratorTest extends BaseTest {

    @Test
    void testMoveGenEmptyBoard() {
        FenFormat fen = new FenFormat("8/7k/8/8/3R4/8/K7/8 w - - 0 1");
        Board board = new Board(fen);

        List<Move> generatedMoves = Piece.WHITE_ROOK.getPseudoLegalMoves(board, 54);

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

        assertGeneratedMoves(generatedMoves, expectedMoves);
    }

    @Test
    void testMoveGenOpponentPieces() {
        FenFormat fen = new FenFormat("7k/8/3n4/8/1b1R1r2/8/3q4/K7 w - - 0 1");
        Board board = new Board(fen);

        List<Move> generatedMoves = Piece.WHITE_ROOK.getPseudoLegalMoves(board, 54);

        List<Move> expectedMoves = new ArrayList<>();
        expectedMoves.add(new Move(D4, D3));
        expectedMoves.add(new Move(D4, D2));
        expectedMoves.add(new Move(D4, D5));
        expectedMoves.add(new Move(D4, D6));
        expectedMoves.add(new Move(D4, C4));
        expectedMoves.add(new Move(D4, B4));
        expectedMoves.add(new Move(D4, E4));
        expectedMoves.add(new Move(D4, F4));

        assertGeneratedMoves(generatedMoves, expectedMoves);
    }

    @Test
    void testMoveGenOwnPieces() {
        FenFormat fen = new FenFormat("7k/8/3N4/8/1B1R1R2/8/3Q4/K7 w - - 0 1");
        Board board = new Board(fen);

        List<Move> generatedMoves = Piece.WHITE_ROOK.getPseudoLegalMoves(board, 54);

        List<Move> expectedMoves = new ArrayList<>();
        expectedMoves.add(new Move(D4, D3));
        expectedMoves.add(new Move(D4, D5));
        expectedMoves.add(new Move(D4, C4));
        expectedMoves.add(new Move(D4, E4));

        assertGeneratedMoves(generatedMoves, expectedMoves);
    }

}
