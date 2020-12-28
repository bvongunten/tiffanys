package ch.nostromo.tiffanys.commons.enums;

import ch.nostromo.tiffanys.commons.TestHelper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ColorTest extends TestHelper {

    @Test
    public void testColorInvertBlack() {
        assertEquals(GameColor.WHITE, GameColor.BLACK.invert());
    }

    @Test
    public void testColorInvertWhite() {
        assertEquals(GameColor.BLACK, GameColor.WHITE.invert());
    }

    @Test
    public void testUnequals() {
        assertNotEquals(GameColor.BLACK, GameColor.WHITE);
        assertNotEquals(GameColor.BLACK.invert(), GameColor.BLACK);
        assertNotEquals(GameColor.WHITE.invert(), GameColor.WHITE);
    }

    @Test
    public void testCalculationModificator() {
        assertEquals("black", -1, GameColor.BLACK.getCalculationModificator());
        assertEquals("white", 1, GameColor.WHITE.getCalculationModificator());
    }

}
