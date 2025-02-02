package ch.nostromo.tiffanys.testing.movegen;

import ch.nostromo.tiffanys.commons.formats.FenFormat;
import org.junit.Test;

public class KnightMoveGeneratorLegalMovesTest extends BaseLegalMovesHelper {
    @Test
    public void testMoveGenEmptyBoardWhite() {
        FenFormat fen = new FenFormat("8/7k/8/8/3N4/8/K7/8 w - - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenEmptyBoardBlack() {
        FenFormat fen = new FenFormat(" r7/8/k7/8/4n3/8/1K6/8 b - - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenOpponentPiecesWhite() {
        FenFormat fen = new FenFormat("3K4/7k/2b1b3/1b3b2/3N4/1b3b2/2b1b3/8 w - - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenOpponentPiecesBlack() {
        FenFormat fen = new FenFormat("3k4/7K/2B1B3/1B3B2/3n4/1B3B2/2B1B3/8 b - - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenOwnPiecesWhite() {
        FenFormat fen = new FenFormat("3k4/8/2B1B3/1B3B2/3N4/1B3B2/K1B1B3/8 w - - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenOwnPiecesBlack() {
        FenFormat fen = new FenFormat("3K3k/8/2b1b3/1b3b2/3n4/1b3b2/2b1b3/8 b - - 0 1");
        compareBaseAndEngineMoves(fen);
    }

}
