package ch.nostromo.tiffanys.commons.opening;

import java.security.SecureRandom;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * MoveSet on a given fen
 */
public class OpeningBookMoveSet {
    Random random = new SecureRandom();

    final PriorityQueue<OpeningBookMove> moves;

    /**
     * Creates a move set
     */
    public OpeningBookMoveSet() {
        this.moves = new PriorityQueue<>();
    }

    /**
     * Adds a move to this move set.
     */
    public void addMove(OpeningBookMove move) {
        this.moves.add(move);
    }

    /**
     * Returns the move in this set with the highest weight.
     */
    public OpeningBookMove getBestMove() {
        return this.moves.peek();
    }

    /**
     * Returns a random move of the list
     */
    public OpeningBookMove getRandomMove() {
        OpeningBookMove[] movesList = this.moves.toArray(new OpeningBookMove[0]);
        return movesList[random.nextInt(movesList.length)];
    }
}
