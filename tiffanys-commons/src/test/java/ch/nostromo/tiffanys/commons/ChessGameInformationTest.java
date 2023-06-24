package ch.nostromo.tiffanys.commons;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChessGameInformationTest extends BaseTest {

    @Test
    void testClone() {
        ChessGameInformation original = new ChessGameInformation();

        original.setEvent("Event");
        original.setWhite("Player White");
        original.setBlack("Player Black");
        original.setDate("12.06.1973");
        original.setSite("Site");
        original.setRound("1");

        original.getOptionalTags().put("Key 1", "Value 2");
        original.setChessGameState(ChessGameState.BLACK_WIN_BY_RESIGNATION);
        original.getPreambleComments().add("Comment 1");
        original.getPreambleComments().add("Comment 2");


        ChessGameInformation copy = original.copy();

        original.getOptionalTags().clear();

        assertEquals("Player White", copy.getWhite());
        assertEquals("Player Black", copy.getBlack());
        assertEquals("Event", copy.getEvent());
        assertEquals("Site", copy.getSite());
        assertEquals("12.06.1973", copy.getDate());
        assertEquals("1", copy.getRound());

        assertEquals(1, copy.getOptionalTags().size());
        assertEquals("Value 2", copy.getOptionalTags().get("Key 1"));

        assertEquals(2, copy.getPreambleComments().size());
        assertEquals("Comment 1", copy.getPreambleComments().getFirst());
        assertEquals("Comment 2", copy.getPreambleComments().getLast());

        assertEquals(ChessGameState.BLACK_WIN_BY_RESIGNATION, copy.getChessGameState());
    }

    @Test
    void testGameStatesOverrideByGameState() {
        ChessGameInformation testee = new  ChessGameInformation();
        testee.setResult("*");
        testee.setChessGameState(ChessGameState.BLACK_WIN_BY_RESIGNATION);

        assertEquals(ChessGameState.BLACK_WIN_BY_RESIGNATION, testee.getChessGameState());
        assertEquals("0-1", testee.getResult());
    }


    @Test
    void testGameStatesOverrideByResult() {
        ChessGameInformation testee = new  ChessGameInformation();
        testee.setResult("1-0");
        testee.setChessGameState(null);

        assertEquals(ChessGameState.WHITE_WIN, testee.getChessGameState());
        assertEquals("1-0", testee.getResult());
    }



    @Test
    void testGameStatesOverrideByDefault() {
        ChessGameInformation testee = new  ChessGameInformation();
        testee.setResult(null);
        testee.setChessGameState(null);

        assertEquals(ChessGameState.GAME_OPEN, testee.getChessGameState());
        assertEquals("*", testee.getResult());
    }


}
