package ch.nostromo.tiffanys.engine.dragonborn;

import ch.nostromo.tiffanys.commons.ChessGame;
import ch.nostromo.tiffanys.commons.ChessGameState;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.engine.commons.*;
import ch.nostromo.tiffanys.engine.commons.events.EngineEvent;
import ch.nostromo.tiffanys.engine.commons.events.EngineEventListener;
import ch.nostromo.tiffanys.engine.commons.opening.OpeningBook;
import ch.nostromo.tiffanys.engine.dragonborn.ai.CalculationResult;
import ch.nostromo.tiffanys.engine.dragonborn.ai.CalculationTimeoutException;
import ch.nostromo.tiffanys.engine.dragonborn.ai.callable.AlphaBetaCallable;
import ch.nostromo.tiffanys.engine.dragonborn.ai.callable.AlphaBetaCallableResult;
import ch.nostromo.tiffanys.engine.dragonborn.board.RobustBoard;
import ch.nostromo.tiffanys.engine.dragonborn.move.EngineMove;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Dragonborn implements Engine, DragonbornConstants {

    protected static Logger LOGGER = Logger.getLogger(Dragonborn.class.getName());


    private Random r = new Random();

    private ChessGame game;

    public static boolean running = true;


    protected EngineSettings engineSettings;

    protected List<EngineEventListener> eventListeners = new ArrayList<>();

    protected OpeningBook openingBook;

    public Dragonborn(EngineSettings engineSettings) {
        this(engineSettings, new OpeningBook());
    }

    public Dragonborn(EngineSettings engineSettings, OpeningBook openingBook) {
        this.engineSettings = engineSettings;
        this.openingBook = openingBook;
    }


    public void asyncScoreMoves(ChessGame game) {

        Move openingMove = openingBook.getNextMove(game);
        if (openingMove != null) {

            EngineResult engineResult = new EngineResult();
            engineResult.setOpeningBook(true);
            engineResult.setSelectedMove(openingMove);

            fireFinishedEvent(new EngineEvent(engineResult));

        } else {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        syncScoreMoves(game);
                    } catch (EngineException e) {
                        LOGGER.log(Level.SEVERE, "Thread aborted", e);
                    }
                }
            });
        }

    }


    protected void fireUpdateEvent(EngineEvent event) {
        for (EngineEventListener listener : eventListeners) {
            listener.handleEngineUpdate(event);
        }
    }

    protected void fireFinishedEvent(EngineEvent event) {
        for (EngineEventListener listener : eventListeners) {
            listener.handleEngineFinished(event);
        }
    }

    public void addEventListener(EngineEventListener listener) {
        this.eventListeners.add(listener);
    }

    private EngineResult createEngineResult(ChessGame game, CalculationResult calculationResult, long timeMs) {

        EngineResult result = new EngineResult();

        EngineMove[] tiffMoves = calculationResult.moves;
        Arrays.sort(tiffMoves);

        List<Move> legalMoves = new ArrayList<Move>();
        for (EngineMove tiffanysMove : tiffMoves) {
            legalMoves.add(tiffanysMove.convertToMove(game.getCurrentSide()));
        }

        result.setLegalMoves(legalMoves);
        result.setPositionsEvaluated(calculationResult.positionsEvaluated);
        result.setTotalTimeInMs(timeMs);

        List<Move> bestMoves = filterBestEqualMoves(legalMoves);

        ListIterator<Move> iter = bestMoves.listIterator();
        while (iter.hasNext()) {
            game.applyMove(iter.next());
            if (game.getCurrentGameState() == ChessGameState.REMIS_BY_THREE) {
                iter.remove();
            }
            game.takeBackMove();
        }

        if (bestMoves.size() > 0) {
            result.setSelectedMove(selectRandomBestMove(bestMoves));
        } else {
            result.setSelectedMove(selectRandomBestMove(legalMoves));
        }

        return result;
    }

    @Override
    public EngineResult syncScoreMoves(ChessGame game) throws EngineException {
        try {
            LOGGER.fine("Start sync scoreMoves");

            long start = System.currentTimeMillis();

            this.game = game;

            // Create board, based on given game
            RobustBoard robustBoard = new RobustBoard(game.getCurrentBoard(), game.getCurrentSide());

            // Start search
            CalculationResult calculationResult = startSearch(robustBoard, engineSettings);

            // Create & return the result
            EngineResult finalResult = createEngineResult(game, calculationResult, System.currentTimeMillis() - start);

            fireFinishedEvent(new EngineEvent(finalResult));

            return finalResult;

        } catch (Exception e) {
            throw new EngineException("Sync score Moves failed with message=" + e.getMessage(), e);
        }
    }

    private Move selectRandomBestMove(List<Move> moves) {
        List<Move> result = new ArrayList<Move>();
        double bestScore = moves.get(0).getMoveAttributes().getScore();
        for (Move move : moves) {
            if (move.getMoveAttributes().getScore() == bestScore) {
                result.add(move);
            }
        }

        return result.get(r.nextInt(result.size()));
    }

    private List<Move> filterBestEqualMoves(List<Move> moves) {
        List<Move> result = new ArrayList<Move>();
        for (Move move : moves) {
            if (move.getMoveAttributes().getScore() > 0 || move.getMoveAttributes().getScore() < 0) {
                result.add(move);
            }
        }
        return result;
    }

    public EngineResult generateLegalMoves(ChessGame game) {
        long start = System.currentTimeMillis();

        EngineResult result = new EngineResult();

        RobustBoard tiffBoard = new RobustBoard(game.getCurrentBoard(), game.getCurrentSide());
        EngineMove[] tiffMoves = tiffBoard.generateLegalMovesList();

        List<Move> legalMoves = new ArrayList<Move>();
        for (EngineMove tiffanysMove : tiffMoves) {
            legalMoves.add(tiffanysMove.convertToMove(game.getCurrentSide()));
        }

        result.setLegalMoves(legalMoves);
        result.setTotalTimeInMs(System.currentTimeMillis() - start);

        return result;

    }

    @Override
    public void halt() {
        running = false;
    }


    private final CalculationResult startSearch(RobustBoard board, EngineSettings engineSettings) throws Exception {


        running = true;

        CalculationResult calculationResult = new CalculationResult();
        Timer timer = new Timer();

        int calculationDepth = engineSettings.getDepth();
        if (engineSettings.getMode() == EngineSettings.EngineMode.TIME_FOR_MOVE) {
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    running = false;
                }
            }, engineSettings.getTime());
            calculationDepth = Integer.MAX_VALUE;
        }

        long fullStartMs = System.currentTimeMillis();

        try {

            EngineMove[] legalMoves = board.generateLegalMovesList();

            calculationResult.moves = legalMoves;




            int targetDepth = 0;
            boolean mateFound = false;

            while (true) {



                long startMs = System.currentTimeMillis();

                targetDepth++;

                ExecutorService executor = Executors.newFixedThreadPool(engineSettings.getThreads());
                List<FutureTask<AlphaBetaCallableResult>> taskList = new ArrayList<FutureTask<AlphaBetaCallableResult>>();

                // Create threads
                for (int i = 0; i < legalMoves.length; i++) {

                    EngineMove currentMove = legalMoves[i];

                    board.makeAndCheckMove(currentMove);

                    AlphaBetaCallable cb = new AlphaBetaCallable(engineSettings, board.safeClone(), currentMove, targetDepth);

                    FutureTask<AlphaBetaCallableResult> futureTask = new FutureTask<AlphaBetaCallableResult>(cb);
                    taskList.add(futureTask);
                    executor.execute(futureTask);

                    board.unmakeMove(legalMoves[i]);
                }

                executor.shutdown();

                // Get result from threads
                for (int i = 0; i < legalMoves.length; i++) {

                    EngineMove currentMove = legalMoves[i];

                    AlphaBetaCallableResult cbResult = taskList.get(i).get();

                    currentMove.score = cbResult.score;
                    currentMove.cutOffs = cbResult.cutOffs;
                    currentMove.nodes = cbResult.nodes;
                    currentMove.maxDepth = cbResult.maxDepth;
                    currentMove.plannedDepth = cbResult.plannedDepth;
                    currentMove.principalVariation = cbResult.foundPv;

                    calculationResult.positionsEvaluated += cbResult.positionsEvaluated;

                    if (cbResult.score > 9000) {
                        mateFound = true;
                    }

                }


                this.fireUpdateEvent(new EngineEvent(createEngineResult(game, calculationResult, System.currentTimeMillis() - startMs)));

                if (targetDepth == calculationDepth || mateFound) {
                    break;
                }
            }

        } catch (ExecutionException e) {
            if (!(e.getCause() instanceof CalculationTimeoutException)) {
                LOGGER.log(Level.INFO, "Unexpected Error", e);
                running = false;
                throw new EngineException("Unexpected execution exception. Message=" + e.getMessage() , e);
            }
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "Unexpected Error", e);
            running = false;
            throw new EngineException("Unexpected exception. Message=" + e.getMessage() , e);
        }

        timer.cancel();

        long timeMs = System.currentTimeMillis() - fullStartMs;
        for (EngineMove move : calculationResult.moves) {
            move.timeMs = timeMs;
        }

        running = false;

        return calculationResult;

    }

    @Override
    public long testPseudoMoveGeneratorPerformance(ChessGame game, int iterations) {
        long start = System.currentTimeMillis();

        RobustBoard tiffBoard = new RobustBoard(game.getCurrentBoard(), game.getCurrentSide());

        EngineMove[] moves = EngineMove.generateMoveArray();

        for (int i = 0; i < iterations; i++) {
            tiffBoard.generateMovesList(moves);
        }

        return System.currentTimeMillis() - start;
    }


    @Override
    public List<Move> generateLegalHitMovesList(ChessGame game) {
        RobustBoard tiffBoard = new RobustBoard(game.getCurrentBoard(), game.getCurrentSide());
        EngineMove[] movesBuffer = tiffBoard.generateLegalHitMovesList();

        List<Move> result = new ArrayList<Move>();
        for (EngineMove move : movesBuffer) {
            result.add(move.convertToMove(game.getCurrentSide()));
        }

        return result;
    }

    @Override
    public List<Move> generateLegalMovesList(ChessGame game) {
        RobustBoard tiffBoard = new RobustBoard(game.getCurrentBoard(), game.getCurrentSide());
        EngineMove[] movesBuffer = tiffBoard.generateLegalMovesList();

        List<Move> result = new ArrayList<Move>();
        for (EngineMove move : movesBuffer) {
            result.add(move.convertToMove(game.getCurrentSide()));
        }

        return result;
    }

}
