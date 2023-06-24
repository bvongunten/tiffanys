package ch.nostromo.tiffanys.dragonborn.engine.transposition;

import java.util.concurrent.atomic.AtomicLongArray;

/** Transposition Table */
public final class TranspositionTable {

    public static final int BOUND_EXACT = 0;
    public static final int BOUND_LOWER = 1;
    public static final int BOUND_UPPER = 2;

    private final AtomicLongArray keys;
    private final AtomicLongArray datas;
    private final int mask;

    private volatile int generation = 0;

    // ---- Bit layout constants ----
    private static final int MOVE_BITS = 22;
    private static final long MOVE_MASK = (1L << MOVE_BITS) - 1;   // 0x3FFFFF
    private static final int DEPTH_SHIFT = 22;
    private static final int BOUND_SHIFT = 30;
    private static final int SCORE_SHIFT = 32;
    private static final int GEN_SHIFT = 48;
    private static final int TAG_SHIFT = 56;
    private static final long TAG_MASK = 0xFFL;                    // 8 bits


    public TranspositionTable(int sizeMB) {
        int entriesWanted = (sizeMB * 1024 * 1024) / 16;
        int size = Integer.highestOneBit(Math.max(entriesWanted, 1024));
        this.keys = new AtomicLongArray(size);
        this.datas = new AtomicLongArray(size);
        this.mask = size - 1;
    }

    public void clear() {
        for (int i = 0; i < keys.length(); i++) {
            keys.set(i, 0L);
            datas.set(i, 0L);
        }
        generation = 0;
    }

    public void newSearch() {
        generation = (generation + 1) & 0xFF;
    }

    public void store(long hash, int move, int depth, int score, int bound) {
        int idx = (int) (hash & mask);

        // Replacement policy: keep deeper entries from the same generation
        long oldData = datas.get(idx);
        if (oldData != 0) {
            int oldDepth = (int) ((oldData >>> DEPTH_SHIFT) & 0xFF);
            int oldGen = (int) ((oldData >>> GEN_SHIFT) & 0xFF);
            if (oldGen == generation && depth < oldDepth) {
                return;
            }
        }

        // Verification tag: upper 8 bits of the hash, stored inside data
        long tag = (hash >>> 56) & TAG_MASK;

        long data = move & MOVE_MASK
                        | (((long) (depth & 0xFF)) << DEPTH_SHIFT)
                        | (((long) (bound & 0x3)) << BOUND_SHIFT)
                        | (((long) (score & 0xFFFF)) << SCORE_SHIFT)
                        | (((long) (generation & 0xFF)) << GEN_SHIFT)
                        | (tag << TAG_SHIFT);

        datas.set(idx, data);
        keys.set(idx, hash ^ data);
    }

    public long probe(long hash) {
        int idx = (int) (hash & mask);

        long key = keys.get(idx);
        long data = datas.get(idx);

        if ((key ^ data) != hash) return 0L;

        long storedTag = (data >>> TAG_SHIFT) & TAG_MASK;
        long expectedTag = (hash >>> 56) & TAG_MASK;
        if (storedTag != expectedTag) return 0L;

        return data == 0 ? 1L : data;
    }

    public static int getMove(long data) {
        return (int) (data & MOVE_MASK);
    }

    public static int getDepth(long data) {
        return (int) ((data >>> DEPTH_SHIFT) & 0xFF);
    }

    public static int getBound(long data) {
        return (int) ((data >>> BOUND_SHIFT) & 0x3);
    }

    public static int getScore(long data) {
        return (short) ((data >>> SCORE_SHIFT) & 0xFFFF);
    }
}
