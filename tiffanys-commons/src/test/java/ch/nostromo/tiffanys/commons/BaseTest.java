package ch.nostromo.tiffanys.commons;

import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.commons.utils.LogUtils;
import org.junit.Before;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.fail;

public class BaseTest {

    private static final Level LOG_LEVEL = Level.SEVERE;

    protected Logger logger = Logger.getLogger(getClass().getName());

    @Before
    public void setUp() {
        LogUtils.initializeConsoleLogging(LOG_LEVEL);
    }


    public void assertAndRemoveExpectedMove(List<Move> moves, Move moveToCheck) {
        if (!moves.remove(moveToCheck)) {
            fail("Move : " + moveToCheck + " not found!");
        }
    }

    public void assertEmptyMoveList(List<Move> moves) {
        if (!moves.isEmpty()) {
            for (Move move : moves) {
                logger.severe("Unexpected Move found: " + move);
            }

            fail("Moves remaining: " + moves.size());
        }
    }


}
