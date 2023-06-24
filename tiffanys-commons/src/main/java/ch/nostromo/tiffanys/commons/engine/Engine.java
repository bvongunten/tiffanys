package ch.nostromo.tiffanys.commons.engine;

import ch.nostromo.tiffanys.commons.ChessGame;

/**
 * Chess engine interface
 */
public interface Engine {

    /**
     * Start a blocking search, wait for result
     */
    EngineResult startSearch(ChessGame game);

    /**
     * Start an asynchronous search, returns immediately, does fire update and finished event
     */
    void startAsyncSearch(ChessGame game, EngineListener engineListener);

    /**
     * Halt a asynchronous search, EngineListener finished event is to be expected
     */
    void stopAsyncSearch();

}
