package ch.nostromo.tiffanys.commons.move.movegenerator;

import ch.nostromo.tiffanys.commons.BaseTest;
import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.move.Castling;
import ch.nostromo.tiffanys.commons.board.Piece;
import ch.nostromo.tiffanys.commons.formats.FenFormat;
import ch.nostromo.tiffanys.commons.move.Move;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static ch.nostromo.tiffanys.commons.board.Square.C3;
import static ch.nostromo.tiffanys.commons.board.Square.C4;
import static ch.nostromo.tiffanys.commons.board.Square.C5;
import static ch.nostromo.tiffanys.commons.board.Square.D1;
import static ch.nostromo.tiffanys.commons.board.Square.D2;
import static ch.nostromo.tiffanys.commons.board.Square.D3;
import static ch.nostromo.tiffanys.commons.board.Square.D4;
import static ch.nostromo.tiffanys.commons.board.Square.D5;
import static ch.nostromo.tiffanys.commons.board.Square.D7;
import static ch.nostromo.tiffanys.commons.board.Square.D8;
import static ch.nostromo.tiffanys.commons.board.Square.E1;
import static ch.nostromo.tiffanys.commons.board.Square.E2;
import static ch.nostromo.tiffanys.commons.board.Square.E3;
import static ch.nostromo.tiffanys.commons.board.Square.E4;
import static ch.nostromo.tiffanys.commons.board.Square.E5;
import static ch.nostromo.tiffanys.commons.board.Square.E7;
import static ch.nostromo.tiffanys.commons.board.Square.E8;
import static ch.nostromo.tiffanys.commons.board.Square.F1;
import static ch.nostromo.tiffanys.commons.board.Square.F2;
import static ch.nostromo.tiffanys.commons.board.Square.F7;
import static ch.nostromo.tiffanys.commons.board.Square.F8;

public class KingMoveGeneratorTest extends BaseTest {

