package ch.nostromo.tiffanys.engine.dragonborn.board.api;

import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.engine.dragonborn.board.RobustBoard;
import ch.nostromo.tiffanys.engine.dragonborn.move.EngineMove;

public interface BoardInteraction {

    public boolean isKingInCheck(RobustBoard board, GameColor color);

    public void applyMove(RobustBoard board, GameColor color, EngineMove move);

    public void unapplyMove(RobustBoard board, GameColor color, EngineMove move);

}
