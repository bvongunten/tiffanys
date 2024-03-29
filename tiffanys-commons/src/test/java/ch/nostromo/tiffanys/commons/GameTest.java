package ch.nostromo.tiffanys.commons;

import ch.nostromo.tiffanys.commons.enums.GameState;
import ch.nostromo.tiffanys.commons.fen.FenFormat;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GameTest extends TestHelper {

    @Test
    public void testInitialFen() {
        ChessGame game = new ChessGame(new ChessGameInfo());
        game.getCurrentFenFormat().toString();
    }

    @Test
    public void testGetCurrentGameStatePreSet() {
        ChessGame game = new ChessGame(new ChessGameInfo());
        game.setFinishedGameState(GameState.REMIS_BY_FIFTY);
        assertEquals(GameState.REMIS_BY_FIFTY, game.getCurrentGameState());
    }

    @Test
    public void testGetCurrentGameStateMate() {
        String fen = "5b2/1p1b1p2/5kp1/P1n1p3/3pP2p/P1pP1P2/5qr1/B3RK2 w - - 0 39";
        ChessGame game = new ChessGame(new ChessGameInfo(), new FenFormat(fen));
        assertEquals(GameState.WIN_BLACK_MATES, game.getCurrentGameState());
    }

}
