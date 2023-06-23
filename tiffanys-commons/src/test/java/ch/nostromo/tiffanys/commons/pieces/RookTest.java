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

public class RookTest extends TestHelper {

    @Test
    public void testMoveGenEmptyBoard() {
        FenFormat fen = new FenFormat("8/7k/8/8/3R4/8/K7/8 w - - 0 1");
        Board board = new Board(fen);

        List<Move> moves = new ArrayList<Move>();
        Piece.ROOK.addPseudoLegalMoves(board, moves, 54, GameColor.WHITE);

        this.checkAndDeleteMove(moves, new Move("D4", "D3"));
        this.checkAndDeleteMove(moves, new Move("D4", "D2"));
        this.checkAndDeleteMove(moves, new Move("D4", "D1"));
        this.checkAndDeleteMove(moves, new Move("D4", "D5"));
        this.checkAndDeleteMove(moves, new Move("D4", "D6"));
        this.checkAndDeleteMove(moves, new Move("D4", "D7"));
        this.checkAndDeleteMove(moves, new Move("D4", "D8"));
        this.checkAndDeleteMove(moves, new Move("D4", "C4"));
        this.checkAndDeleteMove(moves, new Move("D4", "B4"));
        this.checkAndDeleteMove(moves, new Move("D4", "A4"));
        this.checkAndDeleteMove(moves, new Move("D4", "E4"));
        this.checkAndDeleteMove(moves, new Move("D4", "F4"));
        this.checkAndDeleteMove(moves, new Move("D4", "G4"));
        this.checkAndDeleteMove(moves, new Move("D4", "H4"));

        this.checkRemainingMoves(moves);
    }

    @Test
    public void testMoveGenOpponentPieces() {
        FenFormat fen = new FenFormat("7k/8/3n4/8/1b1R1r2/8/3q4/K7 w - - 0 1");
        Board board = new Board(fen);

        List<Move> moves = new ArrayList<Move>();
        Piece.ROOK.addPseudoLegalMoves(board, moves, 54, GameColor.WHITE);

        this.checkAndDeleteMove(moves, new Move("D4", "D3"));
        this.checkAndDeleteMove(moves, new Move("D4", "D2"));
        this.checkAndDeleteMove(moves, new Move("D4", "D5"));
        this.checkAndDeleteMove(moves, new Move("D4", "D6"));
        this.checkAndDeleteMove(moves, new Move("D4", "C4"));
        this.checkAndDeleteMove(moves, new Move("D4", "B4"));
        this.checkAndDeleteMove(moves, new Move("D4", "E4"));
        this.checkAndDeleteMove(moves, new Move("D4", "F4"));

        this.checkRemainingMoves(moves);

    }

    @Test
    public void testMoveGenOwnPieces() {
        FenFormat fen = new FenFormat("7k/8/3N4/8/1B1R1R2/8/3Q4/K7 w - - 0 1");
        Board board = new Board(fen);

        List<Move> moves = new ArrayList<Move>();
        Piece.ROOK.addPseudoLegalMoves(board, moves, 54, GameColor.WHITE);

        this.checkAndDeleteMove(moves, new Move("D4", "D3"));
        this.checkAndDeleteMove(moves, new Move("D4", "D5"));
        this.checkAndDeleteMove(moves, new Move("D4", "C4"));
        this.checkAndDeleteMove(moves, new Move("D4", "E4"));

        this.checkRemainingMoves(moves);

    }

}
