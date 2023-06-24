package ch.nostromo.tiffanys.dragonborn.engine.move;

import lombok.Getter;
import lombok.Setter;

/**
 * Move list
 */
public final class MoveList {

    /** The raw move array. */
    @Getter
    int[] moves;

    /** Number of moves currently stored. */
    @Getter
    @Setter
    int size;

    public MoveList() {
        this(256);
    }

    public MoveList(int capacity) {
        this.moves = new int[capacity];
        this.size = 0;
    }

    /**
     * Add a move to the end of the list.
     */
    public void add(int move) {
        if (size == moves.length) {
            int[] bigger = new int[moves.length * 2];
            System.arraycopy(moves, 0, bigger, 0, size);
            moves = bigger;
        }
        moves[size++] = move;
    }

    /** Get the move at the given index. */
    public int get(int index) {
        return moves[index];
    }

    /** Number of moves in the list. */
    public int size() {
        return size;
    }

    /** Reset the list to empty (does not deallocate — ready for reuse). */
    public void clear() {
        size = 0;
    }

    /** Check whether the list contains a specific move (by exact int match). */
    public boolean contains(int move) {
        for (int i = 0; i < size; i++) {
            if (moves[i] == move) return true;
        }
        return false;
    }

}
