package ch.nostromo.tiffanys.dragonborn.engine.board;

import ch.nostromo.tiffanys.dragonborn.engine.TestHelper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ZobristTest extends TestHelper {

    @Test
    public void zobristTest() {

        long currentBoard = 1L;
        currentBoard ^= Zobrist.ZOBRIST_FACTORS[Zobrist.ZOBRIST_WHITE][Zobrist.ZOBRIST_KING][1];
        currentBoard ^= Zobrist.ZOBRIST_FACTORS[Zobrist.ZOBRIST_BLACK][Zobrist.ZOBRIST_KING][3];

        currentBoard ^= Zobrist.ZOBRIST_FACTORS[Zobrist.ZOBRIST_BLACK][Zobrist.ZOBRIST_KING][3];
        currentBoard ^= Zobrist.ZOBRIST_FACTORS[Zobrist.ZOBRIST_WHITE][Zobrist.ZOBRIST_KING][1];

        assertEquals(1L, currentBoard);


    }

}
