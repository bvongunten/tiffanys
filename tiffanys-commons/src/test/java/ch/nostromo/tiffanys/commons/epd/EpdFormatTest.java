package ch.nostromo.tiffanys.commons.epd;

import ch.nostromo.tiffanys.commons.fen.FenFormat;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EpdFormatTest {


    @Test
    public void epdTest() {

        String epd = "3k3B/7p/p1Q1p3/2n5/6P1/K3b3/PP5q/R7 w - - bm Bh8-f6+; ce +M1; pv Bh8-f6+";
        EpdFormat testee = new EpdFormat(epd);

        assertEquals("bm Bh8-f6+; ce +M1; pv Bh8-f6+", testee.getCommand());

        assertEquals("Bh8-f6+", testee.getOpCommand("bm"));
        assertEquals("+M1", testee.getOpCommand("ce"));
        assertEquals("Bh8-f6+", testee.getOpCommand("pv"));

        assertEquals(null, testee.getOpCommand("globi"));

    }
}
