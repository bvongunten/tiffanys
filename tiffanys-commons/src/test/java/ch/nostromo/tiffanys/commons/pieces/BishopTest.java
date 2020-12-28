package ch.nostromo.tiffanys.commons.pieces;

import ch.nostromo.tiffanys.commons.TestHelper;
import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.enums.Piece;
import ch.nostromo.tiffanys.commons.fen.FenFormat;
import ch.nostromo.tiffanys.commons.move.Move;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BishopTest extends TestHelper {

    @Test
    public void testMoveGenEmptyBoard() {
        FenFormat fen = new FenFormat("8/7k/8/8/3B4/8/K7/8 w - - 0 1");
        Board board = new Board(fen);

        List<Move> moves = new ArrayList<Move>();
        Piece.BISHOP.addPseudoLegalMoves(board, moves, 54, GameColor.WHITE);

        this.checkAndDeleteMove(moves, new Move("D4", "C3"));
        this.checkAndDeleteMove(moves, new Move("D4", "B2"));
        this.checkAndDeleteMove(moves, new Move("D4", "A1"));
        this.checkAndDeleteMove(moves, new Move("D4", "C5"));
        this.checkAndDeleteMove(moves, new Move("D4", "B6"));
        this.checkAndDeleteMove(moves, new Move("D4", "A7"));
        this.checkAndDeleteMove(moves, new Move("D4", "E3"));
        this.checkAndDeleteMove(moves, new Move("D4", "F2"));
        this.checkAndDeleteMove(moves, new Move("D4", "G1"));
        this.checkAndDeleteMove(moves, new Move("D4", "E5"));
        this.checkAndDeleteMove(moves, new Move("D4", "F6"));
        this.checkAndDeleteMove(moves, new Move("D4", "G7"));
        this.checkAndDeleteMove(moves, new Move("D4", "H8"));

        this.checkRemainingMoves(moves);
    }

    @Test
    public void testMoveGenOpponentPieces() {
        FenFormat fen = new FenFormat("8/7k/1q3n2/8/3B4/8/Kb3r2/8 w - - 0 1");
        Board board = new Board(fen);

        List<Move> moves = new ArrayList<Move>();
        Piece.BISHOP.addPseudoLegalMoves(board, moves, 54, GameColor.WHITE);

        this.checkAndDeleteMove(moves, new Move("D4", "C5"));
        this.checkAndDeleteMove(moves, new Move("D4", "B6"));
        this.checkAndDeleteMove(moves, new Move("D4", "C3"));
        this.checkAndDeleteMove(moves, new Move("D4", "B2"));
        this.checkAndDeleteMove(moves, new Move("D4", "E3"));
        this.checkAndDeleteMove(moves, new Move("D4", "F2"));
        this.checkAndDeleteMove(moves, new Move("D4", "E5"));
        this.checkAndDeleteMove(moves, new Move("D4", "F6"));

        this.checkRemainingMoves(moves);

    }

    @Test
    public void testMoveGenOwnPieces() {
        FenFormat fen = new FenFormat("8/7k/1Q3N2/8/3B4/8/KB3R2/8 b - - 0 1");
        Board board = new Board(fen);

        List<Move> moves = new ArrayList<Move>();
        Piece.BISHOP.addPseudoLegalMoves(board, moves, 54, GameColor.WHITE);

        this.checkAndDeleteMove(moves, new Move("D4", "C5"));
        this.checkAndDeleteMove(moves, new Move("D4", "C3"));
        this.checkAndDeleteMove(moves, new Move("D4", "E3"));
        this.checkAndDeleteMove(moves, new Move("D4", "E5"));

        this.checkRemainingMoves(moves);

    }

}
