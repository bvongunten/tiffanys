package ch.nostromo.tiffanys.commons.move.movegenerator;

import ch.nostromo.tiffanys.commons.BaseTest;
import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.board.Piece;
import ch.nostromo.tiffanys.commons.formats.FenFormat;
import ch.nostromo.tiffanys.commons.move.Move;
import org.junit.Test;

import java.util.List;

import static ch.nostromo.tiffanys.commons.board.Square.*;

public class BishopMoveGeneratorTest extends BaseTest {

    @Test
    public void testMoveGenEmptyBoard() {
        FenFormat fen = new FenFormat("8/7k/8/8/3B4/8/K7/8 w - - 0 1");
        Board board = new Board(fen);

        List<Move> moves = Piece.WHITE_BISHOP.getPseudoLegalMoves(board, 54);

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
        FenFormat fen = new FenFormat("8/7k/1q3n2/8/3B4/8/Kb3r2/8 w - - 0 1");
        Board board = new Board(fen);

        List<Move> moves = Piece.WHITE_BISHOP.getPseudoLegalMoves(board, 54);

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
        FenFormat fen = new FenFormat("8/7k/1Q3N2/8/3B4/8/KB3R2/8 b - - 0 1");
        Board board = new Board(fen);

        List<Move> moves = Piece.WHITE_BISHOP.getPseudoLegalMoves(board, 54);

        this.assertAndRemoveExpectedMove(moves, new Move(D4, C5));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, C3));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, E3));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, E5));

        this.assertEmptyMoveList(moves);

    }

}
