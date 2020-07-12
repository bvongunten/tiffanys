package ch.nostromo.tiffanys.commons.pieces;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ch.nostromo.tiffanys.commons.TestHelper;
import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.enums.Piece;
import ch.nostromo.tiffanys.commons.fen.FenFormat;
import ch.nostromo.tiffanys.commons.move.Move;

public class PawnTest extends TestHelper {

    @Test
    public void testMoveGenStartLines() {
        FenFormat fen = new FenFormat("3k4/3p4/4p3/8/8/4P3/3P4/3K4 b - - 0 7");
        Board board = new Board(fen);

        List<Move> whiteMoves = new ArrayList<Move>();
        Piece.PAWN.addPseudoLegalMoves(board, whiteMoves, 34, GameColor.WHITE);
        this.checkAndDeleteMove(whiteMoves, new Move("D2", "D3"));
        this.checkAndDeleteMove(whiteMoves, new Move("D2", "D4"));
        this.checkRemainingMoves(whiteMoves);

        List<Move> blackMoves = new ArrayList<Move>();
        Piece.PAWN.addPseudoLegalMoves(board, blackMoves, 84, GameColor.BLACK);
        this.checkAndDeleteMove(blackMoves, new Move("D7", "D6"));
        this.checkAndDeleteMove(blackMoves, new Move("D7", "D5"));
        this.checkRemainingMoves(blackMoves);
    }

    @Test
    public void testMoveGenStartLinesBlocked() {
        FenFormat fen = new FenFormat("3k4/3p4/3P4/6p1/6P1/3p4/3P4/3K4 b - - 0 7");
        Board board = new Board(fen);

        List<Move> whiteMoves = new ArrayList<Move>();
        Piece.PAWN.addPseudoLegalMoves(board, whiteMoves, 34, GameColor.WHITE);
        this.checkRemainingMoves(whiteMoves);

        List<Move> blackMoves = new ArrayList<Move>();
        Piece.PAWN.addPseudoLegalMoves(board, blackMoves, 84, GameColor.BLACK);
        this.checkRemainingMoves(blackMoves);
    }

    @Test
    public void testMoveGenInField() {
        FenFormat fen = new FenFormat("3k4/3p4/4p3/8/8/4P3/3P4/3K4 b - - 0 7");
        Board board = new Board(fen);

        List<Move> whiteMoves = new ArrayList<Move>();
        Piece.PAWN.addPseudoLegalMoves(board, whiteMoves, 45, GameColor.WHITE);
        this.checkAndDeleteMove(whiteMoves, new Move("E3", "E4"));
        this.checkRemainingMoves(whiteMoves);

        List<Move> blackMoves = new ArrayList<Move>();
        Piece.PAWN.addPseudoLegalMoves(board, blackMoves, 75, GameColor.BLACK);
        this.checkAndDeleteMove(blackMoves, new Move("E6", "E5"));
        this.checkRemainingMoves(blackMoves);
    }

    @Test
    public void testMoveGenInFieldBlocked() {
        FenFormat fen = new FenFormat("3k4/3p4/3P4/6p1/6P1/3p4/3P4/3K4 b - - 0 7");
        Board board = new Board(fen);

        List<Move> whiteMoves = new ArrayList<Move>();
        Piece.PAWN.addPseudoLegalMoves(board, whiteMoves, 57, GameColor.WHITE);
        this.checkRemainingMoves(whiteMoves);

        List<Move> blackMoves = new ArrayList<Move>();
        Piece.PAWN.addPseudoLegalMoves(board, blackMoves, 67, GameColor.BLACK);
        this.checkRemainingMoves(blackMoves);
    }

