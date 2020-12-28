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

public class KnightTest extends TestHelper {

    @Test
    public void testMoveGenEmptyBoard() {
        FenFormat fen = new FenFormat("8/7k/8/8/3N4/8/K7/8 w - - 0 1");
        Board board = new Board(fen);

        List<Move> moves = new ArrayList<Move>();
        Piece.KNIGHT.addPseudoLegalMoves(board, moves, 54, GameColor.WHITE);

        this.checkAndDeleteMove(moves, new Move("D4", "B3"));
        this.checkAndDeleteMove(moves, new Move("D4", "C2"));
        this.checkAndDeleteMove(moves, new Move("D4", "B5"));
        this.checkAndDeleteMove(moves, new Move("D4", "C6"));
        this.checkAndDeleteMove(moves, new Move("D4", "E6"));
        this.checkAndDeleteMove(moves, new Move("D4", "F5"));
        this.checkAndDeleteMove(moves, new Move("D4", "F3"));
        this.checkAndDeleteMove(moves, new Move("D4", "E2"));

        this.checkRemainingMoves(moves);
    }

    @Test
    public void testMoveGenOpponentPieces() {
        FenFormat fen = new FenFormat("8/7k/2b1b3/1b3b2/3N4/1b3b2/K1b1b3/8 w - - 0 1");
        Board board = new Board(fen);

        List<Move> moves = new ArrayList<Move>();
        Piece.KNIGHT.addPseudoLegalMoves(board, moves, 54, GameColor.WHITE);

        this.checkAndDeleteMove(moves, new Move("D4", "B3"));
        this.checkAndDeleteMove(moves, new Move("D4", "C2"));
        this.checkAndDeleteMove(moves, new Move("D4", "B5"));
        this.checkAndDeleteMove(moves, new Move("D4", "C6"));
        this.checkAndDeleteMove(moves, new Move("D4", "E6"));
        this.checkAndDeleteMove(moves, new Move("D4", "F5"));
        this.checkAndDeleteMove(moves, new Move("D4", "F3"));
        this.checkAndDeleteMove(moves, new Move("D4", "E2"));

        this.checkRemainingMoves(moves);

    }

    @Test
    public void testMoveGenOwnPieces() {
        FenFormat fen = new FenFormat("8/7k/2B1B3/1B3B2/3N4/1B3B2/K1B1B3/8 b - - 0 1");
        Board board = new Board(fen);

        List<Move> moves = new ArrayList<Move>();
        Piece.KNIGHT.addPseudoLegalMoves(board, moves, 54, GameColor.WHITE);

        this.checkRemainingMoves(moves);
    }

}
