package ch.nostromo.tiffanys.commons.formats;

import ch.nostromo.tiffanys.commons.BaseTest;
import org.junit.Test;

import java.util.LinkedHashMap;

import static org.junit.Assert.assertEquals;

public class PgnFormatTest extends BaseTest {

    // @formatter:off    
    String expected = "[Event \"Event\"]\n" +
                      "[Site \"Site\"]\n" +
                      "[Date \"Date\"]\n" +
                      "[Round \"Round\"]\n" +
                      "[White \"WhitePlayer\"]\n" +
                      "[Black \"BlackPlayer\"]\n" +
                      "[Result \"Result\"]\n"+
                      "\n" +
                      "PgnMoves";
    // @formatter:on    

    @Test
    public void testPgnFormatConstructorWithPgn() {
        PgnFormat pgnFormat = new PgnFormat(expected);
        assertEquals(expected, pgnFormat.toString());
    }

    @Test
    public void testPgnFormatConstructorWithArguments() {
        PgnFormat pgnFormat = new PgnFormat("Event", "Site", "Date", "Round", "WhitePlayer", "BlackPlayer", "Result", "PgnMoves", new LinkedHashMap<>());
        assertEquals(expected, pgnFormat.toString());
    }

    @Test
    public void testOptionalTags() {
        // @formatter:off
        String optionalTags = "[Optional1 \"Optional Text 1\"]\n" +
                "[Optional2 \"Optional Text 2\"]\n" +
                "\n" +
                "PgnMoves";
        // @formatter:on

        PgnFormat pgnFormat = new PgnFormat(optionalTags);
        assertEquals(optionalTags, pgnFormat.toString());
    }

    @Test
    public void testAnnotatedPgnFormat() {
        // @formatter:off
        String annotatedPgn = "[Event \"Rated correspondence game\"]\n" +
                "[Site \"https://lichess.org/n4Jwb9wK\"]\n" +
                "[Date \"2025.01.19\"]\n" +
                "[White \"Real_Life_07\"]\n" +
                "[Black \"bvongunten\"]\n" +
                "[Result \"0-1\"]\n" +
                "[UTCDate \"2025.01.19\"]\n" +
                "[UTCTime \"23:02:34\"]\n" +
                "[WhiteElo \"1500\"]\n" +
                "[BlackElo \"1741\"]\n" +
                "[WhiteRatingDiff \"-127\"]\n" +
                "[BlackRatingDiff \"+5\"]\n" +
                "[Variant \"Standard\"]\n" +
                "[TimeControl \"-\"]\n" +
                "[ECO \"D02\"]\n" +
                "[Opening \"Queen's Pawn Game: Chigorin Variation\"]\n" +
                "[Termination \"Time forfeit\"]\n" +
                "[Annotator \"lichess.org\"]\n" +
                "\n" +
                "1. d4 d5 2. Nf3 Nc6 { D02 Queen's Pawn Game: Chigorin Variation } 3. c3 Nf6 { Black wins on time. } 0-1";
        // @formatter:on

        String expectedMoves = "1. d4 d5 2. Nf3 Nc6  3. c3 Nf6  0-1";

        PgnFormat pgnFormat = new PgnFormat(annotatedPgn);
        assertEquals(annotatedPgn, pgnFormat.toString());
        assertEquals(expectedMoves, pgnFormat.getStripedPgnMoves());

    }

}
