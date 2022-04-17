package ch.nostromo.tiffanys.dragonborn.commons;

public class EngineSettings {

	public enum EngineMode {
	    DEPTH, TIME_FOR_MOVE, TIME_MOVES_FOR_GAME;
	}

	private EngineMode mode = EngineMode.DEPTH;

	private boolean useOpeningBook = true;
	private int depth = 4;
	private int time = 5000;

	private int timeForGame = 300;
	private int movesForGame = 40;
	
	private int threads = Runtime.getRuntime().availableProcessors();

	public String toString() {
		String result = "";
		switch (mode) {

			case DEPTH:
				result +=  "Mode: DEPTH, depth=" + depth;
				break;
			case TIME_FOR_MOVE:
				result +=  "Mode: TIME_FOR_MOVE, time=" + time;
				break;
			case TIME_MOVES_FOR_GAME:
				result +=  "Mode: TIME_MOVES_FOR_GAME / GAME, timeForGame=" + timeForGame + ", movesForGame=" + movesForGame + ", time=" + time;
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + mode);
		}

		result += ", threads=" + threads +", ob="+  useOpeningBook;
		
		return result;
		
	}
	
	public EngineMode getMode() {
		return mode;
	}

	public void setMode(EngineMode mode) {
		this.mode = mode;
	}

	public boolean isUseOpeningBook() {
		return useOpeningBook;
	}

	public void setUseOpeningBook(boolean useOpeningBook) {
		this.useOpeningBook = useOpeningBook;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getThreads() {
		return threads;
	}

	public void setThreads(int threads) {
		this.threads = threads;
	}

	public int getTimeForGame() {
		return timeForGame;
	}

	public void setTimeForGame(int timeForGame) {
		this.timeForGame = timeForGame;
	}

	public int getMovesForGame() {
		return movesForGame;
	}

	public void setMovesForGame(int movesForGame) {
		this.movesForGame = movesForGame;
	}


}
