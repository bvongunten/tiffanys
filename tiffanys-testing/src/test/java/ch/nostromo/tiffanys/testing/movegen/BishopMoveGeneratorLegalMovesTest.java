package ch.nostromo.tiffanys.testing.movegen;

import ch.nostromo.tiffanys.commons.formats.FenFormat;
import org.junit.Test;

public class BishopMoveGeneratorLegalMovesTest extends BaseLegalMovesHelper {

    @Test
    public void testMoveGenEmptyBoardWhite() {
        FenFormat fen = new FenFormat("8/7k/8/8/3B4/8/K7/8 w - - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenEmptyBoardBlack() {
        FenFormat fen = new FenFormat("8/7K/8/8/3b4/8/8/1k6 b - - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenOpponentPiecesWhite() {
        FenFormat fen = new FenFormat("8/7k/1q3n2/8/3B4/8/Kb3r2/8 w - - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenOpponentPiecesBlack() {
        FenFormat fen = new FenFormat("8/7k/1Q3N2/8/3b4/8/KB3R2/8 b - - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenOwnPiecesWhite() {
        FenFormat fen = new FenFormat("8/7k/1Q3N2/8/3B4/8/KB3R2/8 b - - 0 1");
        compareBaseAndEngineMoves(fen);
    }

    @Test
    public void testMoveGenOwnPiecesBlack() {
        FenFormat fen = new FenFormat("8/7k/1q3n2/8/3b4/8/Kb3r2/8 b - - 0 1");
        compareBaseAndEngineMoves(fen);
    }

}
