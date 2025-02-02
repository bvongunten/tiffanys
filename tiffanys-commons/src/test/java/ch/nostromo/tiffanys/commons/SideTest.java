package ch.nostromo.tiffanys.commons;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SideTest extends BaseTest {

    @Test
    public void testColorInvertBlack() {
        assertEquals(Side.WHITE, Side.BLACK.invert());
    }

    @Test
    public void testColorInvertWhite() {
        assertEquals(Side.BLACK, Side.WHITE.invert());
    }

    @Test
    public void testUnequals() {
        assertNotEquals(Side.BLACK, Side.WHITE);
        assertNotEquals(Side.BLACK.invert(), Side.BLACK);
        assertNotEquals(Side.WHITE.invert(), Side.WHITE);
    }

    @Test
    public void testCalculationModifiers() {
        assertEquals("black", -1, Side.BLACK.getCalculationModifier());
        assertEquals("white", 1, Side.WHITE.getCalculationModifier());
    }

}
