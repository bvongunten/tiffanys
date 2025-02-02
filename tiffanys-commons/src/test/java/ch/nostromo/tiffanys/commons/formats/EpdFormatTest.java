package ch.nostromo.tiffanys.commons.formats;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EpdFormatTest {


    @Test
    public void testCommands() {
        EpdFormat epdFormat = new EpdFormat("3k3B/7p/p1Q1p3/2n5/6P1/K3b3/PP5q/R7 w - - bm Bh8-f6+; ce +M1; pv Bh8-f6+");

        assertEquals("bm Bh8-f6+; ce +M1; pv Bh8-f6+", epdFormat.getCommand());

        assertEquals("Bh8-f6+", epdFormat.getOpCommand("bm"));
        assertEquals("+M1", epdFormat.getOpCommand("ce"));
        assertEquals("Bh8-f6+", epdFormat.getOpCommand("pv"));

        assertNull(epdFormat.getOpCommand("incorrect"));
    }

    @Test
    public void testFen() {
        EpdFormat epdFormat = new EpdFormat("r3rqkb/pp1b1pnp/2p1p1p1/4P1B1/2B1N1P1/5N1P/PPP2P2/2KR3R w - - bm Ne4-f6+; ce +M1; pv Ne4-f6+;");
        FenFormat fenFormat = epdFormat.getFen();

        assertEquals("r3rqkb/pp1b1pnp/2p1p1p1/4P1B1/2B1N1P1/5N1P/PPP2P2/2KR3R w - - 0 0", fenFormat.toString());

    }
}
