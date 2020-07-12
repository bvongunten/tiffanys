package ch.nostromo.tiffanys.commons.pieces;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ch.nostromo.tiffanys.commons.TestHelper;
import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.enums.Castling;
import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.enums.Piece;
import ch.nostromo.tiffanys.commons.fen.FenFormat;
import ch.nostromo.tiffanys.commons.move.Move;

public class KingTest extends TestHelper {

    @Test
    public void testMoveGenEmptyBoard() {
        FenFormat fen = new FenFormat("8/7k/8/8/3K4/8/8/8 w - - 0 1");
        Board board = new Board(fen);

        List<Move> moves = new ArrayList<Move>();
        Piece.KING.addPseudoLegalMoves(board, moves, 54, GameColor.WHITE);

        this.checkAndDeleteMove(moves, new Move("D4", "C3"));
        this.checkAndDeleteMove(moves, new Move("D4", "D3"));
        this.checkAndDeleteMove(moves, new Move("D4", "E3"));
        this.checkAndDeleteMove(moves, new Move("D4", "C4"));
        this.checkAndDeleteMove(moves, new Move("D4", "E4"));
        this.checkAndDeleteMove(moves, new Move("D4", "C5"));
        this.checkAndDeleteMove(moves, new Move("D4", "D5"));
        this.checkAndDeleteMove(moves, new Move("D4", "E5"));

        this.checkRemainingMoves(moves);
    }

    @Test
    public void testMoveGenOpponentPieces() {
        FenFormat fen = new FenFormat("8/7k/8/2qqq3/2qKq3/2qqq3/8/8 w - - 0 1");
        Board board = new Board(fen);

        List<Move> moves = new ArrayList<Move>();
        Piece.KING.addPseudoLegalMoves(board, moves, 54, GameColor.WHITE);

        this.checkAndDeleteMove(moves, new Move("D4", "C3"));
        this.checkAndDeleteMove(moves, new Move("D4", "D3"));
        this.checkAndDeleteMove(moves, new Move("D4", "E3"));
        this.checkAndDeleteMove(moves, new Move("D4", "C4"));
        this.checkAndDeleteMove(moves, new Move("D4", "E4"));
        this.checkAndDeleteMove(moves, new Move("D4", "C5"));
        this.checkAndDeleteMove(moves, new Move("D4", "D5"));
        this.checkAndDeleteMove(moves, new Move("D4", "E5"));

        this.checkRemainingMoves(moves);
    }

    @Test
    public void testMoveGenOwnPieces() {
        FenFormat fen = new FenFormat("8/7k/8/2QQQ3/2QKQ3/2QQQ3/8/8 b - - 0 1");
        Board board = new Board(fen);

        List<Move> moves = new ArrayList<Move>();
        Piece.KING.addPseudoLegalMoves(board, moves, 54, GameColor.WHITE);
        this.checkRemainingMoves(moves);
    }

    @Test
    public void testMoveGenCastlingAllAllowed() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        Board board = new Board(fen);

        List<Move> whiteMoves = new ArrayList<Move>();
        Piece.KING.addPseudoLegalMoves(board, whiteMoves, 25, GameColor.WHITE);
        this.checkAndDeleteMove(whiteMoves, new Move(Castling.WHITE_LONG));
        this.checkAndDeleteMove(whiteMoves, new Move(Castling.WHITE_SHORT));
        this.checkAndDeleteMove(whiteMoves, new Move("E1", "D1"));
        this.checkAndDeleteMove(whiteMoves, new Move("E1", "D2"));
        this.checkAndDeleteMove(whiteMoves, new Move("E1", "E2"));
        this.checkAndDeleteMove(whiteMoves, new Move("E1", "F2"));
        this.checkAndDeleteMove(whiteMoves, new Move("E1", "F1"));
        this.checkRemainingMoves(whiteMoves);

