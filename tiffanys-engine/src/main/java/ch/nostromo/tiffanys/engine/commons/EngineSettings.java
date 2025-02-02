package ch.nostromo.tiffanys.engine.commons;

import lombok.Data;

@Data
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


}
