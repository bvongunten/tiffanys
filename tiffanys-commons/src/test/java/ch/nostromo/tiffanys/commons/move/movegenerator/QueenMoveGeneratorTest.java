package ch.nostromo.tiffanys.commons.move.movegenerator;

import ch.nostromo.tiffanys.commons.BaseTest;
import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.board.Piece;
import ch.nostromo.tiffanys.commons.formats.FenFormat;
import ch.nostromo.tiffanys.commons.move.Move;
import org.junit.Test;

import java.util.List;

import static ch.nostromo.tiffanys.commons.board.Square.A1;
import static ch.nostromo.tiffanys.commons.board.Square.A4;
import static ch.nostromo.tiffanys.commons.board.Square.A7;
import static ch.nostromo.tiffanys.commons.board.Square.B2;
import static ch.nostromo.tiffanys.commons.board.Square.B4;
import static ch.nostromo.tiffanys.commons.board.Square.B6;
import static ch.nostromo.tiffanys.commons.board.Square.C3;
import static ch.nostromo.tiffanys.commons.board.Square.C4;
import static ch.nostromo.tiffanys.commons.board.Square.C5;
import static ch.nostromo.tiffanys.commons.board.Square.D1;
import static ch.nostromo.tiffanys.commons.board.Square.D2;
import static ch.nostromo.tiffanys.commons.board.Square.D3;
import static ch.nostromo.tiffanys.commons.board.Square.D4;
import static ch.nostromo.tiffanys.commons.board.Square.D5;
import static ch.nostromo.tiffanys.commons.board.Square.D6;
import static ch.nostromo.tiffanys.commons.board.Square.D7;
import static ch.nostromo.tiffanys.commons.board.Square.D8;
import static ch.nostromo.tiffanys.commons.board.Square.E3;
import static ch.nostromo.tiffanys.commons.board.Square.E4;
import static ch.nostromo.tiffanys.commons.board.Square.E5;
import static ch.nostromo.tiffanys.commons.board.Square.F2;
import static ch.nostromo.tiffanys.commons.board.Square.F4;
import static ch.nostromo.tiffanys.commons.board.Square.F6;
import static ch.nostromo.tiffanys.commons.board.Square.G1;
import static ch.nostromo.tiffanys.commons.board.Square.G4;
import static ch.nostromo.tiffanys.commons.board.Square.G7;
import static ch.nostromo.tiffanys.commons.board.Square.H4;
import static ch.nostromo.tiffanys.commons.board.Square.H8;

public class QueenMoveGeneratorTest extends BaseTest {

    @Test
    public void testMoveGenEmptyBoard() {
        FenFormat fen = new FenFormat("8/7k/8/8/3Q4/8/K7/8 w - - 0 1");
        Board board = new Board(fen);

        List<Move> moves = Piece.WHITE_QUEEN.getPseudoLegalMoves(board, 54);

        this.assertAndRemoveExpectedMove(moves, new Move(D4, D3));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, D2));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, D1));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, D5));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, D6));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, D7));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, D8));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, C4));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, B4));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, A4));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, E4));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, F4));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, G4));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, H4));

        this.assertAndRemoveExpectedMove(moves, new Move(D4, C3));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, B2));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, A1));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, C5));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, B6));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, A7));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, E3));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, F2));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, G1));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, E5));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, F6));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, G7));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, H8));

        this.assertEmptyMoveList(moves);
    }

    @Test
    public void testMoveGenOpponentPieces() {
        FenFormat fen = new FenFormat("7k/8/1n1n1q2/8/1b1Q1r2/8/1r1q1b2/K7 w - - 0 1");
        Board board = new Board(fen);

        List<Move> moves = Piece.WHITE_QUEEN.getPseudoLegalMoves(board, 54);

        this.assertAndRemoveExpectedMove(moves, new Move(D4, D3));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, D2));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, D5));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, D6));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, C4));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, B4));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, E4));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, F4));

        this.assertAndRemoveExpectedMove(moves, new Move(D4, C5));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, B6));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, C3));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, B2));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, E3));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, F2));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, E5));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, F6));

        this.assertEmptyMoveList(moves);

    }

    @Test
    public void testMoveGenOwnPieces() {
        FenFormat fen = new FenFormat("8/7k/1Q1N1N2/8/1Q1Q1R2/8/KB1B1R2/8 b - - 0 1");
        Board board = new Board(fen);

        List<Move> moves = Piece.WHITE_QUEEN.getPseudoLegalMoves(board, 54);

        this.assertAndRemoveExpectedMove(moves, new Move(D4, C5));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, C3));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, E3));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, E5));

        this.assertAndRemoveExpectedMove(moves, new Move(D4, D3));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, D5));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, C4));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, E4));

        this.assertEmptyMoveList(moves);

    }

}
