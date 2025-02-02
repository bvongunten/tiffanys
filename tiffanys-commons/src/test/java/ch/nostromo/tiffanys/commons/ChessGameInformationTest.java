package ch.nostromo.tiffanys.commons;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ChessGameInformationTest extends BaseTest {

    @Test
    public void testClone() {
        ChessGameInformation original = new ChessGameInformation();
        original.setDate("12.06.1973");
        original.getOptionalTags().put("Key 1", "Value 2");


        ChessGameInformation clone = original.clone();

        original.getOptionalTags().clear();

        assertEquals("Player White", clone.getWhitePlayer());
        assertEquals("Player Black", clone.getBlackPlayer());
        assertEquals("Event", clone.getEvent());
        assertEquals("Site", clone.getSite());
        assertEquals("12.06.1973", clone.getDate());
        assertEquals("1", clone.getRound());

        assertEquals(1, clone.getOptionalTags().size());
        assertEquals("Value 2", clone.getOptionalTags().get("Key 1"));
    }


}
