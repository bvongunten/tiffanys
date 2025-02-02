package ch.nostromo.tiffanys.engine.dragonborn.board.api;

import ch.nostromo.tiffanys.commons.Side;
import ch.nostromo.tiffanys.engine.dragonborn.board.RobustBoard;
import ch.nostromo.tiffanys.engine.dragonborn.move.EngineMove;

public interface BoardInteraction {

    public boolean isKingInCheck(RobustBoard board, Side color);

    public void applyMove(RobustBoard board, Side color, EngineMove move);

    public void unapplyMove(RobustBoard board, Side color, EngineMove move);

}
