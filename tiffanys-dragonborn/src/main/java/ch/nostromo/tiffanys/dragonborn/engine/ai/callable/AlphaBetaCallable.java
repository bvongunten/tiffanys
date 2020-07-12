package ch.nostromo.tiffanys.dragonborn.engine.ai.callable;

import ch.nostromo.tiffanys.dragonborn.commons.EngineSettings;
import ch.nostromo.tiffanys.dragonborn.engine.ai.eval.TiffanysEvaluation;
import ch.nostromo.tiffanys.dragonborn.engine.DragonbornEngineConstants;
import ch.nostromo.tiffanys.dragonborn.engine.ai.PrincipalVariation;
import ch.nostromo.tiffanys.dragonborn.engine.board.RobustBoard;
import ch.nostromo.tiffanys.dragonborn.engine.move.EngineMove;

import java.util.concurrent.Callable;

public class AlphaBetaCallable implements Callable<AlphaBetaCallableResult>, DragonbornEngineConstants {

    private TranspositionsTable transpositionTable;

    private TiffanysEvaluation evaluation = new TiffanysEvaluation();

    private EngineMove[] killersBuffer;

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

    private boolean pvCutOff = false;

    public AlphaBetaCallable(EngineSettings engineSettings, RobustBoard board, EngineMove initialMove, int targetDepth, TranspositionsTable transpositionTable) {

        initializeLists();

        this.transpositionTable = transpositionTable;

        this.initialMove = initialMove;
        this.targetDepth = targetDepth;
        this.currentPv = initialMove.principalVariation;
        this.killersBuffer = initialMove.killersBuffer;

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

    private void initializeLists() {
        for (int i = 0; i < 100; i++) {
            EngineMove[] moves = new EngineMove[100];
            for (int x = 0; x < moves.length; x++) {
                moves[x] = new EngineMove();
            }
            movesBuffer[i] = moves;
        }

        for (int i = 0; i < 100; i++) {
            EngineMove[] moves = new EngineMove[100];
            for (int x = 0; x < moves.length; x++) {
                moves[x] = new EngineMove();
            }
            qmovesBuffer[i] = moves;
        }

        for (int i = 0; i < 100; i++) {
            pvBuffer[i] = new PrincipalVariation();
        }

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


        int origAlpha = alpha;

        boolean doTranspose = true;
        boolean doPv = true;

        int currentRelativeDepth = targetDepth - depth;

        if (inPvPath && currentRelativeDepth > currentPv.moveCount) {
            inPvPath = false;
        }

        PrincipalVariation localPrincipalVariant = pvBuffer[currentRelativeDepth];
        localPrincipalVariant.moveCount = 0;


        // ***************** TRANSPOSITION **********************

        long zobristKey = board.generateBoardZobristHash();

        if (doTranspose) {
            if (transpositionTable.entryExists(zobristKey) && transpositionTable.getDepth(zobristKey) >= depth) {

                int value = transpositionTable.getEval(zobristKey);
                int mode = transpositionTable.getFlag(zobristKey);

                if (mode == TranspositionsTable.VALUE_EXACT) {
                    return value;
                }

                if (mode == TranspositionsTable.VALUE_LOWER && value >= beta) {
                    return beta;
                } else if (mode == TranspositionsTable.VALUE_UPPER && value <= alpha) { // <= ?!
                    return alpha;
                }


            }
        }
        // ***************** TRANSPOSITION **********************


        if (depth == 0) {
            principalVariant.moveCount = 0;

            // current depth
            if (result.maxDepth < targetDepth) {
                result.maxDepth = targetDepth;
            }

            int value = quiescentSearch(alpha, beta);

            // ***************** TRANSPOSITION **********************
//            if (doTranspose) {
//                if (value <= alpha) {
//                    transpositionTable.record(zobristKey, depth, TranspositionsTable.VALUE_UPPER, value, 0);
//                } else if (value >= beta) {
//                    transpositionTable.record(zobristKey, depth, TranspositionsTable.VALUE_LOWER, value, 0);
//                } else {
//                    transpositionTable.record(zobristKey, depth, TranspositionsTable.VALUE_EXACT, value, 0);
//                }
//            }
            // ***************** TRANSPOSITION **********************


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

            for (int i = 0; i < movesCount; i++) {
                if (movesArray[i].from == killersBuffer[currentRelativeDepth].from
                        && movesArray[i].to == killersBuffer[currentRelativeDepth].to) {
                    movesArray[i].hitScore += 9000;
                    break;
                }
            }
        }


        bubbleSortMoves(movesArray, movesCount);

        boolean moveFound = false;

        int best = -10000;

        for (int i = 0; i < movesCount; i++) {
            pvCutOff = false;

            EngineMove currentMove = movesArray[i];

            if (!board.makeAndCheckMove(currentMove)) {

                int score = 0;
                if (i >= 0) {

                    score = -alphaBeta(-beta, -alpha, depth - 1, localPrincipalVariant);

                } else {
                    score = -alphaBeta(-alpha - 1, -alpha, depth - 1, localPrincipalVariant);

                    if (score > alpha && score < beta) {
                        score = -alphaBeta(-beta, -alpha, depth - 1, localPrincipalVariant);
                    }
                }

                board.unmakeMove(currentMove);

                if (score > best) {
                    best = score;
                }

                moveFound = true;

                if (best >= beta) {
                    result.cutOffs++;
                    killersBuffer[currentRelativeDepth] = currentMove.copy();
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

        // ******************************************* TRANSPOSITION ********************************************************
        if (doTranspose && Math.abs(best) < 9000) {
            if (best <= origAlpha) {
                transpositionTable.record(board.generateBoardZobristHash(), depth, TranspositionsTable.VALUE_UPPER, best);
            } else if (best >= beta) {
                transpositionTable.record(board.generateBoardZobristHash(), depth, TranspositionsTable.VALUE_LOWER, best);
            } else {
                transpositionTable.record(board.generateBoardZobristHash(), depth, TranspositionsTable.VALUE_EXACT, best);
            }
        }
        // ******************************************* TRANSPOSITION ********************************************************

        return best;

    }

    private int quiescentSearch(int alpha, int beta) {

        result.nodes++;

        int currentRelativeDepth = targetDepth + qmovesBufferDepth;

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

        bubbleSortMoves(movesArray, movesCount);

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

    private void bubbleSortMoves(EngineMove[] toSort, int len) {
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