    @Test
    public void testMoveGenEmptyBoard() {
        FenFormat fen = new FenFormat("8/7k/8/8/3K4/8/8/8 w - - 0 1");
        Board board = new Board(fen);

        List<Move> moves = new ArrayList<Move>(Piece.WHITE_KING.getPseudoLegalMoves(board,54));

        this.assertAndRemoveExpectedMove(moves, new Move(D4, C3));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, D3));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, E3));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, C4));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, E4));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, C5));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, D5));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, E5));

        this.assertEmptyMoveList(moves);
    }

    @Test
    public void testMoveGenOpponentPieces() {
        FenFormat fen = new FenFormat("8/7k/8/2qqq3/2qKq3/2qqq3/8/8 w - - 0 1");
        Board board = new Board(fen);

        List<Move> moves = new ArrayList<Move>(Piece.WHITE_KING.getPseudoLegalMoves(board,54));

        this.assertAndRemoveExpectedMove(moves, new Move(D4, C3));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, D3));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, E3));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, C4));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, E4));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, C5));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, D5));
        this.assertAndRemoveExpectedMove(moves, new Move(D4, E5));

        this.assertEmptyMoveList(moves);
    }

    @Test
    public void testMoveGenOwnPieces() {
        FenFormat fen = new FenFormat("8/7k/8/2QQQ3/2QKQ3/2QQQ3/8/8 b - - 0 1");
        Board board = new Board(fen);

        List<Move> moves = new ArrayList<Move>(Piece.WHITE_KING.getPseudoLegalMoves(board,54));

        this.assertEmptyMoveList(moves);
    }

    @Test
    public void testMoveGenCastlingAllAllowed() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        Board board = new Board(fen);

        List<Move> whiteMoves = new ArrayList<Move>(Piece.WHITE_KING.getPseudoLegalMoves(board,25));


        this.assertAndRemoveExpectedMove(whiteMoves, new Move(Castling.WHITE_LONG));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(Castling.WHITE_SHORT));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(E1, D1));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(E1, D2));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(E1, E2));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(E1, F2));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(E1, F1));
        this.assertEmptyMoveList(whiteMoves);

        List<Move> blackMoves = new ArrayList<Move>(Piece.BLACK_KING.getPseudoLegalMoves(board,95));

        this.assertAndRemoveExpectedMove(blackMoves, new Move(Castling.BLACK_LONG));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(Castling.BLACK_SHORT));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(E8, D8));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(E8, D7));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(E8, E7));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(E8, F7));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(E8, F8));
        this.assertEmptyMoveList(blackMoves);
    }

    @Test
    public void testMoveGenCastlingNoneAllowed() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R w - - 0 1");
        Board board = new Board(fen);

        List<Move> whiteMoves = new ArrayList<Move>(Piece.WHITE_KING.getPseudoLegalMoves(board,25));


        this.assertAndRemoveExpectedMove(whiteMoves, new Move(E1, D1));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(E1, D2));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(E1, E2));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(E1, F2));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(E1, F1));
        this.assertEmptyMoveList(whiteMoves);

        List<Move> blackMoves = new ArrayList<Move>(Piece.BLACK_KING.getPseudoLegalMoves(board,95));

        this.assertAndRemoveExpectedMove(blackMoves, new Move(E8, D8));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(E8, D7));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(E8, E7));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(E8, F7));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(E8, F8));
        this.assertEmptyMoveList(blackMoves);
    }

    @Test
    public void testMoveGenCastlingNotAllowedChessByDiagonal() {
        FenFormat fen = new FenFormat("r3k2r/8/1B1B4/8/8/1b1b4/8/R3K2R w KQkq - 0 1");
        Board board = new Board(fen);

        List<Move> whiteMoves = new ArrayList<>(Piece.WHITE_KING.getPseudoLegalMoves(board,25));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(E1, D1));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(E1, D2));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(E1, E2));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(E1, F2));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(E1, F1));
        this.assertEmptyMoveList(whiteMoves);

        List<Move> blackMoves = new ArrayList<>(Piece.BLACK_KING.getPseudoLegalMoves(board,95));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(E8, D8));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(E8, D7));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(E8, E7));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(E8, F7));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(E8, F8));
        this.assertEmptyMoveList(blackMoves);
    }

    @Test
    public void testMoveGenCastlingNotAllowedChessByHorizontal() {
        FenFormat fen = new FenFormat("r3k2r/8/2R2R2/8/8/2r2r2/8/R3K2R w KQkq - 0 1");
        Board board = new Board(fen);

        List<Move> whiteMoves = new ArrayList<Move>(Piece.WHITE_KING.getPseudoLegalMoves(board,25));

        this.assertAndRemoveExpectedMove(whiteMoves, new Move(E1, D1));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(E1, D2));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(E1, E2));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(E1, F2));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(E1, F1));
        this.assertEmptyMoveList(whiteMoves);

        List<Move> blackMoves = new ArrayList<>(Piece.BLACK_KING.getPseudoLegalMoves(board,95));

        this.assertAndRemoveExpectedMove(blackMoves, new Move(E8, D8));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(E8, D7));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(E8, E7));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(E8, F7));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(E8, F8));
        this.assertEmptyMoveList(blackMoves);
    }

    @Test
    public void testMoveGenCastlingNotAllowedChessByPawn() {
        FenFormat fen = new FenFormat("r3k2r/2P3P1/8/8/8/8/2p3p1/R3K2R w KQkq - 0 1");
        Board board = new Board(fen);

        List<Move> whiteMoves = new ArrayList<>(Piece.WHITE_KING.getPseudoLegalMoves(board,25));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(E1, D1));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(E1, D2));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(E1, E2));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(E1, F2));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(E1, F1));
        this.assertEmptyMoveList(whiteMoves);

        List<Move> blackMoves = new ArrayList<>(Piece.BLACK_KING.getPseudoLegalMoves(board,95));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(E8, D8));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(E8, D7));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(E8, E7));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(E8, F7));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(E8, F8));
        this.assertEmptyMoveList(blackMoves);
    }

    @Test
    public void testMoveGenCastlingNotAllowedChessByKnight() {
        FenFormat fen = new FenFormat("r3k2r/8/4N3/8/8/4n3/8/R3K2R w KQkq - 0 1");
        Board board = new Board(fen);

        List<Move> whiteMoves = new ArrayList<>(Piece.WHITE_KING.getPseudoLegalMoves(board,25));

        this.assertAndRemoveExpectedMove(whiteMoves, new Move(E1, D1));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(E1, D2));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(E1, E2));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(E1, F2));
        this.assertAndRemoveExpectedMove(whiteMoves, new Move(E1, F1));
        this.assertEmptyMoveList(whiteMoves);

        List<Move> blackMoves = new ArrayList<>(Piece.BLACK_KING.getPseudoLegalMoves(board,95));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(E8, D8));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(E8, D7));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(E8, E7));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(E8, F7));
        this.assertAndRemoveExpectedMove(blackMoves, new Move(E8, F8));
        this.assertEmptyMoveList(blackMoves);
    }
}
