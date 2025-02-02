package ch.nostromo.tiffanys.commons.move;

import ch.nostromo.tiffanys.commons.Side;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static ch.nostromo.tiffanys.commons.board.Square.E2;
import static ch.nostromo.tiffanys.commons.board.Square.E4;
import static ch.nostromo.tiffanys.commons.board.Square.E5;
import static ch.nostromo.tiffanys.commons.board.Square.E7;
import static org.junit.Assert.assertEquals;

public class MoveAttributesTest {

    @Test
    public void simpleTest() {
        List<Move> pv = new ArrayList<Move>();
        pv.add(new Move(E2, E4));
        pv.add(new Move(E7, E5));

        MoveAttributes moveAttributes = new MoveAttributes(Side.WHITE, 10.0, 3, 9, 11, 12, 13, 14, pv);
        assertEquals("MoveAttributes [Score=10.0, mateIn=3, plannedDepth=12, maxDepth=13, timeSpent=14, nodes=9, cutOffs=11] , pv=e2-e4 e7-e5 ]", moveAttributes.toString());

        assertEquals(10.0, moveAttributes.getScore(), 0.0);

    }

}
