package ch.nostromo.tiffanys.dragonborn.commons;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.dragonborn.commons.events.EngineEventListener;

public interface Engine {

    EngineResult syncScoreMoves(ChessGame game) throws EngineException;

    void asyncScoreMoves(ChessGame game);

    long testMoveGen(ChessGame game, int iterations);

    boolean isRunning();

    void halt();

    void addEventListener(EngineEventListener engineEventListener);

}
