package ch.nostromo.tiffanys.engine.dragonborn.move.api;

import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.engine.dragonborn.board.RobustBoard;
import ch.nostromo.tiffanys.engine.dragonborn.move.EngineMove;

public interface MoveGenerator {

    int generateMovesList(RobustBoard board, EngineMove[] moves, GameColor color);

    int generateHitMovesList(RobustBoard board, EngineMove[] moves, GameColor color);

}
