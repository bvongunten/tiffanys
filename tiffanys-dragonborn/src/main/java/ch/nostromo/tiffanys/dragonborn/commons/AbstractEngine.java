package ch.nostromo.tiffanys.dragonborn.commons;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.dragonborn.commons.events.EngineEvent;
import ch.nostromo.tiffanys.dragonborn.commons.events.EngineEventListener;
import ch.nostromo.tiffanys.dragonborn.commons.opening.OpeningBook;

public abstract class AbstractEngine {
	protected static Logger LOGGER = Logger.getLogger(AbstractEngine.class.getName());

	protected EngineSettings engineSettings;

	protected List<EngineEventListener> eventListeners = new ArrayList<>();
	
	protected OpeningBook openingBook;
	
	public AbstractEngine(EngineSettings engineSettings) {
	    this(engineSettings, new OpeningBook());
	}
	
	public AbstractEngine(EngineSettings engineSettings, OpeningBook openingBook) {
	    this.engineSettings = engineSettings;
        this.openingBook = openingBook;
	}
	
	public abstract EngineResult syncScoreMoves(ChessGame game) throws EngineException;

	public abstract boolean isRunning();

	public abstract void halt();

	public abstract long testMoveGen(ChessGame game, int iterations);
	    
    public void asyncScoreMoves(ChessGame game) {
        
        Move openingMove = openingBook.getNextMove(game);
        if (openingMove != null) {

            EngineResult engineResult = new EngineResult();
            engineResult.setOpeningBook(true);
            engineResult.setSelectedMove(openingMove);
            
            fireFinishedEvent(new EngineEvent(engineResult));
            
        } else {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        syncScoreMoves(game);
                    } catch (EngineException e) {
                        LOGGER.log(Level.SEVERE, "Thread aborted", e);
                    }
                }
            });
        }
        
    }

	
	protected void fireUpdateEvent(EngineEvent event) {
		for (EngineEventListener listener : eventListeners) {
			listener.engineUpdateEventOccured(event);
		}
	}
	
	protected void fireFinishedEvent(EngineEvent event) {
		for (EngineEventListener listener : eventListeners) {
			listener.engineFinishedEventOccured(event);
		}
	}
	
	public void addEventListener(EngineEventListener listener) {
		this.eventListeners.add(listener);
	}
}
