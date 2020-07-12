package ch.nostromo.tiffanys.chesslink;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ChesslinkBoardTest {


    @Test
    public void testCoordinates() {
        ChessLinkBoard testee = new ChessLinkBoard();

        assertEquals(28, testee.convertToBoardPos(0));
        assertEquals(21, testee.convertToBoardPos(7));
        assertEquals(98, testee.convertToBoardPos(56));
        assertEquals(91, testee.convertToBoardPos(63));

    }
}
