package ch.nostromo.tiffanys.engine.commons;

import ch.nostromo.tiffanys.commons.move.Move;
import lombok.Data;

import java.util.List;

@Data
public class EngineResult {


    private Move selectedMove;

    private List<Move> legalMoves;

    private boolean openingBook = false;

    private int depth = 0;

    private long totalTimeInMs;

    private int positionsEvaluated;



}
