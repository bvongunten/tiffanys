package ch.nostromo.tiffanys.dragonborn.commons;

import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.move.Move;
import lombok.Data;

import java.util.List;

@Data
public class EngineResult {

    GameColor colorToMove;

    boolean openingBook = false;

    int depth = 0;

	long totalTimeInMs;
    int positionsEvaluated;

    List<Move> legalMoves;

    Move selectedMove;

}
