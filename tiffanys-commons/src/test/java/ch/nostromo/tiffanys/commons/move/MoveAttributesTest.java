package ch.nostromo.tiffanys.commons.move;

import ch.nostromo.tiffanys.commons.Side;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static ch.nostromo.tiffanys.commons.board.Square.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoveAttributesTest {

    @Test
    void simpleTest() {
        List<Move> pv = new ArrayList<Move>();
        pv.add(new Move(E2, E4));
        pv.add(new Move(E7, E5));

        MoveAttributes moveAttributes = new MoveAttributes(Side.WHITE, 10.0, 3, 9, 13, 14, pv);
        assertEquals("MoveAttributes [Score=10.0, mateIn=3, depth=13, timeSpent=14, nodes=9] , pv=e2-e4 e7-e5 ]", moveAttributes.toString());

        assertEquals(10.0, moveAttributes.getScore(), 0.0);

    }

}
