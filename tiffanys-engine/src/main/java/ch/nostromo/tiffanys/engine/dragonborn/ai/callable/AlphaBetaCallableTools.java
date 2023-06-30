package ch.nostromo.tiffanys.engine.dragonborn.ai.callable;

import ch.nostromo.tiffanys.engine.dragonborn.ai.PrincipalVariation;
import ch.nostromo.tiffanys.engine.dragonborn.move.EngineMove;

public class AlphaBetaCallableTools {

    public static final int MOVELIST_DEPTH = 100;
    public static final int MOVELIST_SIZE = 100;

    public static EngineMove[][] generateMovesBuffer() {
        EngineMove[][] result  = new EngineMove[MOVELIST_DEPTH][MOVELIST_SIZE];
        for (int i = 0; i < MOVELIST_DEPTH; i++) {
            EngineMove[] moves = new EngineMove[MOVELIST_SIZE];
            for (int x = 0; x < moves.length; x++) {
                moves[x] = new EngineMove();
            }
            result[i] = moves;
        }

        return result;
    }

    public static PrincipalVariation[] generatePrincipalVariationBuffer() {
        PrincipalVariation[] result = new PrincipalVariation[MOVELIST_DEPTH];
        for (int i = 0; i < MOVELIST_DEPTH; i++) {
            result[i] = new PrincipalVariation();
        }

        return result;
    }


    public static void bubbleSortMovesByHitScore(EngineMove[] toSort, int len) {
        boolean done = false;
        for (int i = 0; i < len; i++) {
            if (done) {
                break;
            }
            done = true;

            for (int j = len - 1; j > i; j--) {
                if (toSort[j].hitScore > toSort[j - 1].hitScore) {
                    EngineMove temp = toSort[j];
                    toSort[j] = toSort[j - 1];
                    toSort[j - 1] = temp;
                    done = false;
                }
            }
        }
    }


}
