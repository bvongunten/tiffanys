package ch.nostromo.tiffanys.engine.dragonborn.ai.callable;

import ch.nostromo.tiffanys.engine.commons.EngineSettings;
import ch.nostromo.tiffanys.engine.dragonborn.ai.PrincipalVariation;
import ch.nostromo.tiffanys.engine.dragonborn.board.RobustBoard;
import ch.nostromo.tiffanys.engine.dragonborn.move.EngineMove;
import ch.nostromo.tiffanys.engine.dragonborn.DragonbornConstants;
import ch.nostromo.tiffanys.engine.dragonborn.ai.eval.TiffanysEvaluation;

import java.util.concurrent.Callable;

public class AlphaBetaCallable implements Callable<AlphaBetaCallableResult>, DragonbornConstants {

    boolean doPv = true;


    private TiffanysEvaluation evaluation = new TiffanysEvaluation();

    private EngineMove[][] movesBuffer =  AlphaBetaCallableTools.generateMovesBuffer();
    private PrincipalVariation[] pvBuffer =  AlphaBetaCallableTools.generatePrincipalVariationBuffer();

    private PrincipalVariation workingPv;
    private boolean inPvPath;
    private int targetDepth;
    private RobustBoard board;

    private AlphaBetaCallableResult result;
    private EngineMove initialMove;

    public AlphaBetaCallable(EngineSettings engineSettings, RobustBoard board, EngineMove initialMove, int targetDepth) {
        this.board = board;
        this.targetDepth = targetDepth;
        this.initialMove = initialMove;
    }

    public AlphaBetaCallableResult runAB() {
        this.result = new AlphaBetaCallableResult();

        if (initialMove.principalVariation == null || initialMove.principalVariation.moveCount < PrincipalVariation.MINIMAL_DEPTH) {
            inPvPath = false;
        } else {
            inPvPath = true;
        }

        workingPv = pvBuffer[0];
        workingPv.moveCount = 0;
        workingPv.moves[0] = initialMove;


        long startMs = System.currentTimeMillis();

        int score = -alphaBeta(-Integer.MAX_VALUE, Integer.MAX_VALUE, 1, workingPv);

        result.score = score;
        result.plannedDepth = targetDepth;
        result.timeMs = System.currentTimeMillis() - startMs;

        PrincipalVariation resultPrincipalVariation = new PrincipalVariation();
        resultPrincipalVariation.moveCount = workingPv.moveCount + 1;
        resultPrincipalVariation.moves[0] = initialMove.copy();

        for (int x = 0; x < workingPv.moveCount; x++) {
            int placeToStore = x + 1;
            resultPrincipalVariation.moves[placeToStore] = workingPv.moves[x].copy();
        }

        result.foundPv = resultPrincipalVariation;

        return result;
    }

    public final int alphaBeta(int alpha, int beta, int depth, PrincipalVariation parentPrincipalVariation) {
        result.nodes++;

        // Switch to quiescent search
        if (depth - targetDepth == 0) {
            return quiescentSearch(alpha, beta, depth + 1);
        }

        // Get new local principal variation for this depth
        PrincipalVariation localPrincipalVariant = pvBuffer[depth];
        localPrincipalVariant.moveCount = 0;


        // Generate Moves
        EngineMove[] localMoves = movesBuffer[depth];
        int movesCount = board.generateMovesList(localMoves);


        // Apply best score to corresponding local move if there is a pv given
        if (doPv) {
            // TODO > || >= ?!
            if (inPvPath && initialMove.principalVariation.moveCount > depth) {
                for (int i = 0; i < movesCount; i++) {
                    if (localMoves[i].from == initialMove.principalVariation.moves[depth].from
                            && localMoves[i].to == initialMove.principalVariation.moves[depth].to) {
                        localMoves[i].hitScore += 10000;
                        break;
                    }
                }
            } else {
                inPvPath = false;
            }
        }

        // Sort moves - pv first :)
        AlphaBetaCallableTools.bubbleSortMovesByHitScore(localMoves, movesCount);

        boolean moveFound = false;
        int best = -10000;

        for (int i = 0; i < movesCount; i++) {

            EngineMove currentMove = localMoves[i];

            if (!board.makeAndCheckMove(currentMove)) {

                int score = -alphaBeta(-beta, -alpha, depth + 1, localPrincipalVariant);

                board.unmakeMove(currentMove);

                moveFound = true;

                if (score > best) {
                    best = score;
                }

                if (best >= beta) {
                    result.cutOffs++;
                    break;
                }

                if (best > alpha) {
                    alpha = best;

                    if (doPv) {
                        parentPrincipalVariation.moves[0] = currentMove.copy();
                        System.arraycopy(localPrincipalVariant.moves, 0, parentPrincipalVariation.moves, 1, localPrincipalVariant.moveCount);
                        parentPrincipalVariation.moveCount = localPrincipalVariant.moveCount + 1;
                    }
                }


            } else {
                board.unmakeMove(currentMove);
            }

        }

        if (!moveFound) {
            if (!board.isCheckNow()) {
                return 0;
            } else {
                return -TiffanysEvaluation.MAT_RANGE + depth;
            }
        }


        return best;

    }

    private int quiescentSearch(int alpha, int beta, int relativeDepth) {

        result.nodes++;

        int value = evaluation.evaluate(board, relativeDepth);

        result.positionsEvaluated++;

        if (result.maxDepth < relativeDepth) {
            result.maxDepth = relativeDepth;
        }

        if (value >= beta) {
            result.cutOffs++;
            return beta;
        }

        if (value > alpha) {
            alpha = value;
        }

        EngineMove[] movesArray = movesBuffer[relativeDepth];
        int movesCount = board.generateMovesHitList(movesArray);

        AlphaBetaCallableTools.bubbleSortMovesByHitScore(movesArray, movesCount);

        for (int i = 0; i < movesCount; i++) {
            if (!board.makeAndCheckMove(movesArray[i])) {

                value = -quiescentSearch(-beta, -alpha, relativeDepth + 1);

                board.unmakeMove(movesArray[i]);

                if (value >= beta) {
                    result.cutOffs++;
                    return beta;
                }

                if (value > alpha) {
                    alpha = value;
                }

            } else {
                board.unmakeMove(movesArray[i]);
            }
        }

        return alpha;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.concurrent.Callable#call()
     */
    @Override
    public AlphaBetaCallableResult call() throws Exception {
        return runAB();
    }


}
