package ch.nostromo.tiffanys.commons.move.movegenerator;

import ch.nostromo.tiffanys.commons.BaseTest;
import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.board.Piece;
import ch.nostromo.tiffanys.commons.formats.FenFormat;
import ch.nostromo.tiffanys.commons.move.Move;
import org.junit.Test;

import java.util.List;

import static ch.nostromo.tiffanys.commons.board.Square.B3;
import static ch.nostromo.tiffanys.commons.board.Square.B4;
import static ch.nostromo.tiffanys.commons.board.Square.B6;
import static ch.nostromo.tiffanys.commons.board.Square.C3;
import static ch.nostromo.tiffanys.commons.board.Square.C5;
import static ch.nostromo.tiffanys.commons.board.Square.C6;
import static ch.nostromo.tiffanys.commons.board.Square.D2;
import static ch.nostromo.tiffanys.commons.board.Square.D3;
import static ch.nostromo.tiffanys.commons.board.Square.D4;
import static ch.nostromo.tiffanys.commons.board.Square.D5;
import static ch.nostromo.tiffanys.commons.board.Square.D6;
import static ch.nostromo.tiffanys.commons.board.Square.D7;
import static ch.nostromo.tiffanys.commons.board.Square.E3;
import static ch.nostromo.tiffanys.commons.board.Square.E4;
import static ch.nostromo.tiffanys.commons.board.Square.E5;
import static ch.nostromo.tiffanys.commons.board.Square.E6;
import static ch.nostromo.tiffanys.commons.board.Square.F1;
import static ch.nostromo.tiffanys.commons.board.Square.F8;
import static ch.nostromo.tiffanys.commons.board.Square.G1;
import static ch.nostromo.tiffanys.commons.board.Square.G2;
import static ch.nostromo.tiffanys.commons.board.Square.G7;
import static ch.nostromo.tiffanys.commons.board.Square.G8;

public class PawnMoveGeneratorTest extends BaseTest {

    @Test
    public void testMoveGenStartLines() {
        FenFormat fen = new FenFormat("3k4/3p4/4p3/8/8/4P3/3P4/3K4 b - - 0 7");
        Board board = new Board(fen);

        List<Move> whiteMoves = Piece.WHITE_PAWN.getPseudoLegalMoves(board, 34);

        this.assertAndRemoveExpectedMove(whiteMoves, new Move(D2, D3));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(D2, D4));
        this.assertEmptyMoveList(whiteMoves);

        List<Move> blackMoves = Piece.BLACK_PAWN.getPseudoLegalMoves(board, 84);

