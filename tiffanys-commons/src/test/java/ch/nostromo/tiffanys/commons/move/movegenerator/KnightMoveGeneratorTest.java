package ch.nostromo.tiffanys.commons.move.movegenerator;

import ch.nostromo.tiffanys.commons.BaseTest;
import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.board.Piece;
import ch.nostromo.tiffanys.commons.formats.FenFormat;
import ch.nostromo.tiffanys.commons.move.Move;
import org.junit.Test;

import java.util.List;

import static ch.nostromo.tiffanys.commons.board.Square.B3;
import static ch.nostromo.tiffanys.commons.board.Square.B5;
import static ch.nostromo.tiffanys.commons.board.Square.C2;
import static ch.nostromo.tiffanys.commons.board.Square.C6;
import static ch.nostromo.tiffanys.commons.board.Square.D4;
import static ch.nostromo.tiffanys.commons.board.Square.E2;
import static ch.nostromo.tiffanys.commons.board.Square.E6;
import static ch.nostromo.tiffanys.commons.board.Square.F3;
import static ch.nostromo.tiffanys.commons.board.Square.F5;

public class KnightMoveGeneratorTest extends BaseTest {

    @Test
    public void testMoveGenEmptyBoard() {
        FenFormat fen = new FenFormat("8/7k/8/8/3N4/8/K7/8 w - - 0 1");
        Board board = new Board(fen);

        List<Move> moves = Piece.WHITE_KNIGHT.getPseudoLegalMoves(board, 54);

        this.assertAndRemoveExpectedMove(moves, new Move(D4, B3));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, C2));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, B5));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, C6));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, E6));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, F5));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, F3));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, E2));

        this.assertEmptyMoveList(moves);
    }

    @Test
    public void testMoveGenOpponentPieces() {
        FenFormat fen = new FenFormat("8/7k/2b1b3/1b3b2/3N4/1b3b2/K1b1b3/8 w - - 0 1");
        Board board = new Board(fen);

        List<Move> moves = Piece.WHITE_KNIGHT.getPseudoLegalMoves(board, 54);

        this.assertAndRemoveExpectedMove(moves, new Move(D4, B3));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, C2));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, B5));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, C6));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, E6));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, F5));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, F3));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, E2));

        this.assertEmptyMoveList(moves);

    }

    @Test
    public void testMoveGenOwnPieces() {
        FenFormat fen = new FenFormat("8/7k/2B1B3/1B3B2/3N4/1B3B2/K1B1B3/8 b - - 0 1");
        Board board = new Board(fen);

        List<Move> moves = Piece.WHITE_KNIGHT.getPseudoLegalMoves(board, 54);

        this.assertEmptyMoveList(moves);
    }

}
