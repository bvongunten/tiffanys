package ch.nostromo.tiffanys.dragonborn.engine.search;

import ch.nostromo.tiffanys.commons.engine.EngineResult;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.commons.move.MoveAttributes;
import ch.nostromo.tiffanys.dragonborn.engine.move.MoveUtils;

import java.util.ArrayList;
import java.util.List;

/** Search result */
public class SearchResult {

    public final int bestMove;
    public final int score;
    public final int depth;
    public final long nodes;
    public final long timeMillis;
    public final int[] pv;
    public final int pvLength;

    public SearchResult(int bestMove, int score, int depth, long nodes, long timeMillis, int[] pv, int pvLength) {
        this.bestMove = bestMove;
        this.score = score;
        this.depth = depth;
        this.nodes = nodes;
        this.timeMillis = timeMillis;
        this.pv = new int[pvLength];
        System.arraycopy(pv, 0, this.pv, 0, pvLength);
        this.pvLength = pvLength;
    }

    /** Convert to Tiffanys Engine result */
    public EngineResult toEngineResult() {
        EngineResult result = new EngineResult();

        Move selectedMove = MoveUtils.toTiffanysMove(bestMove);

        MoveAttributes attributes = selectedMove.getMoveAttributes();
        if (attributes == null) {
            attributes = new MoveAttributes();
            selectedMove.setMoveAttributes(attributes);
        }

        attributes.setDepth(depth);
        attributes.setNodes(nodes);
        attributes.setTimeMs(timeMillis);

        if (Math.abs(score) >= Search.MATE_THRESHOLD) {
            int mateIn = (Search.MATE_SCORE - Math.abs(score) + 1) / 2;
            if (score < 0) mateIn = -mateIn;
            attributes.setMateIn(mateIn);
            attributes.setScore(0);
        } else {
            attributes.setScore(score);
            attributes.setMateIn(0);
        }

        List<Move> pvMoves = new ArrayList<>(pvLength);
        for (int i = 0; i < pvLength; i++) {
            pvMoves.add(MoveUtils.toTiffanysMove(pv[i]));
        }
        attributes.setPrincipalVariations(pvMoves);

        result.setSelectedMove(selectedMove);
        result.setTotalTimeInMs(timeMillis);

        return result;
    }

}
