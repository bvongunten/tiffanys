package ch.nostromo.tiffanys.engine.dragonborn.move.api;

import ch.nostromo.tiffanys.commons.Side;
import ch.nostromo.tiffanys.engine.dragonborn.board.RobustBoard;
import ch.nostromo.tiffanys.engine.dragonborn.move.EngineMove;

public interface MoveGenerator {

    int generateMovesList(RobustBoard board, EngineMove[] moves, Side color);

    int generateHitMovesList(RobustBoard board, EngineMove[] moves, Side color);

}
