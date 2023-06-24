package ch.nostromo.tiffanys.dragonborn.engine.search;

import ch.nostromo.tiffanys.dragonborn.engine.board.Board;
import ch.nostromo.tiffanys.dragonborn.engine.board.PieceUtils;
import ch.nostromo.tiffanys.dragonborn.engine.evaluation.Evaluation;
import ch.nostromo.tiffanys.dragonborn.engine.exception.SearchTimeoutException;
import ch.nostromo.tiffanys.dragonborn.engine.move.MoveGenerator;
import ch.nostromo.tiffanys.dragonborn.engine.move.MoveList;
import ch.nostromo.tiffanys.dragonborn.engine.move.MoveUtils;
import ch.nostromo.tiffanys.dragonborn.engine.transposition.TranspositionTable;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Search ... search baby !!! :)
 */
public final class Search {

    private static final Logger LOG = LoggerFactory.getLogger(Search.class);

    /**
     * Mate Score
     */
    public static final int MATE_SCORE = 30000;

    /**
     * Mate threshold (29999 = Mate in 1)
     */
    public static final int MATE_THRESHOLD = MATE_SCORE - 1000;

    /**
     * Initial a/b score (> mate)
     */
    private static final int INFINITY = 31000;

    /**
     * Max search depth ...
     */
    private static final int MAX_PLY = 64;

    /**
     * Enable principal variation
     */
    private static final boolean ENABLE_PVS = true;

    /**
     * Enable transposition tables
     */
    private static final boolean ENABLE_TT = true;

    /**
     * Enable transposition table cutoffs
     */
    private static final boolean ENABLE_TT_CUTOFFS = true;

    /**
     * Enable move ordering
     */
    private static final boolean ENABLE_MOVE_ORDERING = true;

    /**
     * Enable check extension
     */
    private static final boolean ENABLE_CHECK_EXTENSION = true;

    /**
     * Enable quiescence earch
     */
    private static final boolean ENABLE_QUIESCENCE = true;

    /**
     * Empty result
     */
    private static final int[] EMPTY_RESULT = new int[0];

    /**
     * Transposition table
     */
    private final TranspositionTable transpositionTable;

    /**
     * Move list per depth
     */
    private final MoveList[] moveLists = new MoveList[MAX_PLY];

    /**
     * Ordered move score
     */
    private final int[] orderingScores = new int[256];

    /**
     * Principal variation
     */
    private final int[][] pvTable = new int[MAX_PLY][MAX_PLY];
    private final int[] pvLength = new int[MAX_PLY];
    int[] prevIterationPV = new int[MAX_PLY];
    int prevIterationPVLength = 0;


    /**
     * Deadline / timeout in nano seconds
     */
    private long deadlineNanos;

    /**
     * Time limit flag
     */
    private boolean useTimeLimit;

    /**
     * stop flag for all workers
     */
    private AtomicBoolean stopFlag = new AtomicBoolean(false);

    /**
     * Visited nodes counter
     */
    private long nodesCounter;

    /**
     * best root move & score
     */
    private int rootBestMove;
    private int rootBestScore;

    /**
     * Listener for client information used in async searches
     */
    @Setter
    private SearchListener listener;

    /**
     * Threadpool for workers
     */
    private final ExecutorService rootExecutor;

    /**
     * Shared alpha for workers
     */
    AtomicInteger sharedAlpha;

    /**
     * Create search (single threaded)
     */
    public Search(TranspositionTable transpositionTable) {
        this.transpositionTable = transpositionTable;
        this.rootExecutor = null;
        for (int i = 0; i < MAX_PLY; i++) {
            moveLists[i] = new MoveList(256);
        }
    }

    /**
     * Create multithreaded search
     */
    public Search(TranspositionTable transpositionTable, int numThreads) {
        this.transpositionTable = transpositionTable;
        for (int i = 0; i < MAX_PLY; i++) {
            moveLists[i] = new MoveList(256);
        }

        if (numThreads > 1) {
            this.rootExecutor = Executors.newFixedThreadPool(numThreads, r -> {
                Thread worker = new Thread(r, "search-worker");
                worker.setUncaughtExceptionHandler((thread, ex) -> LOG.error("Uncaught exception in worker thread {}", thread.getName(), ex));
                worker.setDaemon(true);
                return worker;
            });
        } else {
            this.rootExecutor = null;
        }
    }

