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

import static ch.nostromo.tiffanys.commons.enums.Coordinates.A1;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.A4;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.A7;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.B2;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.B4;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.B6;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.C3;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.C4;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.C5;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.D1;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.D2;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.D3;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.D4;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.D5;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.D6;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.D7;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.D8;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.E3;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.E4;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.E5;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.F2;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.F4;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.F6;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.G1;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.G4;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.G7;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.H4;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.H8;

public class QueenTest extends TestHelper {

    @Test
    public void testMoveGenEmptyBoard() {
        FenFormat fen = new FenFormat("8/7k/8/8/3Q4/8/K7/8 w - - 0 1");
        Board board = new Board(fen);

        List<Move> moves = new ArrayList<Move>();
        Piece.QUEEN.addPseudoLegalMoves(board, moves, 54, GameColor.WHITE);

        this.checkAndDeleteMove(moves, new Move(D4, D3));
        this.checkAndDeleteMove(moves, new Move(D4, D2));
        this.checkAndDeleteMove(moves, new Move(D4, D1));
        this.checkAndDeleteMove(moves, new Move(D4, D5));
        this.checkAndDeleteMove(moves, new Move(D4, D6));
        this.checkAndDeleteMove(moves, new Move(D4, D7));
        this.checkAndDeleteMove(moves, new Move(D4, D8));
        this.checkAndDeleteMove(moves, new Move(D4, C4));
        this.checkAndDeleteMove(moves, new Move(D4, B4));
        this.checkAndDeleteMove(moves, new Move(D4, A4));
        this.checkAndDeleteMove(moves, new Move(D4, E4));
        this.checkAndDeleteMove(moves, new Move(D4, F4));
        this.checkAndDeleteMove(moves, new Move(D4, G4));
        this.checkAndDeleteMove(moves, new Move(D4, H4));

        this.checkAndDeleteMove(moves, new Move(D4, C3));
        this.checkAndDeleteMove(moves, new Move(D4, B2));
        this.checkAndDeleteMove(moves, new Move(D4, A1));
        this.checkAndDeleteMove(moves, new Move(D4, C5));
        this.checkAndDeleteMove(moves, new Move(D4, B6));
        this.checkAndDeleteMove(moves, new Move(D4, A7));
        this.checkAndDeleteMove(moves, new Move(D4, E3));
        this.checkAndDeleteMove(moves, new Move(D4, F2));
        this.checkAndDeleteMove(moves, new Move(D4, G1));
        this.checkAndDeleteMove(moves, new Move(D4, E5));
        this.checkAndDeleteMove(moves, new Move(D4, F6));
        this.checkAndDeleteMove(moves, new Move(D4, G7));
        this.checkAndDeleteMove(moves, new Move(D4, H8));

        this.checkRemainingMoves(moves);
    }

    @Test
    public void testMoveGenOpponentPieces() {
        FenFormat fen = new FenFormat("7k/8/1n1n1q2/8/1b1Q1r2/8/1r1q1b2/K7 w - - 0 1");
        Board board = new Board(fen);

        List<Move> moves = new ArrayList<Move>();
        Piece.QUEEN.addPseudoLegalMoves(board, moves, 54, GameColor.WHITE);

        this.checkAndDeleteMove(moves, new Move(D4, D3));
        this.checkAndDeleteMove(moves, new Move(D4, D2));
        this.checkAndDeleteMove(moves, new Move(D4, D5));
        this.checkAndDeleteMove(moves, new Move(D4, D6));
        this.checkAndDeleteMove(moves, new Move(D4, C4));
        this.checkAndDeleteMove(moves, new Move(D4, B4));
        this.checkAndDeleteMove(moves, new Move(D4, E4));
        this.checkAndDeleteMove(moves, new Move(D4, F4));

        this.checkAndDeleteMove(moves, new Move(D4, C5));
        this.checkAndDeleteMove(moves, new Move(D4, B6));
        this.checkAndDeleteMove(moves, new Move(D4, C3));
        this.checkAndDeleteMove(moves, new Move(D4, B2));
        this.checkAndDeleteMove(moves, new Move(D4, E3));
        this.checkAndDeleteMove(moves, new Move(D4, F2));
        this.checkAndDeleteMove(moves, new Move(D4, E5));
        this.checkAndDeleteMove(moves, new Move(D4, F6));

        this.checkRemainingMoves(moves);

    }

    @Test
    public void testMoveGenOwnPieces() {
        FenFormat fen = new FenFormat("8/7k/1Q1N1N2/8/1Q1Q1R2/8/KB1B1R2/8 b - - 0 1");
        Board board = new Board(fen);

        List<Move> moves = new ArrayList<Move>();
        Piece.QUEEN.addPseudoLegalMoves(board, moves, 54, GameColor.WHITE);

        this.checkAndDeleteMove(moves, new Move(D4, C5));
        this.checkAndDeleteMove(moves, new Move(D4, C3));
        this.checkAndDeleteMove(moves, new Move(D4, E3));
        this.checkAndDeleteMove(moves, new Move(D4, E5));

        this.checkAndDeleteMove(moves, new Move(D4, D3));
        this.checkAndDeleteMove(moves, new Move(D4, D5));
        this.checkAndDeleteMove(moves, new Move(D4, C4));
        this.checkAndDeleteMove(moves, new Move(D4, E4));

        this.checkRemainingMoves(moves);

    }

}
