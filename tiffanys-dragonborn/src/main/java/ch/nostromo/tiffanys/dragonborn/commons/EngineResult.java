package ch.nostromo.tiffanys.dragonborn.commons;

import java.util.List;

import ch.nostromo.tiffanys.commons.move.Move;

public class EngineResult {

    boolean openingBook = false;

    int depth = 0;

	long totalTimeInMs;
    int positionsEvaluated;

    List<Move> legalMoves;

    Move selectedMove;

    public List<Move> getLegalMoves() {
        return legalMoves;
    }

    public void setLegalMoves(List<Move> legalMoves) {
        this.legalMoves = legalMoves;
    }

    /**
     * Get the totalTimeInMs.
     * 
     * @return the totalTimeInMs.
     */
    public long getTotalTimeInMs() {
        return totalTimeInMs;
    }

    /**
     * Set the totalTimeInMs.
     * 
     * @param totalTimeInMs
     *            The totalTimeInMs to set.
     */
    public void setTotalTimeInMs(long totalTimeInMs) {
        this.totalTimeInMs = totalTimeInMs;
    }

    /**
     * Get the positionsEvaluated.
     * 
     * @return the positionsEvaluated.
     */
    public int getPositionsEvaluated() {
        return positionsEvaluated;
    }

    /**
     * Set the positionsEvaluated.
     * 
     * @param positionsEvaluated
     *            The positionsEvaluated to set.
     */
    public void setPositionsEvaluated(int positionsEvaluated) {
        this.positionsEvaluated = positionsEvaluated;
    }

    public Move getSelectdMove() {
        return selectedMove;
    }

    public void setSelectedMove(Move selectedMove) {
        this.selectedMove = selectedMove;
    }
    
    public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}
	
    public boolean isOpeningBook() {
        return openingBook;
    }

    public void setOpeningBook(boolean openingBook) {
        this.openingBook = openingBook;
    }


}