    /**
     * Start search for given depth on given board
     */
    public SearchResult searchDepth(Board board, int depth) {
        return doSearch(board, depth, Long.MAX_VALUE, false);
    }

    /**
     * Start search for given time in ms on given board
     */
    public SearchResult searchTime(Board board, long milliseconds) {
        return doSearch(board, MAX_PLY - 1, milliseconds, true);
    }

    /**
     * Does set the stop flag for all workers
     */
    public void stop() {
        stopFlag.set(true);
    }


    /**
     * Shutdown search (including stop flag set)
     */
    public void shutdown() {
        stop();
        if (rootExecutor != null) {
            rootExecutor.shutdownNow();
            try {
                rootExecutor.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Main search (iterative deepening)
     */
    private SearchResult doSearch(Board b, int maxDepth, long maxMillis, boolean timeLimited) {
        long startTimeNs = System.nanoTime();

        deadlineNanos = timeLimited ? startTimeNs + maxMillis * 1_000_000L : Long.MAX_VALUE;
        useTimeLimit = timeLimited;
        stopFlag.set(false);

        nodesCounter = 0;
        rootBestMove = 0;
        rootBestScore = 0;
        prevIterationPVLength = 0;

        if (ENABLE_TT) {
            transpositionTable.newSearch();
        }

        // Best results committed so far across iterations
        int lastBest = 0;
        int lastScore = 0;
        int lastDepth = 0;
        int lastPVLen = 0;
        int[] lastPV = new int[MAX_PLY];
        int mateExtraIterations = 0;

        boolean keepSearching = true;
        for (int depth = 1; depth <= maxDepth && keepSearching; depth++) {
            Arrays.fill(pvLength, 0);

            int score = runOneIteration(b, depth);
            boolean aborted = score == Integer.MIN_VALUE;

            if (aborted) {
                keepSearching = false;
            } else {
                // Commit iteration results
                lastDepth = depth;
                lastScore = rootBestScore;
                lastBest = rootBestMove;
                lastPVLen = pvLength[0];
                System.arraycopy(pvTable[0], 0, lastPV, 0, lastPVLen);

                // Publish PV for next iteration's move ordering
                prevIterationPVLength = lastPVLen;
                System.arraycopy(lastPV, 0, prevIterationPV, 0, lastPVLen);

                fireDepthCompleted(buildResult(lastBest, lastScore, lastDepth, lastPV, lastPVLen, startTimeNs));

                int nextMateIters = shouldContinueMateExtension(score, depth, mateExtraIterations);
                if (nextMateIters < 0) {
                    keepSearching = false;
                } else {
                    mateExtraIterations = nextMateIters;
                }
            }
        }

        SearchResult finalResult = buildResult(lastBest, lastScore, lastDepth, lastPV, lastPVLen, startTimeNs);

        fireSearchFinished(finalResult);

        return finalResult;
    }

    private void fireDepthCompleted(SearchResult result) {
        if (listener != null) {
            listener.onDepthCompleted(result);
        }
    }

    private void fireSearchFinished(SearchResult result) {
        if (listener != null) {
            listener.onSearchFinished(result);
        }
    }


    private int runOneIteration(Board b, int depth) {
        try {
            int score = negamax(b, depth, 0, -INFINITY, INFINITY);
            if (shouldStop()) {
                return Integer.MIN_VALUE;
            }
            return score;
        } catch (SearchTimeoutException e) {
            return Integer.MIN_VALUE;
        }
    }

    private int shouldContinueMateExtension(int score, int depth, int mateExtraIterations) {
        if (score <= -MATE_THRESHOLD) {
            return -1; // getting mated
        }
        if (score >= MATE_THRESHOLD) {
            boolean canImprove = MATE_SCORE - score > depth + 1 && mateExtraIterations < 2;
            if (canImprove) {
                return mateExtraIterations + 1;
            } else {
                return -1;
            }
        }
        return mateExtraIterations; // normal score, keep deepening
    }

    private SearchResult buildResult(int best, int score, int depth, int[] pv, int pvLen, long startTimeNs) {
        long elapsedMs = (System.nanoTime() - startTimeNs) / 1_000_000L;
        return new SearchResult(best, score, depth, nodesCounter, elapsedMs, pv, pvLen);
    }

    /**
     * Negamax
     */
    int negamax(Board board, int depth, int ply, int alpha, int beta) {
        periodicTimeoutCheck();

        nodesCounter++;

        // Shared-alpha update (may narrow beta and cause an immediate cutoff)
        beta = applySharedAlpha(ply, beta);
        if (alpha >= beta) {
            return alpha;
        }

        pvLength[ply] = 0;

        // Leaf → quiescence or static eval
        if (depth <= 0) {
            if (ENABLE_QUIESCENCE) {
                return quiescence(board, ply, alpha, beta);
            } else {
                return Evaluation.evaluate(board);
            }
        }

        // Check extension
        int originalDepth = depth;
        boolean inCheck = ENABLE_CHECK_EXTENSION && board.inCheck(board.getSideToMove());
        if (inCheck) {
            depth++;
        }

        // TT cutoff
        int ttCutoff = probeTTForCutoff(board, depth, ply, alpha, beta);
        if (ttCutoff != Integer.MIN_VALUE) {
            return ttCutoff;
        }

        // Generate moves
        MoveList moves = moveLists[ply];
        moves.clear();
        MoveGenerator.generateLegalMoves(board, moves);

        if (moves.getSize() == 0) {
            return terminalScore(board, ply, inCheck);
        }

        orderMoves(board, moves, ply);

        // Parallel root split
        if (ply == 0 && rootExecutor != null && moves.getSize() > 1) {
            return searchRootParallel(board, depth, originalDepth, alpha, beta, moves);
        }

        // Main PVS loop — bestMove is recovered from pvTable[ply][0]
        int bestScore = searchMoves(board, moves, depth, ply, alpha, beta);

        storeTT(board.getHash(), pvTable[ply][0], originalDepth, bestScore, alpha, beta, ply);
        return bestScore;
    }

    /**
     * Narrows beta based on the shared root-α value (parallel root search). No-op otherwise.
     */
    private int applySharedAlpha(int ply, int beta) {
        if (ply != 1 || sharedAlpha == null) {
            return beta;
        }
        int sa = sharedAlpha.get();
        if (sa > -beta) {
            return -sa;
        } else {
            return beta;
        }
    }

    /**
     * Score to return when no legal moves exist.
     */
    private int terminalScore(Board board, int ply, boolean inCheck) {
        boolean check = ENABLE_CHECK_EXTENSION ? inCheck : board.inCheck(board.getSideToMove());
        if (check) {
            return -MATE_SCORE + ply;
        } else {
            return 0;
        }
    }

    /**
     * Applies move ordering using TT move and previous-iteration PV move.
     */
    private void orderMoves(Board board, MoveList moves, int ply) {
        if (!ENABLE_MOVE_ORDERING) {
            return;
        }
        int ttMove = probeTTForMove(board);
        int pvMove = (ply < prevIterationPVLength) ? prevIterationPV[ply] : 0;
        sortMoves(board, moves, ttMove, pvMove);
    }

    /**
     * Returns the cutoff score if the TT entry allows a cutoff, otherwise Integer.MIN_VALUE.
     */
    private int probeTTForCutoff(Board board, int depth, int ply, int alpha, int beta) {
        if (!ENABLE_TT || !ENABLE_TT_CUTOFFS || ply == 0) {
            return Integer.MIN_VALUE;
        }

        long ttData = transpositionTable.probe(board.getHash());
        if (ttData == 0L || ttData == 1L) {
            return Integer.MIN_VALUE;
        }
        if (TranspositionTable.getDepth(ttData) < depth) {
            return Integer.MIN_VALUE;
        }

        int ttScore = scoreFromTT(TranspositionTable.getScore(ttData), ply);

        // Don't cutoff on mate scores in parallel workers
        if (Math.abs(ttScore) >= MATE_THRESHOLD && sharedAlpha != null) {
            return Integer.MIN_VALUE;
        }

        int ttBound = TranspositionTable.getBound(ttData);
        if (ttBound == TranspositionTable.BOUND_EXACT) {
            return ttScore;
        }
        if (ttBound == TranspositionTable.BOUND_LOWER && ttScore >= beta) {
            return ttScore;
        }
        if (ttBound == TranspositionTable.BOUND_UPPER && ttScore <= alpha) {
            return ttScore;
        }
        return Integer.MIN_VALUE;
    }

    /**
     * Returns the TT move for ordering, or 0 if none.
     */
    private int probeTTForMove(Board board) {
        if (!ENABLE_TT) {
            return 0;
        }
        long ttData = transpositionTable.probe(board.getHash());
        if (ttData == 0L || ttData == 1L) {
            return 0;
        }
        return TranspositionTable.getMove(ttData);
    }

    /**
     * Iterates moves with PVS. Writes PV into pvTable[ply] (so pvTable[ply][0]
     * is the best move after return) and updates root best at ply 0.
     */
    private int searchMoves(Board board, MoveList moves, int depth, int ply, int alpha, int beta) {
        int bestScore = -INFINITY;

        for (int i = 0; i < moves.getSize(); i++) {
            int move = moves.getMoves()[i];
            long undo = board.makeMove(move);
            int score = searchOneChild(board, depth, ply, alpha, beta, i);
            board.unmakeMove(move, undo);

            if (score > bestScore) {
                bestScore = score;
                if (ply == 0) {
                    rootBestMove = move;
                    rootBestScore = score;
                }

                if (score > alpha) {
                    alpha = score;
                    updatePV(ply, move);
                    if (alpha >= beta) {
                        break;
                    }
                }
            }
        }

        return bestScore;
    }

    /**
     * PVS: null-window search on later moves, full re-search on fail-high.
     */
    private int searchOneChild(Board board, int depth, int ply, int alpha, int beta, int moveIndex) {
        if (ENABLE_PVS && moveIndex > 0) {
            int score = -negamax(board, depth - 1, ply + 1, -alpha - 1, -alpha);
            if (score > alpha && score < beta) {
                score = -negamax(board, depth - 1, ply + 1, -beta, -alpha);
            }
            return score;
        }
        return -negamax(board, depth - 1, ply + 1, -beta, -alpha);
    }

    /**
     * Writes the new best move to the PV table at the given ply.
     */
    private void updatePV(int ply, int move) {
        pvTable[ply][0] = move;
        System.arraycopy(pvTable[ply + 1], 0, pvTable[ply], 1, pvLength[ply + 1]);
        pvLength[ply] = pvLength[ply + 1] + 1;
    }

    /**
     * Parallel root search.
     */
    private int searchRootParallel(Board b, int depth, int originalDepth,
                                   int alpha, int beta, MoveList moves) {
        int originalAlpha = alpha;

        // Step 1: search the PV move serially to establish α
        int bestScore = searchRootPvMove(b, depth, alpha, beta, moves.getMoves()[0]);
        int bestMove = moves.getMoves()[0];
        alpha = Math.max(alpha, bestScore);

        if (alpha >= beta) {
            storeTT(b.getHash(), bestMove, originalDepth, bestScore, originalAlpha, beta, 0);
            return bestScore;
        }

        // Step 2: search the remaining moves in parallel
        AtomicInteger sharedAlphaVal = new AtomicInteger(alpha);
        List<Future<int[]>> futures = submitRootWorkers(b, depth, beta, moves, sharedAlphaVal);

        // Step 3: collect
        for (Future<int[]> f : futures) {
            int[] res = awaitWorkerResult(f);
            if (res.length == 0) {
                continue;    // worker aborted (timeout/cancel/interrupt)
            }

            int score = res[1];
            if (score > bestScore) {
                bestScore = score;
                bestMove = res[0];
                rootBestMove = bestMove;
                rootBestScore = bestScore;
                installRootPV(bestMove, res, res[2]);
                if (bestScore > alpha) {
                    alpha = bestScore;
                }
            }
        }

        storeTT(b.getHash(), bestMove, originalDepth, bestScore, originalAlpha, beta, 0);
        return bestScore;
    }

    /**
     * Searches the first (PV) move serially and sets up pvTable[0]. Returns the score.
     */
    private int searchRootPvMove(Board b, int depth, int alpha, int beta, int move) {
        long undo = b.makeMove(move);
        int score = -negamax(b, depth - 1, 1, -beta, -alpha);
        b.unmakeMove(move, undo);

        rootBestMove = move;
        rootBestScore = score;

        pvTable[0][0] = move;
        System.arraycopy(pvTable[1], 0, pvTable[0], 1, pvLength[1]);
        pvLength[0] = pvLength[1] + 1;

        return score;
    }

    /**
     * Submits one worker per non-PV move.
     */
    private List<Future<int[]>> submitRootWorkers(Board b, int depth, int beta,
                                                  MoveList moves, AtomicInteger sharedAlphaVal) {
        int remaining = moves.getSize() - 1;
        List<Future<int[]>> futures = new ArrayList<>(remaining);

        for (int mi = 0; mi < remaining; mi++) {
            int move = moves.getMoves()[mi + 1];
            Board taskBoard = b.copy();
            futures.add(rootExecutor.submit(() -> runRootWorker(taskBoard, move, depth, beta, sharedAlphaVal)));
        }
        return futures;
    }

    /**
     * One parallel-root worker's body. Catches its own abort exceptions and returns an empty
     * array for those cases, so the collector doesn't need a try/catch.
     * Successful result layout: {move, score, pvLen, pv...}.
     */
    private int[] runRootWorker(Board taskBoard, int move, int depth, int beta, AtomicInteger sharedAlphaVal) {
        Search ws = buildWorkerSearch(sharedAlphaVal);

        try {
            long undo = taskBoard.makeMove(move);
            int myAlpha = sharedAlphaVal.get();
            int score = -ws.negamax(taskBoard, depth - 1, 1, -beta, -myAlpha);
            taskBoard.unmakeMove(move, undo);

            if (score > myAlpha) {
                sharedAlphaVal.updateAndGet(old -> Math.max(old, score));
            }

            int childPVLen = ws.pvLength[1];
            int[] result = new int[3 + childPVLen];
            result[0] = move;
            result[1] = score;
            result[2] = childPVLen;
            System.arraycopy(ws.pvTable[1], 0, result, 3, childPVLen);
            return result;
        } catch (SearchTimeoutException e) {
            return EMPTY_RESULT;
        }
    }


    /**
     * Creates a worker Search instance configured with this search's state.
     */
    private Search buildWorkerSearch(AtomicInteger sharedAlphaVal) {
        Search ws = new Search(transpositionTable);
        System.arraycopy(prevIterationPV, 0, ws.prevIterationPV, 0, prevIterationPVLength);
        ws.prevIterationPVLength = prevIterationPVLength;
        ws.sharedAlpha = sharedAlphaVal;
        ws.stopFlag = stopFlag;
        ws.deadlineNanos = deadlineNanos;
        ws.useTimeLimit = useTimeLimit;
        return ws;
    }

    /**
     * Waits for a worker result. Returns EMPTY_RESULT on expected abort paths.
     */
    private int[] awaitWorkerResult(Future<int[]> f) {
        try {
            return f.get(600, TimeUnit.SECONDS);
        } catch (CancellationException e) {
            return EMPTY_RESULT;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return EMPTY_RESULT;
        } catch (ExecutionException | TimeoutException e) {
            throw new IllegalStateException("Parallel search worker failed: " + e.getMessage(), e);
        }
    }

    /**
     * Writes the PV for a new best move at the root. {@code res} is {move, score, pvLen, pv...}.
     */
    private void installRootPV(int bestMove, int[] res, int childPVLen) {
        pvTable[0][0] = bestMove;
        System.arraycopy(res, 3, pvTable[0], 1, childPVLen);
        pvLength[0] = childPVLen + 1;
    }

    /**
     * Quiescence search
     */
    private int quiescence(Board board, int ply, int alpha, int beta) {

        // Abort check
        periodicTimeoutCheck();

        nodesCounter++;
        pvLength[ply] = 0;

        int standPat = Evaluation.evaluate(board);
        if (standPat >= beta) {
            return standPat;
        }

        if (standPat > alpha) {
            alpha = standPat;
        }
        if (ply >= MAX_PLY - 1) {
            return standPat;
        }

        MoveList moves = moveLists[ply];
        moves.clear();
        MoveGenerator.generateLegalCaptureMoves(board, moves);

        if (moves.getSize() == 0) {
            return standPat;
        }

        if (ENABLE_MOVE_ORDERING) {
            sortMoves(board, moves, 0, 0);
        }

        int best = standPat;
        for (int i = 0; i < moves.getSize(); i++) {
            int move = moves.getMoves()[i];
            long undo = board.makeMove(move);
            int score = -quiescence(board, ply + 1, -beta, -alpha);
            board.unmakeMove(move, undo);

            if (score > best) {
                best = score;
                if (score > alpha) {
                    alpha = score;
                    if (alpha >= beta) {
                        break;
                    }
                }
            }
        }
        return best;
    }

    // *************** Sorting ******************

    private void sortMoves(Board board, MoveList moveList, int ttMove, int pvMove) {
        int moveCount = moveList.getSize();
        int[] moves = moveList.getMoves();
        int[] scores = orderingScores;

        // Score each move
        for (int i = 0; i < moveCount; i++) {
            scores[i] = scoreMove(board, moves[i], ttMove, pvMove);
        }

        // Selection sort by score (descending)
        for (int i = 0; i < moveCount - 1; i++) {
            int best = i;
            for (int j = i + 1; j < moveCount; j++) {
                if (scores[j] > scores[best]) {
                    best = j;
                }
            }
            if (best != i) {
                int tmpM = moves[i];
                moves[i] = moves[best];
                moves[best] = tmpM;
                int tmpS = scores[i];
                scores[i] = scores[best];
                scores[best] = tmpS;
            }
        }
    }

    private int scoreMove(Board board, int m, int ttMove, int pvMove) {
        if (pvMove != 0 && m == pvMove) {
            return 2_000_000;
        }
        if (ttMove != 0 && m == ttMove) {
            return 1_000_000;
        }
        if (MoveUtils.isCapture(m)) {
            return scoreCapture(board, m);
        }
        return 0;
    }

    private int scoreCapture(Board board, int m) {
        int victim = MoveUtils.getCaptureType(m);
        int attackerPiece = board.getMailbox()[MoveUtils.getFromSquare(m)];
        int attacker = attackerPiece >= 0 ? PieceUtils.getTypeOfPiece(attackerPiece) : 0;
        return 100_000 + victim * 10 - attacker;
    }

    // *************** TT ******************


    private void storeTT(long hash, int move, int depth, int score, int originalAlpha, int beta, int ply) {
        if (!ENABLE_TT) {
            return;
        }

        int bound;
        if (score <= originalAlpha) {
            bound = TranspositionTable.BOUND_UPPER;
        } else if (score >= beta) {
            bound = TranspositionTable.BOUND_LOWER;
        } else {
            bound = TranspositionTable.BOUND_EXACT;
        }

        transpositionTable.store(hash, move, depth, scoreToTT(score, ply), bound);
    }

    private static int scoreToTT(int score, int ply) {
        if (score >= MATE_THRESHOLD) {
            return score + ply;
        }
        if (score <= -MATE_THRESHOLD) {
            return score - ply;
        }
        return score;
    }

    private static int scoreFromTT(int score, int ply) {
        if (score >= MATE_THRESHOLD) {
            return score - ply;
        }
        if (score <= -MATE_THRESHOLD) {
            return score + ply;
        }
        return score;
    }


// *************** Helpers ******************

    private boolean shouldStop() {
        if (stopFlag.get()) {
            return true;
        }
        return useTimeLimit && System.nanoTime() > deadlineNanos;
    }


    private void periodicTimeoutCheck() {
        if ((nodesCounter & 4095) == 0 && shouldStop()) {
            throw new SearchTimeoutException();
        }
    }


}
