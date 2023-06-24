package ch.nostromo.tiffanys.commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class SideTest extends BaseTest {

    @Test
    void testColorInvertBlack() {
        assertEquals(Side.WHITE, Side.BLACK.invert());
    }

    @Test
    void testColorInvertWhite() {
        assertEquals(Side.BLACK, Side.WHITE.invert());
    }

    @Test
    void testUnequals() {
        assertNotEquals(Side.BLACK, Side.WHITE);
        assertNotEquals(Side.BLACK, Side.BLACK.invert());
        assertNotEquals(Side.WHITE, Side.WHITE.invert());
    }

    @Test
    void testCalculationModifiers() {
        assertEquals(-1, Side.BLACK.getCalculationModifier());
        assertEquals(1, Side.WHITE.getCalculationModifier());
    }

}
