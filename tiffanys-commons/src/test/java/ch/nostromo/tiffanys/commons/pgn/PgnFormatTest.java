package ch.nostromo.tiffanys.commons.pgn;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.nostromo.tiffanys.commons.TestHelper;
import ch.nostromo.tiffanys.commons.pgn.PgnFormat;

import java.util.LinkedHashMap;

public class PgnFormatTest extends TestHelper {

    // @formatter:off    
    String expected = "[Event \"Event\"]\n" +
                      "[Site \"Site\"]\n" +
                      "[Date \"Date\"]\n" +
                      "[Round \"Rount\"]\n" +
                      "[White \"WhitePlayer\"]\n" +
                      "[Black \"BlackPlayer\"]\n" +
                      "[Result \"Result\"]\n"+
                      "\n" +
                      "PgnMoves";
    // @formatter:on    

    @Test
    public void testPgnFormatConstructor1() {
        PgnFormat pgn = new PgnFormat("Event", "Site", "Date", "Rount", "WhitePlayer", "BlackPlayer", "Result", "PgnMoves", new LinkedHashMap<>());
        assertEquals(expected, pgn.generatePgn());
    }

    @Test
    public void testPgnFormatConstructor2() {
        PgnFormat pgn = new PgnFormat(expected);
        assertEquals(expected, pgn.generatePgn());
    }

    @Test
    public void testPgnFormatToString() {
        PgnFormat pgn = new PgnFormat(expected);
        assertEquals(expected, pgn.toString());
    }

}