        List<Move> blackMoves = new ArrayList<Move>();
        Piece.KING.addPseudoLegalMoves(board, blackMoves, 95, GameColor.BLACK);
        this.checkAndDeleteMove(blackMoves, new Move(Castling.BLACK_LONG));
        this.checkAndDeleteMove(blackMoves, new Move(Castling.BLACK_SHORT));
        this.checkAndDeleteMove(blackMoves, new Move("E8", "D8"));
        this.checkAndDeleteMove(blackMoves, new Move("E8", "D7"));
        this.checkAndDeleteMove(blackMoves, new Move("E8", "E7"));
        this.checkAndDeleteMove(blackMoves, new Move("E8", "F7"));
        this.checkAndDeleteMove(blackMoves, new Move("E8", "F8"));
        this.checkRemainingMoves(blackMoves);
    }

    @Test
    public void testMoveGenCastlingNoneAllowed() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R w - - 0 1");
        Board board = new Board(fen);

        List<Move> whiteMoves = new ArrayList<Move>();
        Piece.KING.addPseudoLegalMoves(board, whiteMoves, 25, GameColor.WHITE);
        this.checkAndDeleteMove(whiteMoves, new Move("E1", "D1"));
        this.checkAndDeleteMove(whiteMoves, new Move("E1", "D2"));
        this.checkAndDeleteMove(whiteMoves, new Move("E1", "E2"));
        this.checkAndDeleteMove(whiteMoves, new Move("E1", "F2"));
        this.checkAndDeleteMove(whiteMoves, new Move("E1", "F1"));
        this.checkRemainingMoves(whiteMoves);

        List<Move> blackMoves = new ArrayList<Move>();
        Piece.KING.addPseudoLegalMoves(board, blackMoves, 95, GameColor.BLACK);
        this.checkAndDeleteMove(blackMoves, new Move("E8", "D8"));
        this.checkAndDeleteMove(blackMoves, new Move("E8", "D7"));
        this.checkAndDeleteMove(blackMoves, new Move("E8", "E7"));
        this.checkAndDeleteMove(blackMoves, new Move("E8", "F7"));
        this.checkAndDeleteMove(blackMoves, new Move("E8", "F8"));
        this.checkRemainingMoves(blackMoves);
    }

    @Test
    public void testMoveGenCastlingNotAllowedChessByDiagonal() {
        FenFormat fen = new FenFormat("r3k2r/8/1B1B4/8/8/1b1b4/8/R3K2R w KQkq - 0 1");
        Board board = new Board(fen);
        List<Move> whiteMoves = new ArrayList<Move>();

        Piece.KING.addPseudoLegalMoves(board, whiteMoves, 25, GameColor.WHITE);
        this.checkAndDeleteMove(whiteMoves, new Move("E1", "D1"));
        this.checkAndDeleteMove(whiteMoves, new Move("E1", "D2"));
        this.checkAndDeleteMove(whiteMoves, new Move("E1", "E2"));
        this.checkAndDeleteMove(whiteMoves, new Move("E1", "F2"));
        this.checkAndDeleteMove(whiteMoves, new Move("E1", "F1"));
        this.checkRemainingMoves(whiteMoves);

        List<Move> blackMoves = new ArrayList<Move>();

        Piece.KING.addPseudoLegalMoves(board, blackMoves, 95, GameColor.BLACK);
        this.checkAndDeleteMove(blackMoves, new Move("E8", "D8"));
        this.checkAndDeleteMove(blackMoves, new Move("E8", "D7"));
        this.checkAndDeleteMove(blackMoves, new Move("E8", "E7"));
        this.checkAndDeleteMove(blackMoves, new Move("E8", "F7"));
        this.checkAndDeleteMove(blackMoves, new Move("E8", "F8"));
        this.checkRemainingMoves(blackMoves);
    }

    @Test
    public void testMoveGenCastlingNotAllowedChessByHorizontal() {
        FenFormat fen = new FenFormat("r3k2r/8/2R2R2/8/8/2r2r2/8/R3K2R w KQkq - 0 1");
        Board board = new Board(fen);

        List<Move> whiteMoves = new ArrayList<Move>();
        Piece.KING.addPseudoLegalMoves(board, whiteMoves, 25, GameColor.WHITE);
        this.checkAndDeleteMove(whiteMoves, new Move("E1", "D1"));
        this.checkAndDeleteMove(whiteMoves, new Move("E1", "D2"));
        this.checkAndDeleteMove(whiteMoves, new Move("E1", "E2"));
        this.checkAndDeleteMove(whiteMoves, new Move("E1", "F2"));
        this.checkAndDeleteMove(whiteMoves, new Move("E1", "F1"));
        this.checkRemainingMoves(whiteMoves);

        List<Move> blackMoves = new ArrayList<Move>();
        Piece.KING.addPseudoLegalMoves(board, blackMoves, 95, GameColor.BLACK);
        this.checkAndDeleteMove(blackMoves, new Move("E8", "D8"));
        this.checkAndDeleteMove(blackMoves, new Move("E8", "D7"));
        this.checkAndDeleteMove(blackMoves, new Move("E8", "E7"));
        this.checkAndDeleteMove(blackMoves, new Move("E8", "F7"));
        this.checkAndDeleteMove(blackMoves, new Move("E8", "F8"));
        this.checkRemainingMoves(blackMoves);
    }

    @Test
    public void testMoveGenCastlingNotAllowedChessByPawn() {
        FenFormat fen = new FenFormat("r3k2r/2P3P1/8/8/8/8/2p3p1/R3K2R w KQkq - 0 1");
        Board board = new Board(fen);
        List<Move> whiteMoves = new ArrayList<Move>();

        Piece.KING.addPseudoLegalMoves(board, whiteMoves, 25, GameColor.WHITE);
        this.checkAndDeleteMove(whiteMoves, new Move("E1", "D1"));
        this.checkAndDeleteMove(whiteMoves, new Move("E1", "D2"));
        this.checkAndDeleteMove(whiteMoves, new Move("E1", "E2"));
        this.checkAndDeleteMove(whiteMoves, new Move("E1", "F2"));
        this.checkAndDeleteMove(whiteMoves, new Move("E1", "F1"));
        this.checkRemainingMoves(whiteMoves);

        List<Move> blackMoves = new ArrayList<Move>();
        Piece.KING.addPseudoLegalMoves(board, blackMoves, 95, GameColor.BLACK);
        this.checkAndDeleteMove(blackMoves, new Move("E8", "D8"));
        this.checkAndDeleteMove(blackMoves, new Move("E8", "D7"));
        this.checkAndDeleteMove(blackMoves, new Move("E8", "E7"));
        this.checkAndDeleteMove(blackMoves, new Move("E8", "F7"));
        this.checkAndDeleteMove(blackMoves, new Move("E8", "F8"));
        this.checkRemainingMoves(blackMoves);
    }

    @Test
    public void testMoveGenCastlingNotAllowedChessByKnight() {
        FenFormat fen = new FenFormat("r3k2r/8/4N3/8/8/4n3/8/R3K2R w KQkq - 0 1");
        Board board = new Board(fen);
        List<Move> whiteMoves = new ArrayList<Move>();

        Piece.KING.addPseudoLegalMoves(board, whiteMoves, 25, GameColor.WHITE);
        this.checkAndDeleteMove(whiteMoves, new Move("E1", "D1"));
        this.checkAndDeleteMove(whiteMoves, new Move("E1", "D2"));
        this.checkAndDeleteMove(whiteMoves, new Move("E1", "E2"));
        this.checkAndDeleteMove(whiteMoves, new Move("E1", "F2"));
        this.checkAndDeleteMove(whiteMoves, new Move("E1", "F1"));
        this.checkRemainingMoves(whiteMoves);

        List<Move> blackMoves = new ArrayList<Move>();
        Piece.KING.addPseudoLegalMoves(board, blackMoves, 95, GameColor.BLACK);
        this.checkAndDeleteMove(blackMoves, new Move("E8", "D8"));
        this.checkAndDeleteMove(blackMoves, new Move("E8", "D7"));
        this.checkAndDeleteMove(blackMoves, new Move("E8", "E7"));
        this.checkAndDeleteMove(blackMoves, new Move("E8", "F7"));
        this.checkAndDeleteMove(blackMoves, new Move("E8", "F8"));
        this.checkRemainingMoves(blackMoves);
    }
}
