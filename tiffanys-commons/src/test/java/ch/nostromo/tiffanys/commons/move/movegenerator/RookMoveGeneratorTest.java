package ch.nostromo.tiffanys.commons.move.movegenerator;

import ch.nostromo.tiffanys.commons.BaseTest;
import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.board.Piece;
import ch.nostromo.tiffanys.commons.formats.FenFormat;
import ch.nostromo.tiffanys.commons.move.Move;
import org.junit.Test;

import java.util.List;

import static ch.nostromo.tiffanys.commons.board.Square.A4;
import static ch.nostromo.tiffanys.commons.board.Square.B4;
import static ch.nostromo.tiffanys.commons.board.Square.C4;
import static ch.nostromo.tiffanys.commons.board.Square.D1;
import static ch.nostromo.tiffanys.commons.board.Square.D2;
import static ch.nostromo.tiffanys.commons.board.Square.D3;
import static ch.nostromo.tiffanys.commons.board.Square.D4;
import static ch.nostromo.tiffanys.commons.board.Square.D5;
import static ch.nostromo.tiffanys.commons.board.Square.D6;
import static ch.nostromo.tiffanys.commons.board.Square.D7;
import static ch.nostromo.tiffanys.commons.board.Square.D8;
import static ch.nostromo.tiffanys.commons.board.Square.E4;
import static ch.nostromo.tiffanys.commons.board.Square.F4;
import static ch.nostromo.tiffanys.commons.board.Square.G4;
import static ch.nostromo.tiffanys.commons.board.Square.H4;
import static org.junit.Assert.assertEquals;

public class RookMoveGeneratorTest extends BaseTest {

    @Test
    public void testMoveGenEmptyBoard() {
        FenFormat fen = new FenFormat("8/7k/8/8/3R4/8/K7/8 w - - 0 1");
        Board board = new Board(fen);

        List<Move> moves = Piece.WHITE_ROOK.getPseudoLegalMoves(board, 54);

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

        this.assertEmptyMoveList(moves);
    }

    @Test
    public void testMoveGenOpponentPieces() {
        FenFormat fen = new FenFormat("7k/8/3n4/8/1b1R1r2/8/3q4/K7 w - - 0 1");
        Board board = new Board(fen);

        List<Move> moves = Piece.WHITE_ROOK.getPseudoLegalMoves(board, 54);

        this.assertAndRemoveExpectedMove(moves, new Move(D4, D3));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, D2));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, D5));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, D6));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, C4));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, B4));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, E4));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, F4));

        this.assertEmptyMoveList(moves);

    }

    @Test
    public void testMoveGenOwnPieces() {
        FenFormat fen = new FenFormat("7k/8/3N4/8/1B1R1R2/8/3Q4/K7 w - - 0 1");
        Board board = new Board(fen);

        List<Move> moves = Piece.WHITE_ROOK.getPseudoLegalMoves(board, 54);

        this.assertAndRemoveExpectedMove(moves, new Move(D4, D3));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, D5));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, C4));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, E4));

        this.assertEmptyMoveList(moves);

    }

}
