package ch.nostromo.tiffanys.engine.commons;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.engine.commons.events.EngineEventListener;

import java.util.List;

/**
 * Engine interface to be provided by any engine. Includes sync/async methods to score the best move and interfaces for
 * performance testing.
 */
public interface Engine {

    EngineResult syncScoreMoves(ChessGame game) throws EngineException;

    void asyncScoreMoves(ChessGame game);

    void halt();

    void addEventListener(EngineEventListener engineEventListener);


    // ******************* TEST INTERFACES ***********************

    List<Move> generateLegalHitMovesList(ChessGame game);

    List<Move> generateLegalMovesList(ChessGame game);

    long testPseudoMoveGeneratorPerformance(ChessGame game, int iterations);




}
