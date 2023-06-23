package ch.nostromo.tiffanys.lichess.streams;

import ch.nostromo.tiffanys.lichess.dtos.board.BoardGameFull;
import ch.nostromo.tiffanys.lichess.dtos.commons.GameState;

public interface BoardGameStateListener {

   void onBoardGameStateFull(BoardGameFull gameFull);

   void onBoardGameState(GameState gameState);
}
