package ch.nostromo.tiffanys.commons.board;

import ch.nostromo.tiffanys.commons.TestHelper;
import ch.nostromo.tiffanys.commons.fen.FenFormat;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BoardUtilTest extends TestHelper {

    @Test
    public void testDump() {

        // @formatter:off
        String baseBoard =
        "8 BR BN BB BQ BK BB BN BR \n" +
        "7 BP BP BP BP BP BP BP BP \n" +
        "6 [] [] [] [] [] [] [] [] \n" +
        "5 [] [] [] [] [] [] [] [] \n" +
        "4 [] [] [] [] [] [] [] [] \n" +
        "3 [] [] [] [] [] [] [] [] \n" +
        "2 WP WP WP WP WP WP WP WP \n" +
        "1 WR WN WB WQ WK WB WN WR \n" +
        "  A  B  C  D  E  F  G  H  ";
        //@formatter:on

        assertEquals("Board Dump", baseBoard, BoardUtil.dumpBoard(new Board(new FenFormat("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"))));
    }

    @Test
    public void testCoordToField() {
        assertEquals(21, BoardUtil.coordToField("a1"));
        assertEquals(91, BoardUtil.coordToField("a8"));
        assertEquals(28, BoardUtil.coordToField("h1"));
        assertEquals(98, BoardUtil.coordToField("h8"));

        assertEquals(32, BoardUtil.coordToField("b2"));
        assertEquals(43, BoardUtil.coordToField("c3"));
        assertEquals(54, BoardUtil.coordToField("d4"));
        assertEquals(65, BoardUtil.coordToField("e5"));
        assertEquals(76, BoardUtil.coordToField("f6"));
        assertEquals(87, BoardUtil.coordToField("g7"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoordToFieldInvalidCol() {
        BoardUtil.coordToField("I1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoordToFieldInvalidRow0() {
        BoardUtil.coordToField("A0");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoordToFieldInvalidRow9() {
        BoardUtil.coordToField("A9");
    }


}
