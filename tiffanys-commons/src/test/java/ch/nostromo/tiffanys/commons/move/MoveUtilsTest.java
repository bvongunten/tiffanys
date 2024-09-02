package ch.nostromo.tiffanys.commons.move;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.enums.GameColor;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static ch.nostromo.tiffanys.commons.enums.Coordinates.B1;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.C3;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.D1;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.E2;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.E4;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.F5;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.F7;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.G5;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.G7;
import static ch.nostromo.tiffanys.commons.enums.Coordinates.H5;
import static org.junit.Assert.assertEquals;

public class MoveUtilsTest {

    @Test
    public void testPvGeneration() {
        Move testee = new Move(E2, E4);

        GameColor colorToMove = GameColor.WHITE;
        double score = 3.5;

        int mateIn = 1;
        int nodes = 200;
        int cutOffs = 50000;
        int plannedDepth = 5;
        int maxDepth = 10;
        long timeMs = 5123;

        List<Move> pv = new ArrayList<>();
        pv.add(new Move(F7, F5));
        pv.add(new Move(B1, C3));
        pv.add(new Move(G7, G5));
        pv.add(new Move(D1, H5));

        MoveAttributes moveAttributes = new MoveAttributes(colorToMove, score, mateIn, nodes, cutOffs, plannedDepth, maxDepth, timeMs, pv);
        testee.setMoveAttributes(moveAttributes);

        ChessGame game = new ChessGame();

        String result = MoveUtils.generateSanPrincipalVariation(testee, game.getCurrentBoard(), game.getColorToMove());
        assertEquals("e4 f5 Nc3 g5 Qh5#", result);

    }
}
