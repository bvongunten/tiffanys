package ch.nostromo.tiffanys.dragonborn.engine.move.api;

import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.dragonborn.engine.board.RobustBoard;
import ch.nostromo.tiffanys.dragonborn.engine.move.EngineMove;

public interface MoveGenerator {

    int generateMovesList(RobustBoard board, EngineMove[] moves, GameColor color);

    int generateHitMovesList(RobustBoard board, EngineMove[] moves, GameColor color);

}
