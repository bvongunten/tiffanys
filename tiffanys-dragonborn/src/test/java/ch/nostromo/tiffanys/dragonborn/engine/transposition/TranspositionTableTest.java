package ch.nostromo.tiffanys.dragonborn.engine.transposition;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TranspositionTableTest {

    @Test
    void testStoreAndProbeAndClear() {
        TranspositionTable tt = new TranspositionTable(1);

        long hash = 12345L;
        int move = 42;
        int depth = 10;
        int score = 100;
        int bound = TranspositionTable.BOUND_EXACT;

        tt.store(hash, move, depth, score, bound);

        long data = tt.probe(hash);

        assertNotEquals(0L, data);

        assertEquals(move, TranspositionTable.getMove(data));
        assertEquals(depth, TranspositionTable.getDepth(data));
        assertEquals(bound, TranspositionTable.getBound(data));
        assertEquals(score, TranspositionTable.getScore(data));

        // Clear
        tt.clear();
        assertEquals(0L,  tt.probe(hash));


    }

    @Test
    void testProbeNonExistent() {
        TranspositionTable tt = new TranspositionTable(1);

        long result = tt.probe(99999L);

        assertEquals(0L, result);
    }

    @Test
    void testNewSearchIncrementsGeneration() {
        TranspositionTable tt = new TranspositionTable(1);

        long hash1 = 11111L;
        tt.store(hash1, 10, 5, 50, TranspositionTable.BOUND_LOWER);
        tt.newSearch();

        long hash2 = 22222L;
        tt.store(hash2, 20, 8, 80, TranspositionTable.BOUND_UPPER);

        // Original entry should be retrievable (generation changed)
        long data1 = tt.probe(hash1);
        assertNotEquals(0L, data1);
        assertEquals(10, TranspositionTable.getMove(data1));
    }

}
