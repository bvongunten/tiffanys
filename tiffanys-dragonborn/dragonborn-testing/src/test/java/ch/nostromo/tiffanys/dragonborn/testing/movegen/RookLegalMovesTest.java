package ch.nostromo.tiffanys.dragonborn.testing.movegen;

import ch.nostromo.tiffanys.commons.fen.FenFormat;
import org.junit.Test;

public class RookLegalMovesTest extends BaseLegalMovesHelper {

    @Test
    public void testMoveGenEmptyBoardWhite() {
        FenFormat fen = new FenFormat("8/7k/8/8/3R4/8/K7/8 w - - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenEmptyBoardBlack() {
        FenFormat fen = new FenFormat("8/7k/8/8/3r4/8/K7/8 b - - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenOpponentPiecesWhite() {
        FenFormat fen = new FenFormat("7k/8/3n4/8/1b1R1r2/8/3q4/K7 w - - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenOpponentPiecesBlack() {
        FenFormat fen = new FenFormat("7k/8/3N4/8/1B1r1R2/8/3Q4/K7 b - - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenOwnPiecesWhite() {
        FenFormat fen = new FenFormat("7k/8/3N4/8/1B1R1R2/8/3Q4/K7 w - - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenOwnPiecesBlack() {
        FenFormat fen = new FenFormat("7k/8/3n4/8/1b1r1r2/8/3q4/K7 b - - 0 1");
        compareBaseAndEngineMoves(fen);
    }

}
