package ch.nostromo.tiffanys.dragonborn.engine.board.movegen;

import ch.nostromo.tiffanys.commons.fen.FenFormat;
import ch.nostromo.tiffanys.dragonborn.engine.TestHelper;
import org.junit.Test;

public class KingLegalMovesTest extends TestHelper {
    @Test
    public void testMoveGenEmptyBoardWhite() {
        FenFormat fen = new FenFormat("8/7k/8/8/3K4/8/8/8 w - - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenEmptyBoardBlack() {
        FenFormat fen = new FenFormat("8/7K/8/8/3k4/8/8/8 b - - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenOpponentPiecesWhite() {
        FenFormat fen = new FenFormat("8/7k/8/2qqq3/2qKq3/2qqq3/8/8 w - - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenOpponentPiecesBlack() {
        FenFormat fen = new FenFormat("8/7K/8/2QQQ3/2QkQ3/2QQQ3/8/8 b - - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenOwnPiecesWhite() {
        FenFormat fen = new FenFormat("8/7K/8/2qqq3/2qkq3/2qqq3/8/8 w - - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenOwnPiecesBlack() {
        FenFormat fen = new FenFormat("8/7k/8/2QQQ3/2QKQ3/2QQQ3/8/8 b - - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenCastlingAllAllowedWhite() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenCastlingAllAllowedBlack() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R b KQkq - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenCastlingNoneAllowedWhite() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R w - - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenCastlingNoneAllowedBlack() {
        FenFormat fen = new FenFormat("r3k2r/8/8/8/8/8/8/R3K2R b - - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenCastlingNotAllowedChessByDiagonalWhite() {
        FenFormat fen = new FenFormat("r3k2r/8/1B1B4/8/8/1b1b4/8/R3K2R w KQkq - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenCastlingNotAllowedChessByDiagonalBlack() {
        FenFormat fen = new FenFormat("r3k2r/8/1B1B4/8/8/1b1b4/8/R3K2R b KQkq - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenCastlingNotAllowedChessByHorizontalWhite() {
        FenFormat fen = new FenFormat("r3k2r/8/2R2R2/8/8/2r2r2/8/R3K2R w KQkq - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenCastlingNotAllowedChessByHorizontalBlack() {
        FenFormat fen = new FenFormat("r3k2r/8/2R2R2/8/8/2r2r2/8/R3K2R b KQkq - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenCastlingNotAllowedChessByPawnWhite() {
        FenFormat fen = new FenFormat("r3k2r/2P3P1/8/8/8/8/2p3p1/R3K2R w KQkq - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenCastlingNotAllowedChessByPawnBlack() {
        FenFormat fen = new FenFormat("r3k2r/2P3P1/8/8/8/8/2p3p1/R3K2R b KQkq - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenCastlingNotAllowedChessByKnightWhite() {
        FenFormat fen = new FenFormat("r3k2r/8/4N3/8/8/4n3/8/R3K2R w KQkq - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenCastlingNotAllowedChessByKnightBlack() {
        FenFormat fen = new FenFormat("r3k2r/8/4N3/8/8/4n3/8/R3K2R b KQkq - 0 1");
        compareBaseAndEngineMoves(fen);
    }
}
