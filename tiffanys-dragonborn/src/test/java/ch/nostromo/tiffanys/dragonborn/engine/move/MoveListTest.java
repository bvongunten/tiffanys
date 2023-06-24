package ch.nostromo.tiffanys.dragonborn.engine.move;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveListTest {

    @Test
    void testSizeManagement() {
        MoveList moveList = new MoveList(2);

        assertEquals(2, moveList.getMoves().length);
        moveList.add(1);
        moveList.add(2);

        // Trigger resize
        moveList.add(3);
        assertEquals(4, moveList.getMoves().length);
    }


    @Test
    void testContains() {
        MoveList moveList = new MoveList();

        moveList.add(1);
        moveList.add(2);

        assertTrue(moveList.contains(1));
        assertFalse(moveList.contains(3));

        moveList.clear();

        assertFalse(moveList.contains(1));
        assertFalse(moveList.contains(3));


    }

    @Test
    void testSizeAndClear() {
        MoveList moveList = new MoveList();

        moveList.add(1);
        moveList.add(2);
        assertEquals(256, moveList.getMoves().length);
        assertEquals(2, moveList.getSize());

        moveList.clear();
        assertEquals(256, moveList.getMoves().length);
        assertEquals(0, moveList.getSize());
    }

}