    @Test
    public void testMoveGenHitMoves() {
        FenFormat fen = new FenFormat("3k4/3p4/2PpP3/8/8/2pPp3/3P4/3K4 b - - 0 7");
        Board board = new Board(fen);

        List<Move> whiteMoves = new ArrayList<Move>();
        Piece.PAWN.addPseudoLegalMoves(board, whiteMoves, 34, GameColor.WHITE);
        this.checkAndDeleteMove(whiteMoves, new Move("D2", "C3"));
        this.checkAndDeleteMove(whiteMoves, new Move("D2", "E3"));
        this.checkRemainingMoves(whiteMoves);

        List<Move> blackMoves = new ArrayList<Move>();
        Piece.PAWN.addPseudoLegalMoves(board, blackMoves, 84, GameColor.BLACK);
        this.checkAndDeleteMove(blackMoves, new Move("D7", "C6"));
        this.checkAndDeleteMove(blackMoves, new Move("D7", "E6"));
        this.checkRemainingMoves(blackMoves);
    }

    @Test
    public void testPromotions() {
        FenFormat fen = new FenFormat("3k1q2/6P1/8/8/8/8/6p1/3K1Q2 w - - 0 1");
        Board board = new Board(fen);

        List<Move> whiteMoves = new ArrayList<Move>();
        Piece.PAWN.addPseudoLegalMoves(board, whiteMoves, 87, GameColor.WHITE);
        this.checkAndDeleteMove(whiteMoves, new Move("G7", "G8", Piece.BISHOP));
        this.checkAndDeleteMove(whiteMoves, new Move("G7", "G8", Piece.KNIGHT));
        this.checkAndDeleteMove(whiteMoves, new Move("G7", "G8", Piece.ROOK));
        this.checkAndDeleteMove(whiteMoves, new Move("G7", "G8", Piece.QUEEN));
        this.checkAndDeleteMove(whiteMoves, new Move("G7", "F8", Piece.BISHOP));
        this.checkAndDeleteMove(whiteMoves, new Move("G7", "F8", Piece.KNIGHT));
        this.checkAndDeleteMove(whiteMoves, new Move("G7", "F8", Piece.ROOK));
        this.checkAndDeleteMove(whiteMoves, new Move("G7", "F8", Piece.QUEEN));
        this.checkRemainingMoves(whiteMoves);

        List<Move> blackMoves = new ArrayList<Move>();
        Piece.PAWN.addPseudoLegalMoves(board, blackMoves, 37, GameColor.BLACK);
        this.checkAndDeleteMove(blackMoves, new Move("G2", "G1", Piece.BISHOP));
        this.checkAndDeleteMove(blackMoves, new Move("G2", "G1", Piece.KNIGHT));
        this.checkAndDeleteMove(blackMoves, new Move("G2", "G1", Piece.ROOK));
        this.checkAndDeleteMove(blackMoves, new Move("G2", "G1", Piece.QUEEN));
        this.checkAndDeleteMove(blackMoves, new Move("G2", "F1", Piece.BISHOP));
        this.checkAndDeleteMove(blackMoves, new Move("G2", "F1", Piece.KNIGHT));
        this.checkAndDeleteMove(blackMoves, new Move("G2", "F1", Piece.ROOK));
        this.checkAndDeleteMove(blackMoves, new Move("G2", "F1", Piece.QUEEN));
        this.checkRemainingMoves(blackMoves);
    }

    @Test
    public void testEnPassantWhite() {
        FenFormat fen = new FenFormat("3k4/8/8/1pP5/8/8/8/3K4 w - b6 0 1");
        Board board = new Board(fen);

        List<Move> moves = new ArrayList<Move>();
        Piece.PAWN.addPseudoLegalMoves(board, moves, 63, GameColor.WHITE);
        this.checkAndDeleteMove(moves, new Move("C5", "C6"));
        this.checkAndDeleteMove(moves, new Move("C5", "B6"));
        this.checkRemainingMoves(moves);
    }

    @Test
    public void testEnPassantBlack() {
        FenFormat fen = new FenFormat("3k4/8/8/8/1pP5/8/8/3K4 b - c3 0 1");
        Board board = new Board(fen);

        List<Move> moves = new ArrayList<Move>();
        Piece.PAWN.addPseudoLegalMoves(board, moves, 52, GameColor.BLACK);
        this.checkAndDeleteMove(moves, new Move("B4", "B3"));
        this.checkAndDeleteMove(moves, new Move("B4", "C3"));
        this.checkRemainingMoves(moves);
    }

}
