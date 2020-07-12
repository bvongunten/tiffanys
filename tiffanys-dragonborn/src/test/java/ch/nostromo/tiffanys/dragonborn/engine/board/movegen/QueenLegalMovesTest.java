package ch.nostromo.tiffanys.dragonborn.engine.board.movegen;

import org.junit.Test;

import ch.nostromo.tiffanys.commons.fen.FenFormat;
import ch.nostromo.tiffanys.dragonborn.engine.TestHelper;

public class QueenLegalMovesTest extends TestHelper {

    @Test
    public void testMoveGenEmptyBoardWhite() {
        FenFormat fen = new FenFormat("8/7k/8/8/3Q4/8/K7/8 w - - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenEmptyBoardBlack() {
        FenFormat fen = new FenFormat("8/7k/8/8/3q4/8/K7/8 b - - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenOpponentPiecesWhite() {
        FenFormat fen = new FenFormat("7k/8/1n1n1q2/8/1b1Q1r2/8/1r1q1b2/K7 w - - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenOpponentPiecesBlack() {
        FenFormat fen = new FenFormat("1k6/8/1N1N1Q2/8/1R1q1Q2/8/1R1Q1Q2/K7 b - - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenOwnPiecesWhite() {
        FenFormat fen = new FenFormat("8/7k/1Q1N1N2/8/1Q1Q1R2/8/KB1B1R2/8 b - - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenOwnPiecesBlack() {
        FenFormat fen = new FenFormat("7K/8/1q1n1n2/8/1q1q1r2/8/kb1b1r2/8 b - - 0 1");
        compareBaseAndEngineMoves(fen);
    }

}
