package ch.nostromo.tiffanys.dragonborn.commons;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.dragonborn.commons.events.EngineEventListener;

import java.util.List;

public interface Engine {

    EngineResult syncScoreMoves(ChessGame game) throws EngineException;

    void asyncScoreMoves(ChessGame game);

    boolean isRunning();

    void halt();

    void addEventListener(EngineEventListener engineEventListener);



    List<Move> generateLegalHitMovesList(ChessGame game);

    List<Move> generateLegalMovesList(ChessGame game);


    // Test Methods
    long testGeneratePseudMoves(ChessGame game, int iterations);




}
