package ch.nostromo.tiffanys.engine.dragonborn.ai.callable;

import ch.nostromo.tiffanys.engine.dragonborn.ai.PrincipalVariation;

public class AlphaBetaCallableResult {

    public int score;
    public int nodes;
    public int cutOffs;
    public int plannedDepth;
    public long timeMs;
    public int maxDepth;
    public int positionsEvaluated;
    public PrincipalVariation foundPv;
}
