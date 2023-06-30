package ch.nostromo.tiffanys.engine.dragonborn.ai.callable;

import ch.nostromo.tiffanys.engine.commons.EngineSettings;
import ch.nostromo.tiffanys.engine.dragonborn.ai.PrincipalVariation;
import ch.nostromo.tiffanys.engine.dragonborn.board.RobustBoard;
import ch.nostromo.tiffanys.engine.dragonborn.move.EngineMove;
import ch.nostromo.tiffanys.engine.dragonborn.DragonbornConstants;
import ch.nostromo.tiffanys.engine.dragonborn.ai.eval.TiffanysEvaluation;

import java.util.concurrent.Callable;

public class AlphaBetaCallable implements Callable<AlphaBetaCallableResult>, DragonbornConstants {

    private TiffanysEvaluation evaluation = new TiffanysEvaluation();

    private EngineMove[][] movesBuffer = new EngineMove[100][100];
    private EngineMove[][] qmovesBuffer = new EngineMove[100][100];
    private PrincipalVariation[] pvBuffer = new PrincipalVariation[100];

    public static int MAX_RANGE = 99999;

    private PrincipalVariation currentPv;
    private PrincipalVariation workingPv;
    private boolean inPvPath;
    private int targetDepth;
    private RobustBoard board;
    private int qmovesBufferDepth = 0;

    private AlphaBetaCallableResult result;
    private EngineMove initialMove;

    public AlphaBetaCallable(EngineSettings engineSettings, RobustBoard board, EngineMove initialMove, int targetDepth) {


        this.movesBuffer = AlphaBetaCallableTools.generateMovesBuffer();
        this.qmovesBuffer = AlphaBetaCallableTools.generateMovesBuffer();

        for (int i = 0; i < 100; i++) {
            pvBuffer[i] = new PrincipalVariation();
        }


        this.targetDepth = targetDepth;

        this.initialMove = initialMove;
        this.currentPv = initialMove.principalVariation;

        this.board = board;

        if (currentPv == null || currentPv.moveCount < 2) {
            inPvPath = false;
        } else {
            inPvPath = true;
        }

        this.result = new AlphaBetaCallableResult();

        workingPv = pvBuffer[0];
        workingPv.moveCount = 0;
        workingPv.moves[0] = initialMove;

    }

    public AlphaBetaCallableResult runAB() {

        int score = 0;
        long startMs = System.currentTimeMillis();

        score = -alphaBeta(-MAX_RANGE, +MAX_RANGE, targetDepth - 1, workingPv);

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

    public final int alphaBeta(int alpha, int beta, int depth, PrincipalVariation principalVariant) {

        boolean doPv = true;

        int currentRelativeDepth = targetDepth - depth;

        if (inPvPath && currentRelativeDepth > currentPv.moveCount) {
            inPvPath = false;
        }

        PrincipalVariation localPrincipalVariant = pvBuffer[currentRelativeDepth];
        localPrincipalVariant.moveCount = 0;


        if (depth == 0) {
            principalVariant.moveCount = 0;

            // current depth
            if (result.maxDepth < targetDepth) {
                result.maxDepth = targetDepth;
            }

            int value = quiescentSearch(alpha, beta);

            return value;
        }

        result.nodes++;


        // Generate Moves
        EngineMove[] movesArray = movesBuffer[depth];
        int movesCount = board.generateMovesList(movesArray);

        if (doPv) {
            if (inPvPath && currentPv.moveCount > currentRelativeDepth) {

                for (int i = 0; i < movesCount; i++) {
                    if (movesArray[i].from == currentPv.moves[currentRelativeDepth].from
                            && movesArray[i].to == currentPv.moves[currentRelativeDepth].to) {
                        movesArray[i].hitScore += 10000;
                        break;
                    }
                }

            }
        }


        AlphaBetaCallableTools.bubbleSortMovesByHitScore(movesArray, movesCount);

        boolean moveFound = false;

        int best = -10000;

        for (int i = 0; i < movesCount; i++) {

            EngineMove currentMove = movesArray[i];

            if (!board.makeAndCheckMove(currentMove)) {

                int score = 0;

                score = -alphaBeta(-beta, -alpha, depth - 1, localPrincipalVariant);


                board.unmakeMove(currentMove);

                if (score > best) {
                    best = score;
                }

                moveFound = true;

                if (best >= beta) {
                    result.cutOffs++;
                    break;
                }

                if (best > alpha) {
                    alpha = best;

                    if (doPv) {
                        principalVariant.moves[0] = currentMove.copy();
                        System.arraycopy(localPrincipalVariant.moves, 0, principalVariant.moves, 1, localPrincipalVariant.moveCount);
                        principalVariant.moveCount = localPrincipalVariant.moveCount + 1;
                    }
                }


            } else {
                board.unmakeMove(currentMove);
            }

        }

        if (!moveFound) {
            if (!board.isCheckNow()) {
                best = 0;
            } else {
                best = -TiffanysEvaluation.MAT_RANGE + currentRelativeDepth;
            }
        }


        return best;

    }

    private int quiescentSearch(int alpha, int beta) {

        result.nodes++;

        int currentRelativeDepth = targetDepth + qmovesBufferDepth + 1;

        int value = evaluation.evaluate(board, currentRelativeDepth, true);

        result.positionsEvaluated++;

        if (result.maxDepth < currentRelativeDepth) {
            result.maxDepth = currentRelativeDepth;
        }

        if (value >= beta) {
            result.cutOffs++;
            return beta;
        }

        if (value > alpha) {
            alpha = value;
        }

        EngineMove[] movesArray = qmovesBuffer[qmovesBufferDepth];
        int movesCount = board.generateMovesHitList(movesArray);

        AlphaBetaCallableTools.bubbleSortMovesByHitScore(movesArray, movesCount);

        for (int i = 0; i < movesCount; i++) {
            if (!board.makeAndCheckMove(movesArray[i])) {

                qmovesBufferDepth++;
                value = -quiescentSearch(-beta, -alpha);
                qmovesBufferDepth--;

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
