package ch.nostromo.tiffanys.commons.move;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import ch.nostromo.tiffanys.commons.enums.GameColor;
import org.junit.Test;

import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.commons.move.MoveAttributes;

public class MoveAttributesTest {

    @Test
    public void simpleTest() {
        List<Move> pv = new ArrayList<Move>();
        pv.add(new Move("e2", "e4"));
        pv.add(new Move("e7", "e5"));

        MoveAttributes moveAttributes = new MoveAttributes(GameColor.WHITE, 10.0, 9, 11, 12, 13, 14, pv);
        assertEquals("MoveAttributes [Score=10.0, plannedDepth=12, maxDepth=13, timeSpent=14, nodes=9, cutOffs=11, pv=Move [e2-e4 (35-55)] Move [e7-e5 (85-65)] ]", moveAttributes.toString());

        assertEquals(10.0, moveAttributes.getScore(), 0.0);

    }

}