        this.assertAndRemoveExpectedMove(blackMoves, new Move(D7, D6));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(D7, D5));
        this.assertEmptyMoveList(blackMoves);
    }

    @Test
    public void testOneFieldOpening() {
        FenFormat fen = new FenFormat(" 3k4/3p4/8/3r4/3R4/8/3P4/3K4 b - - 0 7");
        Board board = new Board(fen);

        List<Move> whiteMoves = Piece.WHITE_PAWN.getPseudoLegalMoves(board, 34);

        this.assertAndRemoveExpectedMove(whiteMoves, new Move(D2, D3));
        this.assertEmptyMoveList(whiteMoves);

        List<Move> blackMoves = Piece.BLACK_PAWN.getPseudoLegalMoves(board, 84);

        this.assertAndRemoveExpectedMove(blackMoves, new Move(D7, D6));
        this.assertEmptyMoveList(blackMoves);
    }




    @Test
    public void testMoveGenStartLinesBlocked() {
        FenFormat fen = new FenFormat("3k4/3p4/3P4/6p1/6P1/3p4/3P4/3K4 b - - 0 7");
        Board board = new Board(fen);

        List<Move> whiteMoves = Piece.WHITE_PAWN.getPseudoLegalMoves(board, 34);
        this.assertEmptyMoveList(whiteMoves);

        List<Move> blackMoves = Piece.BLACK_PAWN.getPseudoLegalMoves(board, 84);
        this.assertEmptyMoveList(blackMoves);
    }

    @Test
    public void testMoveGenInField() {
        FenFormat fen = new FenFormat("3k4/3p4/4p3/8/8/4P3/3P4/3K4 b - - 0 7");
        Board board = new Board(fen);

        List<Move> whiteMoves = Piece.WHITE_PAWN.getPseudoLegalMoves(board, 45);

        this.assertAndRemoveExpectedMove(whiteMoves, new Move(E3, E4));
        this.assertEmptyMoveList(whiteMoves);

        List<Move> blackMoves = Piece.BLACK_PAWN.getPseudoLegalMoves(board, 75);

        this.assertAndRemoveExpectedMove(blackMoves, new Move(E6, E5));
        this.assertEmptyMoveList(blackMoves);
    }

    @Test
    public void testMoveGenInFieldBlocked() {
        FenFormat fen = new FenFormat("3k4/3p4/3P4/6p1/6P1/3p4/3P4/3K4 b - - 0 7");
        Board board = new Board(fen);

        List<Move> whiteMoves = Piece.WHITE_PAWN.getPseudoLegalMoves(board, 57);

        this.assertEmptyMoveList(whiteMoves);

        List<Move> blackMoves = Piece.BLACK_PAWN.getPseudoLegalMoves(board, 67);

        this.assertEmptyMoveList(blackMoves);
    }

    @Test
    public void testMoveGenHitMoves() {
        FenFormat fen = new FenFormat("3k4/3p4/2PpP3/8/8/2pPp3/3P4/3K4 b - - 0 7");
        Board board = new Board(fen);

        List<Move> whiteMoves = Piece.WHITE_PAWN.getPseudoLegalMoves(board, 34);

        this.assertAndRemoveExpectedMove(whiteMoves, new Move(D2, C3));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(D2, E3));
        this.assertEmptyMoveList(whiteMoves);

        List<Move> blackMoves = Piece.BLACK_PAWN.getPseudoLegalMoves(board, 84);

        this.assertAndRemoveExpectedMove(blackMoves, new Move(D7, C6));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(D7, E6));
        this.assertEmptyMoveList(blackMoves);
    }

    @Test
    public void testPromotions() {
        FenFormat fen = new FenFormat("3k1q2/6P1/8/8/8/8/6p1/3K1Q2 w - - 0 1");
        Board board = new Board(fen);

        List<Move> whiteMoves = Piece.WHITE_PAWN.getPseudoLegalMoves(board, 87);

        this.assertAndRemoveExpectedMove(whiteMoves, new Move(G7, G8, Piece.WHITE_BISHOP));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(G7, G8, Piece.WHITE_KNIGHT));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(G7, G8, Piece.WHITE_ROOK));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(G7, G8, Piece.WHITE_QUEEN));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(G7, F8, Piece.WHITE_BISHOP));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(G7, F8, Piece.WHITE_KNIGHT));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(G7, F8, Piece.WHITE_ROOK));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(G7, F8, Piece.WHITE_QUEEN));
        this.assertEmptyMoveList(whiteMoves);

        List<Move> blackMoves = Piece.BLACK_PAWN.getPseudoLegalMoves(board, 37);

        this.assertAndRemoveExpectedMove(blackMoves, new Move(G2, G1, Piece.BLACK_BISHOP));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(G2, G1, Piece.BLACK_KNIGHT));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(G2, G1, Piece.BLACK_ROOK));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(G2, G1, Piece.BLACK_QUEEN));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(G2, F1, Piece.BLACK_BISHOP));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(G2, F1, Piece.BLACK_KNIGHT));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(G2, F1, Piece.BLACK_ROOK));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(G2, F1, Piece.BLACK_QUEEN));
        this.assertEmptyMoveList(blackMoves);
    }

    @Test
    public void testEnPassantWhite() {
        FenFormat fen = new FenFormat("3k4/8/8/1pP5/8/8/8/3K4 w - b6 0 1");
        Board board = new Board(fen);

        List<Move> whiteMoves = Piece.WHITE_PAWN.getPseudoLegalMoves(board, 63);

        this.assertAndRemoveExpectedMove(whiteMoves, new Move(C5, C6));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(C5, B6));
        this.assertEmptyMoveList(whiteMoves);
    }

    @Test
    public void testEnPassantBlack() {
        FenFormat fen = new FenFormat("3k4/8/8/8/1pP5/8/8/3K4 b - c3 0 1");
        Board board = new Board(fen);

        List<Move> blackMoves = Piece.BLACK_PAWN.getPseudoLegalMoves(board, 52);

        this.assertAndRemoveExpectedMove(blackMoves, new Move(B4, B3));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(B4, C3));
        this.assertEmptyMoveList(blackMoves);
    }

}
